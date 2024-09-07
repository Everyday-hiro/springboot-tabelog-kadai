package com.example.taberogu.form;

import com.example.taberogu.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreditEditForm {
	@NotNull
	private Integer id;

	@NotNull
	private User user;

	@NotBlank(message = "クレジットカード番号を入力してください。")
	private String creditNumber;

	@NotBlank(message = "有効期限を入力してください。")
	private String goodThru;

	@NotBlank(message = "セキュリティコードを入力してください。")
	private String securityCode;

	@NotBlank(message = "カード名義を入力してください。")
	private String name;
}
