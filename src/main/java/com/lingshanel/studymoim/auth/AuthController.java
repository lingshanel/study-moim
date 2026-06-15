package com.lingshanel.studymoim.auth;

import com.lingshanel.studymoim.auth.dto.FindNicknameRequest;
import com.lingshanel.studymoim.auth.dto.FindNicknameResponse;
import com.lingshanel.studymoim.auth.dto.LoginRequest;
import com.lingshanel.studymoim.auth.dto.PasswordResetCodeSendRequest;
import com.lingshanel.studymoim.auth.dto.PasswordResetCodeVerifyRequest;
import com.lingshanel.studymoim.auth.dto.PasswordResetCodeVerifyResponse;
import com.lingshanel.studymoim.auth.dto.PasswordResetConfirmRequest;
import com.lingshanel.studymoim.auth.dto.SignupRequest;
import com.lingshanel.studymoim.auth.dto.UserResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final CurrentUserService currentUserService;

    public AuthController(AuthService authService, CurrentUserService currentUserService) {
        this.authService = authService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signup(@Valid @RequestBody SignupRequest request, HttpSession session) {
        UserResponse response = authService.signup(request);
        session.setAttribute(AuthSession.LOGIN_USER_ID, response.id());
        return response;
    }

    @PostMapping("/auth/login")
    public UserResponse login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        UserResponse response = authService.login(request);
        session.setAttribute(AuthSession.LOGIN_USER_ID, response.id());
        return response;
    }

    @PostMapping("/admin/login")
    public UserResponse adminLogin(@Valid @RequestBody LoginRequest request, HttpSession session) {
        UserResponse response = authService.adminLogin(request);
        session.setAttribute(AuthSession.LOGIN_USER_ID, response.id());
        return response;
    }

    @PostMapping("/auth/find-nickname")
    public FindNicknameResponse findNickname(@Valid @RequestBody FindNicknameRequest request) {
        return authService.findNickname(request);
    }

    @PostMapping("/auth/password-reset/code")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendPasswordResetCode(@Valid @RequestBody PasswordResetCodeSendRequest request) {
        authService.sendPasswordResetCode(request);
    }

    @PostMapping("/auth/password-reset/verify")
    public PasswordResetCodeVerifyResponse verifyPasswordResetCode(
            @Valid @RequestBody PasswordResetCodeVerifyRequest request
    ) {
        return authService.verifyPasswordResetCode(request);
    }

    @PostMapping("/auth/password-reset/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
        authService.confirmPasswordReset(request);
    }

    @PostMapping("/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @GetMapping("/users/me")
    public UserResponse me(HttpSession session) {
        return UserResponse.from(currentUserService.getCurrentUser(session));
    }
}
