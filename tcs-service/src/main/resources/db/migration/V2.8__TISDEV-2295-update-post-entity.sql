-- Wrapper Site table
CREATE TABLE `Site` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Post sites join table - many to many join table
CREATE TABLE `PostSite` (
  `postId` bigint(20) NOT NULL,
  `siteId` varchar(255) DEFAULT NULL,
  `postSiteType` varchar(255) DEFAULT NULL,
  KEY `fk_post_postsite_post_id` (`postId`),
  CONSTRAINT `fk_post_postsite_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`),
  KEY `fk_site_postsite_site_id` (`siteId`),
  CONSTRAINT `fk_site_postsite_site_id` FOREIGN KEY (`siteId`) REFERENCES `Site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Wrapper Grade table
CREATE TABLE `Grade` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- other Grades join table
CREATE TABLE `PostGrade` (
  `postId` bigint(20) NOT NULL,
  `gradeId` varchar(255) DEFAULT NULL,
  `postGradeType` varchar(255) DEFAULT NULL,
  KEY `fk_post_postgrade_post_id` (`postId`),
  CONSTRAINT `fk_post_postgrade_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`),
  KEY `fk_grade_postgrade_grade_id` (`gradeId`),
  CONSTRAINT `fk_grade_postgrade_grade_id` FOREIGN KEY (`gradeId`) REFERENCES `Grade` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- post specialties join table - many to many join table
CREATE TABLE `PostSpecialty` (
  `postId` bigint(20) NOT NULL,
  `specialtyId` bigint(20) DEFAULT NULL,
  `postSpecialtyType` varchar(255) DEFAULT NULL,
  KEY `fk_post_postspecialty_post_id` (`postId`),
  CONSTRAINT `fk_post_postspecialty_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`),
  KEY `fk_post_postspecialty_specialty_id` (`specialtyId`),
  CONSTRAINT `fk_post_postspecialty_specialty_id` FOREIGN KEY (`specialtyId`) REFERENCES `Specialty` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- placement history join table
CREATE TABLE `PostPlacementHistory` (
  `postId` bigint(20) NOT NULL,
  `placementId` bigint(20) DEFAULT NULL,
  KEY `fk_post_placementhistory_post_id` (`postId`),
  CONSTRAINT `fk_post_placementhistory_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`),
  KEY `fk_post_placementhistory_placement_id` (`placementId`),
    CONSTRAINT `fk_post_placementhistory_placement_id` FOREIGN KEY (`placementId`) REFERENCES `Placement` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE Post DROP COLUMN postOwner;
ALTER TABLE Post DROP COLUMN leadSite;
ALTER TABLE Post DROP COLUMN postSpecialty;
ALTER TABLE Post DROP COLUMN fullTimeEquivelent;
ALTER TABLE Post DROP COLUMN leadProvider;
ALTER TABLE Post DROP COLUMN mainSiteLocated;
ALTER TABLE Post DROP COLUMN approvedGrade;

ALTER TABLE Post CHANGE employingBody employingBodyId varchar(255);
ALTER TABLE Post CHANGE trainingBody trainingBodyId varchar(255);
ALTER TABLE Post CHANGE oldPostId oldPostId bigint(20);
ALTER TABLE Post CHANGE newPostId newPostId bigint(20);

ALTER TABLE Post ADD COLUMN suffix varchar(255);
ALTER TABLE Post ADD COLUMN managingLocalOffice varchar(255);
ALTER TABLE Post ADD COLUMN postFamily varchar(255);
ALTER TABLE Post ADD COLUMN localPostNumber varchar(255);
ALTER TABLE Post ADD COLUMN programmeId bigint(20);
ALTER TABLE Post ADD COLUMN trainingDescription varchar(255);

ALTER TABLE `Post` ADD INDEX `oldPostId` (`oldPostId`);
ALTER TABLE `Post` ADD CONSTRAINT `oldPostId` FOREIGN KEY (`oldPostId`) REFERENCES `Post` (`id`);
ALTER TABLE `Post` ADD INDEX `newPostId` (`newPostId`);
ALTER TABLE `Post` ADD CONSTRAINT `newPostId` FOREIGN KEY (`newPostId`) REFERENCES `Post` (`id`);
