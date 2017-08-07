-- Other sites join table
CREATE TABLE `PostOtherSites` (
  `postId` bigint(20) NOT NULL,
  `siteId` varchar(255) DEFAULT NULL,
  KEY `fk_post_othersites_post_id` (`postId`),
  CONSTRAINT `fk_post_othersites_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- other Grades join table
CREATE TABLE `PostOtherGrades` (
  `postId` bigint(20) NOT NULL,
  `gradeId` varchar(255) DEFAULT NULL,
  KEY `fk_post_othergrades_post_id` (`postId`),
  CONSTRAINT `fk_post_othergrades_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Other specialties join table
CREATE TABLE `PostOtherSpecialties` (
  `postId` bigint(20) NOT NULL,
  `specialtyId` bigint(20) DEFAULT NULL,
  KEY `fk_post_otherspecialties_post_id` (`postId`),
  CONSTRAINT `fk_post_otherspecialties_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`),
  KEY `fk_post_otherspecialties_specialty_id` (`specialtyId`),
    CONSTRAINT `fk_post_otherspecialties_specialty_id` FOREIGN KEY (`specialtyId`) REFERENCES `Specialty` (`id`)
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

ALTER TABLE Post CHANGE mainSiteLocated mainSiteLocatedId varchar(255);
ALTER TABLE Post CHANGE employingBody employingBodyId varchar(255);
ALTER TABLE Post CHANGE trainingBody trainingBodyId varchar(255);
ALTER TABLE Post CHANGE approvedGrade approvedGradeId varchar(255);
ALTER TABLE Post CHANGE oldPostId oldPostId bigint(20);
ALTER TABLE Post CHANGE newPostId newPostId bigint(20);


ALTER TABLE Post ADD COLUMN suffix varchar(255);
ALTER TABLE Post ADD COLUMN managingLocalOffice varchar(255);
ALTER TABLE Post ADD COLUMN postFamily varchar(255);
ALTER TABLE Post ADD COLUMN localPostNumber varchar(255);
ALTER TABLE Post ADD COLUMN specialtyId bigint(20);
ALTER TABLE Post ADD COLUMN subspecialtyId bigint(20);
ALTER TABLE Post ADD COLUMN programmeId bigint(20);
ALTER TABLE Post ADD COLUMN trainingDescription varchar(255);

ALTER TABLE `Post` ADD INDEX `oldPostId` (`oldPostId`);
ALTER TABLE `Post` ADD CONSTRAINT `oldPostId` FOREIGN KEY (`oldPostId`) REFERENCES `Post` (`id`);
ALTER TABLE `Post` ADD INDEX `newPostId` (`newPostId`);
ALTER TABLE `Post` ADD CONSTRAINT `newPostId` FOREIGN KEY (`newPostId`) REFERENCES `Post` (`id`);
ALTER TABLE `Post` ADD INDEX `specialtyId` (`specialtyId`);
ALTER TABLE `Post` ADD CONSTRAINT `specialtyId` FOREIGN KEY (`specialtyId`) REFERENCES `Specialty` (`id`);
ALTER TABLE `Post` ADD INDEX `subspecialtyId` (`subspecialtyId`);
ALTER TABLE `Post` ADD CONSTRAINT `subspecialtyId` FOREIGN KEY (`subspecialtyId`) REFERENCES `Specialty` (`id`);
ALTER TABLE `Post` ADD INDEX `programmeId` (`programmeId`);
ALTER TABLE `Post` ADD CONSTRAINT `programmeId` FOREIGN KEY (`programmeId`) REFERENCES `Programme` (`id`);
