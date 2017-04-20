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
