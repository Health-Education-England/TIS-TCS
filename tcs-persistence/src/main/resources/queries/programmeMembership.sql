select distinct
  pm.id,
  pm.personId,
  pm.programmeId,
  prg.programmeName,
  prg.programmeNumber,
  NULL as trainingNumber,
  pm.programmeMembershipStatus
from
  (
  select
    cmem.id,
    pmem.personId,
    pmem.programmeId,
    pmem.trainingNumberId,
    get_programmeMembershipStatus(pmem.programmeStartDate, pmem.programmeEndDate) as programmeMembershipStatus
  from
    CurriculumMembership cmem JOIN ProgrammeMembership pmem ON cmem.programmeMembershipUuid = pmem.uuid WHERECLAUSE
  ) as pm
left join Programme prg on (prg.id = pm.programmeId);
