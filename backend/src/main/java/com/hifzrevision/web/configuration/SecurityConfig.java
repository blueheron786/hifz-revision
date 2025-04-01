package com.hifzrevision.web.configuration;

import com.hifzrevision.web.authentication.token.JwtUtils;
import com.hifzrevision.web.model.User;
import com.hifzrevision.web.repository.UserRepository;
import com.hifzrevision.web.authentication.filters.JwtAuthenticationFilter;
import com.hifzrevision.web.authentication.filters.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public SecurityConfig(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
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
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        // No need for manual UserDetails conversion now
    }

    // 3. Password encoder remains unchanged
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 4. Main security configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/favicon.ico").permitAll()
                        .requestMatchers("/static/**", "/assets/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/debug-test").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtils),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new JwtAuthorizationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}