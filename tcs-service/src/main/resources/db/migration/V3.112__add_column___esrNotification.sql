ALTER TABLE `EsrNotification`
ADD COLUMN `currentTraineeWorkingHoursIndicator` varchar(255)DEFAULT NULL AFTER
`currentTraineeVpdForNextPlacement`
;
