package com.example.taberogu.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taberogu.entity.Reservation;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;
import com.example.taberogu.repository.ReservationRepository;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.repository.UserRepository;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final RestaurantRepository restaurantRepository;
	private final UserRepository userRepository;

	public ReservationService(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository,
			UserRepository userRepository) {
		this.reservationRepository = reservationRepository;
		this.restaurantRepository = restaurantRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public void create(Map<String, String> paymentIntentObject) {
		Reservation reservation = new Reservation();

		Integer restaurantId = Integer.valueOf(paymentIntentObject.get("restaurantId"));
		Integer userId = Integer.valueOf(paymentIntentObject.get("userId"));

		Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
		User user = userRepository.getReferenceById(userId);
		String checkinDate = String.valueOf(paymentIntentObject.get("checkinDate"));
		Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("numberOfPeople"));
		Integer amount = Integer.valueOf(paymentIntentObject.get("amount"));
		String paymentId = paymentIntentObject.get("paymentId");
		String sessionId = paymentIntentObject.get("sessionId");

		reservation.setRestaurant(restaurant);
		reservation.setUser(user);
		reservation.setCheckinDate(checkinDate);
		reservation.setNumberOfPeople(numberOfPeople);
		reservation.setAmount(amount);
		reservation.setPaymentId(paymentId);
		reservation.setSessionId(sessionId);

		reservationRepository.save(reservation);
	}

	public Integer calculateAmount(Integer price, Integer numberOfPeople) {
		return price * numberOfPeople;
	}

	public Reservation findById(Integer reservationId) {
		return reservationRepository.findById(reservationId)
				.orElse(null);
	}

	public void delete(Integer reservationId) {
		reservationRepository.deleteById(reservationId);
	}
}
