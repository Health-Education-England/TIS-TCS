SELECT p.*, s.name primarySpecialtyName, c.forenames, c.legalforenames, c.surname, c.legalsurname, c.id traineeId, c.email, p.id placementId, ps.placementSpecialtyType, po.nationalPostNumber
FROM Placement p
LEFT JOIN PlacementSpecialty ps
ON p.id = ps.placementId
LEFT JOIN Specialty s
ON s.id = ps.specialtyId
LEFT JOIN ContactDetails c
ON c.id = p.traineeId
LEFT JOIN Post po
ON po.id = p.postId
WHERE
-- TODO: uncomment this when changes to the FE adds a specialty on creation
-- "ps.placementSpecialtyType = :specialtyType
-- AND
p.id = :id
ORDER BY dateTo DESC
