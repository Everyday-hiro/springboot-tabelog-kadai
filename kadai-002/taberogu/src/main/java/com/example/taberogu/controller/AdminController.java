package com.example.taberogu.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.form.RestaurantEditForm;
import com.example.taberogu.form.RestaurantRegisterForm;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.service.RestaurantService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final RestaurantRepository restaurantRepository;
	private final RestaurantService restaurantService;

	public AdminController(RestaurantRepository restaurantRepository, RestaurantService restaurantService) {
		this.restaurantRepository = restaurantRepository;
		this.restaurantService = restaurantService;
	}

	@GetMapping
	public String top() {
		return "admin/adminTop";
	}

	@GetMapping("/restaurant")
	public String restaurant(
			Model model,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			@RequestParam(name = "keyword", required = false) String keyword) {
		Page<Restaurant> restaurantPage;

		if (keyword != null && !keyword.isEmpty()) {
			restaurantPage = restaurantRepository.findByNameLike("%" + keyword + "%", pageable);
		} else {
			restaurantPage = restaurantRepository.findAll(pageable);
		}
		model.addAttribute("restaurantPage", restaurantPage);
		model.addAttribute("keyword", keyword);
		return "admin/restaurant/adminRestaurant";
	}

	@GetMapping("/restaurant/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		model.addAttribute("restaurant", restaurant);
		return "admin/restaurant/show";
	}

	@GetMapping("/restaurant/register")
	public String register(Model model) {
		model.addAttribute("restaurantRegisterForm", new RestaurantRegisterForm());
		return "admin/restaurant/register";
	}

	@PostMapping("/restaurant/create")
	public String create(@ModelAttribute @Validated RestaurantRegisterForm restaurantRegisterForm,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin/restaurant/register";
		}
		restaurantService.create(restaurantRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "店舗を登録できました。");
		return "redirect:/admin/restaurant";
	}

	@GetMapping("/restaurant/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id, Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		String image = restaurant.getImage();
		RestaurantEditForm restaurantEditForm = new RestaurantEditForm(restaurant.getId(), restaurant.getName(), null,
				restaurant.getCategory(), restaurant.getRestaurantDescription(), restaurant.getPhoneNumber(),
				restaurant.getOpenTime(), restaurant.getPrice(), restaurant.getAddress(), restaurant.getPostalCode(),
				restaurant.getClosingDay());

		model.addAttribute("image", image);
		model.addAttribute("restaurantEditForm", restaurantEditForm);

		return "admin/restaurant/edit";
	}

	@PostMapping("/restaurant/{id}/update")
	public String update(
			@ModelAttribute @Validated RestaurantEditForm restaurantEditForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin/restaurant/edit";
		}
		restaurantService.update(restaurantEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
		return "redirect:/admin/restaurant";
	}

	@PostMapping("/restaurant/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		restaurantRepository.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "店舗情報を削除しました。");
		return "redirect:/admin/restaurant";
	}
}