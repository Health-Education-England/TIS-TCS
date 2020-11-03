TRUNCATE TABLE `PostSpecialty`;
ALTER TABLE `PostSpecialty` DROP COLUMN `id`;
ALTER TABLE `PostSpecialty` ADD PRIMARY KEY(`postId`, `specialtyId`);

TRUNCATE TABLE `PostGrade`;
ALTER TABLE `PostGrade` DROP COLUMN `id`;
ALTER TABLE `PostGrade` ADD PRIMARY KEY(`postId`, `gradeId`);

TRUNCATE TABLE `PostSite`;
ALTER TABLE `PostSite` DROP COLUMN `id`;
ALTER TABLE `PostSite` ADD PRIMARY KEY(`postId`, `siteId`);
