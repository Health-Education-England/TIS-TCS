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
    prg.owner
  from
    Person p
  join ContactDetails cd on (cd.id = p.id)
  left join GmcDetails gmc on (gmc.id = p.id)
  left join (select distinct pmi.personId, pmi.programmeStartDate, pmi.programmeEndDate,
          pmi.programmeId, pmi.programmeMembershipType
          from CurriculumMembership pmi
          inner join (select personId, MAX(programmeEndDate) as latestEndDate
              from CurriculumMembership
              group by personId) latest on pmi.personId = latest.personId
              and pmi.programmeEndDate = latest.latestEndDate
          ) latestPm on (latestPm.personId = p.id)
  left join Programme prg on (prg.id = latestPm.programmeId)
  ) as ot
;
