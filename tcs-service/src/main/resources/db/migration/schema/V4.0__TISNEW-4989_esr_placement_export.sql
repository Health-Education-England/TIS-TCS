CREATE TABLE PlacementEsrEvent (
id bigint(20) NOT NULL AUTO_INCREMENT,
placementId bigint(20) NOT NULL,
eventDateTime datetime NOT NULL,
filename varchar(255) NOT NULL,
positionNumber bigint(20) NOT NULL,
positionId bigint(20) NOT NULL,
status varchar(255) NOT NULL,
PRIMARY KEY (id),
CONSTRAINT `PlceEvnt_Plce_id` FOREIGN KEY (placementId) REFERENCES `Placement` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
