TRUNCATE TABLE `ProgrammeMembership`;

ALTER TABLE `ProgrammeMembership`
MODIFY `programmeId` bigint(20) NOT NULL;

ALTER TABLE `ProgrammeMembership`
MODIFY `curriculumId` bigint(20) NOT NULL;

ALTER TABLE `ProgrammeMembership`
MODIFY `trainingNumberId` bigint(20) NOT NULL;

ALTER TABLE `ProgrammeMembership` ADD CONSTRAINT `fk_ProgrammeMembership_programme_id` FOREIGN KEY (`programmeId`) REFERENCES `Programme` (`id`);
ALTER TABLE `ProgrammeMembership` ADD CONSTRAINT `fk_ProgrammeMembership_curriculum_id` FOREIGN KEY (`curriculumId`) REFERENCES `Curriculum` (`id`);
ALTER TABLE `ProgrammeMembership` ADD CONSTRAINT `fk_ProgrammeMembership_training_number_id` FOREIGN KEY (`trainingNumberId`) REFERENCES `TrainingNumber` (`id`);