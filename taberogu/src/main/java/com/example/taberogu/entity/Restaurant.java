package com.example.taberogu.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "restaurant")
@Data
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "category")
	private String category;

	@Column(name = "name")
	private String name;

	@Column(name = "image")
	private String image;

	@Column(name = "restaurant_description")
	private String restaurantDescription;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "open_time")
	private String openTime;

	@Column(name = "price")
	private Integer price;

	@Column(name = "address")
	private String address;

	@Column(name = "postal_code")
	private String postalCode;

	@Column(name = "closing_day")
	private String closingDay;

	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;

	public int getId() {
		return this.id;
	}
}
