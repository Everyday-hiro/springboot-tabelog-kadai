package com.example.taberogu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.User;
import com.example.taberogu.entity.VerificationToken;
import com.example.taberogu.service.UserService;
import com.example.taberogu.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

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
		return "password/passwordResetRequest";
	}

	// パスワード再設定リクエスト
	@PostMapping("/reset-request")
	public String resetPasswordRequest(@RequestParam("email") String email, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		// ユーザー取得やトークン生成をサービスに依頼
		User user = userService.findByEmail(email);
		if (user != null) {
			String token = verificationTokenService.createVerificationToken(user); // リセット用トークンの生成
			String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath();
			String resetUrl = appUrl + "/password/reset?token=" + token;
			userService.sendPasswordResetEmail(user.getEmail(), resetUrl);
			redirectAttributes.addFlashAttribute("successMessage",
					"ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、パスワードの再設定を完了してください。");
		}
		return "redirect:/";
	}

	// 再設定ページ表示
	@GetMapping("/reset")
	public String showResetForm(@RequestParam("token") String token, Model model,
			RedirectAttributes redirectAttributes) {
		VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
		if (verificationToken == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "無効なトークンです。");
			return "password/reset";
		}
		model.addAttribute("token", token);
		return "password/reset";
	}

	// 新しいパスワードの適用
	@PostMapping("/reset/confirm")
	public String confirmPasswordReset(@RequestParam("token") String token,
			@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes) {
		System.out.println("confirmPasswordReset called with token: " + token);
		VerificationToken verificationToken = verificationTokenService.findByToken(token);
		if (verificationToken == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "パスワード再設定に失敗しました。");
			return "redirect:/";
		}

		User user = verificationToken.getUser();
		if (!userService.checkPassword(user, currentPassword)) {
			redirectAttributes.addFlashAttribute("errorMessage", "現在のパスワードが正しくありません。");
			return "redirect:/";
		}

		// パスワードの更新
		userService.updatePassword(user, newPassword);
		redirectAttributes.addFlashAttribute("successMessage", "パスワードが正常に再設定されました。");
		return "redirect:/"; // パスワード再設定成功後のリダイレクト先
	}
}
