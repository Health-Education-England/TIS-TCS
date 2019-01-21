-- SQL to clean up the insert of programmes with curricula and specialties
SET FOREIGN_KEY_CHECKS = 0;
DELETE `Programme` WHERE `id` IN (1, 2, 3, 4, 5, 6);
DELETE `Specialty` WHERE `id` IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 19);
DELETE `Curriculum` WHERE `id` IN (1, 2, 3, 4, 5, 6, 7, 17, 27, 8, 9, 19);
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 1 AND `curriculumId` = 1;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 1 AND `curriculumId` = 2;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 2 AND `curriculumId` = 3;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 2 AND `curriculumId` = 4;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 3 AND `curriculumId` = 5;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 3 AND `curriculumId` = 6;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 4 AND `curriculumId` = 7;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 4 AND `curriculumId` = 17;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 4 AND `curriculumId` = 27;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 5 AND `curriculumId` = 8;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 6 AND `curriculumId` = 9;
DELETE `ProgrammeCurriculum` WHERE `programmeId` = 6 AND `curriculumId` = 19;
SET FOREIGN_KEY_CHECKS = 1;