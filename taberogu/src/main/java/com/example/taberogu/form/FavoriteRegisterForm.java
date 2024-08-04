package com.example.taberogu.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FavoriteRegisterForm {
	@NotNull
	private Integer restaurant;
	@NotNull
	private Integer user;
}
