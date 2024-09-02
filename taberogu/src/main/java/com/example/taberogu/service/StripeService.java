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
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerListParams;
import com.stripe.param.SubscriptionListParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

import jakarta.servlet.http.HttpServletRequest;

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
		String requestUrl = new String(httpServletRequest.getRequestURL());

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

	public void processReservationPayment(Event event) {
		// TODO 自動生成されたメソッド・スタブ
	}

	public Subscription getSubscriptionByLookupKey(String lookupKey) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public void cancelSubscription(String email) throws StripeException {
		Stripe.apiKey = stripeApiKey;

		// email から customerId を取得
		String customerId = findCustomerIdByEmail(email);

		if (customerId == null) {
			throw new RuntimeException("Customer with email " + email + " not found.");
		}

		// サブスクリプションのリストを取得
		SubscriptionListParams params = SubscriptionListParams.builder()
				.setCustomer(customerId)
				.build();

		SubscriptionCollection subscriptions = Subscription.list(params);

		if (subscriptions.getData().isEmpty()) {
			throw new RuntimeException("No subscriptions found for customer.");
		}

		// 最初のサブスクリプションを取得し解約
		String subscriptionId = subscriptions.getData().get(0).getId();
		Subscription subscription = Subscription.retrieve(subscriptionId);
		subscription.cancel();

		// 認証情報からユーザーを取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		User user = userDetails.getUser();

		if (user != null) {
			// RoleRepositoryを使用してROLE_FREEのRoleを取得
			Role roleFree = roleRepository.findByName("ROLE_FREE");
			if (roleFree != null) {
				user.setRole(roleFree); // Roleエンティティをセット
				userRepository.save(user); // 変更をデータベースに保存
			} else {
				throw new RuntimeException("ROLE_FREE role not found.");
			}
		}
	}

	private String findCustomerIdByEmail(String email) throws StripeException {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Email cannot be null or empty");
		}

		Stripe.apiKey = "your-stripe-api-key";

		// 顧客のリストを取得
		CustomerListParams params = CustomerListParams.builder()
				.setLimit((long) 100) // 必要に応じて取得件数を調整
				.build();

		CustomerCollection customers = Customer.list(params);

		for (Customer customer : customers.getData()) {
			// email が一致するか確認
			if (email.equals(customer.getEmail())) {
				return customer.getId(); // customerId を返す
			}
		}
		return null; // 該当する顧客が見つからない場合
	}
}