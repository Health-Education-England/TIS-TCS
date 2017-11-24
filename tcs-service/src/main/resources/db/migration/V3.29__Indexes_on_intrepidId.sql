CREATE UNIQUE INDEX person_intrepid_id ON `Person` (`intrepidId`);
CREATE UNIQUE INDEX post_intrepid_id ON `Post` (`intrepidId`);
CREATE UNIQUE INDEX placement_intrepid_id ON `Placement` (`intrepidId`);
CREATE UNIQUE INDEX programme_intrepid_id ON `Programme` (`intrepidId`);
CREATE UNIQUE INDEX specialty_intrepid_id ON `Specialty` (`intrepidId`);
CREATE UNIQUE INDEX curriculum_intrepid_id ON `Curriculum` (`intrepidId`);
CREATE UNIQUE INDEX qualification_intrepid_id ON `Qualification` (`intrepidId`);

ALTER TABLE `Placement` ADD `placementType` varchar(255) DEFAULT NULL;
ALTER TABLE `Placement` ADD `status` varchar(255) DEFAULT NULL;
ALTER TABLE `Placement` DROP COLUMN `placementTypeId`;
ALTER TABLE `Placement` DROP COLUMN `trainingDescription`;
ALTER TABLE `Placement` ADD `trainingDescription` TEXT DEFAULT NULL;

TRUNCATE TABLE `PlacementSpecialty`;
ALTER TABLE `PlacementSpecialty` DROP COLUMN `id`;
ALTER TABLE `PlacementSpecialty` ADD PRIMARY KEY(`placementId`, `specialtyId`);
