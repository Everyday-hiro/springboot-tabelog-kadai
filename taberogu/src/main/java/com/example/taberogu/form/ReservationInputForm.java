package com.example.taberogu.form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
	@NotBlank(message = "チェックイン日時を選択してください。")
	private String fromCheckinDateToCheckoutDate;

	@NotNull(message = "予約人数を入力してください。")
	@Min(value = 1, message = "予約人数は一人以上に設定してください。")
	private Integer numberOfPeople;

	public LocalDate getCheckinDate() {
		if (fromCheckinDateToCheckoutDate == null || fromCheckinDateToCheckoutDate.isEmpty()) {
			return null;
		}

		// LocalDateTimeとしてパース
		LocalDateTime dateTime = LocalDateTime.parse(fromCheckinDateToCheckoutDate,
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

		// 日付のみを返す
		return dateTime.toLocalDate();
	}
}
