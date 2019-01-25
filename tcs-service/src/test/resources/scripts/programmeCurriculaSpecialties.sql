-- SQL to insert programmes with curricula and specialties
SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `Programme` (`id`, `status`, `owner`, `programmeName`, `programmeNumber`)
VALUES
	(1, 'CURRENT', 'Health Education England Yorkshire and the Humber', 'EXPECTED PROGRAMME NAME 1', 'PROG-1'),
	(2, 'CURRENT', 'Health Education England East of England', 'EXPECTED PROGRAMME NAME 2', 'PROG-2'),
	(3, 'CURRENT', 'Health Education England North East', 'Neurosurgery', 'PROG-3'),
	(4, 'CURRENT', 'Health Education England West Midlands', 'Plastic surgery', 'PROG-4'),
	(5, 'INACTIVE', 'Health Education England West Midlands', 'Foundation/Legacy', 'PROG-5'),
	(6, 'CURRENT', 'Health Education England London', 'Foundation', 'PROG-6');

INSERT INTO `Specialty` (`id`, `status`, `college`, `specialtyCode`, `specialtyGroupId`, `name`)
VALUES
	(1, 'CURRENT', 'Royal College of Pathologists', 'SPE-1', NULL, 'Forensic Histopathology'),
	(2, 'CURRENT', 'Joint Committee on Surgical Training', 'SPE-2', NULL, 'Congenital cardiac surgery'),
	(3, 'CURRENT', 'Faculty Of Dental Surgery', 'SPE-3', NULL, 'Oral and maxillofacial pathology'),
	(4, 'CURRENT', 'Royal College of Obstetricians and Gynaecologists', 'SPE-4', NULL, 'Urogynaecology'),
	(5, 'CURRENT', 'Joint Royal Colleges of Physicians Training Board', 'SPE-5', NULL, 'Metabolic Medicine'),
	(6, 'CURRENT', 'Faculty Of Dental Surgery', 'SPE-6', NULL, 'Oral Medicine'),
	(7, 'CURRENT', 'Royal College of Paediatrics and Child Health', 'SPE-7', NULL, 'Paediatric inherited Metabolic Medicine'),
	(8, 'CURRENT', 'Royal College of Obstetricians and Gynaecologists', 'SPE-8', NULL,  'Reproductive medicine'),
	(9, 'CURRENT', 'Royal College of Obstetricians', 'SPE-9', NULL,  'active specialty'),
	(19, 'INACTIVE', 'COLLEGE', 'SPE-19', NULL,  'Inactive specialty');


INSERT INTO `Curriculum` (`id`, `name`, `curriculumSubType`, `assessmentType`, `doesThisCurriculumLeadToCct`, `periodOfGrace`, `specialtyId`, `status`, `length`)
VALUES
	(100, 'NE - Vacant Dr-Dummy', NULL, 'ACADEMIC', 0, NULL, '1', 'CURRENT', 12),
	(200, 'Clinical Teaching Fellow (Scotland)', NULL, 'ACADEMIC', 0, 0, '2', 'CURRENT', 12),
	(300, 'Clinical Research Fellow', NULL, 'ACADEMIC', 0, 0, '3', 'CURRENT', 12),
	(400, 'Clinical Lecturer (Scotland)', NULL, 'ACADEMIC', 0, 0,  '4', 'CURRENT', 12),
	(500, 'Other fellowship', NULL, 'ACADEMIC', 0, 0, '5', 'CURRENT', 12),
	(600, 'ACL (N Ireland)', NULL, 'ACADEMIC', 0, 0,  '6', 'CURRENT', 12),
	(700, 'University of Cambridge ? ACF (Local)', 'ACF_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '7', 'CURRENT', 0),
	(1700, 'University of Cambridge ? ACF (Local) - NEW', 'ACF_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '7', 'CURRENT', 0),
	(2700, 'University of Cambridge ? ACF (Local) - NEWER DUPE', 'ACF_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '7', 'CURRENT', 0),
	(800, 'EOE - ACL (matched substantive)', 'ACL_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '8', 'CURRENT', 0),
	(900, 'AAA', 'ACL_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '9', 'CURRENT', 0),
	(1900, 'BBB', 'ACL_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '19', 'CURRENT', 0);

INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (1, 100);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (1, 200);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (2, 300);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (2, 400);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (3, 500);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (3, 600);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (4, 700);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (4, 1700);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (4, 2700);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (5, 800);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (6, 900);
INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES (6, 1900);

INSERT INTO `Post` (`id`, `nationalPostNumber`, `status`, `owner`, `legacy`, `bypassNPNGeneration`)
VALUES
	(11, 'XXX', 'CURRENT', 'Health Education England Wessex', 0, 1),
  (12, 'YYY', 'CURRENT', 'Health Education England Wessex', 0, 1),
  (13, 'ZZZ', 'CURRENT', 'Health Education England Wessex', 0, 1);

INSERT INTO `PostSpecialty` (`id`, `postId`, `specialtyId`, `postSpecialtyType`)
VALUES
	(1, 11, 3, 'PRIMARY'),
	(2, 12, 4, 'PRIMARY'),
	(3, 13, 5, 'PRIMARY');

INSERT INTO `ProgrammePost` (`programmeId`, `postId`)
VALUES
	(1, 11),
	(1, 12);

INSERT INTO `Placement` (`id`, `dateFrom`, `dateTo`, `placementWholeTimeEquivalent`, `traineeId`, `postId`)
VALUES
	(33, '2015-03-06', '2019-06-06', 0.6, 1, 11),
	(34, '2016-03-06', '2019-06-06', 0.3, 1, 12),
	(35, '2017-03-06', '2019-06-06', 1, 2, 11);

INSERT INTO `Person` (`id`, `role`, `status`, `comments`, `inactiveDate`, `inactiveNotes`, `publicHealthNumber`, `regulator`)
VALUES
	(1, 'DR in Training', 'CURRENT', NULL, NULL, NULL, NULL, NULL),
	(2, 'DR in Training', 'CURRENT', NULL, NULL, NULL, NULL, NULL);


SET FOREIGN_KEY_CHECKS = 1;
