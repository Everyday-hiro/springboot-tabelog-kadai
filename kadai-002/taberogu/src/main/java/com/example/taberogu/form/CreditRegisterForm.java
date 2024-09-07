package com.example.taberogu.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreditRegisterForm {
	@NotBlank(message = "カード番号を入力してください。")
	@Size(min = 16, max = 16, message = "カード番号は16桁で入力してください。")
	private String creditNumber;

	@NotBlank(message = "カードの有効期限を入力してください。")
	private String goodThru;

	@NotBlank(message = "セキュリティコードを入力してください。")
	@Size(min = 3, max = 3, message = "セキュリティコードは３桁で入力してください。")
	private String securityCode;

	@NotBlank(message = "カード名義を入力してください。")
	private String name;
}
