DROP TABLE `PlacementSupervisor`;

CREATE TABLE `PlacementSupervisor` (
  `placementId` bigint(20) NOT NULL,
  `personId` bigint(20) NOT NULL,
  `type` tinyint NOT NULL,
  `addedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`placementId`, `personId`, `type`),
  CONSTRAINT `fk_placementsupervisor_placementId` FOREIGN KEY (`placementId`) REFERENCES `Placement` (`id`),
  CONSTRAINT `fk_placementsupervisor_personId` FOREIGN KEY (`personId`) REFERENCES `Person` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
