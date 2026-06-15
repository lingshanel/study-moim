package com.lingshanel.studymoim.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
class AdminControllerTest {

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
    void setUp() {
        postReportRepository.deleteAll();
        commentRepository.deleteAll();
        studyPostRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void adminCanListAndBanUsers() throws Exception {
        MockHttpSession adminSession = createAdminAndLogin();
        signup("member@example.com", "member-user", "password123");
        Long memberId = userRepository.findByNickname("member-user").orElseThrow().getId();

        mockMvc.perform(get("/api/admin/users").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.nickname == 'member-user')]").exists());

        mockMvc.perform(patch("/api/admin/users/{userId}/status", memberId)
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "BANNED",
                                  "reason": "반복적인 광고성 게시글 작성"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BANNED"))
                .andExpect(jsonPath("$.banReason").value("반복적인 광고성 게시글 작성"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "member-user",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("제재된 계정입니다. 사유: 반복적인 광고성 게시글 작성 관리자에게 문의해주세요."));

        mockMvc.perform(get("/api/admin/action-logs").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].actionType").value("USER_BANNED"))
                .andExpect(jsonPath("$[0].targetId").value(memberId));
    }

    @Test
    void normalUserCannotUseAdminApi() throws Exception {
        MockHttpSession userSession = signupAndLogin("normal@example.com", "normal-user");

        mockMvc.perform(get("/api/admin/users").session(userSession))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanDeleteStudyPostAndComment() throws Exception {
        MockHttpSession adminSession = createAdminAndLogin();
        MockHttpSession writerSession = signupAndLogin("writer@example.com", "writer-user");
        Long postId = createPost(writerSession);
        Long commentId = createComment(writerSession, postId);

        mockMvc.perform(delete("/api/admin/comments/{commentId}", commentId).session(adminSession))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/study-posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].deleted").value(true));

        mockMvc.perform(delete("/api/admin/study-posts/{postId}", postId).session(adminSession))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/study-posts/{postId}", postId))
                .andExpect(status().isNotFound());
    }

    private MockHttpSession createAdminAndLogin() throws Exception {
        userRepository.save(User.admin(
                "admin@example.com",
                passwordEncoder.encode("admin1234!"),
                "superadmin"
        ));

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/api/admin/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "superadmin",
                                  "password": "admin1234!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));

        return session;
    }

    private MockHttpSession signupAndLogin(String email, String nickname) throws Exception {
        signup(email, nickname, "password123");
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

    private void signup(String email, String nickname, String password) throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "%s",
                                  "nickname": "%s"
                                }
                                """.formatted(email, password, nickname)))
                .andExpect(status().isCreated());
    }

    private Long createPost(MockHttpSession session) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/study-posts")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "categorySlug": "backend",
                                  "title": "admin delete target",
                                  "content": "post content",
                                  "studyType": "ONLINE",
                                  "location": "",
                                  "maxMembers": 5
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        return extractId(result);
    }

    private Long createComment(MockHttpSession session, Long postId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/study-posts/{postId}/comments", postId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": "admin delete comment target"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        return extractId(result);
    }

    private Long extractId(MvcResult result) throws Exception {
        String body = result.getResponse().getContentAsString();
        String idValue = body.replaceAll(".*\\\"id\\\":(\\d+).*", "$1");
        return Long.parseLong(idValue);
    }
}
