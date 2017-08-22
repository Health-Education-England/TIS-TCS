ALTER TABLE Post ADD COLUMN intrepidId varchar(255);
ALTER TABLE `Post` ADD INDEX `postIntrepidId` (`intrepidId`);

ALTER TABLE Placement ADD COLUMN intrepidId varchar(255);
ALTER TABLE `Placement` ADD INDEX `placementIntrepidId` (`intrepidId`);
