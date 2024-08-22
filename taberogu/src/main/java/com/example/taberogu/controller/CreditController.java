package com.example.taberogu.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.form.CreditRegisterForm;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.CreditService;

@Controller
@RequestMapping("/credit")
public class CreditController {
	private final CreditService creditService;

	public CreditController(CreditService creditService) {
		this.creditService = creditService;
	}

	@GetMapping
	public String register(Model model) {
		model.addAttribute("creditRegisterForm", new CreditRegisterForm());
		return "credit/register";
	}

	@PostMapping("/create")
	public String create(@ModelAttribute @Validated CreditRegisterForm creditRegisterForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		if (bindingResult.hasErrors()) {
			return "credit/register";
		}
		creditService.create(creditRegisterForm, userDetailsImpl);
		redirectAttributes.addFlashAttribute("successMessage", "クレジットカードを登録しました。");

		return "redirect:/user";
	}
}
