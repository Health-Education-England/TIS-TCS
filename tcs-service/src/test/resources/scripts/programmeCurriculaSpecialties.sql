-- SQL to insert programmes with curricula and specialties

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
	(1, 'NE - Vacant Dr-Dummy', NULL, 'ACADEMIC', 0, NULL, '1', 'CURRENT', 12),
	(2, 'Clinical Teaching Fellow (Scotland)', NULL, 'ACADEMIC', 0, 0, '2', 'CURRENT', 12),
	(3, 'Clinical Research Fellow (Scotland)', NULL, 'ACADEMIC', 0, 0, '3', 'CURRENT', 12),
	(4, 'Clinical Lecturer (Scotland)', NULL, 'ACADEMIC', 0, 0,  '4', 'CURRENT', 12),
	(5, 'Other fellowship', NULL, 'ACADEMIC', 0, 0, '5', 'CURRENT', 12),
	(6, 'ACL (N Ireland)', NULL, 'ACADEMIC', 0, 0,  '6', 'CURRENT', 12),
	(7, 'University of Cambridge ? ACF (Local)', 'ACF_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '7', 'CURRENT', 0),
	(17, 'University of Cambridge ? ACF (Local) - NEW', 'ACF_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '7', 'CURRENT', 0),
	(27, 'University of Cambridge ? ACF (Local) - NEWER DUPE', 'ACF_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '7', 'CURRENT', 0),
	(8, 'EOE - ACL (matched substantive)', 'ACL_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '8', 'CURRENT', 0),
	(9, 'AAA', 'ACL_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '9', 'CURRENT', 0),
	(19, 'BBB', 'ACL_OTHER_FUNDING', 'ACADEMIC', 0, NULL, '19', 'CURRENT', 0);

INSERT INTO `ProgrammeCurriculum` (`programmeId`, `curriculumId`) VALUES
	(1, 1),
	(1, 2),
	(2, 3),
	(2, 4),
	(3, 5),
	(3, 6),
	(4, 7),
	(4, 17),
	(4, 27),
	(5, 8),
	(6, 9),
	(6, 19);
