package com.shady.book.auth;

import com.shady.book.email.EmailService;
import com.shady.book.email.EmailTemplate;
import com.shady.book.role.RoleRepository;
import com.shady.book.user.Token;
import com.shady.book.user.TokenRepository;
import com.shady.book.user.User;
import com.shady.book.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(()-> new IllegalStateException("Role user wasn't Initialized"));
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);

    }

    private void sendValidationEmail(User user) throws MessagingException {

        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplate.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );


    }

    private String generateAndSaveActivationToken(User user) {
        // first step lets generate a token
        // so we here created a valid token for 15 mins
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdTAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenRepository.save(token);
        return generatedToken;
    }

        private String generateActivationCode(int length) {
        String chars = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
            SecureRandom secureRandom = new SecureRandom();
            for (int i = 0 ; i < length ; i++){
                int randomIndex = secureRandom.nextInt(chars.length());
                codeBuilder.append(chars.charAt(randomIndex));
            }
        return codeBuilder.toString();
    }
}
