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

SELECT DISTINCT p.`id`                AS `id`,
       pg.`gradeId`                   AS `approvedGradeId`,
       ps.`specialtyId`               AS `primarySpecialtyId`,
       sp.`specialtyCode`             AS `primarySpecialtyCode`,
       sp.`name`                      AS `primarySpecialtyName`,
       pst.`siteId`                   AS `primarySiteId`,
       (SELECT GROUP_CONCAT(DISTINCT prg.`programmeName` SEPARATOR '; ')
        FROM `ProgrammePost` pp
               JOIN `Programme` prg ON prg.`id` = pp.`programmeId`
        WHERE pp.`postId` = p.`id`)   AS `programmes`,
       (SELECT GROUP_CONCAT(DISTINCT pp.`programmeId` SEPARATOR ';')
        FROM `ProgrammePost` pp
        WHERE pp.`postId` = p.`id`)   AS `programmeIds`,
       (SELECT GROUP_CONCAT(DISTINCT pf.`fundingTypeId` SEPARATOR ';')
        FROM `PostFunding` pf
        WHERE pf.`postId` = p.`id`
          AND (curdate() BETWEEN pf.`startDate` AND pf.`endDate` OR
               pf.`endDate` IS NULL)) AS `fundingTypeIds`,
       (SELECT GROUP_CONCAT(DISTINCT pf.`fundingSubtypeId` SEPARATOR ';')
        FROM `PostFunding` pf
        WHERE pf.`postId` = p.`id`
          AND (curdate() BETWEEN pf.`startDate` AND pf.`endDate` OR
               pf.`endDate` IS NULL)) AS `fundingSubtypeIds`,
       (SELECT GROUP_CONCAT(DISTINCT pt.`trustId` SEPARATOR ';')
        FROM `PostTrust` pt
        WHERE pt.`postId` = p.`id`)   AS `trustIds`,
       p.`nationalPostNumber`,
       p.`fundingStatus`,
       p.`owner`,
       p.`intrepidId`,
       trainees.`surnames`,
       trainees.`forenames`
FROM `Post` p
       LEFT JOIN `PostGrade` pg ON pg.`postId` = p.`id` AND pg.`postGradeType` = 'APPROVED'
       LEFT JOIN `PostSpecialty` ps ON ps.`postId` = p.`id` AND ps.`postSpecialtyType` = 'PRIMARY'
       LEFT JOIN `Specialty` sp ON sp.`id` = ps.`specialtyId`
       LEFT JOIN `PostSite` pst ON pst.`postId` = p.`id` AND pst.`postSiteType` = 'PRIMARY'
       LEFT JOIN (SELECT t.`postId`,
                         GROUP_CONCAT(t.`surname` ORDER BY t.`traineeId` SEPARATOR ', ')   AS `surnames`,
                         GROUP_CONCAT(t.`forenames` ORDER BY t.`traineeId` SEPARATOR ', ') AS `forenames`
                  FROM (SELECT DISTINCT pl.`postId`, pl.`traineeId`, c.`surname`, c.`forenames`
                        FROM `Placement` pl
                               LEFT JOIN `ContactDetails` c ON c.`id` = pl.`traineeId`
                        WHERE curdate() BETWEEN pl.`dateFrom` AND pl.`dateTo`) t
                  GROUP BY t.`postId`) trainees ON trainees.`postId` = p.`id`
WHERECLAUSE
ORDERBYCLAUSE
LIMITCLAUSE
;
