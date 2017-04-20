
CREATE TABLE `TariffRate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gradeAbbreviation` varchar(255) DEFAULT NULL,
  `tariffRate` varchar(255) DEFAULT NULL,
  `tariffRateFringe` varchar(255) DEFAULT NULL,
  `tariffRateLondon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `PlacementFunder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `localOffice` varchar(255) DEFAULT NULL,
  `trust` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `Funding` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `fundingType` varchar(255) DEFAULT NULL,
  `fundingIssue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `FundingComponents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `percentage` int(11) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `fundingOrganisationId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `funding_organisation_id` (`fundingOrganisationId`),
  CONSTRAINT `fk_funding_components_funding_organisation_id` FOREIGN KEY (`fundingOrganisationId`) REFERENCES `PlacementFunder` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Placement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  `nationalPostNumber` varchar(255) DEFAULT NULL,
  `site` varchar(255) DEFAULT NULL,
  `grade` varchar(255) DEFAULT NULL,
  `specialty` varchar(255) DEFAULT NULL,
  `dateFrom` date DEFAULT NULL,
  `dateTo` date DEFAULT NULL,
  `placementType` varchar(255) DEFAULT NULL,
  `placementWholeTimeEquivalent` float DEFAULT NULL,
  `slotShare` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `Post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nationalPostNumber` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `postOwner` varchar(255) DEFAULT NULL,
  `mainSiteLocated` varchar(255) DEFAULT NULL,
  `leadSite` varchar(255) DEFAULT NULL,
  `employingBody` varchar(255) DEFAULT NULL,
  `trainingBody` varchar(255) DEFAULT NULL,
  `approvedGrade` varchar(255) DEFAULT NULL,
  `postSpecialty` varchar(255) DEFAULT NULL,
  `fullTimeEquivelent` float DEFAULT NULL,
  `leadProvider` varchar(255) DEFAULT NULL,
  `oldPostId` bigint(20) DEFAULT NULL,
  `newPostId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `old_post_id` (`oldPostId`),
  UNIQUE KEY `new_post_id` (`newPostId`),
  CONSTRAINT `fk_post_new_post_id` FOREIGN KEY (`newPostId`) REFERENCES `Post` (`id`),
  CONSTRAINT `fk_post_old_post_id` FOREIGN KEY (`oldPostId`) REFERENCES `Post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `PostFunding` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fundingId` bigint(20) DEFAULT NULL,
  `fundingComponentsId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `funding_components_id` (`fundingComponentsId`),
  UNIQUE KEY `funding_id` (`fundingId`),
  CONSTRAINT `fk_post_funding_funding_components_id` FOREIGN KEY (`fundingComponentsId`) REFERENCES `FundingComponents` (`id`),
  CONSTRAINT `fk_post_funding_funding_id` FOREIGN KEY (`fundingId`) REFERENCES `Funding` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `TariffFundingTypeFields` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `effectiveDateFrom` date DEFAULT NULL,
  `effectiveDateTo` date DEFAULT NULL,
  `tariffRate` decimal(10,2) DEFAULT NULL,
  `placementRate` decimal(10,2) DEFAULT NULL,
  `levelOfPostId` bigint(20) DEFAULT NULL,
  `placementRateFundedById` bigint(20) DEFAULT NULL,
  `placementRateProvidedToId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `level_of_post_id` (`levelOfPostId`),
  UNIQUE KEY `placement_rate_funded_by_id` (`placementRateFundedById`),
  UNIQUE KEY `placement_rate_provided_to_id` (`placementRateProvidedToId`),
  CONSTRAINT `fk_tariff_funding_type_fields_level_of_post_id` FOREIGN KEY (`levelOfPostId`) REFERENCES `TariffRate` (`id`),
  CONSTRAINT `fk_tariff_funding_type_fields_placement_rate_funded_by_id` FOREIGN KEY (`placementRateFundedById`) REFERENCES `PlacementFunder` (`id`),
  CONSTRAINT `fk_tariff_funding_type_fields_placement_rate_provided_to_id` FOREIGN KEY (`placementRateProvidedToId`) REFERENCES `PlacementFunder` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


