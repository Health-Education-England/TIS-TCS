select distinct ot.*
from (
  select
    cd.id personId,
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
    ContactDetails cd
  left join GmcDetails gmc on (gmc.id = cd.id)
  left join (
    -- count current PMs for each person
    select
      personId,
      count(if(pm.programmeStartDate <= current_date() and pm.programmeEndDate >= current_date(), true, null)) as count_num
    from ProgrammeMembership pm
    WHERECLAUSE(pm, personId)
    group by personId
  ) currentPmCounts on cd.id = currentPmCounts.personId
  left join ProgrammeMembership pm1
    on pm1.personId = currentPmCounts.personId and currentPmCounts.count_num = 1 and (pm1.programmeStartDate <= current_date() and pm1.programmeEndDate >= current_date())
  left join (
    -- get max curriculumEndDate for every PM
      select
          cm.programmeMembershipUuid,
          max(cm.curriculumEndDate) curriculumEndDate
      from CurriculumMembership cm
      WHERECLAUSE(cm, personId)
      group by cm.programmeMembershipUuid
  ) latestCm on latestCm.programmeMembershipUuid = pm1.uuid
  left join Programme prg on prg.id = pm1.programmeId
  WHERECLAUSE(cd, id)
  ) as ot
ORDERBYCLAUSE
LIMITCLAUSE
;
