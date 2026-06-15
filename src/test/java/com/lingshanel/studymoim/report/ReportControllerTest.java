package com.lingshanel.studymoim.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lingshanel.studymoim.comment.repository.CommentRepository;
import com.lingshanel.studymoim.report.repository.PostReportRepository;
import com.lingshanel.studymoim.study.repository.StudyPostRepository;
import com.lingshanel.studymoim.user.domain.User;
import com.lingshanel.studymoim.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyPostRepository studyPostRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostReportRepository postReportRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    @AfterEach
    void cleanUp() {
        postReportRepository.deleteAll();
        commentRepository.deleteAll();
        studyPostRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void userCanReportPostAndAdminCanResolveIt() throws Exception {
        MockHttpSession writerSession = signupAndLogin("writer-report@example.com", "report-writer");
        MockHttpSession reporterSession = signupAndLogin("reporter@example.com", "reporter");
        MockHttpSession adminSession = createAdminAndLogin();
        Long postId = createPost(writerSession);

        mockMvc.perform(post("/api/study-posts/{postId}/reports", postId)
                        .session(reporterSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "reason": "스터디와 관련 없는 홍보성 내용입니다."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));

        mockMvc.perform(get("/api/admin/reports")
                        .session(adminSession)
                        .param("status", "PENDING")
                        .param("keyword", "홍보성"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postId").value(postId))
                .andExpect(jsonPath("$[0].reporterNickname").value("reporter"));

        Long reportId = postReportRepository.findAll().getFirst().getId();
        mockMvc.perform(patch("/api/admin/reports/{reportId}/resolve", reportId).session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }

    @Test
    void duplicatePendingReportIsRejected() throws Exception {
        MockHttpSession writerSession = signupAndLogin("writer-duplicate@example.com", "duplicate-writer");
        MockHttpSession reporterSession = signupAndLogin("reporter-duplicate@example.com", "duplicate-reporter");
        Long postId = createPost(writerSession);

        reportPost(reporterSession, postId).andExpect(status().isOk());
        reportPost(reporterSession, postId).andExpect(status().isBadRequest());
    }

    @Test
    void userCanReportCommentAndAdminCanSearchIt() throws Exception {
        MockHttpSession writerSession = signupAndLogin("comment-report-writer@example.com", "comment-writer");
        MockHttpSession reporterSession = signupAndLogin("comment-reporter@example.com", "comment-reporter");
        MockHttpSession adminSession = createAdminAndLogin();
        Long postId = createPost(writerSession);
        Long commentId = createComment(writerSession, postId);

        mockMvc.perform(post("/api/comments/{commentId}/reports", commentId)
                        .session(reporterSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "reason": "댓글 내용이 부적절합니다."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetType").value("COMMENT"))
                .andExpect(jsonPath("$.commentId").value(commentId));

        mockMvc.perform(get("/api/admin/reports")
                        .session(adminSession)
                        .param("keyword", "부적절")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].targetType").value("COMMENT"))
                .andExpect(jsonPath("$[0].commentContent").value("reported comment"));
    }

    private org.springframework.test.web.servlet.ResultActions reportPost(MockHttpSession session, Long postId) throws Exception {
        return mockMvc.perform(post("/api/study-posts/{postId}/reports", postId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "reason": "중복 신고 확인"
                        }
                        """));
    }

    private MockHttpSession createAdminAndLogin() throws Exception {
        userRepository.save(User.admin(
                "admin-report@example.com",
                passwordEncoder.encode("admin1234!"),
                "report-admin"
        ));

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/api/admin/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "report-admin",
                                  "password": "admin1234!"
                                }
                                """))
                .andExpect(status().isOk());
        return session;
    }

    private MockHttpSession signupAndLogin(String email, String nickname) throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "password123",
                                  "nickname": "%s"
                                }
                                """.formatted(email, nickname)))
                .andExpect(status().isCreated());

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/api/auth/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "%s",
                                  "password": "password123"
                                }
                                """.formatted(nickname)))
                .andExpect(status().isOk());
        return session;
    }

    private Long createPost(MockHttpSession session) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/study-posts")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "categorySlug": "backend",
                                  "title": "report target",
                                  "content": "post content",
                                  "studyType": "ONLINE",
                                  "location": "",
                                  "maxMembers": 5
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        return Long.parseLong(body.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));
    }

    private Long createComment(MockHttpSession session, Long postId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/study-posts/{postId}/comments", postId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": "reported comment"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        return Long.parseLong(body.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));
    }
}
