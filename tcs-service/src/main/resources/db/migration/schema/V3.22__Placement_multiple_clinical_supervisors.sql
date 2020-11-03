ALTER TABLE `Placement`
DROP FOREIGN KEY `fk_placement_clinical_supervisor_id`;

ALTER TABLE `Placement`
DROP COLUMN `clinicalSupervisorId`;

ALTER TABLE `Placement`
DROP COLUMN `placementType`;

ALTER TABLE `Placement`
ADD COLUMN `placementTypeId` BIGINT(20) DEFAULT NULL;

CREATE TABLE `PlacementSupervisor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `placementId` bigint(20) NOT NULL,
  `clinicalSupervisorId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
    KEY `fk_placementsupervisor_placement` (`placementId`),
  CONSTRAINT `fk_placementsupervisor_placement_id` FOREIGN KEY (`placementId`) REFERENCES `Placement` (`id`),
    KEY `fk_placementsupervisor_clinical_supervisor` (`clinicalSupervisorId`),
  CONSTRAINT `fk_placementsupervisor_clinical_supervisor_id` FOREIGN KEY (`clinicalSupervisorId`) REFERENCES `Person` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;