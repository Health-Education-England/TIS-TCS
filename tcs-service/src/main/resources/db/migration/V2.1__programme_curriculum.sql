SET FOREIGN_KEY_CHECKS=0;
DROP TABLE `Grade`;
DROP TABLE `CurriculumGrade`;
SET FOREIGN_KEY_CHECKS=1;

ALTER TABLE `Curriculum` ADD `intrepidId` varchar(255);
ALTER TABLE `Programme` ADD `intrepidId` varchar(255);

CREATE TABLE `ProgrammeCurriculum` (
  `programmeId` bigint(20) NOT NULL,
  `curriculumId` bigint(20) NOT NULL,
  PRIMARY KEY (`programmeId`,`curriculumId`),
  KEY `fk_programme_curriculum` (`curriculumId`),
  CONSTRAINT `fk_curriculum_programme` FOREIGN KEY (`programmeId`) REFERENCES `Programme` (`id`),
  CONSTRAINT `fk_programme_curriculum` FOREIGN KEY (`curriculumId`) REFERENCES `Curriculum` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;