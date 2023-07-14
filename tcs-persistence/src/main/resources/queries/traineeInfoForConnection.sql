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
  inner join GmcDetails gmc on (gmc.id = cd.id)
    -- note: null values are filtered out by the condition below
    and lower(gmc.gmcNumber) <> 'unknown'
    and gmc.gmcNumber not like CONCAT('%', UNHEX('c2a0'), '%') -- filter out all gmc number with non-breaking space
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
      cm2.programmeMembershipUuid,
      max(cm2.curriculumEndDate) curriculumEndDate,
      cm2.personId
    from (
        select cm.programmeMembershipUuid, cm.curriculumEndDate, pm2.personId
        from CurriculumMembership cm
        join ProgrammeMembership pm2 on cm.programmeMembershipUuid = pm2.uuid
      ) cm2
    WHERECLAUSE(cm2, personId)
    group by cm2.programmeMembershipUuid
  ) latestCm on latestCm.programmeMembershipUuid = pm1.uuid
  WHERECLAUSE(cd, id)
) as ot
ORDERBYCLAUSE
LIMITCLAUSE
;
