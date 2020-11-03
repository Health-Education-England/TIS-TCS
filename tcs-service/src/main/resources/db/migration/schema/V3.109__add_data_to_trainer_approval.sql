INSERT INTO `TrainerApproval` (`startDate`, `endDate`, `trainerType`, `approvalStatus`, `personId`)
SELECT null, null, get_personRoleCategory(role) as 'roleCategory', p.status, p.id from Person p
HAVING roleCategory != ""
