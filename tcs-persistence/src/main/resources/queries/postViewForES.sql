/*
  Elasticsearch-specific PostView query.

  Kept separate from the standard postView.sql because TIS ES requires additional
  machine-readable fields, such as trustIds, programmeIds, for Elasticsearch
  permission filtering and incremental re-indexing. These fields are not
  required by the standard TCS post list API/UI response.

  Some fields like, intrepidId is omitted here.
*/
select distinct id,
approvedGradeId,
primarySpecialtyId,
primarySpecialtyCode,
primarySpecialtyName,
primarySiteId,
GROUP_CONCAT(distinct programmeName SEPARATOR ',') programmeNames,
GROUP_CONCAT(distinct fundingType SEPARATOR ',') fundingTypes,
nationalPostNumber,
fundingStatus,
owner,
surnames,
forenames,
GROUP_CONCAT(distinct trustId SEPARATOR ',') trustIds,
GROUP_CONCAT(distinct programmeId SEPARATOR ',') programmeIds
from (SELECT p.`id`,
        pg.`gradeId` as `approvedGradeId`,
        ps.`specialtyId` as `primarySpecialtyId`,
        sp.`specialtyCode` as `primarySpecialtyCode`,
        sp.`name` as `primarySpecialtyName`,
        pst.`siteId` as `primarySiteId`,
        prg.`programmeName`,
        pf.`fundingType`,
        p.`nationalPostNumber`,
        p.`fundingStatus`,
        p.`owner`,
        trainees.`surnames`,
        trainees.`forenames`,
        pt.`trustId` as `trustId`,
        pp.`programmeId` as `programmeId`
    FROM `Post` p
    LEFT JOIN `PostGrade` pg on p.`id` = pg.`postId` AND pg.`postGradeType` = 'APPROVED'
    LEFT JOIN `PostSpecialty` ps on p.`id` = ps.`postId` AND ps.`postSpecialtyType` = 'PRIMARY'
    LEFT JOIN `Specialty` sp on sp.`id` = ps.`specialtyId`
    LEFT JOIN `PostSite` pst on p.`id` = pst.`postId` AND pst.`postSiteType` = 'PRIMARY'
    LEFT JOIN `PostFunding` pf on p.`id` = pf.`postId` and (curdate() BETWEEN pf.startDate AND pf.endDate or pf.endDate is NULL)
    LEFT JOIN (
      SELECT t.`postId`,
      GROUP_CONCAT(t.`surname` ORDER BY t.`traineeId` SEPARATOR ', ') AS `surnames`,
      GROUP_CONCAT(t.`forenames` ORDER BY t.`traineeId` SEPARATOR ', ') AS `forenames`
      FROM (
        SELECT DISTINCT pl.`postId`, pl.`traineeId`, c.`surname`, c.`forenames`
        FROM `Placement` pl
        LEFT JOIN `ContactDetails` c on pl.`traineeId` = c.`id`
        WHERE curdate() BETWEEN pl.`dateFrom` AND pl.`dateTo`
       ) t
       GROUP BY t.`postId`
    ) trainees on trainees.`postId` = p.`id`
    LEFT JOIN `ProgrammePost` pp on pp.`postId` = p.`id`
    LEFT JOIN `Programme` prg on prg.`id` = pp.`programmeId`
    LEFT JOIN `PostTrust` pt on pt.`postId` = p.`id`
WHERECLAUSE
) as ot
group by id,approvedGradeId,primarySpecialtyId,primarySpecialtyCode,primarySpecialtyName,primarySiteId,surnames,forenames
ORDERBYCLAUSE
LIMITCLAUSE
;
