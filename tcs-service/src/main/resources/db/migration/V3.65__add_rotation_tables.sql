CREATE TABLE `Rotation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `programmeId` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` varchar(255) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_rotation_programme_id` FOREIGN KEY (`programmeId`) REFERENCES `Programme` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `RotationPost` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `postId` bigint(20) NOT NULL,
  `rotationId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_rotation_post_post_id` FOREIGN KEY (`postId`) REFERENCES `Post` (`id`),
  CONSTRAINT `fk_rotation_post_rotation_id` FOREIGN KEY (`rotationId`) REFERENCES `Rotation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `RotationPerson` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `personId` bigint(20) NOT NULL,
  `rotationId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_rotation_person_person_id` FOREIGN KEY (`personId`) REFERENCES `Person` (`id`),
  CONSTRAINT `fk_rotation_person_rotation_id` FOREIGN KEY (`rotationId`) REFERENCES `Rotation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

