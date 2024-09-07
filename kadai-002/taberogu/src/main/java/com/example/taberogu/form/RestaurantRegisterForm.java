package com.example.taberogu.form;

import org.springframework.web.multipart.MultipartFile;

import com.example.taberogu.entity.Category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantRegisterForm {
	@NotBlank(message = "店舗名を入力してください。")
	private String name;

	private MultipartFile imageFile;

	@NotBlank(message = "カテゴリを入力してください。")
	private Category category;

	@NotBlank(message = "説明を入力してください。")
	private String restaurantDescription;

	@NotBlank(message = "電話番号を入力してください。")
	private String phoneNumber;

	@NotBlank(message = "営業時間を入力してください。")
	private String openTime;

	@NotNull(message = "料金を入力してください。")
	@Min(value = 1, message = "料金は１円以上に設定してください。")
	private Integer price;

	@NotBlank(message = "住所を入力してください。")
	private String address;

	@NotBlank(message = "郵便番号を入力してください。")
	private String postalCode;

	@NotBlank(message = "定休日を入力してください。")
	private String closingDay;
}
