package com.lingshanel.studymoim.study;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudyPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudyPostRepository studyPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        studyPostRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createAndSearchStudyPost() throws Exception {
        MockHttpSession session = signupAndLogin("post@example.com", "writer");

        mockMvc.perform(post("/api/study-posts")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "categorySlug": "backend",
                                  "title": "Spring MVC study",
                                  "content": "We study Controller, Service, Repository flow.",
                                  "studyType": "ONLINE",
                                  "location": "",
                                  "maxMembers": 5
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Spring MVC study"))
                .andExpect(jsonPath("$.categorySlug").value("backend"))
                .andExpect(jsonPath("$.authorNickname").value("writer"));

        mockMvc.perform(get("/api/study-posts")
                        .param("keyword", "Spring")
                        .param("category", "backend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Spring MVC study"));
    }

    @Test
    void updateAndDeleteStudyPostByWriter() throws Exception {
        MockHttpSession session = signupAndLogin("writer@example.com", "edit-writer");
        Long postId = createPost(session);

        mockMvc.perform(patch("/api/study-posts/{postId}", postId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "categorySlug": "cs",
                                  "title": "CS interview study",
                                  "content": "We study network and database questions.",
                                  "studyType": "HYBRID",
                                  "location": "Seoul",
                                  "maxMembers": 4
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("CS interview study"))
                .andExpect(jsonPath("$.categorySlug").value("cs"));

        mockMvc.perform(delete("/api/study-posts/{postId}", postId).session(session))
                .andExpect(status().isNoContent());
    }

    @Test
    void createStudyPostRequiresLogin() throws Exception {
        mockMvc.perform(post("/api/study-posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "categorySlug": "backend",
                                  "title": "anonymous post",
                                  "content": "Guest users cannot create posts.",
                                  "studyType": "ONLINE",
                                  "location": "",
                                  "maxMembers": 3
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void uploadStudyPostImageByWriter() throws Exception {
        MockHttpSession session = signupAndLogin("image@example.com", "image-writer");
        Long postId = createPost(session);
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "study.png",
                "image/png",
                "sample".getBytes()
        );

        mockMvc.perform(multipart("/api/study-posts/{postId}/image", postId)
                        .file(image)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").exists());
    }

    @Test
    void getStudyPostIncreasesViewCountOncePerSession() throws Exception {
        MockHttpSession session = signupAndLogin("view@example.com", "view-user");
        Long postId = createPost(session);

        mockMvc.perform(get("/api/study-posts/{postId}", postId).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viewCount").value(1));

        mockMvc.perform(get("/api/study-posts/{postId}", postId)
                        .session(session)
                        .param("increaseView", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viewCount").value(1));

        mockMvc.perform(get("/api/study-posts/{postId}", postId).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viewCount").value(1));

        mockMvc.perform(get("/api/study-posts/{postId}", postId).session(new MockHttpSession()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viewCount").value(2));
    }

    @Test
    void searchStudyPostsSupportsFiltersAndPaginationBounds() throws Exception {
        MockHttpSession session = signupAndLogin("filter@example.com", "filter-user");
        Long recruitingPostId = createPost(session, "backend", "Spring Boot filter test", "Spring keyword content", "ONLINE", "", 4);
        Long closedPostId = createPost(session, "backend", "Spring closed test", "Spring keyword content", "ONLINE", "", 4);
        createPost(session, "frontend", "JavaScript filter test", "browser keyword content", "OFFLINE", "Seoul", 3);

        mockMvc.perform(patch("/api/study-posts/{postId}/status", closedPostId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recruitmentStatus": "CLOSED"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/study-posts")
                        .param("category", "backend")
                        .param("keyword", "Spring")
                        .param("studyType", "ONLINE")
                        .param("status", "RECRUITING")
                        .param("page", "-3")
                        .param("size", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(50))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(recruitingPostId))
                .andExpect(jsonPath("$.content[0].recruitmentStatus").value("RECRUITING"));
    }

    @Test
    void searchStudyPostsRejectsUnsupportedFilters() throws Exception {
        mockMvc.perform(get("/api/study-posts").param("studyType", "REMOTE"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/study-posts").param("status", "WAITING"))
                .andExpect(status().isBadRequest());
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
        return createPost(session, "backend", "first study post", "post content for update and delete test", "ONLINE", "", 5);
    }

    private Long createPost(
            MockHttpSession session,
            String categorySlug,
            String title,
            String content,
            String studyType,
            String location,
            int maxMembers
    ) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/study-posts")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "categorySlug": "%s",
                                  "title": "%s",
                                  "content": "%s",
                                  "studyType": "%s",
                                  "location": "%s",
                                  "maxMembers": %d
                                }
                                """.formatted(categorySlug, title, content, studyType, location, maxMembers)))
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
