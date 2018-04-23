SELECT pd.placementId placementId, pd.specialtyId specialtyId, pd.placementSpecialtyType placementSpecialtyType
FROM PlacementSpecialty pd
WHERE pd.placementId = :id