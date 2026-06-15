package com.lingshanel.studymoim.auth;

import com.lingshanel.studymoim.common.error.UnauthorizedException;
import com.lingshanel.studymoim.user.domain.User;
import com.lingshanel.studymoim.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(HttpSession session) {
        Object userId = session.getAttribute(AuthSession.LOGIN_USER_ID);
        if (!(userId instanceof Long id)) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("로그인이 필요합니다."));
        if (!user.isActive()) {
            throw new UnauthorizedException("사용할 수 없는 계정입니다.");
        }
        return user;
    }
}
