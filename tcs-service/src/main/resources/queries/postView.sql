select distinct ot.* from (SELECT p.`id`,
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
    curPlacement.`surnames`,
    curPlacement.`forenames`
    FROM `Post` p
    LEFT JOIN `PostGrade` pg on p.`id` = pg.`postId` AND pg.`postGradeType` = 'APPROVED'
    LEFT JOIN `PostSpecialty` ps on p.`id` = ps.`postId` AND ps.`postSpecialtyType` = 'PRIMARY'
    LEFT JOIN `Specialty` sp on sp.`id` = ps.`specialtyId`
    LEFT JOIN `PostSite` pst on p.`id` = pst.`postId` AND pst.`postSiteType` = 'PRIMARY'
    LEFT JOIN `PostFunding` pf on p.`id` = pf.`postId`
    LEFT JOIN `Programme` prg on p.`programmeId` = prg.`id`
    LEFT JOIN (
      SELECT curPlacement.postId, GROUP_CONCAT(curPlacement.surname SEPARATOR ', ') surnames, GROUP_CONCAT(curPlacement.forenames SEPARATOR ', ') forenames
    	FROM (
    		SELECT pl.postId, pl.dateFrom, pl.dateTo, c.surname, c.forenames
    		FROM `Placement` pl
    		JOIN `ContactDetails` c ON pl.traineeId = c.id
    		WHERE curdate() BETWEEN pl.dateFrom AND pl.dateTo
    	) curPlacement
    	GROUP BY curPlacement.postId
  ) curPlacement on curPlacement.postId = p.id
 WHERECLAUSE
 ORDERBYCLAUSE
 LIMITCLAUSE
) as ot
;