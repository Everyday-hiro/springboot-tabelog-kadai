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
import com.example.taberogu.entity.User;
import com.example.taberogu.form.ReviewEditForm;
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

	/**
	 * これはレビュー一覧に遷移するためのメソッドです。
	 */
	@GetMapping("/{id}")
	public String review(@PathVariable(name = "id") Integer id, Model model,
			@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
		Page<Review> reviewPage = reviewRepository.findByRestaurantId(id, pageable);
		Restaurant restaurant = restaurantRepository.getReferenceById(id);

		model.addAttribute("reviewPage", reviewPage);
		model.addAttribute("restaurant", restaurant);
		return "review/index";
	}

	/**
	 * これはレビュー登録フォームに遷移するためのメソッドです。
	 */
	@GetMapping("/register/{id}")
	public String register(@PathVariable(name = "id") Integer id, Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		Review review = new Review();

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("review", review);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());

		return "review/register";
	}

	/**
	 * レビューを登録するためにサービスクラスの処理をビューに渡すためのメソッドです。
	 */
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

	/**
	 * レビューを更新するためのページに遷移するためのメソッドです。
	 */
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id, Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		Review review = reviewRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getStar(), review.getDescription());

		model.addAttribute("user", user);
		model.addAttribute("review", review);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("reviewEditForm", reviewEditForm);

		return "review/edit";
	}

	/**
	 * レビューを更新するための処理をビューに渡すためのメソッド
	 */
	@PostMapping("/{id}/update")
	public String update(@PathVariable(name = "id") Integer id,
			@ModelAttribute @Validated ReviewEditForm reviewEditForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "review/edit";
		}

		Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
		review.setStar(reviewEditForm.getStar());
		review.setDescription(reviewEditForm.getDescription());
		review.setUser(userDetailsImpl.getUser());

		reviewRepository.save(review);

		Integer restaurantId = review.getRestaurant().getId();
		redirectAttributes.addFlashAttribute("successMessage", "レビューの内容を変更しました。");

		return "redirect:/restaurant/" + restaurantId;
	}

	/**
	 * レビューを削除するためのメソッドです。
	 */
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
		reviewRepository.deleteById(id);
		Integer restaurantId = review.getRestaurant().getId();
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
		return "redirect:/restaurant/" + restaurantId;
	}
}
