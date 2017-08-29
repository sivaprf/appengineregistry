DROP TABLE `products`;
CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `summary` varchar(2048) NOT NULL,
  `sku` varchar(1024) NOT NULL,
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
