UPDATE `Person` p
SET `status` = 'INACTIVE'
WHERE
p.status <> 'INACTIVE' AND
p.role NOT LIKE '%Leave.Approver.%' AND
p.id NOT IN (SELECT `personId` FROM `ProgrammeMembership` pm WHERE `programmeEndDate` >= curdate() GROUP BY `personId`);
