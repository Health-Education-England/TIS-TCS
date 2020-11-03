ALTER TABLE `Post`
ALTER `bypassNPNGeneration` SET DEFAULT 1;

UPDATE `Post`
SET `bypassNPNGeneration` = 1;