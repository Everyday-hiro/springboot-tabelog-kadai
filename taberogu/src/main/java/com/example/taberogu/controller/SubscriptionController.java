package com.example.taberogu.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.User;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.StripeService;
import com.stripe.exception.StripeException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SubscriptionController {
	private final StripeService stripeService;

	public SubscriptionController(StripeService stripeService) {
		this.stripeService = stripeService;
	}

	@GetMapping("/subsc")
	public String subsc(HttpServletRequest httpServletRequest,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model) {
		User user = userDetailsImpl.getUser(); // 現在のユーザー情報を取得するメソッド

		// サブスクリプション作成のためのセッションIDを生成
		String sessionId = stripeService.createSubscription(user, httpServletRequest);

		// セッションIDをモデルに追加
		model.addAttribute("sessionId", sessionId);
		return "user/subscription";
	}

	@GetMapping("/user/success")
	public String success(@RequestParam("session_id") String sessionId, RedirectAttributes redirectAttributes) {
		// 成功した場合の処理
		redirectAttributes.addFlashAttribute("successMessage", "サブスクリプション支払いが成功しました。");
		return "redirect:/user"; // 成功メッセージページにリダイレクト
	}

	@GetMapping("/user/cancel")
	public String cancel(RedirectAttributes redirectAttributes) {
		// キャンセルした場合の処理
		redirectAttributes.addFlashAttribute("errorMessage", "サブスクリプション支払いが失敗しました。");
		return "redirect:/user"; // キャンセルページにリダイレクト
	}

	@GetMapping("/withdrawal")
	public String withdrawal(Model model) {
		return "user/withdrawal";
	}

	@PostMapping("/cancel-subscription")
	public String cancelSubscription(Authentication authentication) {
		// 認証情報からcustomerIdを取得する仮定
		String customerId = getCustomerIdFromAuth(authentication);
		try {
			stripeService.cancelSubscription(customerId);
			return "Subscription canceled successfully.";
		} catch (StripeException e) {
			return "Failed to cancel subscription: " + e.getMessage();
		}
	}

	private String getCustomerIdFromAuth(Authentication authentication) {
		// ここで認証情報からcustomerIdを取得するロジックを配置
		// 例: UserDetailsのカスタム実装から取得
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return userDetails.getCustomerId(); // 仮のメソッド、実装に応じて変更する必要あり
	}
}
