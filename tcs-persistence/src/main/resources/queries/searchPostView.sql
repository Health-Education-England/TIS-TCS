select distinct ot.*
from (
  SELECT p.`id`,
    pg.`gradeId` as `approvedGradeId`,
    ps.`specialtyId` as `primarySpecialtyId`,
    sp.`specialtyCode` as `primarySpecialtyCode`,
    sp.`name` as `primarySpecialtyName`,
    pst.`siteId` as `primarySiteId`,
    p.`nationalPostNumber`,
    p.`status`,
    p.`owner`,
    pt.trustId
    FROM `Post` p
    LEFT JOIN `PostGrade` pg on p.`id` = pg.`postId` AND pg.`postGradeType` = 'APPROVED'
    LEFT JOIN `PostSpecialty` ps on p.`id` = ps.`postId` AND ps.`postSpecialtyType` = 'PRIMARY'
    LEFT JOIN `Specialty` sp on sp.`id` = ps.`specialtyId`
    LEFT JOIN `PostSite` pst on p.`id` = pst.`postId` AND pst.`postSiteType` = 'PRIMARY'
    LEFT JOIN `PostTrust` pt on pt.`postId` = p.`id`
 WHERECLAUSE
 ORDERBYCLAUSE
 LIMITCLAUSE
) as ot
;