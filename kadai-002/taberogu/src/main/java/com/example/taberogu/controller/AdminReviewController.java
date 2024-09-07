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

import com.example.taberogu.entity.Review;
import com.example.taberogu.repository.ReviewRepository;

@Controller
@RequestMapping("/admin/review")
public class AdminReviewController {
	private final ReviewRepository reviewRepository;

	public AdminReviewController(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}

	@GetMapping
	public String index(
			Model model,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			@RequestParam(name = "keyword", required = false) String keyword) {
		Page<Review> reviewPage;

		if (keyword != null && !keyword.isEmpty()) {
			reviewPage = reviewRepository.findByNameLike("%" + keyword + "%", pageable);
		} else {
			reviewPage = reviewRepository.findAll(pageable);
		}
		model.addAttribute("reviewPage", reviewPage);
		model.addAttribute("keyword", keyword);

		return "admin/review/index";
	}
}