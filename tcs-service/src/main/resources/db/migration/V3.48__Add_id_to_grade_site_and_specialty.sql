-- Post sites join table - many to many join table
DROP TABLE IF EXISTS `PostSite`;
DROP TABLE IF EXISTS `PostGrade`;
DROP TABLE IF EXISTS `PostSpecialty`;

CREATE TABLE `PostSite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `postId` bigint(20) NOT NULL,
  `siteId` varchar(255) NOT NULL,
  `postSiteType` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_post_postsite_post_id` (`postId`),
  CONSTRAINT `fk_post_postsite_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- other Grades join table
CREATE TABLE `PostGrade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `postId` bigint(20) NOT NULL,
  `gradeId` varchar(255) NOT NULL,
  `postGradeType` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_post_postgrade_post_id` (`postId`),
  CONSTRAINT `fk_post_postgrade_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- post specialties join table - many to many join table
CREATE TABLE `PostSpecialty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `postId` bigint(20) NOT NULL,
  `specialtyId` bigint(20) NOT NULL,
  `postSpecialtyType` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_post_postspecialty_post_id` (`postId`),
  CONSTRAINT `fk_post_postspecialty_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`),
  KEY `fk_post_postspecialty_specialty_id` (`specialtyId`),
  CONSTRAINT `fk_post_postspecialty_specialty_id` FOREIGN KEY (`specialtyId`) REFERENCES `Specialty` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
