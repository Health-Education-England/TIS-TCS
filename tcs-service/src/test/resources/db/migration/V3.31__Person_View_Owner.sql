CREATE TABLE `PersonOwner` (
  `id` bigint(20) NOT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `rule` varchar(255) DEFAULT NULL,
  UNIQUE KEY `person_owner_pk` (`id`)
);

ALTER TABLE `PersonOwner` ADD CONSTRAINT `fk_person_owner_person_id` FOREIGN KEY (`id`) REFERENCES `Person` (`id`);


-- Because our testing runs on an H2 in memory database and H2 does not like our PersonView syntax, we overwrite
-- this database migration step with a table instead of a view
CREATE TABLE `PersonView` (
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
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `currentOwner` varchar(255) DEFAULT NULL,
  `currentOwnerRule` varchar(255) DEFAULT NULL,
  UNIQUE KEY `person_view_pk` (`id`)
);