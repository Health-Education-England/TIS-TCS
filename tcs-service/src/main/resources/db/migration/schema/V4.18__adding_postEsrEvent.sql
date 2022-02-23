CREATE TABLE `PostEsrEvent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `postId` bigint(20) NOT NULL,
  `eventDateTime` datetime NOT NULL,
  `filename` varchar(255) NOT NULL,
  `positionNumber` bigint(20) NOT NULL,
  `positionId` bigint(20) NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `PostEvnt_Post_id` (`postId`),
  CONSTRAINT `PostEvnt_Post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
