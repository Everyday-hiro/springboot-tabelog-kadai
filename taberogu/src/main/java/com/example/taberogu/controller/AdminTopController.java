package com.example.taberogu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminTopController {
	@GetMapping
	public String top() {
		return "admin/adminTop";
	}
}
