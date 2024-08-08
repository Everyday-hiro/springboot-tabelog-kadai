package com.example.taberogu.controller;

import java.util.List;

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
	public String restaurant(Model model) {
		List<Restaurant> restaurant = restaurantRepository.findAll();
		model.addAttribute("restaurant", restaurant);
		return "admin/adminRestaurant";
	}
}
