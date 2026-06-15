package com.lingshanel.studymoim.auth;

import com.lingshanel.studymoim.auth.dto.FindNicknameRequest;
import com.lingshanel.studymoim.auth.dto.FindNicknameResponse;
import com.lingshanel.studymoim.auth.dto.LoginRequest;
import com.lingshanel.studymoim.auth.domain.PasswordResetVerification;
import com.lingshanel.studymoim.auth.dto.PasswordResetCodeSendRequest;
import com.lingshanel.studymoim.auth.dto.PasswordResetCodeVerifyRequest;
import com.lingshanel.studymoim.auth.dto.PasswordResetCodeVerifyResponse;
import com.lingshanel.studymoim.auth.dto.PasswordResetConfirmRequest;
import com.lingshanel.studymoim.auth.dto.SignupRequest;
import com.lingshanel.studymoim.auth.dto.UserResponse;
import com.lingshanel.studymoim.auth.repository.PasswordResetVerificationRepository;
import com.lingshanel.studymoim.common.error.BadRequestException;
import com.lingshanel.studymoim.common.error.UnauthorizedException;
import com.lingshanel.studymoim.user.domain.User;
import com.lingshanel.studymoim.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final int MAXIMUM_LOGIN_ATTEMPTS = 5;
    private static final int LOGIN_LOCK_MINUTES = 10;
    private static final int CODE_EXPIRATION_MINUTES = 5;
    private static final int TOKEN_EXPIRATION_MINUTES = 10;
    private static final int MAXIMUM_CODE_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetVerificationRepository verificationRepository;
    private final PasswordResetCodeGenerator codeGenerator;
    private final PasswordResetCodeSender codeSender;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PasswordResetVerificationRepository verificationRepository,
            PasswordResetCodeGenerator codeGenerator,
            PasswordResetCodeSender codeSender
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationRepository = verificationRepository;
        this.codeGenerator = codeGenerator;
        this.codeSender = codeSender;
    }

    @Transactional
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(request.nickname())) {
            throw new BadRequestException("이미 사용 중인 닉네임입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.email(), encodedPassword, request.nickname());
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional(noRollbackFor = UnauthorizedException.class)
    public UserResponse login(LoginRequest request) {
        return authenticate(request, false);
    }

    @Transactional(noRollbackFor = UnauthorizedException.class)
    public UserResponse adminLogin(LoginRequest request) {
        return authenticate(request, true);
    }

    private UserResponse authenticate(LoginRequest request, boolean adminOnly) {
        User user = userRepository.findByNickname(request.nickname())
                .orElseThrow(() -> new UnauthorizedException("닉네임 또는 비밀번호가 올바르지 않습니다."));

        LocalDateTime now = LocalDateTime.now();
        if (user.isLoginLocked(now)) {
            long remainingMinutes = Math.max(1, ChronoUnit.MINUTES.between(now, user.getLoginLockedUntil()) + 1);
            throw new UnauthorizedException("로그인 시도가 일시적으로 제한되었습니다. 약 " + remainingMinutes + "분 후 다시 시도해주세요.");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            user.registerLoginFailure(MAXIMUM_LOGIN_ATTEMPTS, now.plusMinutes(LOGIN_LOCK_MINUTES));
            if (user.isLoginLocked(now)) {
                throw new UnauthorizedException("로그인에 여러 번 실패하여 10분 동안 로그인이 제한됩니다.");
            }
            int remainingAttempts = MAXIMUM_LOGIN_ATTEMPTS - user.getFailedLoginAttempts();
            throw new UnauthorizedException("닉네임 또는 비밀번호가 올바르지 않습니다. 남은 시도: " + remainingAttempts + "회");
        }

        if (!user.isActive()) {
            if (user.getStatus().name().equals("BANNED")) {
                String reason = user.getBanReason() == null || user.getBanReason().isBlank()
                        ? "운영 정책 위반"
                        : user.getBanReason();
                throw new UnauthorizedException("제재된 계정입니다. 사유: " + reason + " 관리자에게 문의해주세요.");
            }
            throw new UnauthorizedException("사용할 수 없는 계정입니다.");
        }

        if (adminOnly && !user.isAdmin()) {
            throw new UnauthorizedException("관리자 권한이 필요합니다.");
        }

        user.resetLoginFailures();
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public FindNicknameResponse findNickname(FindNicknameRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("가입된 이메일을 찾을 수 없습니다."));

        return new FindNicknameResponse(user.getNickname());
    }

    @Transactional
    public void sendPasswordResetCode(PasswordResetCodeSendRequest request) {
        userRepository.findByEmailAndNickname(request.email(), request.nickname())
                .orElseThrow(() -> new BadRequestException("입력한 계정 정보를 확인할 수 없습니다."));

        String code = codeGenerator.generateCode();
        verificationRepository.save(new PasswordResetVerification(
                request.email(),
                request.nickname(),
                passwordEncoder.encode(code),
                LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES)
        ));
        codeSender.send(request.email(), code);
    }

    @Transactional(noRollbackFor = BadRequestException.class)
    public PasswordResetCodeVerifyResponse verifyPasswordResetCode(PasswordResetCodeVerifyRequest request) {
        PasswordResetVerification verification = verificationRepository
                .findTopByEmailAndNicknameOrderByIdDesc(request.email(), request.nickname())
                .orElseThrow(() -> new BadRequestException("인증번호를 먼저 요청해주세요."));

        LocalDateTime now = LocalDateTime.now();
        if (verification.getVerifiedAt() != null || verification.getConsumedAt() != null) {
            throw new BadRequestException("이미 사용된 인증 요청입니다. 인증번호를 다시 요청해주세요.");
        }
        if (verification.getCodeExpiresAt().isBefore(now)) {
            throw new BadRequestException("인증번호가 만료되었습니다. 새 인증번호를 요청해주세요.");
        }
        if (verification.getFailedCodeAttempts() >= MAXIMUM_CODE_ATTEMPTS) {
            throw new BadRequestException("인증번호 확인 횟수를 초과했습니다. 새 인증번호를 요청해주세요.");
        }
        if (!passwordEncoder.matches(request.code(), verification.getCodeHash())) {
            verification.registerCodeFailure();
            int remainingAttempts = MAXIMUM_CODE_ATTEMPTS - verification.getFailedCodeAttempts();
            throw new BadRequestException("인증번호가 올바르지 않습니다. 남은 시도: " + remainingAttempts + "회");
        }

        String rawToken = UUID.randomUUID().toString();
        verification.verify(
                passwordEncoder.encode(rawToken),
                now.plusMinutes(TOKEN_EXPIRATION_MINUTES)
        );
        return new PasswordResetCodeVerifyResponse(verification.getId() + "." + rawToken);
    }

    @Transactional
    public void confirmPasswordReset(PasswordResetConfirmRequest request) {
        String[] tokenParts = request.resetToken().split("\\.", 2);
        if (tokenParts.length != 2) {
            throw new BadRequestException("유효하지 않은 비밀번호 재설정 요청입니다.");
        }

        Long verificationId;
        try {
            verificationId = Long.valueOf(tokenParts[0]);
        } catch (NumberFormatException exception) {
            throw new BadRequestException("유효하지 않은 비밀번호 재설정 요청입니다.");
        }

        PasswordResetVerification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new BadRequestException("유효하지 않은 비밀번호 재설정 요청입니다."));
        LocalDateTime now = LocalDateTime.now();
        if (verification.getVerifiedAt() == null
                || verification.getConsumedAt() != null
                || verification.getResetTokenExpiresAt() == null
                || verification.getResetTokenExpiresAt().isBefore(now)
                || !passwordEncoder.matches(tokenParts[1], verification.getResetTokenHash())) {
            throw new BadRequestException("비밀번호 재설정 요청이 만료되었거나 이미 사용되었습니다.");
        }

        User user = userRepository.findByEmailAndNickname(verification.getEmail(), verification.getNickname())
                .orElseThrow(() -> new BadRequestException("계정을 찾을 수 없습니다."));
        user.changePassword(passwordEncoder.encode(request.newPassword()));
        user.resetLoginFailures();
        verification.consume();
    }
}
