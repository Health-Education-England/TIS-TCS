ALTER TABLE `ProgrammeCurriculum` 
  DROP FOREIGN KEY `fk_programme_curriculum`,
  DROP FOREIGN KEY `fk_curriculum_programme`,
  DROP INDEX `fk_programme_curriculum`;

ALTER TABLE `ProgrammeCurriculum`
  ADD COLUMN `gmcProgrammeCode` varchar(255) NULL DEFAULT NULL,
  ADD COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT,
  DROP PRIMARY KEY,
  ADD PRIMARY KEY (`id`),
  ADD CONSTRAINT `fk_programme_curriculum` FOREIGN KEY (`programmeId`) REFERENCES `Programme` (`id`),
  ADD CONSTRAINT `fk_curriculum_programme` FOREIGN KEY (`curriculumId`) REFERENCES `Curriculum` (`id`);
