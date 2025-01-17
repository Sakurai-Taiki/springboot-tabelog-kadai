-- カテゴリテーブル
INSERT IGNORE INTO category(id, category_name)VALUE(1, '和食');
INSERT IGNORE INTO category(id, category_name)VALUE(2, '揚げ物');
INSERT IGNORE INTO category(id, category_name)VALUE(3, '居酒屋');
INSERT IGNORE INTO category(id, category_name)VALUE(4, 'イタリアン');
INSERT IGNORE INTO category(id, category_name)VALUE(5, 'ラーメン');
INSERT IGNORE INTO category(id, category_name)VALUE(6, '中華料理');
INSERT IGNORE INTO category(id, category_name)VALUE(7, 'バー');
INSERT IGNORE INTO category(id, category_name)VALUE(8, '定食');
INSERT IGNORE INTO category(id, category_name)VALUE(9, 'ハンバーガー');
INSERT IGNORE INTO category(id, category_name)VALUE(10, '天ぷら');
INSERT IGNORE INTO category(id, category_name)VALUE(11, 'スイーツ');
INSERT IGNORE INTO category(id, category_name)VALUE(12, 'パン');
INSERT IGNORE INTO category(id, category_name)VALUE(13, 'すき焼き');
INSERT IGNORE INTO category(id, category_name)VALUE(14, 'ハンバーグ');
INSERT IGNORE INTO category(id, category_name)VALUE(15, '海鮮料理');
INSERT IGNORE INTO category(id, category_name)VALUE(16, '鉄板焼き');
INSERT IGNORE INTO category(id, category_name)VALUE(17, '寿司');
INSERT IGNORE INTO category(id, category_name)VALUE(18, '焼き鳥');
INSERT IGNORE INTO category(id, category_name)VALUE(19, 'お好み焼き');
INSERT IGNORE INTO category(id, category_name)VALUE(20, '鍋料理');

-- 店舗テーブル
INSERT IGNORE INTO stores (id, category_id, category_name, store_name, photo_name, description, min_budget, max_budget, open_hour, close_hour, store_post_code, store_address, store_phone_number, close_day, seats) VALUES (1, 3, '居酒屋', '居酒屋ぽんぬ', 'store01.jpg', '居酒屋の説明文項目', 1000, 5000, '11:00', '22:00', '073-0145', '愛知県名古屋市', '012-345-678', '不定休', 30);
INSERT IGNORE INTO stores (id, category_id, category_name, store_name, photo_name, description, min_budget, max_budget, open_hour, close_hour, store_post_code, store_address, store_phone_number, close_day, seats) VALUES (2, 8, '定食', '楽々屋', 'store03.jpg', '居酒屋の説明文項目', 2000, 4000, '11:00', '22:00', '073-0145', '愛知県名古屋市', '012-345-678', '不定休', 30);
INSERT IGNORE INTO stores (id, category_id, category_name, store_name, photo_name, description, min_budget, max_budget, open_hour, close_hour, store_post_code, store_address, store_phone_number, close_day, seats) VALUES (3, 4, 'イタリアン', 'パスタと猫', 'store02.jpg', '居酒屋の説明文項目', 3000, 5000, '11:00', '22:00', '073-0145', '愛知県名古屋市', '012-345-678', '不定休', 30);
INSERT IGNORE INTO stores (id, category_id, category_name, store_name, photo_name, description, min_budget, max_budget, open_hour, close_hour, store_post_code, store_address, store_phone_number, close_day, seats) VALUES (4, 3, '居酒屋', '酒を飲むなら三十郎	', 'store01.jpg', '居酒屋の説明文項目', 4000, 7000, '11:00', '22:00', '073-0145', '愛知県名古屋市', '012-345-678', '不定休', 30);
INSERT IGNORE INTO stores (id, category_id, category_name, store_name, photo_name, description, min_budget, max_budget, open_hour, close_hour, store_post_code, store_address, store_phone_number, close_day, seats) VALUES (5, 4, 'イタリアン', 'mokka', 'store02.jpg', '居酒屋の説明文項目', 5000, 9000, '11:00', '22:00', '073-0145', '愛知県名古屋市', '012-345-678', '不定休', 30);
INSERT IGNORE INTO stores (id, category_id, category_name, store_name, photo_name, description, min_budget, max_budget, open_hour, close_hour, store_post_code, store_address, store_phone_number, close_day, seats) VALUES (6, 2, 'アジアン居酒屋', 'アジアンチキン', 'store01.jpg', '居酒屋の説明文項目', 2000, 5000, '11:00', '22:00', '073-0145', '愛知県名古屋市', '012-345-678', '不定休', 30);
INSERT IGNORE INTO stores (id, category_id, category_name, store_name, photo_name, description, min_budget, max_budget, open_hour, close_hour, store_post_code, store_address, store_phone_number, close_day, seats) VALUES (7, 7, 'バー', 'ririya', 'store03.jpg', '居酒屋の説明文項目', 3000, 8000, '11:00', '22:00', '073-0145', '愛知県名古屋市', '012-345-678', '不定休', 30);
INSERT IGNORE INTO stores (id, category_id, category_name, store_name, photo_name, description, min_budget, max_budget, open_hour, close_hour, store_post_code, store_address, store_phone_number, close_day, seats) VALUES (8, 3, '居酒屋', 'ぽんしゅえっと', 'store01.jpg', '居酒屋の説明文項目', 2000, 4000, '11:00', '22:00', '073-0145', '愛知県名古屋市', '012-345-678', '不定休', 30);
INSERT IGNORE INTO stores (id, category_id, category_name, store_name, photo_name, description, min_budget, max_budget, open_hour, close_hour, store_post_code, store_address, store_phone_number, close_day, seats) VALUES (9, 19, 'お好み焼き', 'nago屋', 'store03.jpg', '居酒屋の説明文項目', 1000, 3000, '11:00', '22:00', '073-0145', '愛知県名古屋市', '012-345-678', '不定休', 30);
INSERT IGNORE INTO stores (id, category_id, category_name, store_name, photo_name, description, min_budget, max_budget, open_hour, close_hour, store_post_code, store_address, store_phone_number, close_day, seats) VALUES (10, 2, '揚げ物', 'ナゴヤン', 'store02.jpg', '居酒屋の説明文項目', 4000, 10000, '11:00', '22:00', '073-0145', '愛知県名古屋市', '012-345-678', '不定休', 30);

