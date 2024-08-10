package com.example.taberogu.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryRegisterForm {
	@NotNull
	private String name;
}
