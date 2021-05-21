SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `Specialty` WHERE `id` IN (1, 10);
DELETE FROM `Post` WHERE `id` IN (2, 20);
DELETE FROM `PostSpecialty` WHERE `id` IN (9, 90);
DELETE FROM `Person` WHERE `id` IN (4, 40);
DELETE FROM `ContactDetails` WHERE `id` IN (4, 40);
DELETE FROM `Placement` WHERE `id` IN (3, 30, 300);
DELETE FROM `PlacementSpecialty` WHERE `placementId` IN (3, 30, 300);
DELETE FROM `Programme` WHERE `id` IN (5, 50);
DELETE FROM `ProgrammePost` WHERE `programmeId` IN (5, 50);

SET FOREIGN_KEY_CHECKS = 1;
