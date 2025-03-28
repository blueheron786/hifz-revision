package com.hifzrevision.web.authentication;

import com.hifzrevision.web.user.User;
import com.hifzrevision.web.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Validate password (e.g., using BCrypt, plain-text for demo)
        if (password.equals(user.getPassword())) {
            return jwtTokenManager.generateToken(username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}

