ALTER TABLE `ContactDetails`
ADD COLUMN `address1` varchar(255) DEFAULT NULL;

ALTER TABLE `ContactDetails`
ADD COLUMN `address2` varchar(255) DEFAULT NULL;

ALTER TABLE `ContactDetails`
ADD COLUMN `address3` varchar(255) DEFAULT NULL;

ALTER TABLE `ContactDetails`
ADD COLUMN `address4` varchar(255) DEFAULT NULL;

ALTER TABLE `ContactDetails`
DROP COLUMN `address`;