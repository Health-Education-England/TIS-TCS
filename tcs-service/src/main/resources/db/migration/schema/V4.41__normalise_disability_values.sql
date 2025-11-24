UPDATE `PersonalDetails`
SET `disability` = 'NO'
WHERE UPPER(`disability`) = 'NO' AND `disability` COLLATE `utf8_bin` <> 'NO';

UPDATE `PersonalDetails`
SET `disability` = 'YES'
WHERE UPPER(`disability`) = 'YES' AND `disability` COLLATE `utf8_bin` <> 'YES';
