ALTER TABLE `Comment`
  ADD COLUMN `source` VARCHAR(255) DEFAULT NULL AFTER `placementId`;
