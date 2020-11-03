ALTER TABLE `ProgrammeMembership`
ADD `intrepidId` varchar(255) DEFAULT NULL;

ALTER TABLE `ProgrammeMembership`
ADD `personId` bigint(20) NOT NULL;

ALTER TABLE `ProgrammeMembership` ADD CONSTRAINT `fk_ProgrammeMembership_person_id` FOREIGN KEY (`personId`) REFERENCES `Person` (`id`);