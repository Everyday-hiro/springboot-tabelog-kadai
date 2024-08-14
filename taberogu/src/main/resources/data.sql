INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (1, 'カフェ', 'SAMURAI COFFEE', 'house01.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '9:00~23:00', '1000円~2000円', '愛知県名古屋市天白区大岡2-3290' , '457-6609', '木曜日');
INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (2, 'カフェ', '侍の喫茶', 'house02.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '9:00~22:00', '1000円~2000円', '愛知県名古屋市東区山岳2-3290' , '457-6609', '金曜日');
INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (3, '焼肉', 'サムライ焼き', 'house03.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '17:00~23:00', '2000円~3000円', '愛知県名古屋市西区豊本2-3290' , '457-6609', '土曜日');
INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (4, '焼肉', 'SAMURI炎', 'house04.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '16:00~23:00', '2000円~3000円', '愛知県半田市河野区中洲2-3290' , '457-6609', '日曜日');
INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (5, '寿司', 'さむらい握り', 'house05.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '10:00~24:00', '3000円~4000円', '愛知県犬山市猿島区雉兆2-3290' , '457-6609', '月曜日');
INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (6, '寿司', '侍寿司', 'house06.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '11:30~23:00', '3000円~4000円', '愛知県甘市香苗美区味元2-3290' , '457-6609', '火曜日');
INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (7, '中華', 'ラーメンSAMURAI', 'house07.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '12:00~25:00', '1000円~2000円', '愛知県愛西市愛東区南北2-3290' , '457-6609', '水曜日');
INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (8, '中華', 'SAMURAI亭', 'house08.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '11:00~26:00', '1000円~2000円', '愛知県名古屋市緑区黄金2-3290' , '457-6609', '木曜日');
INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (9, '丼物', 'SAMURAI屋', 'house09.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '10:00~23:00', '1000円~2000円', '愛知県名古屋市中区牡丹2-3290' , '457-6609', '金曜日');
INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (10, '丼物', '侍丼', 'house10.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '9:00~22:00', '1000円~2000円', '愛知県名古屋市関西区都響2-3290' , '457-6609', '土曜日');
INSERT IGNORE INTO restaurant (id, category, name, image, restaurant_description, phone_number, open_time, price, address, postal_code, closing_day) VALUES (11, '和食', '和食侍', 'house01.jpg', '最寄駅から10分の場所にあり割といい立地である。', '052-334-5347', '12:00~20:00', '2000円~4000円', '愛知県名古屋市関東区加賀和那2-3290' , '457-6609', '日曜日');

INSERT IGNORE INTO role(id, name) VALUES (1, 'ROLE_FREE');
INSERT IGNORE INTO role(id, name) VALUES (2, 'ROLE_PAID');
INSERT IGNORE INTO role(id, name) VALUES (3, 'ROLE_ADMIN');

INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (1, '侍 太郎', 'サムライ タロウ', '101-0022', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'taro.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (2, '侍 花子', 'サムライ ハナコ', '101-0022', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'hanako.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 2, true);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (3, '侍 義勝', 'サムライ ヨシカツ', '638-0644', '奈良県五條市西吉野町湯川X-XX-XX', '090-1234-5678', 'yoshikatsu.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 3, true);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (4, '侍 幸美', 'サムライ サチミ', '342-0006', '埼玉県吉川市南広島X-XX-XX', '090-1234-5678', 'sachimi.samurai@example.com', 'password', 2, false);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (5, '侍 雅', 'サムライ ミヤビ', '527-0209', '滋賀県東近江市佐目町X-XX-XX', '090-1234-5678', 'miyabi.samurai@example.com', 'password', 1, false);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (6, '侍 正保', 'サムライ マサヤス', '989-1203', '宮城県柴田郡大河原町旭町X-XX-XX', '090-1234-5678', 'masayasu.samurai@example.com', 'password', 2, false);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (7, '侍 真由美', 'サムライ マユミ', '951-8015', '新潟県新潟市松岡町X-XX-XX', '090-1234-5678', 'mayumi.samurai@example.com', 'password', 1, false);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (8, '侍 安民', 'サムライ ヤスタミ', '241-0033', '神奈川県横浜市旭区今川町X-XX-XX', '090-1234-5678', 'yasutami.samurai@example.com', 'password', 2, false);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (9, '侍 章緒', 'サムライ アキオ', '739-2103', '広島県東広島市高屋町宮領X-XX-XX', '090-1234-5678', 'akio.samurai@example.com', 'password', 1, false);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (10, '侍 祐子', 'サムライ ユウコ', '601-0761', '京都府南丹市美山町高野X-XX-XX', '090-1234-5678', 'yuko.samurai@example.com', 'password', 2, false);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (11, '侍 秋美', 'サムライ アキミ', '606-8235', '京都府京都市左京区田中西春菜町X-XX-XX', '090-1234-5678', 'akimi.samurai@example.com', 'password', 1, false);
INSERT IGNORE INTO user (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled) VALUES (12, '侍 信平', 'サムライ シンペイ', '673-1324', '兵庫県加東市新定X-XX-XX', '090-1234-5678', 'shinpei.samurai@example.com', 'password', 2, false);

INSERT IGNORE INTO review(id, name, star, description, restaurant_id, user_id) VALUES (1, '侍 幸美', 5, '静かでまったりできて最高でした。', 1, 4);
INSERT IGNORE INTO review(id, name, star, description, restaurant_id, user_id) VALUES (2, '侍 花子', 3, 'まあまあでした。', 1, 2);
INSERT IGNORE INTO review(id, name, star, description, restaurant_id, user_id) VALUES (3, '侍 正保', 4, 'コーヒーが最高です。', 1, 6);
INSERT IGNORE INTO review(id, name, star, description, restaurant_id, user_id) VALUES (4, '侍 安民', 5, 'ゆっくりできる場所です。', 1, 8);
INSERT IGNORE INTO review(id, name, star, description, restaurant_id, user_id) VALUES (5, '侍 裕子', 5, '雰囲気がゆっくりできて落ち着ける場所です。', 1, 10);
INSERT IGNORE INTO review(id, name, star, description, restaurant_id, user_id) VALUES (6, '侍 信平', 5, 'コーヒーだけではなく、カフェオレやランチのトーストなども美味しく、いっぱい食べれて最高でした。', 1, 12);

INSERT IGNORE INTO favorite (id, user_id, restaurant_id) VALUES (1, 2, 1);
INSERT IGNORE INTO favorite (id, user_id, restaurant_id) VALUES (2, 2, 3);
INSERT IGNORE INTO favorite (id, user_id, restaurant_id) VALUES (3, 2, 5);
INSERT IGNORE INTO favorite (id, user_id, restaurant_id) VALUES (4, 2, 7);
INSERT IGNORE INTO favorite (id, user_id, restaurant_id) VALUES (5, 2, 9);
INSERT IGNORE INTO favorite (id, user_id, restaurant_id) VALUES (6, 2, 11);

INSERT IGNORE INTO category (id, name) VALUES(1, "カフェ");
INSERT IGNORE INTO category (id, name) VALUES(2, "焼肉");
INSERT IGNORE INTO category (id, name) VALUES(3, "寿司");
INSERT IGNORE INTO category (id, name) VALUES(4, "中華");
INSERT IGNORE INTO category (id, name) VALUES(5, "丼物");
INSERT IGNORE INTO category (id, name) VALUES(6, "和食");

INSERT IGNORE INTO reservation (id, restaurant_id, user_id, checkin_date, number_of_people, amount) VALUES (1, 1, 2, '2023-04-01　13:00', 2, 2000);
INSERT IGNORE INTO reservation (id, restaurant_id, user_id, checkin_date, number_of_people, amount) VALUES (2, 2, 2, '2023-04-01 17:30', 3, 3000);
INSERT IGNORE INTO reservation (id, restaurant_id, user_id, checkin_date, number_of_people, amount) VALUES (3, 4, 2, '2023-04-01 18:00', 4, 8000);
INSERT IGNORE INTO reservation (id, restaurant_id, user_id, checkin_date, number_of_people, amount) VALUES (4, 6, 2, '2023-04-01 20:00', 1, 3000);
INSERT IGNORE INTO reservation (id, restaurant_id, user_id, checkin_date, number_of_people, amount) VALUES (5, 7, 2, '2023-04-01 15:00', 2, 2000);
INSERT IGNORE INTO reservation (id, restaurant_id, user_id, checkin_date, number_of_people, amount) VALUES (6, 9, 2, '2023-04-01 16:00', 3, 3000);