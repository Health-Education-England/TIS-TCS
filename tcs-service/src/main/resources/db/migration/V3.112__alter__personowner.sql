ALTER TABLE `PersonOwner`
ADD COLUMN `entity` VARCHAR(45) NOT NULL AFTER `owner`;

UPDATE `PersonOwner`
SET entity = 'HEE';
