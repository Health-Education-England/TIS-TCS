select distinct id,
approvedGradeId,
primarySpecialtyId,
primarySpecialtyCode,
primarySpecialtyName,
primarySiteId,
GROUP_CONCAT(programmeName SEPARATOR ', ') programmes,
fundingType,
nationalPostNumber,
status,
owner,
intrepidId,
GROUP_CONCAT(surnames SEPARATOR ', ') surnames, GROUP_CONCAT(forenames SEPARATOR ', ') forenames
 from (SELECT p.`id`,
    pg.`gradeId` as `approvedGradeId`,
    ps.`specialtyId` as `primarySpecialtyId`,
    sp.`specialtyCode` as `primarySpecialtyCode`,
    sp.`name` as `primarySpecialtyName`,
    pst.`siteId` as `primarySiteId`,
    prg.`programmeName`,
    pf.`fundingType`,
    p.`nationalPostNumber`,
    p.`status`,
    p.`owner`,
    p.`intrepidId`,
    c.surname surnames, c.forenames forenames,
    pt.trustId
    FROM `Post` p
    LEFT JOIN `PostGrade` pg on p.`id` = pg.`postId` AND pg.`postGradeType` = 'APPROVED'
    LEFT JOIN `PostSpecialty` ps on p.`id` = ps.`postId` AND ps.`postSpecialtyType` = 'PRIMARY'
    LEFT JOIN `Specialty` sp on sp.`id` = ps.`specialtyId`
    LEFT JOIN `PostSite` pst on p.`id` = pst.`postId` AND pst.`postSiteType` = 'PRIMARY'
    LEFT JOIN `PostFunding` pf on p.`id` = pf.`postId`
    LEFT JOIN `Placement` pl on pl.postId = p.id and curdate() BETWEEN pl.dateFrom AND pl.dateTo
    LEFT JOIN `ContactDetails` c on pl.traineeId = c.id
    LEFT JOIN `ProgrammePost` pp on pp.postId = p.id
    LEFT JOIN `Programme` prg on prg.`id` = pp.`programmeId`
    LEFT JOIN `PostTrust` pt on pt.`postId` = p.`id`
 WHERECLAUSE
 ORDERBYCLAUSE
) as ot
group by id,approvedGradeId,primarySpecialtyId,primarySpecialtyCode,primarySpecialtyName,primarySiteId,fundingType,nationalPostNumber,status,owner,intrepidId
 LIMITCLAUSE
;