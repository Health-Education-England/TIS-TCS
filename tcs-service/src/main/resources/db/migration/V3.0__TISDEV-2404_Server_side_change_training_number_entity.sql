ALTER TABLE `trainingNumber`
DROP COLUMN `localOffice`;

ALTER TABLE `trainingNumber`
ADD programmeID BIGINT(20);

SET foreign_key_checks=0;

ALTER TABLE `trainingNumber`
ADD CONSTRAINT FK_Programme_trainingNumber_Ref
FOREIGN KEY (`programmeID`)
REFERENCES `Programme`(`Id`);

SET foreign_key_checks=1;