package com.example.taberogu.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;
import com.example.taberogu.repository.FavoriteRepository;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.FavoriteService;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;
	private final RestaurantRepository restaurantRepository;

	public FavoriteController(FavoriteRepository favoriteRepository,
			FavoriteService favoriteService,
			RestaurantRepository restaurantRepository) {
		this.favoriteRepository = favoriteRepository;
		this.favoriteService = favoriteService;
		this.restaurantRepository = restaurantRepository;
	}

	/**
	 * お気に入り一覧への遷移
	 */
	@GetMapping("/")
	public String index(Model model,
			@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		User user = userDetailsImpl.getUser();
		Page<Favorite> favoritePage = favoriteRepository.findByUser(user, pageable);

		model.addAttribute("favoritePage", favoritePage);

		return "favorite/index";
	}

	/**
	 * お気に入り登録
	 */
	@PostMapping("/add/{restaurantId}")
	public String add(@PathVariable(name = "restaurantId") Integer restaurantId, Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes) {
		Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
		if (restaurantOpt.isPresent()) {
			Restaurant restaurant = restaurantOpt.get();
			User user = userDetailsImpl.getUser();

			favoriteService.add(restaurant, user);
			redirectAttributes.addFlashAttribute("successMessage", "お気に入りに追加しました。");
			return "redirect:/restaurant/" + restaurantId;
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "指定された店舗が見つかりませんでした。");
			return "redirect:/restaurant/";
		}
	}

	/**
	 * お気に入り解除
	 */
	@PostMapping("/delete/{restaurantId}")
	public String delete(@PathVariable(name = "restaurantId") Integer restaurantId, Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes) {
		User user = userDetailsImpl.getUser();

		favoriteService.delete(restaurantId, user.getId());
		redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");
		return "redirect:/restaurant/" + restaurantId;

	}
}
