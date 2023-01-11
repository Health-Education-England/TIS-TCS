INSERT INTO `Person` (`id`)
VALUES
    (1),
    (2),
    (3);

INSERT INTO `Programme` (`id`, `status`, `owner`, `programmeName`, `programmeNumber`, `intrepidId`)
VALUES
	(1, 'CURRENT', 'Health Education England Yorkshire and the Humber', 'Core Psychiatry Training', 'YAH052', '64063'),
	(2, 'CURRENT', 'Health Education England East of England', 'General Practice', 'EOE801', '66687'),
	(3, 'CURRENT', 'Health Education England North East', 'Neurosurgery', 'NOR089', '113289');

INSERT INTO `ProgrammeMembership` (`uuid`, `programmeId`, `personId`, `programmeEndDate`)
VALUES
  ('0023f7bc-defa-48b4-8186-5e680e981db4', 1, 1, '2023-08-01'),
  ('002e46cf-c966-4ca9-ac2b-ef2e5a2b57f0', 2, 2, '2022-09-07'),
  ('00aca6ce-6e0d-405c-931e-1fcb1aa7a28c', 3, 1, '2022-08-01'),
  ('004c4a2a-80fd-4312-83b4-5e8666db5166', 1, 3, '2027-09-04');

INSERT INTO `CurriculumMembership` (`id`, `programmeMembershipUuid`, `curriculumEndDate`)
VALUES
  (1, '0023f7bc-defa-48b4-8186-5e680e981db4', '2023-07-01'),
  (2, '0023f7bc-defa-48b4-8186-5e680e981db4', '2023-08-01'),
  (3, '002e46cf-c966-4ca9-ac2b-ef2e5a2b57f0', '2022-09-07'),
  (4, '00aca6ce-6e0d-405c-931e-1fcb1aa7a28c', '2022-08-01'),
  (5, '004c4a2a-80fd-4312-83b4-5e8666db5166', '2027-09-04');
