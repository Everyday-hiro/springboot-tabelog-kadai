package com.example.taberogu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.taberogu.service.StripeService;
import com.stripe.exception.StripeException;

@Controller
@RequestMapping("/api/payment")
public class CreditController {
	@Autowired
	private StripeService stripeService;

	@GetMapping("/credit")
	public String register() {
		return "credit/register";
	}

	@GetMapping("/create-checkout")
	public String createCheckoutSession(Model model) {
		try {
			String sessionId = stripeService.createCheckoutSession();
			model.addAttribute("sessionId", sessionId); // テンプレートにsessionIdを渡す
		} catch (StripeException e) {
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // エラーメッセージもテンプレートに渡す
		}
		return "credit/register"; // テンプレートのパスを返す
	}
}
