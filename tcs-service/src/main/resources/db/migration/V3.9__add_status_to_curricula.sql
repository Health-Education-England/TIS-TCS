ALTER TABLE `Curriculum` ADD `status` varchar(255) NOT NULL;
UPDATE `Curriculum` set `status` = 'CURRENT';