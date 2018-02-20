DROP TABLE `DocumentTag` CASCADE;
DROP TABLE `Document` CASCADE;
DROP TABLE `Tag` CASCADE;

CREATE TABLE `Document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `addedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amendedDate` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `inactiveDate` datetime NULL,
  `uploadedBy` varchar(100) NOT NULL,
  `name` varchar(255) NOT NULL,
  `fileName` varchar(255) NOT NULL,
  `fileExtension` varchar(4) NOT NULL,
  `contentType` varchar(100) NOT NULL,
  `size` int(10) NOT NULL,
  `personId` bigint(20) NOT NULL,
  `fileLocation` varchar(255) NOT NULL,
  `status` varchar(8) NOT NULL,
  `version` tinyint(20) NULL,
  `intrepidDocumentUId` varchar(255) DEFAULT NULL,
  `intrepidParentRecordId` varchar(255) DEFAULT NULL,
  `intrepidFolderPath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_document_person_id` FOREIGN KEY (`personId`) REFERENCES `person` (`id`),
  CONSTRAINT `ck_document_status` CHECK (`status` IN ('CURRENT', 'INACTIVE', 'DELETE'))
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `Tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `addedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amendedDate` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `DocumentTag` (
  `documentId` bigint(20) NOT NULL,
  `tagId` bigint(20) NOT NULL,
  `addedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`documentId`, `tagId`),
  CONSTRAINT `fk_documenttag_document_id` FOREIGN KEY (`documentId`) REFERENCES `document` (`id`),
  CONSTRAINT `fk_documenttag_tag_id` FOREIGN KEY (`tagId`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
