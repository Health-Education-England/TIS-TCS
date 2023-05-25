select distinct ot.*
from (
  select
    cd.id as personId,
    gmc.gmcNumber,
    cd.forenames,
    cd.surname,
    pm1.programmeMembershipType,
    pm1.programmeStartDate,
    pm1.programmeEndDate,
    latestCm.curriculumEndDate,
    currentPmCounts.programmeNames as programmeName,
    if(currentPmCounts.count_num > 1, NULL, currentPmCounts.owner) as owner
  from
    ContactDetails cd
  left join GmcDetails gmc on (gmc.id = cd.id)
  and gmc.id is not null
  and not lower(gmc.id) = "unknown"
  left join (
    -- count current PMs with combined programme names for each person
    select
      pm.personId,
      GROUP_CONCAT(prg.programmeName SEPARATOR " | ") programmeNames,
      GROUP_CONCAT(prg.owner SEPARATOR " | ") owner,
      count(if(pm.programmeStartDate <= current_date() and pm.programmeEndDate >= current_date(), true, null)) as count_num
    from ProgrammeMembership pm
    left join Programme prg
      on pm.programmeId = prg.id and pm.programmeStartDate <= current_date() and pm.programmeEndDate >= current_date()
    WHERECLAUSE(pm, personId)
    group by pm.personId
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
  WHERECLAUSE(cd, id)
  ) as ot
ORDERBYCLAUSE
LIMITCLAUSE
;
