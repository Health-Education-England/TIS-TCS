SELECT pd.placementId placementId, pd.specialtyId specialtyId, pd.placementSpecialtyType placementSpecialtyType, ps.name specialtyName
FROM PlacementSpecialty pd, Specialty ps
WHERE pd.placementId = :id and pd.specialtyId = ps.id
