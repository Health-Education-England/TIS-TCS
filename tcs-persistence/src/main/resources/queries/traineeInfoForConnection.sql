select distinct ot.*
from (
  select
    p.id personId,
    gmc.gmcNumber,
    cd.forenames,
    cd.surname,
    pm1.programmeMembershipType,
    pm1.programmeStartDate,
    pm1.programmeEndDate,
    latestCm.curriculumEndDate,
    if(prg.programmeName is null and currentPmCounts.count_num > 1, "multiple programmes", prg.programmeName) as programmeName,
    prg.owner
  from
    Person p
  join ContactDetails cd on (cd.id = p.id)
  left join GmcDetails gmc on (gmc.id = p.id)
  left join (
    -- count current PMs for each person
    select
      personId,
      count(if(pm.programmeStartDate <= current_date() and pm.programmeEndDate >= current_date(), true, null)) as count_num
    from ProgrammeMembership pm
    WHERECLAUSE
    group by personId
  ) currentPmCounts on p.id = currentPmCounts.personId
  left join ProgrammeMembership pm1
    on pm1.personId = currentPmCounts.personId and currentPmCounts.count_num = 1 and (pm1.programmeStartDate <= current_date() and pm1.programmeEndDate >= current_date())
  left join (
    -- get max curriculumEndDate for every PM
      select
          cm.programmeMembershipUuid,
          max(cm.curriculumEndDate) curriculumEndDate
      from CurriculumMembership cm
      WHERECLAUSE
      group by cm.programmeMembershipUuid
  ) latestCm on latestCm.programmeMembershipUuid = pm1.uuid
  left join Programme prg on prg.id = pm1.programmeId
  WHERECLAUSE
  ) as ot
ORDERBYCLAUSE
LIMITCLAUSE
;
