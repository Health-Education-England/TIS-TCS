CREATE TABLE `SpecialtyGroup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Specialty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  `college` varchar(255) DEFAULT NULL,
  `nhsSpecialtyCode` varchar(255) DEFAULT NULL,
  `specialtyType` varchar(255) DEFAULT NULL,
  `specialtyGroupId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_specialty_specialty_group_id` (`specialtyGroupId`),
  CONSTRAINT `fk_specialty_specialty_group_id` FOREIGN KEY (`specialtyGroupId`) REFERENCES `SpecialtyGroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `TrainingNumber` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trainingNumberType` varchar(255) DEFAULT NULL,
  `localOffice` varchar(255) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `appointmentYear` int(11) DEFAULT NULL,
  `typeOfContract` varchar(255) DEFAULT NULL,
  `suffix` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Grade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Programme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  `managingDeanery` varchar(255) DEFAULT NULL,
  `programmeName` varchar(255) DEFAULT NULL,
  `programmeNumber` varchar(255) DEFAULT NULL,
  `leadProvider` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Curriculum` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `start` date DEFAULT NULL,
  `end` date DEFAULT NULL,
  `curriculumSubType` varchar(255) DEFAULT NULL,
  `assessmentType` varchar(255) DEFAULT NULL,
  `doesThisCurriculumLeadToCct` bit(1) DEFAULT NULL,
  `periodOfGrace` int(11) DEFAULT NULL,
  `specialtyId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_curriculum_specialty_id` (`specialtyId`),
  CONSTRAINT `fk_curriculum_specialty_id` FOREIGN KEY (`specialtyId`) REFERENCES `Specialty` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `CurriculumGrade` (
  `gradesId` bigint(20) NOT NULL,
  `curriculaId` bigint(20) NOT NULL,
  PRIMARY KEY (`curriculaId`,`gradesId`),
  KEY `fk_curriculum_grade_grades_id` (`gradesId`),
  CONSTRAINT `fk_curriculum_grade_curricula_id` FOREIGN KEY (`curriculaId`) REFERENCES `Curriculum` (`id`),
  CONSTRAINT `fk_curriculum_grade_grades_id` FOREIGN KEY (`gradesId`) REFERENCES `Grade` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ProgrammeMembership` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `programmeMembershipType` varchar(255) DEFAULT NULL,
  `rotation` varchar(255) DEFAULT NULL,
  `curriculumStartDate` date DEFAULT NULL,
  `curriculumEndDate` date DEFAULT NULL,
  `periodOfGrace` int(11) DEFAULT NULL,
  `programmeStartDate` date DEFAULT NULL,
  `curriculumCompletionDate` date DEFAULT NULL,
  `programmeEndDate` date DEFAULT NULL,
  `leavingDestination` varchar(255) DEFAULT NULL,
  `programmeId` bigint(20) DEFAULT NULL,
  `curriculumId` bigint(20) DEFAULT NULL,
  `trainingNumberId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_programme_membership_programme_id` (`programmeId`),
  KEY `fk_programme_membership_curriculum_id` (`curriculumId`),
  KEY `fk_programme_membership_training_number_id` (`trainingNumberId`),
  CONSTRAINT `fk_programme_membership_curriculum_id` FOREIGN KEY (`curriculumId`) REFERENCES `Curriculum` (`id`),
  CONSTRAINT `fk_programme_membership_programme_id` FOREIGN KEY (`programmeId`) REFERENCES `Programme` (`id`),
  CONSTRAINT `fk_programme_membership_training_number_id` FOREIGN KEY (`trainingNumberId`) REFERENCES `TrainingNumber` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tariffRate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gradeAbbreviation` varchar(255) DEFAULT NULL,
  `tariffRate` varchar(255) DEFAULT NULL,
  `tariffRateFringe` varchar(255) DEFAULT NULL,
  `tariffRateLondon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `placementFunder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `localOffice` varchar(255) DEFAULT NULL,
  `trust` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `funding` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `fundingType` varchar(255) DEFAULT NULL,
  `fundingIssue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `fundingComponents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `percentage` int(11) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `fundingOrganisationId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `funding_organisation_id` (`fundingOrganisationId`),
  CONSTRAINT `fk_funding_components_funding_organisation_id` FOREIGN KEY (`fundingOrganisationId`) REFERENCES `placementfunder` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `placement` (
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


CREATE TABLE `post` (
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
  CONSTRAINT `fk_post_new_post_id` FOREIGN KEY (`newPostId`) REFERENCES `post` (`id`),
  CONSTRAINT `fk_post_old_post_id` FOREIGN KEY (`oldPostId`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `postFunding` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fundingId` bigint(20) DEFAULT NULL,
  `fundingComponentsId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `funding_components_id` (`fundingComponentsId`),
  UNIQUE KEY `funding_id` (`fundingId`),
  CONSTRAINT `fk_post_funding_funding_components_id` FOREIGN KEY (`fundingComponentsId`) REFERENCES `fundingcomponents` (`id`),
  CONSTRAINT `fk_post_funding_funding_id` FOREIGN KEY (`fundingId`) REFERENCES `funding` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tariffFundingTypeFields` (
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
  CONSTRAINT `fk_tariff_funding_type_fields_level_of_post_id` FOREIGN KEY (`levelOfPostId`) REFERENCES `tariffrate` (`id`),
  CONSTRAINT `fk_tariff_funding_type_fields_placement_rate_funded_by_id` FOREIGN KEY (`placementRateFundedById`) REFERENCES `placementfunder` (`id`),
  CONSTRAINT `fk_tariff_funding_type_fields_placement_rate_provided_to_id` FOREIGN KEY (`placementRateProvidedToId`) REFERENCES `placementfunder` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


