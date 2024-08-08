package com.example.taberogu.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.repository.RestaurantRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final RestaurantRepository restaurantRepository;

	public AdminController(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@GetMapping
	public String top() {
		return "admin/adminTop";
	}

	@GetMapping("/restaurant")
	public String restaurant(
			Model model,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		Page<Restaurant> restaurantPage = restaurantRepository.findAll(pageable);
		model.addAttribute("restaurantPage", restaurantPage);
		return "admin/adminRestaurant";
	}
}
