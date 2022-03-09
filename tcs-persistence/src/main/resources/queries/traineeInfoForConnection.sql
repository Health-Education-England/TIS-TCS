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
    prg.programmeName,
    prg.owner,
    cm.curriculumEndDate
  from
    Person p
  join ContactDetails cd on (cd.id = p.id)
  left join GmcDetails gmc on (gmc.id = p.id)
  left join (select distinct pmi.personId, pmi.programmeStartDate, pmi.programmeEndDate,
          pmi.programmeId, pmi.programmeMembershipType, pmi.curriculumId
          from ProgrammeMembership pmi
          inner join (select personId, MAX(programmeEndDate) as latestEndDate
              from ProgrammeMembership
              group by personId) latest on pmi.personId = latest.personId
              and pmi.programmeEndDate = latest.latestEndDate
          ) latestPm on (latestPm.personId = p.id)
  left join Programme prg on (prg.id = latestPm.programmeId)
  left join CurriculumMembership cm on (cm.id = latestPm.curriculumId)
  ) as ot
;
