package com.example.taberogu.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.Reservation;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;
import com.example.taberogu.form.ReservationInputForm;
import com.example.taberogu.form.ReservationRegisterForm;
import com.example.taberogu.repository.ReservationRepository;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.ReservationService;

@Controller
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final RestaurantRepository restaurantRepository;
	private final ReservationService reservationService;

	public ReservationController(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository,
			ReservationService reservationService) {
		this.reservationRepository = reservationRepository;
		this.restaurantRepository = restaurantRepository;
		this.reservationService = reservationService;
	}

	@GetMapping("/reservation")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		User user = userDetailsImpl.getUser();
		Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);

		model.addAttribute("reservationPage", reservationPage);

		return "reservation/index";
	}

	@GetMapping("/restaurant/{id}/reservation/input")
	public String input(@PathVariable(name = "id") Integer id,
			@ModelAttribute @Validated ReservationInputForm reservationInputForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(id);

		if (bindingResult.hasErrors()) {
			model.addAttribute("restaurant", restaurant);
			model.addAttribute("errorMessage", "予約内容に不備があります。");
			return "restaurant/show";
		}

		redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);
		return "redirect:/restaurant/{id}/reservation/confirm";
	}

	@GetMapping("/restaurant/{id}/reservation/confirm")
	public String confirm(@PathVariable(name = "id") Integer id,
			@ModelAttribute ReservationInputForm reservationInputForm,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();

		LocalDate checkinDate = reservationInputForm.getCheckinDate();
		Integer numberOfPeople = reservationInputForm.getNumberOfPeople();
		Integer price = restaurant.getPrice();

		Integer amount = reservationService.calculateAmount(checkinDate, price, numberOfPeople);

		ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(restaurant.getId(), user.getId(),
				checkinDate.toString(), reservationInputForm.getNumberOfPeople(), amount);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("reservationRegisterForm", reservationRegisterForm);
		return "reservation/confirm";
	}

	@PostMapping("/restaurant/{id}/reservation/create")
	public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {
		reservationService.create(reservationRegisterForm);
		return "redirect:/reservation?reserved";
	}
}
