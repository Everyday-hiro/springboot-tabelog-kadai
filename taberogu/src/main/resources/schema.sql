CREATE TABLE IF NOT EXISTS restaurant
(
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   category VARCHAR (255) NOT NULL,
   name VARCHAR (50) NOT NULL,
   image VARCHAR (255),
   restaurant_description VARCHAR (255) NOT NULL,
   phone_number VARCHAR (50) NOT NULL,
   open_time VARCHAR (50) NOT NULL,
   price INT NOT NULL,
   address VARCHAR (255) NOT NULL,
   postal_code VARCHAR (50) NOT NULL,
   closing_day VARCHAR (50) NOT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS role
(
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (50) NOT NULL
);
CREATE TABLE IF NOT EXISTS user
(
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (50) NOT NULL,
   furigana VARCHAR (50) NOT NULL,
   postal_code VARCHAR (50) NOT NULL,
   address VARCHAR (255) NOT NULL,
   phone_number VARCHAR (50) NOT NULL,
   email VARCHAR (255) NOT NULL UNIQUE,
   password VARCHAR (255) NOT NULL,
   role_id INT NOT NULL,
   enabled BOOLEAN NOT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (role_id) REFERENCES role (id)
);
CREATE TABLE IF NOT EXISTS review
(
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (255) NOT NULL,
   star VARCHAR (50) NOT NULL,
   description VARCHAR (255) NOT NULL,
   restaurant_id INT NOT NULL,
   user_id INT NOT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (restaurant_id) REFERENCES restaurant (id),
   FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE TABLE IF NOT EXISTS favorite
(
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   user_id INT NOT NULL,
   restaurant_id INT NOT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (restaurant_id) REFERENCES restaurant (id),
   FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE TABLE IF NOT EXISTS verification_token
(
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   user_id INT NOT NULL UNIQUE,
   token VARCHAR (255) NOT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE TABLE IF NOT EXISTS category(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);