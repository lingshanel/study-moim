package com.lingshanel.studymoim.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import com.lingshanel.studymoim.comment.repository.CommentRepository;
import com.lingshanel.studymoim.auth.repository.PasswordResetVerificationRepository;
import com.lingshanel.studymoim.study.repository.StudyPostRepository;
import com.lingshanel.studymoim.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyPostRepository studyPostRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordResetVerificationRepository passwordResetVerificationRepository;

    @MockitoBean
    private PasswordResetCodeGenerator passwordResetCodeGenerator;

    @BeforeEach
    void setUp() {
        passwordResetVerificationRepository.deleteAll();
        commentRepository.deleteAll();
        studyPostRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void signup() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "study@example.com",
                                  "password": "password123",
                                  "nickname": "스터디원"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("study@example.com"))
                .andExpect(jsonPath("$.nickname").value("스터디원"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void loginAndMe() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "login@example.com",
                                  "password": "password123",
                                  "nickname": "로그인유저"
                                }
                                """))
                .andExpect(status().isCreated());

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/api/auth/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "로그인유저",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("login@example.com"));

        mockMvc.perform(get("/api/users/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("로그인유저"));
    }

    @Test
    void meRequiresLogin() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findNicknameByEmail() throws Exception {
        signup("find@example.com", "find-user", "password123");

        mockMvc.perform(post("/api/auth/find-nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "find@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("find-user"));
    }

    @Test
    void resetPasswordAndLoginWithNewPassword() throws Exception {
        signup("reset@example.com", "reset-user", "password123");
        when(passwordResetCodeGenerator.generateCode()).thenReturn("123456");

        mockMvc.perform(post("/api/auth/password-reset/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "reset@example.com",
                                  "nickname": "reset-user"
                                }
                                """))
                .andExpect(status().isNoContent());

        MvcResult verificationResult = mockMvc.perform(post("/api/auth/password-reset/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "reset@example.com",
                                  "nickname": "reset-user",
                                  "code": "123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resetToken").isNotEmpty())
                .andReturn();

        String resetToken = verificationResult.getResponse().getContentAsString()
                .replaceAll(".*\\\"resetToken\\\":\\\"([^\\\"]+)\\\".*", "$1");

        mockMvc.perform(post("/api/auth/password-reset/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "resetToken": "%s",
                                  "newPassword": "newPassword123"
                                }
                                """.formatted(resetToken)))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/auth/password-reset/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "resetToken": "%s",
                                  "newPassword": "anotherPassword123"
                                }
                                """.formatted(resetToken)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호 재설정 요청이 만료되었거나 이미 사용되었습니다."));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "reset-user",
                                  "password": "newPassword123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("reset@example.com"));
    }

    @Test
    void loginIsLockedForTenMinutesAfterFiveFailures() throws Exception {
        signup("lock@example.com", "lock-user", "password123");

        for (int attempt = 1; attempt <= 4; attempt++) {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "nickname": "lock-user",
                                      "password": "wrong-password"
                                    }
                                    """))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("닉네임 또는 비밀번호가 올바르지 않습니다. 남은 시도: " + (5 - attempt) + "회"));
        }

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "lock-user",
                                  "password": "wrong-password"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("로그인에 여러 번 실패하여 10분 동안 로그인이 제한됩니다."));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "lock-user",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("로그인 시도가 일시적으로 제한되었습니다.")));
    }

    @Test
    void successfulLoginResetsFailureCount() throws Exception {
        signup("retry@example.com", "retry-user", "password123");

        attemptLogin("retry-user", "wrong-password", status().isUnauthorized());
        attemptLogin("retry-user", "wrong-password", status().isUnauthorized());
        attemptLogin("retry-user", "password123", status().isOk());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "retry-user",
                                  "password": "wrong-password"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("닉네임 또는 비밀번호가 올바르지 않습니다. 남은 시도: 4회"));
    }

    private void attemptLogin(
            String nickname,
            String password,
            org.springframework.test.web.servlet.ResultMatcher expectedStatus
    ) throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "%s",
                                  "password": "%s"
                                }
                                """.formatted(nickname, password)))
                .andExpect(expectedStatus);
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
}
