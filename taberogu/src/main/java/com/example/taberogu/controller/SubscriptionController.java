package com.example.taberogu.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.Role;
import com.example.taberogu.entity.User;
import com.example.taberogu.repository.RoleRepository;
import com.example.taberogu.repository.UserRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.StripeService;
import com.stripe.exception.StripeException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SubscriptionController {
	private final StripeService stripeService;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public SubscriptionController(StripeService stripeService, UserRepository userRepository,
			RoleRepository roleRepository) {
		this.stripeService = stripeService;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
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
	public String cancelSubscription(@AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
		try {
			// 現在のユーザーからサブスクリプションIDを取得
			String subscriptionId = user.getSubscriptionId();

			if (subscriptionId != null) {
				// Stripeでサブスクリプションをキャンセル
				stripeService.cancelSubscription(subscriptionId);

				// ユーザーのサブスクリプションIDをクリア
				user.setSubscriptionId(null);
				Role freeRole = roleRepository.findById(1)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				user.setRole(freeRole);
				userRepository.save(user);

				redirectAttributes.addFlashAttribute("message", "サブスクリプションがキャンセルされました。");
			} else {
				redirectAttributes.addFlashAttribute("error", "サブスクリプションIDが見つかりません。");
			}
		} catch (StripeException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "サブスクリプションのキャンセル中にエラーが発生しました。");
		}

		return "redirect:/user/profile"; // キャンセル後のリダイレクト先
	}
}
