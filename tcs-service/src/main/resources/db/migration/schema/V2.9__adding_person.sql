CREATE TABLE `RightToWork` (
  `id` bigint(20) NOT NULL,
  `eeaResident` varchar(255) DEFAULT NULL,
  `permitToWork` varchar(255) DEFAULT NULL,
  `settled` varchar(255) DEFAULT NULL,
  `visaIssued` date DEFAULT NULL,
  `visaValidTo` date DEFAULT NULL,
  `visaDetails` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Qualification` (
  `id` bigint(20) NOT NULL,
  `qualification` varchar(255) DEFAULT NULL,
  `qualificationType` varchar(255) DEFAULT NULL,
  `qualifiactionAttainedDate` date DEFAULT NULL,
  `medicalSchool` varchar(255) DEFAULT NULL,
  `countryOfQualification` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `PersonalDetails` (
  `id` bigint(20) NOT NULL,
  `maritalStatus` varchar(255) DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `nationality` varchar(255) DEFAULT NULL,
  `dualNationality` varchar(255) DEFAULT NULL,
  `sexualOrientation` varchar(255) DEFAULT NULL,
  `religiousBelief` varchar(255) DEFAULT NULL,
  `ethnicOrigin` varchar(255) DEFAULT NULL,
  `disability` varchar(255) DEFAULT NULL,
  `disabilityDetails` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `GmcDetails` (
  `id` bigint(20) NOT NULL,
  `gmcNumber` varchar(255) DEFAULT NULL,
  `gmcStatus` varchar(255) DEFAULT NULL,
  `gmcStartDate` date DEFAULT NULL,
  `gmcEndDate` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `GdcDetails` (
  `id` bigint(20) NOT NULL,
  `gdcNumber` varchar(255) DEFAULT NULL,
  `gdcStatus` varchar(255) DEFAULT NULL,
  `gdcStartDate` date DEFAULT NULL,
  `gdcEndDate` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ContactDetails` (
  `id` bigint(20) NOT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `forenames` varchar(255) DEFAULT NULL,
  `knownAs` varchar(255) DEFAULT NULL,
  `maidenName` varchar(255) DEFAULT NULL,
  `initials` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `contactPhoneNr1` varchar(255) DEFAULT NULL,
  `contactPhoneNr2` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `workEmail` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `postCode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `intrepidId` varchar(255) DEFAULT NULL,
  `addedDate` date DEFAULT NULL,
  `amendedDate` date DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `inactiveDate` date DEFAULT NULL,
  `inactiveNotes` varchar(255) DEFAULT NULL,
  `publicHealthNumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `RightToWork` ADD CONSTRAINT `fk_right_to_work_person_id` FOREIGN KEY (`id`) REFERENCES `Person` (`id`);
ALTER TABLE `Qualification` ADD CONSTRAINT `fk_qualification_person_id` FOREIGN KEY (`id`) REFERENCES `Person` (`id`);
ALTER TABLE `PersonalDetails` ADD CONSTRAINT `fk_personal_details_person_id` FOREIGN KEY (`id`) REFERENCES `Person` (`id`);
ALTER TABLE `GmcDetails` ADD CONSTRAINT `fk_gmc_details_person_id` FOREIGN KEY (`id`) REFERENCES `Person` (`id`);
ALTER TABLE `GdcDetails` ADD CONSTRAINT `fk_gdc_details_person_id` FOREIGN KEY (`id`) REFERENCES `Person` (`id`);
ALTER TABLE `ContactDetails` ADD CONSTRAINT `fk_contact_details_person_id` FOREIGN KEY (`id`) REFERENCES `Person` (`id`);