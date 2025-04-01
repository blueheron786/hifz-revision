package com.hifzrevision.web.authentication.filters;

import com.hifzrevision.web.authentication.token.JwtUtils;
import com.hifzrevision.web.configuration.ApiRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    private final RequestMatcher apiMatcher = new ApiRequestMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // Skip all non-API requests
        if (!apiMatcher.matches(request)) {
            chain.doFilter(request, response);
            return;
        }

        // Process API requests
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
            return;
        }

        String token = header.substring(7);
        if (!jwtUtils.validateJwtToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(
                jwtUtils.getAuthentication(token)
        );
        chain.doFilter(request, response);
    }
}