-- rolesテーブル
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_GENERAL');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (id, name) VALUES (3, 'ROLE_PRIME');


-- 会員テーブル
INSERT IGNORE INTO users (id, user_name, furigana, mail_address, user_password, user_post_code, user_address, user_phone_number, roles_id, enabled) VALUES (1, '櫻井　ぽんぬ','サクライ　ポンヌ', 'ponnu.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', '123-4567', '長野県三条市本原11-6', '090-1234-5678', 1, 1);
INSERT IGNORE INTO users (id, user_name, furigana, mail_address, user_password, user_post_code, user_address, user_phone_number, roles_id, enabled) VALUES (2, '櫻井　らら','サクライ　ララ', 'lala.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', '123-4567', '長野県三条市本原11-6', '090-1234-5678', 3, 1);
INSERT IGNORE INTO users (id, user_name, furigana, mail_address, user_password, user_post_code, user_address, user_phone_number, roles_id, enabled) VALUES (3, '櫻井　モカ','サクライ　モカ', 'moka.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', '123-4567', '長野県三条市本原11-6', '090-1234-5678', 1, 1);
INSERT IGNORE INTO users (id, user_name, furigana, mail_address, user_password, user_post_code, user_address, user_phone_number, roles_id, enabled) VALUES (4, '櫻井　キキ','サクライ　キキ', 'kiki.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', '123-4567', '長野県三条市本原11-6', '090-1234-5678', 2, 1);
INSERT IGNORE INTO users (id, user_name, furigana, mail_address, user_password, user_post_code, user_address, user_phone_number, roles_id, enabled) VALUES (5, '櫻井　三十郎','サクライ　サンジュウロウ', 'sanjyurou.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', '123-4567', '岡山県長岡市条南町17', '090-1234-5678', 3, 1);
INSERT IGNORE INTO users (id, user_name, furigana, mail_address, user_password, user_post_code, user_address, user_phone_number, roles_id, enabled) VALUES (6, '櫻井　チキン','サクライ　チキン', 'chikin.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', '123-4567', '新潟県柏崎市真田町本原90', '090-1234-5678', 1, 1);

-- 予約テーブル
INSERT IGNORE INTO reserve (user_id, store_id, checkin_date, checkin_time, number_of_people) VALUES (1, 1, '2024-11-20', '11:00:00', 5);
INSERT IGNORE INTO reserve (user_id, store_id, checkin_date, checkin_time, number_of_people) VALUES (2, 1, '2024-11-20', '17:00:00', 4);


-- レビューテーブル
INSERT IGNORE INTO reviews (score, content, user_id, store_id) VALUES  (5, '絶品料理！', 5, 1);
INSERT IGNORE INTO reviews (score, content, user_id, store_id) VALUES  (1, '最低料理！', 2, 1);

-- お気に入りテーブル
INSERT IGNORE INTO favorite (user_id, store_id) VALUES (5, 1);
