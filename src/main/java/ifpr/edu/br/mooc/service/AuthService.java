package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.user.LoginRequest;
import ifpr.edu.br.mooc.dto.user.LoginResponse;
import ifpr.edu.br.mooc.dto.user.UserResponse;
import ifpr.edu.br.mooc.entity.User;
import ifpr.edu.br.mooc.exceptions.user.InvalidCredentialsException;
import ifpr.edu.br.mooc.exceptions.user.UserNotActiveException;
import ifpr.edu.br.mooc.mapper.UserMapper;
import ifpr.edu.br.mooc.repository.UserRepository;
import ifpr.edu.br.mooc.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public LoginResponse authenticate(LoginRequest request) {
        log.info("Authenticating user with email: {}", request.email());

        // Find user by email (only during login)
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Authentication failed: User not found with email: {}", request.email());
                    return new InvalidCredentialsException();
                });

        // Verify password
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Authentication failed: Invalid password for email: {}", request.email());
            throw new InvalidCredentialsException();
        }

        // Check if user is active
        if (!user.getActive()) {
            log.warn("Authentication failed: User is not active: {}", request.email());
            throw new UserNotActiveException(request.email());
        }

        // Generate JWT token with user information
        String accessToken = jwtUtils.generateToken(user);
        Long expiresIn = jwtUtils.getExpirationTime();

        // Convert user to response DTO
        UserResponse userResponse = userMapper.toResponse(user);

        log.info("User authenticated successfully: {}", user.getId());

        return new LoginResponse(accessToken, expiresIn, userResponse);
    }
}