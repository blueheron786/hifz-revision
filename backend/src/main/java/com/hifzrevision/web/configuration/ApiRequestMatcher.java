package com.hifzrevision.web.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

public class ApiRequestMatcher implements RequestMatcher {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean matches(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/");
    }
}
