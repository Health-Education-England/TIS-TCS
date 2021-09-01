UPDATE `Person` p
SET `status` = 'INACTIVE'
WHERE
p.status <> 'INACTIVE' AND
p.id NOT IN (SELECT `personId` FROM `ProgrammeMembership` pm WHERE `programmeStartDate` <= curdate() AND `programmeEndDate` >= curdate() GROUP BY `personId`);

UPDATE `Person` p
SET `status` = 'CURRENT'
WHERE p.status <> 'CURRENT' AND
p.id IN (SELECT `personId` FROM `ProgrammeMembership` pm WHERE `programmeStartDate` <= curdate() AND `programmeEndDate` >= curdate() GROUP BY `personId`);

