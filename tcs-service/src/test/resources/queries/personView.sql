select p.id,
     p.intrepidId,
     cd.surname,
     cd.forenames,
     gmc.gmcNumber,
     gdc.gdcNumber,
     p.publicHealthNumber,
     pm.programmeId,
     prg.programmeName,
     prg.programmeNumber,
     pm.TrainingNumberId as trainingNumber,
     pl.gradeId,
     pl.gradeAbbreviation,
     pl.siteId,
     pl.siteCode,
     pl.placementType,
     p.role,
     p.status,
     lo.owner as currentOwner,
     lo.rule as currentOwnerRule
from Person p
join ContactDetails cd on (cd.id = p.id)
join GmcDetails gmc on (gmc.id = p.id)
join GdcDetails gdc on (gdc.id = p.id)
left join ProgrammeMembership pm on (pm.personId = p.id) and curdate() between pm.programmeStartDate and pm.programmeEndDate
left join Programme prg on (prg.id = pm.programmeId)
left join Placement pl on (pl.traineeId = p.id) and curdate() between pl.dateFrom and pl.dateTo
left join PersonOwner lo on (lo.id = p.id)
 WHERECLAUSE
 ORDERBYCLAUSE
 LIMITCLAUSE
;