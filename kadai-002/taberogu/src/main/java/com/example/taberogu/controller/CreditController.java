package com.example.taberogu.controller;

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

import com.example.taberogu.entity.Credit;
import com.example.taberogu.form.CreditEditForm;
import com.example.taberogu.form.CreditRegisterForm;
import com.example.taberogu.repository.CreditRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.CreditService;
import com.example.taberogu.service.UserService;

@Controller
@RequestMapping("/credit")
public class CreditController {
	private final CreditService creditService;
	private final CreditRepository creditRepository;
	private final UserService userService;

	public CreditController(CreditService creditService, CreditRepository creditRepository, UserService userService) {
		this.creditService = creditService;
		this.creditRepository = creditRepository;
		this.userService = userService;
	}

	@GetMapping("/info/{creditId}")
	public String index(@PathVariable(name = "creditId") Integer creditId, Model model) {
		Credit credit = creditRepository.getReferenceById(creditId);
		model.addAttribute("credit", credit);
		return "credit/index";
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

	@GetMapping("/{creditId}/edit")
	public String edit(@PathVariable(name = "creditId") Integer creditId, Model model) {
		Credit credit = creditRepository.getReferenceById(creditId);
		CreditEditForm creditEditForm = new CreditEditForm(credit.getId(), credit.getUser(), credit.getCreditNumber(),
				credit.getGoodThru(), credit.getSecurityCode(), credit.getName());
		model.addAttribute("credit", credit);
		model.addAttribute("creditEditForm", creditEditForm);
		return "credit/edit";
	}

	@PostMapping("/{creditId}/update")
	public String update(@PathVariable(name = "creditId") Integer creditId,
			@ModelAttribute @Validated CreditEditForm creditEditForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "credit/edit";
		}
		Credit credit = creditRepository.findById(creditId).orElseThrow(() -> new RuntimeException("Credit not Fonud"));
		credit.setCreditNumber(creditEditForm.getCreditNumber());
		credit.setGoodThru(creditEditForm.getGoodThru());
		credit.setSecurityCode(creditEditForm.getSecurityCode());
		credit.setName(creditEditForm.getName());

		creditRepository.save(credit);
		redirectAttributes.addFlashAttribute("successMessage", "クレジットカードの内容を編集しました。");

		return "redirect:/credit/info/{creditId}";
	}

	@PostMapping("/{creditId}/delete")
	public String delete(@PathVariable(name = "creditId") Integer creditId,
			RedirectAttributes redirectAttributes,
			Model model) {
		Credit credit = creditRepository.findById(creditId).orElseThrow(() -> new RuntimeException("Credit not found"));
		creditRepository.delete(credit);
		redirectAttributes.addFlashAttribute("successMessage", "クレジットカードを削除しました。");
		return "redirect:/user";
	}
}
