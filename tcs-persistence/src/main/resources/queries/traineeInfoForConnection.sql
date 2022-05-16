select distinct ot.*
from (
  select
    p.id,
    gmc.gmcNumber,
    cd.forenames,
    cd.surname,
    latestPm.programmeMembershipType,
    latestPm.programmeStartDate,
    latestPm.programmeEndDate,
    latestPm.curriculumEndDate,
    prg.programmeName,
    prg.owner
  from
    Person p
  join ContactDetails cd on (cd.id = p.id)
  left join GmcDetails gmc on (gmc.id = p.id)
  left join (select distinct pm.personId, pm.programmeStartDate, pm.programmeEndDate,
          pm.programmeId, pm.programmeMembershipType, cm.curriculumId, cm.curriculumEndDate
          from CurriculumMembership cm
          inner join ProgrammeMembership pm ON cm.programmeMembershipUuid = pm.uuid
          inner join (select personId, MAX(programmeEndDate) as latestEndDate
              from ProgrammeMembership
              group by personId) latest on pm.personId = latest.personId
              and pm.programmeEndDate = latest.latestEndDate
          ) latestPm on (latestPm.personId = p.id)
  left join Programme prg on (prg.id = latestPm.programmeId)
  ) as ot
;
