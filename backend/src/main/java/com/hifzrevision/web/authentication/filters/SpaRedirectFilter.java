package com.hifzrevision.web.authentication.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpaRedirectFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();

        if (!path.startsWith("/api")
                && !path.contains(".")
                && !path.startsWith("/ws")) {
            request.getRequestDispatcher("/").forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
