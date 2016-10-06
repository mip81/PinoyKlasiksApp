-- Table will consists information about last update
CREATE TABLE `tb_version` (`version`	TEXT NOT NULL,`last_changes_datetime`	TEXT NOT NULL);
INSERT INTO tb_version (version, last_changes_datetime) VALUES ('1', '');
-- Table keeps information about status_name
CREATE TABLE `tb_status` (`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`status_name`	INTEGER NOT NULL);
INSERT INTO tb_status (_id, status_name) VALUES (1, 'Cancel');
INSERT INTO tb_status (_id, status_name) VALUES (2, 'Pending');
INSERT INTO tb_status (_id, status_name) VALUES (3, 'Finished');
-- Table keeps information about types of orders
CREATE TABLE "tb_typeorder" (`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,`type_order`	TEXT NOT NULL UNIQUE);
-- Table keeps information about suborders
CREATE TABLE tb_suborder (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, dish_id INTEGER NOT NULL REFERENCES tb_dish (_id), quantity INTEGER NOT NULL, price REAL NOT NULL, order_id INTEGER REFERENCES tb_order (_id));
-- Table: tb_order
CREATE TABLE tb_order (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, order_datetime_now DATETIME NOT NULL, order_datetime_for DATETIME NOT NULL, customer_id INTEGER NOT NULL REFERENCES tb_customer (_id), sub_order_id INTEGER NOT NULL REFERENCES tb_suborder (_id), status_id INTEGER NOT NULL REFERENCES tb_status (_id), type_order_id INTEGER NOT NULL REFERENCES tb_typeorder (_id), num_persons INTEGER NOT NULL, location TEXT NOT NULL, global_id INTEGER, comment TEXT);
-- Table keeps information about dishes available in resauraunt
CREATE TABLE tb_dish (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, cat_id INT NOT NULL REFERENCES tb_category (_id), dish_name TEXT NOT NULL UNIQUE, dish_desc TEXT NOT NULL, dish_price REAL NOT NULL, dish_pic TEXT);
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (1, 1, 'Tapsilog', 'Beef strips marinated in soy sauce.', 13.5, 'tapsilog.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (2, 1, 'Longsilog', 'Filipino style pork sausage.', 13.5, 'lonsilog.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (3, 1, 'Tocilog', 'Cured pork meat with sugar and salt.', 13.5, 'tocilog.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (4, 1, 'Bangsilog', 'Marinated milkfish in vinegar.', 15, 'bansilog.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (5, 3, 'Sizzling Pinakbet', 'All the best vegetables in the Philipines has to offer come together with bagoong in this llocano vegetable medley.', 15, 'sizpin.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (6, 3, 'Laing', 'Taro leaves, slowly simmered in coconut cream and flavoured with pork, ginger and chrimp paste.', 14.5, 'laing.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (7, 3, 'Ensaladang Talong (Seasonal)', 'A traditional accompaniment pinoy fare: grilled eggplants with freshly chopped onion, tomatoes & vinaigrette.', 14.5, 'enstal.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (8, 2, 'BBQ Pork', 'Tender pork cutlets marinated in Chef Mira''s secret sauce. Grilled to your taste.', 15, 'bbqpor.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (9, 2, 'Crispy Binagoongan', 'Deep fried pork belly to a golden crisp. Served with shrimp paste flavoured rice. Sinful but heavenly.', 17, 'cribin.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (10, 2, 'Crispy Sinigang', 'Deep fried pork belly to a golden crisp. Served with tamarind flavored rice.', 17, 'crisin.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (11, 2, 'Lechon Kawali', 'Crispy and lightly seasoned pork served with soy sauce and mang tomas.', 15, 'leckaw.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (12, 2, 'Crispy Pata', 'Deep fried pork hock. Crispy and meat just melts in your mouth.', 31.5, 'cripat.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (13, 2, 'Dinuguan', 'Pork blood stew. We challenge you to eat this one of the most popular dishes in the Phillippines.', 16, 'dinuguan.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (14, 2, 'Lechon Paksiw', 'Crispy pork belly with liver pate sauce.', 16, 'lecpak.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (15, 2, 'Pork Sinigang', 'Pork belly, slow-cooked in tamarind broth. This will make you hangry!', 15, 'porsin.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (16, 2, 'Sizzling Sisig', 'Pork meat grilled and chopped to perfection. Crispy, tangy and meaty!', 16, 'sizsis.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (17, 9, 'Chicken Inasal', 'Grilled chicken marinated in vinegar.', 15, 'chiina.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (18, 9, 'Pinoy Pride Chicken', 'Deep fried chicken the Pinoy style.', 15, 'pinpri.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (19, 9, 'Chicken BBQ', 'Grilled chicken wings. Marinated in soy sauce and honey. Thumbs up for the juciness!', 15, 'chibbq.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (20, 8, 'Sinigang na Bangus', 'Milkfish belly cooked in tamrind soup.', 13.5, 'sinban.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (21, 8, 'Sinigang na Hipon', 'Prawns cooked in tamarind soup.', 17, 'sinna.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (22, 8, 'Halabos na Hipon', 'Prawns cooked in butter and garllic.', 17, 'halna.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (23, 7, 'Caldereta', 'Filipino style beef stew. Ohhhh so yum...', 15, 'caldereta.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (24, 7, 'Bistek Tagalog', 'Filipino version of beef steak. Marinated beef tender cuts in lemon, salt and pepper.', 14, 'bistag.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (25, 7, 'Salpicao', 'Beef and mushroom sauteed in Worcestershire sauce.', 14, 'salpicao.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (26, 7, 'Kare-Kare', 'Slow cooked beef in rich peanut sauce with vegetables.', 15.5, 'kare-kare.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (27, 7, 'Sizzling Bulalo', 'Sizzling beef bone shank with a kight Chef''s gravy.', 15.5, 'sizbul.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (28, 6, 'Bihon Guisado', 'Stir-Fry rice noodles with pork, shrimp, fish balls and vegetables.', 13, 'bihgui.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (29, 6, 'Canton Guisado', 'Stir-Fry egg noodles with pork, shrimp-flavoured thick sauce.', 13.5, 'cangui.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (30, 6, 'Palabok', 'Thick rice noodles with shrimp-flavoured thick sauce.', 13.5, 'palabok.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (31, 6, 'Bulalo Mami', 'Egg noodles and beef bone shank soup.', 16, 'bulmam.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (32, 5, 'Shanghai', 'Pinoy style spring rolls (9 pcs.)', 7.5, 'shanghai.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (33, 5, 'Tokwa''t Baboy', 'Tofu & fried pork cut in small pieces. With soy vinegar sauce.', 5, 'tokbab.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (34, 5, 'BBQ Pork on Skewer', '  ', 3.5, 'bbqpor.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (35, 4, 'Sinigang Rice', 'Tamarind Rice.', 4.5, 'sinric.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (36, 4, 'Binagoongan Rice', 'Shrimp-paste Rice.', 4.5, 'binric.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (37, 4, 'Adobo Rice', 'Pork and soy vinegar Rice.', 4.5, 'adoric.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (38, 4, 'Aligue Rice', 'Fried rice sauteed with crab paste.', 4.5, 'aliric.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (39, 4, 'Tinapa Fried Rice', 'Fried rice with bits of smocked fish.', 4.5, 'tinfri.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (40, 4, 'Java Rice', 'Rice cooked with tumeric and annatto powder.', 3.5, 'javric.jpg');
INSERT INTO tb_dish (_id, cat_id, dish_name, dish_desc, dish_price, dish_pic) VALUES (41, 4, 'Plain Rice', ' ', 2, 'plaric');
-- Table keeps information about categories of dishes
CREATE TABLE tb_category (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, cat_name TEXT NOT NULL UNIQUE, description TEXT (60), order_id INT REFERENCES tb_order (_id));
INSERT INTO tb_category (_id, cat_name, description, order_id) VALUES (1, 'All day breakfast', 'Comes with garlic fried rice and fried egg. Combined into one meal.', 1);
INSERT INTO tb_category (_id, cat_name, description, order_id) VALUES (2, 'Pork', NULL, 2);
INSERT INTO tb_category (_id, cat_name, description, order_id) VALUES (3, 'Vegetables', NULL, 3);
INSERT INTO tb_category (_id, cat_name, description, order_id) VALUES (4, 'Extra rice', NULL, 8);
INSERT INTO tb_category (_id, cat_name, description, order_id) VALUES (5, 'Extras', NULL, 9);
INSERT INTO tb_category (_id, cat_name, description, order_id) VALUES (6, 'Noodles', NULL, 4);
INSERT INTO tb_category (_id, cat_name, description, order_id) VALUES (7, 'Beef', NULL, 5);
INSERT INTO tb_category (_id, cat_name, description, order_id) VALUES (8, 'Seafoods', NULL, 6);
INSERT INTO tb_category (_id, cat_name, description, order_id) VALUES (9, 'Chicken', NULL, 7);
-- Table keeps information about customer
CREATE TABLE tb_customer (_id INTEGER NOT NULL UNIQUE, customer_name TEXT NOT NULL, phone_number TEXT NOT NULL, email INTEGER, address TEXT (150), PRIMARY KEY (_id));
-- Table keeps information about comment and rating that customer made for dish
CREATE TABLE tb_comment (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, dish_id INTEGER NOT NULL REFERENCES tb_dish (_id), comment_text TEXT NOT NULL, rate INTEGER NOT NULL);
