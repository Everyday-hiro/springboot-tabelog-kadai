package com.example.taberogu.service;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.taberogu.form.ReservationRegisterForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;
	private final ReservationService reservationService;
	private static final Logger logger = LoggerFactory.getLogger(StripeService.class);

	public StripeService(ReservationService reservationService) {
		this.reservationService = reservationService;
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
		logger.info("processSessionCompleted メソッドが呼び出されました。");
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
}
