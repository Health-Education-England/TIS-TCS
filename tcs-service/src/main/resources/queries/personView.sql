select distinct ot.*
from (
  select
    p.id,
    p.intrepidId,
    cd.surname,
    cd.forenames,
    gmc.gmcNumber,
    gdc.gdcNumber,
    p.publicHealthNumber,
    pm.programmeId,
    prg.programmeName,
    prg.programmeNumber,
    tn.trainingNumber as trainingNumber,
    pl.gradeId,
    pl.gradeAbbreviation,
    pl.siteId,
    pl.siteCode,
    pl.placementType,
    p.role,
    s.name as specialty,
    p.status,
    lo.owner as currentOwner,
    lo.rule as currentOwnerRule,
    pt.trustId
  from
    Person p
  join ContactDetails cd on (cd.id = p.id)
  left join GmcDetails gmc on (gmc.id = p.id)
  left join GdcDetails gdc on (gdc.id = p.id)
  left join ProgrammeMembership pm on (pm.personId = p.id) and curdate() between pm.programmeStartDate and pm.programmeEndDate
  left join Programme prg on (prg.id = pm.programmeId)
  left join TrainingNumber tn on tn.id = pm.trainingNumberId
  left join Placement pl on (pl.traineeId = p.id) and curdate() between pl.dateFrom and pl.dateTo
  left join PlacementSpecialty ps on ps.placementId = pl.id and ps.placementSpecialtyType = 'PRIMARY'
  left join Specialty s on s.id = ps.specialtyId
  left join PersonOwner lo on (lo.id = p.id)
  left join PersonTrust pt on (pt.personId = p.id)
  WHERECLAUSE
  ) as ot
ORDERBYCLAUSE
LIMITCLAUSE
;