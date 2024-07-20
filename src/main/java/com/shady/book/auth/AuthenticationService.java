package com.shady.book.auth;

import com.shady.book.role.RoleRepository;
import com.shady.book.user.User;
import com.shady.book.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void register(RegistrationRequest request) {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(()-> new IllegalStateException("Role user wasn't Initialized"));
        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);

    }
}
