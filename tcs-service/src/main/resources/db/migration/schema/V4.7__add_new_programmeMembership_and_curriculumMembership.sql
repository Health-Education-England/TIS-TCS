-- create tables
CREATE TABLE `ProgrammeMembershipInterim` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `personId` bigint(20) NOT NULL,
  `programmeId` bigint(20) NOT NULL,
  `rotationId` bigint(20) DEFAULT NULL,
  `trainingNumberId` bigint(20) DEFAULT NULL,
  `rotation` varchar(255) DEFAULT NULL,
  `programmeMembershipType` varchar(255) DEFAULT NULL,
  `programmeStartDate` date DEFAULT NULL,
  `programmeEndDate` date DEFAULT NULL,
  `leavingDestination` varchar(255) DEFAULT NULL,
  `leavingReason` varchar(255) DEFAULT NULL,
  `trainingPathway` varchar(255) DEFAULT NULL,
  `amendedDate` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `fk_ProgrammeMembershipInterim_person_id` (`personId`),
  KEY `fk_ProgrammeMembershipInterim_programme_id` (`programmeId`),
  KEY `fk_programmeMembershipInterim_rotation_id` (`rotationId`),
  KEY `fk_ProgrammeMembershipInterim_training_number_id` (`trainingNumberId`),
  CONSTRAINT `fk_ProgrammeMembershipInterim_person_id` FOREIGN KEY (`personId`) REFERENCES `Person` (`id`),
  CONSTRAINT `fk_ProgrammeMembershipInterim_programme_id` FOREIGN KEY (`programmeId`) REFERENCES `Programme` (`id`),
  CONSTRAINT `fk_ProgrammeMembershipInterim_training_number_id` FOREIGN KEY (`trainingNumberId`) REFERENCES `TrainingNumber` (`id`),
  CONSTRAINT `fk_programmeMembershipInterim_rotation_id` FOREIGN KEY (`rotationId`) REFERENCES `Rotation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `CurriculumMembershipInterim` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `programmeMembershipId` bigint(20) NOT NULL,
  `curriculumId` bigint(20) NOT NULL,
  `curriculumStartDate` date DEFAULT NULL,
  `curriculumEndDate` date DEFAULT NULL,
  `periodOfGrace` int(11) DEFAULT NULL,
  `amendedDate` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `curriculumCompletionDate` date DEFAULT NULL,
  `intrepidId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_curriculumMembershipInterim_programmeMembership_id` (`programmeMembershipId`),
  KEY `fk_curriculumMembershipInterim_curriculum_id` (`curriculumId`),
  CONSTRAINT `fk_curriculumMembershipInterim_curriculum_id` FOREIGN KEY (`curriculumId`) REFERENCES `Curriculum` (`id`),
  CONSTRAINT `fk_curriculumMembershipInterim_programmeMembership_id` FOREIGN KEY (`programmeMembershipId`) REFERENCES `ProgrammeMembershipInterim` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- add data
INSERT INTO `ProgrammeMembershipInterim`(`personId`, `programmeId`, `rotationId`, `trainingNumberId`, `rotation`, `programmeMembershipType`,
`programmeStartDate`, `programmeEndDate`, `leavingDestination`, `leavingReason`, `trainingPathway`, `amendedDate`)
SELECT `personId`, `programmeId`, `rotationId`, `trainingNumberId`, `rotation`, `programmeMembershipType`,
`programmeStartDate`, `programmeEndDate`, `leavingDestination`, `leavingReason`, `trainingPathway`, `amendedDate` FROM `ProgrammeMembership`
WHERE `id` IN (
	SELECT MAX(`id`) from `ProgrammeMembership` GROUP BY `personId`, `programmeStartDate`, `programmeEndDate`, `programmeId`, `programmeMembershipType`
);
-- select count(*) from (
-- select distinct personId, programmeStartDate, programmeEndDate, programmeId, programmeMembershipType from ProgrammeMembership) a;

INSERT INTO `CurriculumMembershipInterim` (
	`id`, `programmeMembershipId`, `curriculumId`, `curriculumStartDate`, `curriculumEndDate`,
    `periodOfGrace`, `amendedDate`, `curriculumCompletionDate`, `intrepidId`)
SELECT `b`.`id` AS `id`, `a`.`id` AS `programmeMembershipId`, `curriculumId`, `curriculumStartDate`,
    `curriculumEndDate`, `periodOfGrace`, `b`.`amendedDate`, `curriculumCompletionDate`, `intrepidId`
FROM `ProgrammeMembershipInterim` `a`
JOIN `ProgrammeMembership` `b`
ON `a`.`personId` = `b`.`personId` AND `a`.`programmeId` = `b`.`programmeId` AND `a`.`programmeStartDate` = `b`.`programmeStartDate`
AND `a`.`programmeEndDate` = `b`.`programmeEndDate` AND
(`a`.`programmeMembershipType` = `b`.`programmeMembershipType` OR `a`.`programmeMembershipType` IS NULL AND `b`.`programmeMembershipType` IS NULL) ORDER BY `b`.`id`;
-- NULL == NULL is false in SQL
