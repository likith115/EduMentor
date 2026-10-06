package com.example.studyagent.service;

import com.example.studyagent.dto.SignInRequest;
import com.example.studyagent.entity.User;
import com.example.studyagent.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(SignInRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already registered";
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encodedPassword);
        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(SignInRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return "Invalid email or password";
        }
        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())) {
            return "Invalid email or password";
        }

        return jwtService.generateToken(user.getEmail());
    }

}