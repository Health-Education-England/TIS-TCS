ALTER TABLE `Post`
CHANGE `managingLocalOffice` `owner` varchar(255) DEFAULT NULL;

ALTER TABLE `Programme`
CHANGE `managingDeanery` `owner` varchar(255) DEFAULT NULL;


ALTER TABLE `PlacementFunder`
CHANGE `localOffice` `owner` varchar(255) DEFAULT NULL;