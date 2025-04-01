package com.hifzrevision.web.configuration;

import com.hifzrevision.web.authentication.filters.JwtAuthorizationFilter;
import com.hifzrevision.web.authentication.token.JwtUtils;
import com.hifzrevision.web.repository.UserRepository;
import com.hifzrevision.web.authentication.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final JwtAuthenticationFilter authFilter;

    @Autowired
    public SecurityConfig(UserRepository userRepository, JwtUtils jwtUtils, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authFilter = new JwtAuthenticationFilter(authManager, jwtUtils);
    }

    // 1. Extract AuthenticationManager creation to separate bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 2. Separate UserDetailsService configuration
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    // 3. Password encoder remains unchanged
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 4. Main security configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. Get AuthenticationManager without circular reference
        AuthenticationManager authManager = http.getSharedObject(AuthenticationManager.class);

        // 2. Create filter instance
        JwtAuthenticationFilter authFilter = new JwtAuthenticationFilter(authManager, jwtUtils);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilter(authFilter)
                .addFilterBefore(
                        new JwtAuthorizationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}