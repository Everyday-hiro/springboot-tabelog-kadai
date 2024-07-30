package com.example.taberogu.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.Review;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.repository.ReviewRepository;
import com.example.taberogu.service.ReviewService;

@Controller
@RequestMapping("/review")
public class ReviewController {
private final RestaurantRepository restaurantRepository;
private final ReviewRepository reviewRepository;
private final ReviewService reviewService;

public ReviewController(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository, ReviewService reviewService) {
	this.restaurantRepository = restaurantRepository;
	this.reviewRepository = reviewRepository;
	this.reviewService = reviewService;
}

//レビュー一覧への遷移
@GetMapping("/{id}")
public String review(@PathVariable(name = "id")Integer id, Model model, @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
	Page<Review> reviewPage = reviewRepository.findByRestaurantId(id, pageable);
	Restaurant restaurant = restaurantRepository.getReferenceById(id);
	
	model.addAttribute("reviewPage", reviewPage);
	model.addAttribute("restaurant", restaurant);
	return "review/index";
}
}
