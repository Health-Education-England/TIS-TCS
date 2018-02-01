UPDATE `Post` SET `employingBodyId` = NULL, `trainingBodyId` = NULL;

ALTER TABLE `Post`
MODIFY COLUMN `employingBodyId` bigint(20);

ALTER TABLE `Post`
MODIFY COLUMN `trainingBodyId` bigint(20);
