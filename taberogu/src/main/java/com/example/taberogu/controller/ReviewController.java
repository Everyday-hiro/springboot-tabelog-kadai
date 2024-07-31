package com.example.taberogu.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.Review;
import com.example.taberogu.form.ReviewRegisterForm;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.repository.ReviewRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.ReviewService;

@Controller
@RequestMapping("/review")
public class ReviewController {
	private final RestaurantRepository restaurantRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;

	public ReviewController(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository,
			ReviewService reviewService) {
		this.restaurantRepository = restaurantRepository;
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
	}

	//レビュー一覧への遷移
	@GetMapping("/{id}")
	public String review(@PathVariable(name = "id") Integer id, Model model,
			@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
		Page<Review> reviewPage = reviewRepository.findByRestaurantId(id, pageable);
		Restaurant restaurant = restaurantRepository.getReferenceById(id);

		model.addAttribute("reviewPage", reviewPage);
		model.addAttribute("restaurant", restaurant);
		return "review/index";
	}

	//レビュー登録フォームへの遷移
	@GetMapping("/register/{id}")
	public String register(@PathVariable(name = "id") Integer id, Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		Review review = new Review();

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("review", review);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());

		return "review/register";
	}

	//レビュー登録
	@PostMapping("/{id}/create")
	public String create(@PathVariable(name = "id") Integer id,
			@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		Review review = new Review();

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("review", review);

		if (bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "投稿内容に不備があります。");
			return "review/register";
		}
		reviewService.create(restaurant, reviewRegisterForm, userDetailsImpl);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを登録しました。");

		return "redirect:/restaurant/{id}";
	}
}
