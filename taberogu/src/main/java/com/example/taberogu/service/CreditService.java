package com.example.taberogu.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taberogu.entity.Credit;
import com.example.taberogu.entity.User;
import com.example.taberogu.form.CreditRegisterForm;
import com.example.taberogu.repository.CreditRepository;
import com.example.taberogu.security.UserDetailsImpl;

@Service
public class CreditService {
	private final CreditRepository creditRepository;

	public CreditService(CreditRepository creditRepository) {
		this.creditRepository = creditRepository;
	}

	@Transactional
	public void create(CreditRegisterForm creditRegisterForm,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		Credit credit = new Credit();
		User user = userDetailsImpl.getUser();

		credit.setUser(user);
		credit.setCrediNumber(creditRegisterForm.getCreditNumber());
		credit.setGoodThru(creditRegisterForm.getGoodThru());
		credit.setSecurityCode(creditRegisterForm.getSecurityCode());
		credit.setName(creditRegisterForm.getName());

		creditRepository.save(credit);
	}
}
