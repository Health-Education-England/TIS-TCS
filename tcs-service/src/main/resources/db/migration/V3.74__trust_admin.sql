-- add support for people and posts to be linked to a trust

CREATE TABLE `PostTrust` (
  `id` bigint(20) NOT NULL,
  `postId` bigint(20) NOT NULL,
  `trustId` bigint(20) NOT NULL,
  `trustCode` varchar(255) DEFAULT NULL,
  `trustName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `posttrust_post_id_idx` (`postId`),
  CONSTRAINT `fk_posttrust_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `PersonTrust` (
  `id` bigint(20) NOT NULL,
  `personId` bigint(20) NOT NULL,
  `trustId` bigint(20) NOT NULL,
  `trustCode` varchar(255) DEFAULT NULL,
  `trustName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `persontrust_person_id_idx` (`personId`),
  CONSTRAINT `fk_persontrust_person_id` FOREIGN KEY (`personId`) REFERENCES `Person` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
