package com.hifzrevision.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public API";
    }

    @GetMapping("/protected")
    @PreAuthorize("isAuthenticated()")
    public String protectedEndpoint() {
        return "Protected API";
    }
}