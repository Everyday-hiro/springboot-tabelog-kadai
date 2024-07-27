package com.example.taberogu.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.repository.RestaurantRepository;

@Controller
public class TopPageController {
	private final RestaurantRepository restaurantRepository;
	
	public TopPageController(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}
	 @GetMapping("/")
     public String index(Model model) {
		 List<Restaurant> newRestaurant = restaurantRepository.findTop3ByOrderByCreatedAtDesc();
		 model.addAttribute("newRestaurant", newRestaurant);
         return "index";
     }   
}
