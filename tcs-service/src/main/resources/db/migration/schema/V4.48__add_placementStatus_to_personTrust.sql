-- This migration adds the status column to the PersonTrust table.
ALTER TABLE `PersonTrust` ADD COLUMN `status` varchar(255);
