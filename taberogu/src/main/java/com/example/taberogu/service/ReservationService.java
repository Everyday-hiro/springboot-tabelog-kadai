package com.example.taberogu.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taberogu.entity.Reservation;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;
import com.example.taberogu.form.ReservationRegisterForm;
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
	public void create(ReservationRegisterForm reservationRegisterForm) {
		Reservation reservation = new Reservation();
		Restaurant restaurant = restaurantRepository.getReferenceById(reservationRegisterForm.getRestaurantId());
		User user = userRepository.getReferenceById(reservationRegisterForm.getUserId());
		LocalDate checkinDate = LocalDate.parse(reservationRegisterForm.getCheckinDate());

		reservation.setRestaurant(restaurant);
		reservation.setUser(user);
		reservation.setCheckinDate(checkinDate);
		reservation.setNumberOfPeople(reservationRegisterForm.getNumberOfPeople());
		reservation.setAmount(reservationRegisterForm.getAmount());

		reservationRepository.save(reservation);
	}

	public Integer calculateAmount(LocalDate checkinDate, Integer price, Integer numberOfPeople) {
		return price * numberOfPeople;
	}
}
