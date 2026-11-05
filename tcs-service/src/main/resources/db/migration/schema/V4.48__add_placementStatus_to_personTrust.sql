-- This migration adds the placementStatus column to the PersonTrust table.
ALTER TABLE `PersonTrust` ADD COLUMN `placementStatus` varchar(255);
