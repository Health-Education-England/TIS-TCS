CREATE TABLE `PlacementLog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dateFrom` date DEFAULT NULL,
  `dateTo` date DEFAULT NULL,
  `lifecycleState` enum('DRAFT','APPROVED') DEFAULT NULL,
  `placementId` bigint(20) NOT NULL,
  `logType` enum('CREATE','UPDATE','DELETE') DEFAULT NULL,
  `validDateFrom` datetime DEFAULT NULL,
  `validDateTo` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_placement_approval_record_placement_id` FOREIGN KEY (`placementId`) REFERENCES `Placement` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

