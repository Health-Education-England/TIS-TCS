set autocommit = 0;

ALTER TABLE `Placement`
DROP COLUMN `siteId`;

ALTER TABLE `Placement`
DROP COLUMN `gradeId`;

ALTER TABLE `Placement`
ADD COLUMN `siteCode` varchar(255) DEFAULT NULL;

ALTER TABLE `Placement`
ADD COLUMN `gradeAbbreviation` varchar(255) DEFAULT NULL;

commit;
set autocommit = 1;
