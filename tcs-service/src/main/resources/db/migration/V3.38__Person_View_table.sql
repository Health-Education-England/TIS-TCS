DROP VIEW IF EXISTS `PersonView`;
DROP VIEW IF EXISTS currprogsview;
DROP VIEW IF EXISTS currplcsview;

CREATE TABLE IF NOT EXISTS `PersonView` (
  `id` bigint(20) NOT NULL,
  `intrepidId` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `forenames` varchar(255) DEFAULT NULL,
  `gmcNumber` varchar(255) DEFAULT NULL,
  `gdcNumber` varchar(255) DEFAULT NULL,
  `publicHealthNumber` varchar(255) DEFAULT NULL,
  `programmeId` bigint(20) DEFAULT NULL,
  `programmeName` varchar(255) DEFAULT NULL,
  `programmeNumber` varchar(255) DEFAULT NULL,
  `trainingNumber` varchar(255) DEFAULT NULL,
  `gradeAbbreviation` varchar(255) DEFAULT NULL,
  `siteCode` varchar(255) DEFAULT NULL,
  `placementType` varchar(255) DEFAULT NULL,
  `role` TEXT DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `currentOwner` varchar(255) DEFAULT NULL,
  `currentOwnerRule` varchar(255) DEFAULT NULL,
  UNIQUE KEY `person_view_pk` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;