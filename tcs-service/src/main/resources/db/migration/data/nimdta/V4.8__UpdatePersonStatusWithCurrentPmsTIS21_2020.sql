UPDATE `Person` p
SET `status` = 'CURRENT'
WHERE p.status <> 'CURRENT' AND
p.id IN (SELECT `personId` FROM `ProgrammeMembership` pm WHERE `programmeStartDate` <= curdate() AND `programmeEndDate` >= curdate() GROUP BY `personId`);
