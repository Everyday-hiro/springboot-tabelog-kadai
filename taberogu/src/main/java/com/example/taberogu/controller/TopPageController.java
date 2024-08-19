package com.example.taberogu.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.taberogu.entity.Category;
import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.repository.CategoryRepository;
import com.example.taberogu.repository.FavoriteRepository;
import com.example.taberogu.repository.RestaurantRepository;

@Controller
public class TopPageController {
	private final RestaurantRepository restaurantRepository;
	private final FavoriteRepository favoriteRepository;
	private final CategoryRepository categoryRepository;

	public TopPageController(RestaurantRepository restaurantRepository, FavoriteRepository favoriteRepository,
			CategoryRepository categoryRepository) {
		this.restaurantRepository = restaurantRepository;
		this.favoriteRepository = favoriteRepository;
		this.categoryRepository = categoryRepository;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Restaurant> newRestaurant = restaurantRepository.findTop3ByOrderByCreatedAtDesc();
		List<Favorite> newFavorite = favoriteRepository.findTop3ByOrderByCreatedAtDesc();
		List<Category> category = categoryRepository.findAll();
		model.addAttribute("newFavorite", newFavorite);
		model.addAttribute("newRestaurant", newRestaurant);
		model.addAttribute("category", category);
		return "index";
	}
}
