CREATE TABLE `JsonPatch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tableDtoName` varchar(255) NOT NULL,
  `patchId` varchar(255) DEFAULT NULL,
  `patch` VARCHAR(255) DEFAULT NULL,
  `dateAdded` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `enabled` bit DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;