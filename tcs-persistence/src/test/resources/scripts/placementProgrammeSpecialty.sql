SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `Specialty` (`id`, `status`, `college`, `specialtyCode`,  `name`)
VALUES
	(1, 'CURRENT', 'Faculty Of Dental Surgery', 'I10', 'GP Substance Misuse'),
	(10, 'CURRENT', 'Faculty Of Dental Surgery', 'I10', 'GP Substance Misuse');

INSERT INTO `Post` (`id`, `nationalPostNumber`, `status`, `employingBodyId`, `trainingBodyId`, `oldPostId`, `newPostId`, `suffix`, `owner`, `postFamily`, `localPostNumber`, `trainingDescription`, `legacy`, `bypassNPNGeneration`)
VALUES
	(2, 'XX/XXX', 'CURRENT', NULL, NULL, NULL, NULL, NULL, 'Health Education England Wessex', NULL, NULL, NULL, 0, 1),
	(20, 'XX/XXX', 'CURRENT', NULL, NULL, NULL, NULL, NULL, 'Health Education England Wessex', NULL, NULL, NULL, 0, 1);

INSERT INTO `PostSpecialty` (`id`, `postId`, `specialtyId`, `postSpecialtyType`)
VALUES
	(9, 2, 1, 'PRIMARY'),
	(90, 20, 10, 'PRIMARY');

INSERT INTO `Person` (`id`, `addedDate`, `amendedDate`, `role`, `status`, `comments`, `inactiveDate`, `inactiveNotes`, `publicHealthNumber`, `regulator`)
VALUES
	(4, '2017-05-31 00:00:00', '2017-08-24 00:00:00.000', 'DR in Training', 'CURRENT', NULL, NULL, NULL, NULL, NULL),
	(40, '2000-05-31 00:00:00', '2000-08-24 00:00:00.000', 'DR in Training', 'CURRENT', NULL, NULL, NULL, NULL, NULL);

INSERT INTO `ContactDetails` (`id`, `surname`, `forenames`, `knownAs`, `maidenName`, `initials`, `title`, `email`, `postCode`, `legalSurname`, `legalForenames`)
VALUES
	(4, 'Jones', 'John', 'John', NULL, 'JJ', 'Dr', 'john.jones3@nhs.net', 'XX1 1XX', 'Jones', 'John'),
	(40, 'Jo', 'Joanne', 'Joanne', NULL, 'JJ', 'Dr', 'joanne.jo@nhs.net', 'XX1 1XX', 'Jo', 'Joanne');

INSERT INTO `Placement` (`id`, `dateFrom`, `dateTo`, `placementWholeTimeEquivalent`, `traineeId`, `postId`, `localPostNumber`, `siteCode`, `placementType`, `siteId`, `gradeId`)
VALUES
	(3, '2018-03-06', '2019-06-06', 0.6, 4, 2, NULL, 'C81071', 'In Post', 10, 99),
	(30, '2017-03-06', '2019-06-06', 1, 40, 2, NULL, 'C81071', 'In Post', 10, 99),
	(300, '2017-03-06', '2019-06-06', 1, 40, 20, NULL, 'C81071', 'In Post', 10, 99);

INSERT INTO `PlacementSpecialty` (`placementId`, `specialtyId`, `placementSpecialtyType`)
VALUES
	(3, 1, 'PRIMARY'),
	(30, 1, 'PRIMARY'),
	(300, 10, 'PRIMARY');

INSERT INTO `Programme` (`id`, `status`, `owner`, `programmeName`, `programmeNumber`)
VALUES
	(5, 'CURRENT', 'Health Education England North West London', 'Clinical Radiology North West LDN', 'LON183 '),
	(50, 'CURRENT', 'Health Education England North West London', 'SHOULD NOT BE RETRIEVED', 'LON183 ');

INSERT INTO `ProgrammePost` (`programmeId`, `postId`)
VALUES
	(5, 2),
	(50, 20);


SET FOREIGN_KEY_CHECKS = 1;
