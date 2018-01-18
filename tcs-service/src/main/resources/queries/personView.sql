select p.id,
     p.intrepidId,
     cd.surname,
     cd.forenames,
     gmc.gmcNumber,
     gdc.gdcNumber,
     p.publicHealthNumber,
     pm.programmeId,
     pm.programmeName,
     pm.programmeNumber,
     pm.TrainingNumber as trainingNumber,
     pl.gradeAbbreviation,
     pl.siteCode,
     pl.placementType,
     p.role,
     p.status,
     lo.owner as currentOwner,
     lo.rule as currentOwnerRule
from Person p
left join ContactDetails cd on (cd.id = p.id)
left join GmcDetails gmc on (gmc.id = p.id)
left join GdcDetails gdc on (gdc.id = p.id)
left join (select pm.personid,
        pm.programmeStartDate,
        pm.programmeEndDate,
                pm.programmeId,
        prg.programmeName,
        prg.programmeNumber,
                pm.TrainingNumberId as TrainingNumber,
        prg.owner as lo
     from ProgrammeMembership pm
         join Programme prg on (prg.id = pm.programmeId)
     ) pm on (pm.personId = p.id and curdate() between pm.programmeStartDate and pm.programmeEndDate)
left join Placement pl on (pl.traineeId = p.id) and curdate() between pl.dateFrom and pl.dateTo
join PersonOwner lo on (lo.id = p.id)
 WHERECLAUSE
 ORDERBYCLAUSE
limit  :start , :end;