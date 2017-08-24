ALTER TABLE `TrainingNumber`
DROP COLUMN `localOffice`;

ALTER TABLE `TrainingNumber`
ADD programmeId BIGINT(20);

ALTER TABLE `TrainingNumber`
ADD CONSTRAINT FK_Programme_trainingNumber_Ref
FOREIGN KEY (`programmeId`)
REFERENCES `Programme`(`Id`);