package com.lingshanel.studymoim.user;

import com.lingshanel.studymoim.common.error.BadRequestException;
import com.lingshanel.studymoim.common.error.NotFoundException;
import com.lingshanel.studymoim.user.domain.User;
import com.lingshanel.studymoim.user.dto.PasswordChangeRequest;
import com.lingshanel.studymoim.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public void changePassword(User user, PasswordChangeRequest request) {
        User managedUser = getUser(user.getId());
        if (!passwordEncoder.matches(request.currentPassword(), managedUser.getPassword())) {
            throw new BadRequestException("현재 비밀번호가 올바르지 않습니다.");
        }
        managedUser.changePassword(passwordEncoder.encode(request.newPassword()));
    }

    @Transactional
    public void delete(User user) {
        getUser(user.getId()).delete();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }
}
