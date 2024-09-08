package com.example.taberogu.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.taberogu.entity.User;
import com.example.taberogu.entity.VerificationToken;
import com.example.taberogu.service.UserService;
import com.example.taberogu.service.VerificationTokenService;

@Controller
@RequestMapping("/password")
public class PasswordResetController {
	private final VerificationTokenService verificationTokenService;
	private final UserService userService;

	public PasswordResetController(VerificationTokenService verificationTokenService, UserService userService) {
		this.verificationTokenService = verificationTokenService;
		this.userService = userService;
	}

	@GetMapping("/request")
	public String passwordRequest() {
		return "user/passwordResetRequest";
	}

	// パスワード再設定リクエスト
	@PostMapping("/reset-request")
	public String resetPasswordRequest(@RequestParam("email") String email) {
		// ユーザー取得やトークン生成をサービスに依頼
		User user = userService.findByEmail(email);
		if (user != null) {
			String token = UUID.randomUUID().toString();
			verificationTokenService.createAndPublishEvent(user, token);
		} else {
			System.out.println("User not found for email: " + email);
		}
		return "redirect:/";
	}

	// 再設定ページ表示
	@GetMapping("/reset")
	public String showResetForm(@RequestParam("token") String token, Model model) {
		VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
		if (verificationToken == null) {
			model.addAttribute("error", "無効なトークンです。");
			return "password/reset-error";
		}
		model.addAttribute("token", token);
		return "password/reset-form";
	}

	// 新しいパスワードの適用
	@PostMapping("/reset")
	public String applyNewPassword(@RequestParam("token") String token, @RequestParam("password") String password,
			Model model) {
		VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
		if (verificationToken == null) {
			model.addAttribute("error", "無効なトークンです。");
			return "password/reset-error";
		}
		User user = verificationToken.getUser();
		userService.updatePassword(user, password);
		return "redirect:/login?resetSuccess";
	}
}
