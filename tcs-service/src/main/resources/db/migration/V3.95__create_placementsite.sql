CREATE TABLE PlacementSite (
   id bigint(20) NOT NULL AUTO_INCREMENT,
   placementId bigint(20) NULL,
   siteId bigint(20) NULL DEFAULT NULL,
   placementSiteType varchar(255) NULL DEFAULT NULL,
   UNIQUE KEY placement_site_pk (id),
   CONSTRAINT fk_placement_site FOREIGN KEY (placementId) REFERENCES Placement (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



