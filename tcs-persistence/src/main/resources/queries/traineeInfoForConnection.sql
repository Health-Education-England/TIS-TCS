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
  left join (select distinct cm.personId, cm.programmeStartDate, cm.programmeEndDate,
          cm.programmeId, cm.programmeMembershipType, cm.curriculumId, cm.curriculumEndDate
          from CurriculumMembership cm
          inner join (select personId, MAX(programmeEndDate) as latestEndDate
              from CurriculumMembership
              group by personId) latest on cm.personId = latest.personId
              and cm.programmeEndDate = latest.latestEndDate
          ) latestPm on (latestPm.personId = p.id)
  left join Programme prg on (prg.id = latestPm.programmeId)
  ) as ot
;
