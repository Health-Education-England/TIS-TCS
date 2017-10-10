ALTER TABLE `Post`
DROP COLUMN `fundingType`;

ALTER TABLE `Post`
DROP COLUMN `fundingInfo`;

ALTER TABLE `PostFunding`
ADD COLUMN `intrepidId` VARCHAR(255) DEFAULT NULL;

ALTER TABLE `PostFunding`
ADD COLUMN `postId` BIGINT(20) NOT NULL;

ALTER TABLE `PostFunding`
ADD CONSTRAINT `fk_post` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`);

ALTER TABLE `PostFunding`
ADD COLUMN `fundingType` VARCHAR(255) DEFAULT NULL;

ALTER TABLE `PostFunding`
DROP COLUMN `fundingId`;

ALTER TABLE `PostFunding`
DROP COLUMN `fundingComponentsId`;


