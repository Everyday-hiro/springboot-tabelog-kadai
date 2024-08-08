package com.example.taberogu.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.form.RestaurantRegisterForm;
import com.example.taberogu.repository.RestaurantRepository;

@Service
public class RestaurantService {
	private final RestaurantRepository restaurantRepository;

	public RestaurantService(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@Transactional
	public void create(RestaurantRegisterForm restaurantRegisterForm) {
		Restaurant restaurant = new Restaurant();
		MultipartFile imageFile = restaurantRegisterForm.getImageFile();

		if (!imageFile.isEmpty()) {
			String image = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(image);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			restaurant.setImage(hashedImageName);
		}
		restaurant.setName(restaurantRegisterForm.getName());
		restaurant.setCategory(restaurantRegisterForm.getCategory());
		restaurant.setRestaurantDescription(restaurantRegisterForm.getRestaurantDescription());
		restaurant.setPhoneNumber(restaurantRegisterForm.getPhoneNumber());
		restaurant.setOpenTime(restaurantRegisterForm.getOpenTime());
		restaurant.setPrice(restaurantRegisterForm.getPrice());
		restaurant.setAddress(restaurantRegisterForm.getAddress());
		restaurant.setPostalCode(restaurantRegisterForm.getPostalCode());
		restaurant.setClosingDay(restaurantRegisterForm.getClosingDay());

		restaurantRepository.save(restaurant);

	}

	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		for (int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}
		String hashedFileName = String.join(".", fileNames);
		return hashedFileName;
	}

	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
