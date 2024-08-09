package com.example.taberogu.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.repository.FavoriteRepository;
import com.example.taberogu.repository.RestaurantRepository;

@Controller
public class TopPageController {
	private final RestaurantRepository restaurantRepository;
	private final FavoriteRepository favoriteRepository;

	public TopPageController(RestaurantRepository restaurantRepository, FavoriteRepository favoriteRepository) {
		this.restaurantRepository = restaurantRepository;
		this.favoriteRepository = favoriteRepository;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Restaurant> newRestaurant = restaurantRepository.findTop3ByOrderByCreatedAtDesc();
		List<Favorite> newFavorite = favoriteRepository.findTop3ByOrderByCreatedAtDesc();
		model.addAttribute("newFavorite", newFavorite);
		model.addAttribute("newRestaurant", newRestaurant);
		return "index";
	}
}
