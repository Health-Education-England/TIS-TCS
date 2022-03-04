select distinct
  pm.id,
  pm.personId,
  pm.programmeId,
  prg.programmeName,
  prg.programmeNumber,
  tn.trainingNumber,
  pm.programmeMembershipStatus
from
  (
  select
    id,
    personId,
    programmeId,
    trainingNumberId,
    get_programmeMembershipStatus(programmeStartDate, programmeEndDate) as programmeMembershipStatus
  from
    CurriculumMembership WHERECLAUSE
  ) as pm
left join Programme prg on (prg.id = pm.programmeId)
left join TrainingNumber tn on (tn.id = pm.trainingNumberId);
