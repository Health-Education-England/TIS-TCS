INSERT INTO `Person` (`id`)
VALUES
    (1),
    (2);

INSERT INTO `Absence` (`id`, `personId`, `startDate`, `endDate`, `durationInDays`, `absenceAttendanceId`)
VALUES
	(1, 2, '2020-08-01', '2020-08-02', 1, '1'),
	(2, 1, '2020-01-01', '2021-02-03', 30, '11122'),
	(3, 1, '2020-01-01', '2020-01-03', 3, '2222');
