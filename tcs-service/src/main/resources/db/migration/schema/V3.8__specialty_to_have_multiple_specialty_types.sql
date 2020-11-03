CREATE TABLE `SpecialtyTypes` (
  `specialtyId` bigint(20) NOT NULL,
  `specialtyType` varchar(255) NOT NULL check (`specialtyType` in ('CURRICULUM', 'POST', 'PLACEMENT', 'SUB_SPECIALTY')),
  PRIMARY KEY (`specialtyId`,`specialtyType`),
  CONSTRAINT `fk_specialty` FOREIGN KEY (`specialtyId`) REFERENCES `Specialty` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `SpecialtyTypes`(`specialtyId`, `specialtyType`)
SELECT `Specialty`.`id`, `Specialty`.`specialtyType` FROM `Specialty`
WHERE `Specialty`.`specialtyType` IS NOT NULL;

ALTER TABLE `Specialty` DROP COLUMN `specialtyType`;