SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE `Placement`;

-- Placement specialties join table - many to many join table
CREATE TABLE `PlacementSpecialty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `placementId` bigint(20) NOT NULL,
  `specialtyId` bigint(20) NOT NULL,
  `placementSpecialtyType` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
    KEY `fk_placementspecialty_placement` (`placementId`),
  CONSTRAINT `fk_placementspecialty_placement_id` FOREIGN KEY (`placementId`) REFERENCES `Placement` (`id`),
    KEY `fk_placementspecialty_specialty` (`specialtyId`),
  CONSTRAINT `fk_placementspecialty_specialty_id` FOREIGN KEY (`specialtyId`) REFERENCES `Specialty` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `Placement`
DROP COLUMN `slotShare`;

ALTER TABLE `Placement`
ADD COLUMN `traineeId` BIGINT(20) DEFAULT NULL;

ALTER TABLE `Placement`
ADD COLUMN `clinicalSupervisorId` BIGINT(20) DEFAULT NULL;

ALTER TABLE `Placement`
ADD CONSTRAINT `fk_placement_trainee_id` FOREIGN KEY (`traineeId`) REFERENCES `Person` (`id`);

ALTER TABLE `Placement`
ADD CONSTRAINT `fk_placement_clinical_supervisor_id` FOREIGN KEY (`clinicalSupervisorId`) REFERENCES `Person` (`id`);

ALTER TABLE `Placement`
ADD COLUMN `postId` BIGINT(20) DEFAULT NULL;

ALTER TABLE `Placement`
ADD CONSTRAINT `fk_placement_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`);

ALTER TABLE `Placement`
CHANGE `grade` `gradeId` BIGINT(20) DEFAULT NULL;

ALTER TABLE `Placement`
CHANGE `site` `siteId` BIGINT(20) DEFAULT NULL;

ALTER TABLE `Placement`
ADD COLUMN `managingLocalOffice` VARCHAR(255) DEFAULT NULL;

ALTER TABLE `Placement`
ADD COLUMN `trainingDescription` VARCHAR(255) DEFAULT NULL;

ALTER TABLE `Placement`
ADD COLUMN `localPostNumber` VARCHAR(255) DEFAULT NULL;

SET FOREIGN_KEY_CHECKS = 1;
