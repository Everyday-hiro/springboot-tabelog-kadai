package com.example.taberogu.controller;

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

import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.User;
import com.example.taberogu.repository.FavoriteRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.FavoriteService;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteRepository favoriteRepository,
			FavoriteService favoriteService) {
		this.favoriteRepository = favoriteRepository;
		this.favoriteService = favoriteService;
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
	@PostMapping("/add/{id}")
	public String add(@PathVariable(name = "id") Integer id, Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		favoriteService.add(userDetailsImpl, id);
		return "redirect:/restaurant/" + id;
	}

	/**
	 * お気に入り解除
	 */
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id, Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		favoriteRepository.deleteById(id);
		return "redirect:/restaurant/" + id;
	}
}
