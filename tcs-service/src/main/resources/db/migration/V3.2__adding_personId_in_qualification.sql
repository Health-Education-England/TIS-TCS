ALTER TABLE `Qualification` DROP FOREIGN KEY `fk_qualification_person_id`;

ALTER TABLE `Qualification`
ADD `personId` bigint(20) NOT NULL;

ALTER TABLE `Qualification` CHANGE COLUMN `qualifiactionAttainedDate` `qualificationAttainedDate` date DEFAULT NULL;

ALTER TABLE `Qualification` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Qualification` ADD CONSTRAINT `fk_qualification_person_id` FOREIGN KEY (`personId`) REFERENCES `Person` (`id`);