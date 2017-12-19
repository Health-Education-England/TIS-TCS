create or replace view `PersonView`
as
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
                  tn.number as TrainingNumber,
          prg.managingDeanery as lo
       from ProgrammeMembership pm
           join Programme prg on (prg.id = pm.programmeId)      
       join TrainingNumber tn on (tn.programmeId = pm.programmeId)) pm on (pm.personId = p.id
                                                           and curdate() between pm.programmeStartDate and pm.programmeEndDate)
left join (select pl.traineeId,
          pl.dateFrom,
          pl.dateTo,
          pl.gradeAbbreviation,
          pl.siteCode,
          pl.placementType,
          pst.managingLocalOffice as lo
       from Placement pl
       join Post pst on (pst.id = pl.postId)) pl on (pl.traineeId = p.id
                           and curdate() between pl.dateFrom and pl.dateTo)
left join PersonOwner lo on (lo.id = p.id);