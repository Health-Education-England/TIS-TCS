DROP VIEW IF EXISTS `PostView`;

CREATE TABLE IF NOT EXISTS `PostView` (
  `id` bigint(20) NOT NULL,
  `currentTraineeId` bigint(20) DEFAULT NULL,
  `currentTraineeGmcNumber` varchar(255) DEFAULT NULL,
  `currentTraineeSurname` varchar(255) DEFAULT NULL,
  `currentTraineeForenames` varchar(255) DEFAULT NULL,
  `approvedGradeCode` varchar(255) DEFAULT NULL,
  `primarySpecialtyId` bigint(20) DEFAULT NULL,
  `primarySpecialtyCode` varchar(255) DEFAULT NULL,
  `primarySpecialtyName` varchar(255) DEFAULT NULL,
  `primarySiteCode` varchar(255) DEFAULT NULL,
  `programmeName` varchar(255) DEFAULT NULL,
  `fundingType` varchar(255) DEFAULT NULL,
  `nationalPostNumber` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `managingLocalOffice` varchar(255) DEFAULT NULL,
  `intrepidId` varchar(255) DEFAULT NULL,
  UNIQUE KEY `post_view_pk` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;