package com.lingshanel.studymoim.flow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lingshanel.studymoim.admin.repository.AdminActionLogRepository;
import com.lingshanel.studymoim.auth.repository.PasswordResetVerificationRepository;
import com.lingshanel.studymoim.bookmark.repository.BookmarkRepository;
import com.lingshanel.studymoim.comment.repository.CommentRepository;
import com.lingshanel.studymoim.like.repository.LikeRepository;
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
class UserJourneyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminActionLogRepository adminActionLogRepository;

    @Autowired
    private PasswordResetVerificationRepository passwordResetVerificationRepository;

    @Autowired
    private PostReportRepository postReportRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private StudyPostRepository studyPostRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @AfterEach
    void cleanUp() {
        adminActionLogRepository.deleteAll();
        passwordResetVerificationRepository.deleteAll();
        postReportRepository.deleteAll();
        commentRepository.deleteAll();
        likeRepository.deleteAll();
        bookmarkRepository.deleteAll();
        studyPostRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void completeUserJourneyFromSignupToAdminResolution() throws Exception {
        MockHttpSession writerSession = signupAndLogin("journey-writer@example.com", "journey-writer");
        MockHttpSession memberSession = signupAndLogin("journey-member@example.com", "journey-member");
        MockHttpSession adminSession = createAdminAndLogin();

        Long postId = createPost(writerSession);

        mockMvc.perform(get("/api/study-posts/{postId}", postId).session(memberSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viewCount").value(1));

        mockMvc.perform(post("/api/study-posts/{postId}/likes", postId).session(memberSession))
                .andExpect(status().isNoContent());
        mockMvc.perform(post("/api/study-posts/{postId}/bookmarks", postId).session(memberSession))
                .andExpect(status().isNoContent());

        Long commentId = createComment(memberSession, postId, null, "함께 참여하고 싶습니다.");
        createComment(writerSession, postId, commentId, "좋습니다. 일정은 댓글로 조율할게요.");

        mockMvc.perform(get("/api/study-posts/{postId}", postId)
                        .session(memberSession)
                        .param("increaseView", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viewCount").value(1))
                .andExpect(jsonPath("$.likeCount").value(1))
                .andExpect(jsonPath("$.bookmarkCount").value(1))
                .andExpect(jsonPath("$.commentCount").value(2))
                .andExpect(jsonPath("$.likedByMe").value(true))
                .andExpect(jsonPath("$.bookmarkedByMe").value(true));

        mockMvc.perform(get("/api/study-posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("함께 참여하고 싶습니다."))
                .andExpect(jsonPath("$[1].parentCommentId").value(commentId));

        MvcResult reportResult = mockMvc.perform(post("/api/study-posts/{postId}/reports", postId)
                        .session(memberSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "reason": "운영 정책 확인이 필요한 모집글입니다."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn();
        Long reportId = responseId(reportResult);

        mockMvc.perform(get("/api/admin/reports")
                        .session(adminSession)
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reportId))
                .andExpect(jsonPath("$[0].reporterNickname").value("journey-member"));

        mockMvc.perform(patch("/api/admin/reports/{reportId}/resolve", reportId).session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"));

        mockMvc.perform(get("/api/admin/action-logs").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].actionType").value("REPORT_RESOLVED"))
                .andExpect(jsonPath("$[0].targetId").value(reportId));
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

    private MockHttpSession createAdminAndLogin() throws Exception {
        userRepository.save(User.admin(
                "journey-admin@example.com",
                passwordEncoder.encode("admin1234!"),
                "journey-admin"
        ));

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/api/admin/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "journey-admin",
                                  "password": "admin1234!"
                                }
                                """))
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
                                  "title": "Spring MVC 흐름 같이 공부해요",
                                  "content": "작은 예제를 만들며 요청과 응답 흐름을 정리합니다.",
                                  "studyType": "ONLINE",
                                  "location": "",
                                  "maxMembers": 5
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        return responseId(result);
    }

    private Long createComment(
            MockHttpSession session,
            Long postId,
            Long parentCommentId,
            String content
    ) throws Exception {
        String parent = parentCommentId == null ? "null" : parentCommentId.toString();
        MvcResult result = mockMvc.perform(post("/api/study-posts/{postId}/comments", postId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "parentCommentId": %s,
                                  "content": "%s"
                                }
                                """.formatted(parent, content)))
                .andExpect(status().isCreated())
                .andReturn();
        return responseId(result);
    }

    private Long responseId(MvcResult result) throws Exception {
        String body = result.getResponse().getContentAsString();
        String idValue = body.replaceAll(".*\\\"id\\\":(\\d+).*", "$1");
        return Long.parseLong(idValue);
    }
}
