package com.example.taberogu.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.Review;
import com.example.taberogu.entity.User;
import com.example.taberogu.repository.FavoriteRepository;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.repository.ReviewRepository;
import com.example.taberogu.security.UserDetailsImpl;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

	private final RestaurantRepository restaurantRepository;
	private final ReviewRepository reviewRepository;
	private final FavoriteRepository favoriteRepository;

	public RestaurantController(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository,
			FavoriteRepository favoriteRepository) {
		this.restaurantRepository = restaurantRepository;
		this.reviewRepository = reviewRepository;
		this.favoriteRepository = favoriteRepository;
	}

	@GetMapping
	public String index(
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "area", required = false) String area,
			@RequestParam(name = "order", required = false) String order,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		Page<Restaurant> restaurantPage;

		if (keyword != null && !keyword.isEmpty()) {
			if (order != null && order.equals("priceAsc")) {
				restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%",
						"%" + keyword + "%", pageable);
			} else {
				restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc(
						"%" + keyword + "%", "%" + keyword + "%", pageable);
			}
		} else if (area != null && !area.isEmpty()) {
			if (order != null && order.equals("priceAsc")) {
				restaurantPage = restaurantRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
			} else {
				restaurantPage = restaurantRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
			}
		} else if (category != null) {
			if (order != null && order.equals("priceAsc")) {
				restaurantPage = restaurantRepository.findByCategoryLikeOrderByPriceAsc(category, pageable);
			} else {
				restaurantPage = restaurantRepository.findByCategoryLikeOrderByCreatedAtDesc(category, pageable);
			}
		} else {
			if (order != null && order.equals("priceAsc")) {
				restaurantPage = restaurantRepository.findAllByOrderByPriceAsc(pageable);
			} else {
				restaurantPage = restaurantRepository.findAllByOrderByCreatedAtDesc(pageable);
			}
		}

		model.addAttribute("restaurantPage", restaurantPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("category", category);
		model.addAttribute("area", area);
		model.addAttribute("order", order);

		return "restaurant/index";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model,
			@PageableDefault(page = 0, size = 3, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		Page<Review> reviewPage = reviewRepository.findByRestaurantId(id, pageable);

		if (userDetailsImpl != null) {
			User user = userDetailsImpl.getUser();
			List<Review> userHasReview = reviewRepository.findByUserAndRestaurantId(user, id);
			boolean notFavorite = !favoriteRepository.favoriteJudge(restaurant, user);
			model.addAttribute("userHasReview", !userHasReview.isEmpty());
			model.addAttribute("notFavorite", notFavorite);
		}
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("reviewPage", reviewPage);

		return "restaurant/show";
	}
}