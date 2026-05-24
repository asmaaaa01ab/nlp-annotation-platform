package com.nlpAnnotation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	@GetMapping("/test")
    public String test() {
        return "login"; // Devrait charger login.html
    }
}
