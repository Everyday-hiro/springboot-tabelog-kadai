package com.example.taberogu.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.User;
import com.example.taberogu.repository.UserRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class StripeWebhookController {
	private final StripeService stripeService;
	private final UserRepository userRepository;

	@Value("${stripe.api-key}")
	private String stripeApiKey;

	@Value("${stripe.webhook-secret}")
	private String webhookSecret;

	public StripeWebhookController(StripeService stripeService, UserRepository userRepository) {
		this.stripeService = stripeService;
		this.userRepository = userRepository;
	}

	@PostMapping("/stripe/webhook")
	public ResponseEntity<String> webhook(@RequestBody String payload,
			@RequestHeader("Stripe-Signature") String sigHeader, Integer userId) {
		Stripe.apiKey = stripeApiKey;
		Event event = null;

		try {
			event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
		} catch (SignatureVerificationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		if ("checkout.session.completed".equals(event.getType())) {
			stripeService.processSessionCompleted(event);
		}

		if ("customer.subscription.created".equals(event.getType())) {
			Subscription subscription = (Subscription) event.getData().getObject();
			String subscriptionId = subscription.getId();

			// サブスクリプションIDのみを処理する
			stripeService.processSubscriptionCreated(subscriptionId);
		}

		return new ResponseEntity<>("Success", HttpStatus.OK);

	}

	@PostMapping("/create-checkout-session")
	public String createCheckoutSession(@RequestParam("lookup_key") String lookupKey,
			HttpServletRequest httpServletRequest,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes) {
		// 現在のユーザー情報を取得
		User user = userDetailsImpl.getUser();

		// サブスクリプションの作成とセッションIDの取得
		String sessionId = stripeService.createSubscription(user, httpServletRequest);

		if (sessionId != null && !sessionId.isEmpty()) {
			// セッションIDをリダイレクト先に渡す
			redirectAttributes.addAttribute("session_id", sessionId);
			return "redirect:/user/success";
		} else {
			// 失敗した場合の処理
			redirectAttributes.addFlashAttribute("errorMessage", "サブスクリプションの作成に失敗しました。");
			return "redirect:/user/cancel";
		}
	}
}
