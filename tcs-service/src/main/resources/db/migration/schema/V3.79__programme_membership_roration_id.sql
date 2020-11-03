ALTER TABLE `ProgrammeMembership`
ADD COLUMN `rotationId` bigint NULL;

ALTER TABLE `ProgrammeMembership`
ADD CONSTRAINT `fk_programme_membership_rotation_id` FOREIGN KEY (`rotationId`) REFERENCES `Rotation` (`id`);

ALTER TABLE `RotationPost`
ADD CONSTRAINT `uk_post_id_rotation_id` UNIQUE (postId, rotationId);

ALTER TABLE `Rotation`
ADD CONSTRAINT `uk_programme_id_name` UNIQUE (programmeId, name);
