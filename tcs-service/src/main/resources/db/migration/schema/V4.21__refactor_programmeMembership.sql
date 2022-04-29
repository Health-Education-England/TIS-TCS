DROP TABLE `ProgrammeMembership`;
CREATE TABLE `ProgrammeMembership` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `programmeMembershipType` varchar(255) DEFAULT NULL,
  `programmeStartDate` date DEFAULT NULL,
  `programmeEndDate` date DEFAULT NULL,
  `programmeId` bigint(20) NOT NULL,
  `trainingNumberId` bigint(20) DEFAULT NULL,
  `personId` bigint(20) NOT NULL,
  `rotation` varchar(255) DEFAULT NULL,
  `rotationId` bigint(20) DEFAULT NULL,
  `trainingPathway` varchar(255) DEFAULT NULL,
  `leavingReason` varchar(255) DEFAULT NULL,
  `amendedDate` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `fk_ProgrammeMembership_person_id` (`personId`),
  KEY `fk_ProgrammeMembership_programme_id` (`programmeId`),
  KEY `fk_ProgrammeMembership_training_number_id` (`trainingNumberId`),
  KEY `fk_ProgrammeMembership_rotation_id` (`rotationId`),
  CONSTRAINT `fk_ProgrammeMembership_person_id` FOREIGN KEY (`personId`) REFERENCES `Person` (`id`),
  CONSTRAINT `fk_ProgrammeMembership_programme_id` FOREIGN KEY (`programmeId`) REFERENCES `Programme` (`id`),
  CONSTRAINT `fk_ProgrammeMembership_training_number_id` FOREIGN KEY (`trainingNumberId`) REFERENCES `TrainingNumber` (`id`),
  CONSTRAINT `fk_ProgrammeMembership_rotation_id` FOREIGN KEY (`rotationId`) REFERENCES `Rotation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

INSERT INTO ProgrammeMembership (personId, programmeId, rotationId, rotation, programmeStartDate, programmeEndDate,
programmeMembershipType, trainingPathway, leavingReason)
SELECT
personId, programmeId, rotationId, max(rotation), programmeStartDate, programmeEndDate,
programmeMembershipType, max(trainingPathway), max(leavingReason)
FROM CurriculumMembership
GROUP BY
personId, programmeId, rotationId, programmeStartDate, programmeEndDate, programmeMembershipType;

ALTER TABLE CurriculumMembership ADD COLUMN `programmeMembershipId` bigint(20) DEFAULT NULL;

UPDATE tcs.CurriculumMembership cm
SET programmeMembershipId = (SELECT id FROM tcs.ProgrammeMembership pm
WHERE pm.personId = cm.personId AND
pm.programmeId = cm.programmeId AND
IFNULL(pm.rotationId,0) = IFNULL(cm.rotationId,0) AND
pm.programmeStartDate = cm.programmeStartDate AND
pm.programmeEndDate = cm.programmeEndDate AND
IFNULL(pm.programmeMembershipType,'') = IFNULL(cm.programmeMembershipType,''));

ALTER TABLE CurriculumMembership MODIFY COLUMN `programmeMembershipId` bigint(20) NOT NULL;
ALTER TABLE CurriculumMembership ADD INDEX `fk_ProgrammeMembership_programme_membership_id` (`programmeMembershipId`);
ALTER TABLE CurriculumMembership ADD FOREIGN KEY (`programmeMembershipId`) REFERENCES `ProgrammeMembership` (`id`);
ALTER TABLE CurriculumMembership CHANGE COLUMN `programmeId` `programmeId` BIGINT(20) NULL,
                                 CHANGE COLUMN `personId` `personId` BIGINT(20) NULL;

