ALTER TABLE `ProgrammeCurriculum` DROP FOREIGN KEY `fk_programme_curriculum`;
ALTER TABLE `ProgrammeCurriculum` DROP FOREIGN KEY `fk_curriculum_programme`;
ALTER TABLE `ProgrammeCurriculum` DROP INDEX `fk_programme_curriculum`;

ALTER TABLE `ProgrammeCurriculum` ADD COLUMN `gmcProgrammeCode` varchar(255) NULL DEFAULT NULL;
ALTER TABLE `ProgrammeCurriculum` ADD COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE `ProgrammeCurriculum`DROP PRIMARY KEY;
ALTER TABLE `ProgrammeCurriculum` ADD PRIMARY KEY (`id`);
ALTER TABLE `ProgrammeCurriculum` ADD CONSTRAINT `fk_programme_curriculum` FOREIGN KEY (`programmeId`) REFERENCES `Programme` (`id`);
ALTER TABLE `ProgrammeCurriculum` ADD CONSTRAINT `fk_curriculum_programme` FOREIGN KEY (`curriculumId`) REFERENCES `Curriculum` (`id`);
