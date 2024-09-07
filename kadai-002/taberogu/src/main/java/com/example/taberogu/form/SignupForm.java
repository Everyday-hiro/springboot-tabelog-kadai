package com.example.taberogu.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupForm {
	@NotBlank(message = "氏名を入力してください。")
	private String name;

	@NotBlank(message = "フリガナを入力してください。")
	private String furigana;

	@NotBlank(message = "郵便番号を入力して下さい。")
	private String postalCode;

	@NotBlank(message = "住所を入力してください。")
	private String address;

	@NotBlank(message = "電話番号を入力してください。")
	private String phoneNumber;

	@NotBlank(message = "メールアドレスを入力してください。")
	@Email(message = "メールアドレスは正しい形式で入力してください。")
	private String email;

	@NotBlank(message = "パスワードを入力してください。")
	@Length(min = 8, max = 16, message = "パスワードの文字は8文字以上1６文字以下で入力してください。")
	private String password;

	@NotBlank(message = "確認用のパスワードを入力してください。")
	private String passwordConfirmation;
}
