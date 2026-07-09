/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

select distinct id,
approvedGradeId,
primarySpecialtyId,
primarySpecialtyCode,
primarySpecialtyName,
primarySiteId,
GROUP_CONCAT(distinct programmeName SEPARATOR '|||') programmes,
GROUP_CONCAT(distinct fundingType SEPARATOR '|||') fundingType,
nationalPostNumber,
fundingStatus,
owner,
intrepidId,
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
        p.`intrepidId`,
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
