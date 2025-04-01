package com.hifzrevision.web;

import com.hifzrevision.web.authentication.token.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
class BareApp {
	public static void main(String[] args) {
		SpringApplication.run(BareApp.class, args);
	}
}

@Controller
class OnlyController {
	@RequestMapping("/**")
	public String wildcard() {
		return "forward:/";
	}
}