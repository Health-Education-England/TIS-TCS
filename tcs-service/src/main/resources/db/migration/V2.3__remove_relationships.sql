ALTER TABLE `Curriculum` DROP FOREIGN KEY `fk_curriculum_specialty_id`;
ALTER TABLE `Curriculum` DROP COLUMN `specialtyId`;
ALTER TABLE `Curriculum` ADD COLUMN `specialtyId` varchar(255);

ALTER TABLE `FundingComponents` DROP FOREIGN KEY `fk_funding_components_funding_organisation_id`;
ALTER TABLE `FundingComponents` DROP COLUMN `fundingOrganisationId`;
ALTER TABLE `FundingComponents` ADD COLUMN `fundingOrganisationId` varchar(255);

ALTER TABLE `Post` DROP FOREIGN KEY `fk_post_new_post_id`;
ALTER TABLE `Post` DROP FOREIGN KEY `fk_post_old_post_id`;
ALTER TABLE `Post` DROP COLUMN `oldPostId`;
ALTER TABLE `Post` ADD COLUMN `oldPostId` varchar(255);
ALTER TABLE `Post` DROP COLUMN `newPostId`;
ALTER TABLE `Post` ADD COLUMN `newPostId` varchar(255);

ALTER TABLE `PostFunding` DROP FOREIGN KEY `fk_post_funding_funding_components_id`;
ALTER TABLE `PostFunding` DROP FOREIGN KEY `fk_post_funding_funding_id`;
ALTER TABLE `PostFunding` DROP COLUMN `fundingId`;
ALTER TABLE `PostFunding` ADD COLUMN `fundingId` varchar(255);
ALTER TABLE `PostFunding` DROP COLUMN `fundingComponentsId`;
ALTER TABLE `PostFunding` ADD COLUMN `fundingComponentsId` varchar(255);

ALTER TABLE `ProgrammeMembership` DROP FOREIGN KEY `fk_programme_membership_curriculum_id`;
ALTER TABLE `ProgrammeMembership` DROP FOREIGN KEY `fk_programme_membership_programme_id`;
ALTER TABLE `ProgrammeMembership` DROP FOREIGN KEY `fk_programme_membership_training_number_id`;
ALTER TABLE `ProgrammeMembership` DROP COLUMN `programmeId`;
ALTER TABLE `ProgrammeMembership` ADD COLUMN `programmeId` varchar(255);
ALTER TABLE `ProgrammeMembership` DROP COLUMN `curriculumId`;
ALTER TABLE `ProgrammeMembership` ADD COLUMN `curriculumId` varchar(255);
ALTER TABLE `ProgrammeMembership` DROP COLUMN `trainingNumberId`;
ALTER TABLE `ProgrammeMembership` ADD COLUMN `trainingNumberId` varchar(255);

ALTER TABLE `TariffFundingTypeFields` DROP FOREIGN KEY `fk_tariff_funding_type_fields_level_of_post_id`;
ALTER TABLE `TariffFundingTypeFields` DROP FOREIGN KEY `fk_tariff_funding_type_fields_placement_rate_funded_by_id`;
ALTER TABLE `TariffFundingTypeFields` DROP FOREIGN KEY `fk_tariff_funding_type_fields_placement_rate_provided_to_id`;
ALTER TABLE `TariffFundingTypeFields` DROP COLUMN `levelOfPostId`;
ALTER TABLE `TariffFundingTypeFields` ADD COLUMN `levelOfPostId` varchar(255);
ALTER TABLE `TariffFundingTypeFields` DROP COLUMN `placementRateFundedById`;
ALTER TABLE `TariffFundingTypeFields` ADD COLUMN `placementRateFundedById` varchar(255);
ALTER TABLE `TariffFundingTypeFields` DROP COLUMN `placementRateProvidedToId`;
ALTER TABLE `TariffFundingTypeFields` ADD COLUMN `placementRateProvidedToId` varchar(255);
