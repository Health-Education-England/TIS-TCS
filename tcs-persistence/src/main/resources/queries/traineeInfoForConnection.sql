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
    currentGrade.currentGrades,
    latestCm.curriculumEndDate,
    currentPmCounts.programmeNames as programmeName,
    if(currentPmCounts.count_num > 1, NULL, currentPmCounts.owner) as owner
  from
    ContactDetails cd
  inner join GmcDetails gmc on (gmc.id = cd.id)
    -- note: null values are filtered out by the condition below
    and lower(gmc.gmcNumber) <> 'unknown' and lower(gmc.gmcNumber) <> 'na' and lower(gmc.gmcNumber) <> 'n/a'
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
  left join (
      select
  		placement.traineeId,
  		-- one row per trainee
  		GROUP_CONCAT(distinct placement.gradeId SEPARATOR " | ") currentGrades
      from (
        select traineeId, gradeId, dateFrom, dateTo
        from Placement pl
        WHERECLAUSE(pl, traineeId)
      ) placement
      where placement.dateFrom <= current_date() and placement.dateTo >= current_date()
      group by placement.traineeId
    ) currentGrade on cd.id = currentGrade.traineeId
  WHERECLAUSE(cd, id)
) as ot
-- Filter based on "role" from the "Person" table
where ot.personId not in (
  select distinct p.id
  from Person p
  where lower(p.role) like '%dummy%' or lower(p.role) like '%placeholder%'
)
ORDERBYCLAUSE
LIMITCLAUSE
;
