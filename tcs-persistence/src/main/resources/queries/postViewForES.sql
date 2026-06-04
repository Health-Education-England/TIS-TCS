select distinct ot.*
from (
  select
    p.id,
    p.intrepidId,
    p.nationalPostNumber,
    p.fundingStatus,
    p.owner,

    pst.siteId as primarySiteId,

    pg.gradeId as approvedGradeId,

    ps.specialtyId as primarySpecialtyId,
    s.specialtyCode as primarySpecialtyCode,
    s.name as primarySpecialtyName,

    group_concat(distinct prog.programmeName separator ', ') as programmes,
    group_concat(distinct pf.fundingType separator ', ') as fundingType,

    cd.id as currentTraineeId,
    gmc.gmcNumber as currentTraineeGmcNumber,
    cd.surname as surnames,
    cd.forenames as forenames

  from Post p

  left join PostSite pst
    on pst.postId = p.id
    and pst.postSiteType = 'PRIMARY'

  left join PostGrade pg
    on pg.postId = p.id
    and pg.postGradeType = 'APPROVED'

  left join PostSpecialty ps
    on ps.postId = p.id
    and ps.postSpecialtyType = 'PRIMARY'

  left join Specialty s
    on s.id = ps.specialtyId

  left join PostFunding pf
    on pf.postId = p.id

  left join ProgrammePost pp
    on pp.postId = p.id

  left join Programme prog
    on prog.id = pp.programmeId

  left join Placement pl
    on pl.postId = p.id
    and curdate() between pl.dateFrom and pl.dateTo

  left join Person trainee
    on trainee.id = pl.traineeId

  left join ContactDetails cd
    on cd.id = trainee.id

  left join GmcDetails gmc
    on gmc.id = trainee.id

  TRUST_JOIN
  PROGRAMME_JOIN
  WHERECLAUSE

  group by
    p.id,
    p.intrepidId,
    p.nationalPostNumber,
    p.fundingStatus,
    p.owner,
    pst.siteId,
    pg.gradeId,
    ps.specialtyId,
    s.specialtyCode,
    s.name,
    cd.id,
    gmc.gmcNumber,
    cd.surname,
    cd.forenames
) as ot
ORDERBYCLAUSE
LIMITCLAUSE
;
