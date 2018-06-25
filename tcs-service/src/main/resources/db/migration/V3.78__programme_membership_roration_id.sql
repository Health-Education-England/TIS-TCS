ALTER TABLE `ProgrammeMembership`
ADD COLUMN `rotationId` bigint NULL;

ALTER TABLE `ProgrammeMembership`
ADD CONSTRAINT `fk_programme_membership_rotation_id` FOREIGN KEY (`rotationId`) REFERENCES `Rotation` (`id`);
