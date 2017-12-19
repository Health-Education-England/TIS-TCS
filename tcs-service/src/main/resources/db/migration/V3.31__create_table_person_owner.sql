CREATE TABLE `PersonOwner` (
  `id` bigint(20) NOT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `rule` varchar(255) DEFAULT NULL,
  UNIQUE KEY `person_owner_pk` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `PersonOwner` ADD CONSTRAINT `fk_person_owner_person_id` FOREIGN KEY (`id`) REFERENCES `Person` (`id`);

