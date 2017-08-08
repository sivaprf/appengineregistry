DROP TABLE `products`;
CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `summary` varchar(2048) NOT NULL,
  `description` varchar(2048) NOT NULL,
  `price` decimal(5,2) DEFAULT '0.00',
  `thumb` varchar(2048) DEFAULT NULL,
  `image` varchar(2048) DEFAULT NULL,
  `last_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `category` varchar(1000) DEFAULT NULL,
  `subcategory` varchar(1000) DEFAULT NULL,
  `details` varchar(4000) default NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `gclouddemo_productstore`.`products` (`summary`, `description`, `price`, `category`, `subcategory`, `details`, `image`, `thumb`)
VALUES ('Google Women\'s Short Sleeve Hero Tee Black', '100% cotton jersey fabric makes this Google t-shirt perfect for the Google hero in your life. Made in USA.',
	'16.99', 'Women\'s Apparel', 'T-Shirts', '100% Cotton\n30 Singles jersey\nCrew neck\nMade in USA',
	'https://storage.googleapis.com/wombat-171617-ecimgs/women_tshirt.jpg',
	'https://storage.googleapis.com/wombat-171617-ecimgs/img01-thumb.jpg');

INSERT INTO `gclouddemo_productstore`.`products` (`summary`, `description`, `price`, `category`, `subcategory`, `details`, `image`, `thumb`)
VALUES ('Google Women\'s Long Sleeve Blended Cardigan Charcoal', 'Made in the USA quality and a soft-feeling triple blended material make this cardigan a winner in cool weather or cold offices.',
	'20.99', 'Women\'s Apparel', 'Outerwear', '38% cotton/50% polyester/12% rayon\nMade in USA',
	'https://storage.googleapis.com/wombat-171617-ecimgs/womens_outerwear.jpg',
	'https://storage.googleapis.com/wombat-171617-ecimgs/GGOEGAAX0308.jpg');

INSERT INTO `gclouddemo_productstore`.`products` (`summary`, `description`, `price`, `category`, `subcategory`, `details`, `image`, `thumb`)
VALUES ('Google Men\'s 100% Cotton Short Sleeve Hero Tee White', 'Be a Google Hero. 100% cotton jersey fabric sets this Google t-shirt above the crowd. Features the Google logo across the chest.',
	'16.99', 'Men\'s Apparel', 'T-Shirts', '100% Cotton\n30 Singles Jersey\nMade in the USA',
	'https://storage.googleapis.com/wombat-171617-ecimgs/mens.jpg',
	'https://storage.googleapis.com/wombat-171617-ecimgs/GGOEGAAX0104.jpg');

