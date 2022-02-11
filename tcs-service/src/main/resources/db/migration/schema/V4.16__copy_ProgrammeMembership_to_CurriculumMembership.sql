CREATE TABLE CurriculumMembership LIKE ProgrammeMembership;
INSERT INTO CurriculumMembership SELECT * FROM ProgrammeMembership;
SET @correct_auto_increment = (SELECT AUTO_INCREMENT FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'tcs' AND TABLE_NAME = 'ProgrammeMembership');
SET @sql = CONCAT('ALTER TABLE `CurriculumMembership` AUTO_INCREMENT = ', @correct_auto_increment);
PREPARE st FROM @sql;
EXECUTE st;
