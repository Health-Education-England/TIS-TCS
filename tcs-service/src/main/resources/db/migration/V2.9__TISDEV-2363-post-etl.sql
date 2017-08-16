ALTER TABLE Post ADD COLUMN intrepidId varchar(255);

ALTER TABLE `Post` ADD INDEX `intrepidId` (`intrepidId`);
