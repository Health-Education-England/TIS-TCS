ALTER TABLE `PostView`
CHANGE `managingLocalOffice` `owner` varchar(255) DEFAULT NULL;

ALTER TABLE `Post` MODIFY trainingDescription TEXT;
