CREATE TABLE PlacementSite (
   id bigint(20) NOT NULL AUTO_INCREMENT,
   placementId bigint(20) DEFAULT NULL,
   siteId bigint(20) NULL DEFAULT NULL,
   placementSiteType varchar(255) NULL DEFAULT NULL,
   PRIMARY KEY (`id`),
   CONSTRAINT fk_placement_site_id FOREIGN KEY (placementId) REFERENCES Placement (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



