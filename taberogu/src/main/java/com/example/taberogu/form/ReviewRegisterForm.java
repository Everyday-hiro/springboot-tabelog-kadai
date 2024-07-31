package com.example.taberogu.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ReviewRegisterForm {

	@Max(value = 5)
	private Integer star;
	
	@NotBlank(message = "コメントを入力してください。")
	private String description;
}
