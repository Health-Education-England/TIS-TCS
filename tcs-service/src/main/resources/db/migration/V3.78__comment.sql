CREATE TABLE Comment (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parentId` bigint(20) DEFAULT NULL,
  `threadId` bigint(20) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `body` TEXT DEFAULT NULL,
  `addedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amendedDate` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `inactiveDate` datetime NULL,
  `placementId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_comment_parent_id` FOREIGN KEY (`parentId`) REFERENCES `Comment` (`id`),
  CONSTRAINT `fk_comment_thread_id` FOREIGN KEY (`threadId`) REFERENCES `Comment` (`id`),
  CONSTRAINT `fk_comment_placement_id` FOREIGN KEY (`placementId`) REFERENCES `Placement` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
