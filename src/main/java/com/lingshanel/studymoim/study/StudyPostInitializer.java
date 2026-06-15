package com.lingshanel.studymoim.study;

import com.lingshanel.studymoim.category.domain.Category;
import com.lingshanel.studymoim.category.repository.CategoryRepository;
import com.lingshanel.studymoim.study.domain.StudyPost;
import com.lingshanel.studymoim.study.domain.StudyType;
import com.lingshanel.studymoim.study.repository.StudyPostRepository;
import com.lingshanel.studymoim.user.domain.User;
import com.lingshanel.studymoim.user.repository.UserRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class StudyPostInitializer implements CommandLineRunner {

    private final StudyPostRepository studyPostRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    public StudyPostInitializer(
            StudyPostRepository studyPostRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.studyPostRepository = studyPostRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User sampleUser = userRepository.findByEmail("sample@study-moim.local")
                .orElseGet(() -> userRepository.save(new User(
                        "sample@study-moim.local",
                        passwordEncoder.encode("password123"),
                        "샘플스터디원"
                )));

        List<PostSeed> seeds = List.of(
                new PostSeed("backend", "Spring MVC 기본 흐름 같이 정리해요", "Controller, Service, Repository 역할을 예제 코드로 같이 정리하는 스터디입니다.", StudyType.ONLINE, "", 5),
                new PostSeed("cs", "CS 면접 질문 하루 5개씩", "네트워크, 데이터베이스, 운영체제 질문을 말로 설명해보는 연습을 같이 합니다.", StudyType.ONLINE, "", 6),
                new PostSeed("backend", "SQL 문제 풀이 기록 남기기", "MySQL 기준으로 JOIN, GROUP BY, 서브쿼리를 천천히 풀어봅니다.", StudyType.HYBRID, "온라인", 4),
                new PostSeed("frontend", "Vanilla JS DOM 조작 복습", "작은 화면을 만들면서 이벤트 처리와 fetch 흐름을 다시 공부합니다.", StudyType.ONLINE, "", 4),
                new PostSeed("interview", "신입 백엔드 면접 답변 연습", "아는 척보다 내가 이해한 만큼 차분히 설명하는 연습을 합니다.", StudyType.ONLINE, "", 5),
                new PostSeed("certificate", "정보처리기사 실기 기출 정리", "기출 문제를 나눠 풀고 헷갈린 개념을 같이 정리합니다.", StudyType.OFFLINE, "서울", 6)
        );

        for (PostSeed seed : seeds) {
            if (studyPostRepository.existsByTitle(seed.title())) {
                continue;
            }

            Category category = categoryRepository.findBySlug(seed.categorySlug())
                    .orElseThrow();
            studyPostRepository.save(new StudyPost(
                    sampleUser,
                    category,
                    seed.title(),
                    seed.content(),
                    seed.studyType(),
                    seed.location(),
                    seed.maxMembers()
            ));
        }
    }

    private record PostSeed(
            String categorySlug,
            String title,
            String content,
            StudyType studyType,
            String location,
            Integer maxMembers
    ) {
    }
}
