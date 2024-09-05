package com.example.taberogu.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.taberogu.entity.Role;
import com.example.taberogu.entity.User;
import com.example.taberogu.form.ReservationRegisterForm;
import com.example.taberogu.repository.RoleRepository;
import com.example.taberogu.repository.UserRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.SubscriptionCancelParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class StripeService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;
	private final ReservationService reservationService;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserService userService;

	public StripeService(ReservationService reservationService, UserRepository userRepository,
			RoleRepository roleRepository, UserService userService) {
		this.reservationService = reservationService;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userService = userService;
	}

	public String createStripeSession(String restaurantName, ReservationRegisterForm reservationRegisterForm,
			HttpServletRequest httpServletRequest) {
		Stripe.apiKey = stripeApiKey;
		String requestUrl = new String(httpServletRequest.getRequestURL());
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.addLineItem(
						SessionCreateParams.LineItem.builder()
								.setPriceData(
										SessionCreateParams.LineItem.PriceData.builder()
												.setProductData(
														SessionCreateParams.LineItem.PriceData.ProductData.builder()
																.setName(restaurantName)
																.build())
												.setUnitAmount((long) reservationRegisterForm.getAmount())
												.setCurrency("jpy")
												.build())
								.setQuantity(1L)
								.build())
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(
						requestUrl.replaceAll("/restaurant/[0-9]+/reservation/confirm", "") + "/reservation?reserved")
				.setCancelUrl(requestUrl.replace("/reservation/confirm", ""))
				.setPaymentIntentData(
						SessionCreateParams.PaymentIntentData.builder()
								.putMetadata("restaurantId", reservationRegisterForm.getRestaurantId().toString())
								.putMetadata("userId", reservationRegisterForm.getUserId().toString())
								.putMetadata("checkinDate", reservationRegisterForm.getCheckinDate())
								.putMetadata("numberOfPeople", reservationRegisterForm.getNumberOfPeople().toString())
								.putMetadata("amount", reservationRegisterForm.getAmount().toString())
								.build())
				.build();
		try {
			Session session = Session.create(params);
			return session.getId();
		} catch (StripeException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void processSessionCompleted(Event event) {
		Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
		optionalStripeObject.ifPresentOrElse(stripeObject -> {
			Session session = (Session) stripeObject;
			SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("payment_intent").build();

			try {
				session = Session.retrieve(session.getId(), params, null);
				Map<String, String> paymentIntentObject = session.getPaymentIntentObject().getMetadata();
				reservationService.create(paymentIntentObject);
			} catch (StripeException e) {
				e.printStackTrace();
			}
			System.out.println("予約一覧ページの登録処理が成功しました。");
			System.out.println("Stripe API Version: " + event.getApiVersion());
			System.out.println("stripe-java Version: " + Stripe.VERSION);
		},
				() -> {
					System.out.println("予約一覧ページの登録処理が失敗しました。");
					System.out.println("Stripe API Version: " + event.getApiVersion());
					System.out.println("stripe-java Version: " + Stripe.VERSION);
				});
	}

	public String createSubscription(User user, HttpServletRequest httpServletRequest) {
		Stripe.apiKey = stripeApiKey;
		String MY_DOMAIN = "http://localhost:8080";

		// セッション作成
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.addLineItem(
						SessionCreateParams.LineItem.builder()
								.setQuantity(1L)
								.setPrice("price_1PrWUNIzVhtPxktW0z7zQ0n8") // あなたが作成した価格ID
								.build())
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.setSuccessUrl(MY_DOMAIN + "/user/success?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl(MY_DOMAIN + "/user/cancel")
				.build();

		try {
			Session session = Session.create(params);

			if (session != null && session.getId() != null) {

				Role paidMemberRole = roleRepository.findById(2)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				user.setRole(paidMemberRole);
				// ユーザーの情報を更新
				userRepository.save(user);
			}
			return session.getId();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Transactional
	public void processSubscriptionCreated(String subscriptionId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			System.out.println("Authentication: " + authentication.getClass().getName());
			System.out.println("Principal: " + authentication.getPrincipal());
		} else {
			System.err.println("Authentication is null.");
		}
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		User user = userDetailsImpl.getUser();
		user.setSubscriptionId(subscriptionId);
		userRepository.save(user);
	}

	public void cancelSubscription(String subscriptionId) throws StripeException {
		Stripe.apiKey = stripeApiKey;

		SubscriptionCancelParams params = SubscriptionCancelParams.builder()
				.setProrate(true)
				.build();

		Subscription subscription = Subscription.retrieve(subscriptionId);
		subscription.cancel(params);
	}

	public void processReservationPayment(Event event) {
		// TODO 自動生成されたメソッド・スタブ
	}

	public Subscription getSubscriptionByLookupKey(String lookupKey) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public Subscription getSubscriptionByCustomerId(String customerId) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}