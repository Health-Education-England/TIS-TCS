create view ProgrammeMembership_v as
select a.id, b.programmeMembershipType, b.rotation, a.curriculumStartDate, a.curriculumEndDate,
    a.periodOfGrace, b.programmeStartDate, a.curriculumCompletionDate, b.programmeEndDate,
    b.leavingDestination, b.programmeId, a.curriculumId, b.trainingNumberId, a.intrepidId,
    b.personId, a.amendedDate, b.rotationId, b.trainingPathway, b.leavingReason
from CurriculumMembershipInterim a
join ProgrammeMembershipInterim b
on a.programmeMembershipId = b.id;
