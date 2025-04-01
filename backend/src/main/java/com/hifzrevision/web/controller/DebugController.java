package com.hifzrevision.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DebugController {
    @RequestMapping("/debug-test")
    public String test() {
        return "This works!";
    }
}