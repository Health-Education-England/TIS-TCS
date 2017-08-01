CREATE TABLE `TariffRate` (
  `id`                BIGINT(20) NOT NULL AUTO_INCREMENT,
  `gradeAbbreviation` VARCHAR(255)        DEFAULT NULL,
  `tariffRate`        VARCHAR(255)        DEFAULT NULL,
  `tariffRateFringe`  VARCHAR(255)        DEFAULT NULL,
  `tariffRateLondon`  VARCHAR(255)        DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

CREATE TABLE `PlacementFunder` (
  `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
  `localOffice` VARCHAR(255)        DEFAULT NULL,
  `trust`       VARCHAR(255)        DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

CREATE TABLE `Funding` (
  `id`           BIGINT(20) NOT NULL AUTO_INCREMENT,
  `status`       VARCHAR(255)        DEFAULT NULL,
  `startDate`    DATE                DEFAULT NULL,
  `endDate`      DATE                DEFAULT NULL,
  `fundingType`  VARCHAR(255)        DEFAULT NULL,
  `fundingIssue` VARCHAR(255)        DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

CREATE TABLE `FundingComponents` (
  `id`                    BIGINT(20) NOT NULL AUTO_INCREMENT,
  `percentage`            INT(11)             DEFAULT NULL,
  `amount`                DECIMAL(10, 2)      DEFAULT NULL,
  `fundingOrganisationId` BIGINT(20)          DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `funding_organisation_id` (`fundingOrganisationId`),
  CONSTRAINT `fk_funding_components_funding_organisation_id` FOREIGN KEY (`fundingOrganisationId`) REFERENCES `PlacementFunder` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `Placement` (
  `id`                           BIGINT(20) NOT NULL AUTO_INCREMENT,
  `status`                       VARCHAR(255)        DEFAULT NULL,
  `nationalPostNumber`           VARCHAR(255)        DEFAULT NULL,
  `site`                         VARCHAR(255)        DEFAULT NULL,
  `grade`                        VARCHAR(255)        DEFAULT NULL,
  `specialty`                    VARCHAR(255)        DEFAULT NULL,
  `dateFrom`                     DATE                DEFAULT NULL,
  `dateTo`                       DATE                DEFAULT NULL,
  `placementType`                VARCHAR(255)        DEFAULT NULL,
  `placementWholeTimeEquivalent` FLOAT               DEFAULT NULL,
  `slotShare`                    BIT(1)              DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `Post` (
  `id`                 BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nationalPostNumber` VARCHAR(255)        DEFAULT NULL,
  `status`             VARCHAR(255)        DEFAULT NULL,
  `postOwner`          VARCHAR(255)        DEFAULT NULL,
  `mainSiteLocated`    VARCHAR(255)        DEFAULT NULL,
  `leadSite`           VARCHAR(255)        DEFAULT NULL,
  `employingBody`      VARCHAR(255)        DEFAULT NULL,
  `trainingBody`       VARCHAR(255)        DEFAULT NULL,
  `approvedGrade`      VARCHAR(255)        DEFAULT NULL,
  `postSpecialty`      VARCHAR(255)        DEFAULT NULL,
  `fullTimeEquivelent` FLOAT               DEFAULT NULL,
  `leadProvider`       VARCHAR(255)        DEFAULT NULL,
  `oldPostId`          BIGINT(20)          DEFAULT NULL,
  `newPostId`          BIGINT(20)          DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `old_post_id` (`oldPostId`),
  UNIQUE KEY `new_post_id` (`newPostId`),
  CONSTRAINT `fk_post_new_post_id` FOREIGN KEY (`newPostId`) REFERENCES `Post` (`id`),
  CONSTRAINT `fk_post_old_post_id` FOREIGN KEY (`oldPostId`) REFERENCES `Post` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `PostFunding` (
  `id`                  BIGINT(20) NOT NULL AUTO_INCREMENT,
  `fundingId`           BIGINT(20)          DEFAULT NULL,
  `fundingComponentsId` BIGINT(20)          DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `funding_components_id` (`fundingComponentsId`),
  UNIQUE KEY `funding_id` (`fundingId`),
  CONSTRAINT `fk_post_funding_funding_components_id` FOREIGN KEY (`fundingComponentsId`) REFERENCES `FundingComponents` (`id`),
  CONSTRAINT `fk_post_funding_funding_id` FOREIGN KEY (`fundingId`) REFERENCES `Funding` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `TariffFundingTypeFields` (
  `id`                        BIGINT(20) NOT NULL AUTO_INCREMENT,
  `effectiveDateFrom`         DATE                DEFAULT NULL,
  `effectiveDateTo`           DATE                DEFAULT NULL,
  `tariffRate`                DECIMAL(10, 2)      DEFAULT NULL,
  `placementRate`             DECIMAL(10, 2)      DEFAULT NULL,
  `levelOfPostId`             BIGINT(20)          DEFAULT NULL,
  `placementRateFundedById`   BIGINT(20)          DEFAULT NULL,
  `placementRateProvidedToId` BIGINT(20)          DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `level_of_post_id` (`levelOfPostId`),
  UNIQUE KEY `placement_rate_funded_by_id` (`placementRateFundedById`),
  UNIQUE KEY `placement_rate_provided_to_id` (`placementRateProvidedToId`),
  CONSTRAINT `fk_tariff_funding_type_fields_level_of_post_id` FOREIGN KEY (`levelOfPostId`) REFERENCES `TariffRate` (`id`),
  CONSTRAINT `fk_tariff_funding_type_fields_placement_rate_funded_by_id` FOREIGN KEY (`placementRateFundedById`) REFERENCES `PlacementFunder` (`id`),
  CONSTRAINT `fk_tariff_funding_type_fields_placement_rate_provided_to_id` FOREIGN KEY (`placementRateProvidedToId`) REFERENCES `PlacementFunder` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


