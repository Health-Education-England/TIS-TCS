-- Use reference.fundingType.id column as the source to populate the new fundingTypeId column
ALTER TABLE `PostFunding` ADD COLUMN `fundingTypeId` bigint(20);
