SELECT p.*, s.name primarySpecialtyName, c.forenames, c.surname, c.id traineeId, p.id placementId, ps.placementSpecialtyType
FROM Placement p
LEFT JOIN PlacementSpecialty ps
ON p.id = ps.placementId
LEFT JOIN Specialty s
ON s.id = ps.specialtyId
LEFT JOIN ContactDetails c
ON c.id = p.traineeId
WHERE
-- TODO: uncomment this when changes to the FE adds a specialty on creation
-- ps.placementSpecialtyType = :specialtyType
-- AND
p.traineeId = :traineeId
ORDER BY dateTo DESC