package com.lingshanel.studymoim.comment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lingshanel.studymoim.comment.repository.CommentRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private StudyPostRepository studyPostRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        studyPostRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createAndGetComments() throws Exception {
        MockHttpSession session = signupAndLogin("comment@example.com", "댓글작성자");
        Long postId = createPost(session);

        mockMvc.perform(post("/api/study-posts/{postId}/comments", postId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": "저도 같이 참여하고 싶습니다."
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("저도 같이 참여하고 싶습니다."))
                .andExpect(jsonPath("$.authorNickname").value("댓글작성자"));

        mockMvc.perform(get("/api/study-posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("저도 같이 참여하고 싶습니다."));
    }

    @Test
    void updateAndDeleteCommentByWriter() throws Exception {
        MockHttpSession session = signupAndLogin("comment-writer@example.com", "수정댓글");
        Long postId = createPost(session);
        Long commentId = createComment(session, postId);

        mockMvc.perform(patch("/api/comments/{commentId}", commentId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": "시간이 맞으면 참여하고 싶습니다."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("시간이 맞으면 참여하고 싶습니다."));

        mockMvc.perform(delete("/api/comments/{commentId}", commentId).session(session))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/study-posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].deleted").value(true))
                .andExpect(jsonPath("$[0].content").value("삭제된 댓글입니다."));
    }

    @Test
    void createCommentRequiresLogin() throws Exception {
        MockHttpSession session = signupAndLogin("post-owner@example.com", "글작성자");
        Long postId = createPost(session);

        mockMvc.perform(post("/api/study-posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": "로그인 없이 댓글 작성"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createReplyCommentKeepsParentCommentId() throws Exception {
        MockHttpSession session = signupAndLogin("reply@example.com", "reply-user");
        Long postId = createPost(session);
        Long parentCommentId = createComment(session, postId);

        mockMvc.perform(post("/api/study-posts/{postId}/comments", postId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "parentCommentId": %d,
                                  "content": "reply content"
                                }
                                """.formatted(parentCommentId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parentCommentId").value(parentCommentId))
                .andExpect(jsonPath("$.content").value("reply content"));

        mockMvc.perform(get("/api/study-posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].parentCommentId").value(parentCommentId))
                .andExpect(jsonPath("$[1].content").value("reply content"));
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
                                  "title": "댓글 테스트 모집글",
                                  "content": "댓글 기능을 확인하기 위한 모집글입니다.",
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
                                  "content": "참여하고 싶습니다."
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

