ALTER TABLE `Post`
DROP COLUMN `programmeId`;

CREATE TABLE `ProgrammePost` (
  `programmeId` bigint(20) NOT NULL,
  `postId` bigint(20) NOT NULL,
  `addedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`programmeId`, `postId`),
  CONSTRAINT `fk_programmepost_programme_id` FOREIGN KEY (`programmeId`) REFERENCES `Programme` (`id`),
  CONSTRAINT `fk_programmepost_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
