package com.example.taberogu.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.repository.RestaurantRepository;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

	private final RestaurantRepository restaurantRepository;
	
	public RestaurantController(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}
	
	@GetMapping
	public String index(
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "area", required = false) String area,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model
			)
	{
		Page<Restaurant> restaurantPage;
		
		if (keyword != null && !keyword.isEmpty()) {
            restaurantPage =restaurantRepository.findByNameLikeOrAddressLike("%" + keyword + "%", "%" + keyword + "%", pageable);
        } else if (area != null && !area.isEmpty()) {
        	restaurantPage = restaurantRepository.findByAddressLike("%" + area + "%", pageable);
        } else if (category != null) {
        	restaurantPage = restaurantRepository.findByCategoryLike(category, pageable);
        } else {
            restaurantPage = restaurantRepository.findAll(pageable);
        } 

		model.addAttribute("restaurantPage", restaurantPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("category", category);
		model.addAttribute("area", area);
		
		return "restaurant/index";
	}
}