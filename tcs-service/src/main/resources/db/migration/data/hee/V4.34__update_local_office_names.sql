SET SQL_SAFE_UPDATES = 0;
UPDATE `Post` SET `owner` = REPLACE(`owner`, 'Health Education England ', '') WHERE `owner` LIKE 'Health Education England %';
UPDATE `Programme` SET `owner` = REPLACE(`owner`, 'Health Education England ', '') WHERE `owner` LIKE 'Health Education England %';
SET SQL_SAFE_UPDATES = 1;