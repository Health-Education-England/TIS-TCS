DELETE FROM `CurriculumMembership` WHERE `id` IN (1, 2, 3, 4, 5);
DELETE FROM `ProgrammeMembership` WHERE `uuid` IN
  ('0023f7bc-defa-48b4-8186-5e680e981db4', '002e46cf-c966-4ca9-ac2b-ef2e5a2b57f0',
  '00aca6ce-6e0d-405c-931e-1fcb1aa7a28c', '004c4a2a-80fd-4312-83b4-5e8666db5166');
DELETE FROM `Person` WHERE `id` IN (1, 2, 3);
DELETE FROM `Programme` WHERE `id` IN (1, 2, 3);
