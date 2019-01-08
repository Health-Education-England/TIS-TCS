INSERT INTO `Person` (`id`, `intrepidId`, `addedDate`, `amendedDate`, `role`, `status`, `comments`, `inactiveDate`, `inactiveNotes`, `publicHealthNumber`, `regulator`)
VALUES
	(1, NULL, '2019-01-05 14:24:35', '2019-01-05 14:26:57.332', '', 'CURRENT', NULL, NULL, NULL, NULL, NULL);

INSERT INTO `Post` (`id`, `nationalPostNumber`, `status`, `employingBodyId`, `trainingBodyId`, `oldPostId`, `newPostId`, `suffix`, `owner`, `postFamily`, `localPostNumber`, `trainingDescription`, `intrepidId`, `legacy`, `bypassNPNGeneration`)
VALUES
	(2, 'xxx/xxx03/080/xxx/015', 'CURRENT', 1411, 1411, NULL, NULL, NULL, 'Health Education England North West London', NULL, NULL, '', '128357847', 0, 1);


INSERT INTO `Placement` (`id`, `dateFrom`, `dateTo`, `placementWholeTimeEquivalent`, `intrepidId`, `traineeId`, `postId`, `localPostNumber`, `siteCode`, `gradeAbbreviation`, `placementType`, `trainingDescription`, `siteId`, `gradeId`)
VALUES
	(3, '2018-03-05', '2019-03-04', 1, '1111111', 1, 2, NULL, 'SSW21', 'ST1', 'In post', '', 2217, 306);


INSERT INTO `Comment` (`id`, `parentId`, `threadId`, `author`, `body`, `addedDate`, `amendedDate`, `inactiveDate`, `placementId`, `source`)
VALUES
	(4, NULL, NULL, 'user', '1st BODY OF COMMENT', '2018-01-01', '2018-01-01', NULL, 3, 'INTREPID'),
	(5, NULL, NULL, 'user', '2nd BODY OF COMMENT', '2018-01-05', '2018-01-05', NULL, 3, 'INTREPID');
