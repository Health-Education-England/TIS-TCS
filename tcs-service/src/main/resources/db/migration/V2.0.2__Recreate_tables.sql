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
  `specialtyGroupId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
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
  `specialtyId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `CurriculumGrade` (
  `gradesId` varchar(255) NOT NULL,
  `curriculaId` varchar(255) NOT NULL,
  PRIMARY KEY (`curriculaId`,`gradesId`)
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
  `programmeId` varchar(255) DEFAULT NULL,
  `curriculumId` varchar(255) DEFAULT NULL,
  `trainingNumberId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `fundingOrganisationId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
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
  `oldPostId` varchar(255) DEFAULT NULL,
  `newPostId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `PostFunding` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fundingId` varchar(255) DEFAULT NULL,
  `fundingComponentsId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `TariffFundingTypeFields` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `effectiveDateFrom` date DEFAULT NULL,
  `effectiveDateTo` date DEFAULT NULL,
  `tariffRate` decimal(10,2) DEFAULT NULL,
  `placementRate` decimal(10,2) DEFAULT NULL,
  `levelOfPostId` varchar(255) DEFAULT NULL,
  `placementRateFundedById` varchar(255) DEFAULT NULL,
  `placementRateProvidedToId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

