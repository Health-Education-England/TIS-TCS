SET FOREIGN_KEY_CHECKS = 0;

DELETE `Specialty` WHERE `id` IN (1, 10);
DELETE `Post` WHERE `id` IN (2, 20);
DELETE `PostSpecialty` WHERE `id` IN (9, 90);
DELETE `Person` WHERE `id` IN (4, 40);
DELETE `ContactDetails` WHERE `id` IN (4, 40);
DELETE `Placement` WHERE `id` IN (3, 30, 300);
DELETE `PlacementSpecialty` WHERE `placementId` IN (3, 30, 300);
DELETE `Programme` WHERE `id` IN (5, 50);
DELETE `ProgrammePost` WHERE `programmeId` IN (5, 50);

SET FOREIGN_KEY_CHECKS = 1;
