INSERT INTO `Person` (`id`)
VALUES
    (1),
    (2),
    (3);

INSERT INTO `ContactDetails`(`id`)
VALUES
    (1),
    (2),
    (3);

INSERT INTO `GmcDetails`(`id`, `gmcNumber`)
VALUES
    (1, 11111111),
    (2, 22222222),
    (3, 33333333);

INSERT INTO `Programme` (`id`, `status`, `owner`, `programmeName`, `programmeNumber`, `intrepidId`)
VALUES
	(1, 'CURRENT', 'Yorkshire and the Humber', 'Core Psychiatry Training', 'YAH052', '64063'),
	(2, 'CURRENT', 'East of England', 'General Practice', 'EOE801', '66687'),
	(3, 'CURRENT', 'North East', 'Neurosurgery', 'NOR089', '113289'),
	(4, 'CURRENT', 'West Midlands', 'Plastic surgery', 'WMD692', '386806'),
	(5, 'INACTIVE', 'West Midlands', 'Foundation/Legacy', 'WMD2200/LEGACY', '435450');

INSERT INTO `ProgrammeMembership` (`uuid`, `programmeId`, `personId`, `programmeStartDate`, `programmeEndDate`, `programmeMembershipType`)
VALUES
  ('0023f7bc-defa-48b4-8186-5e680e981db4', 1, 1, '2099-08-01', '2099-08-01', 'MILITARY'),
  ('002e46cf-c966-4ca9-ac2b-ef2e5a2b57f0', 2, 1, '2022-09-07', '2099-08-01', 'MILITARY'),
  ('00aca6ce-6e0d-405c-931e-1fcb1aa7a28c', 3, 1, '2022-08-01', '2022-12-01', 'MILITARY'),
  ('004c4a2a-80fd-4312-83b4-5e8666db5166', 4, 2, '2022-09-04', '2099-08-01', 'SUBSTANTIVE'),
  ('01f69190-487c-4904-a9d9-fa4a2acadf4e', 5, 3, '2022-09-01', '2099-09-01', 'SUBSTANTIVE');

INSERT INTO `Placement` (`id`, `traineeId`, `dateFrom`, `dateTo`, `gradeId`)
VALUES
  (1, 1, '2099-07-01', '2099-08-01', 111),
  (2, 1, '2022-09-10', '2099-08-10', 111),
  (3, 1, '2022-09-07', '2022-12-01', 111),
  (4, 2, '2022-09-04', '2099-08-01', 279),
  (5, 3, '2022-09-01', '2099-09-01', 111);
