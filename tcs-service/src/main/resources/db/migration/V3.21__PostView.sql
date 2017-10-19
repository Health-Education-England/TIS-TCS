CREATE VIEW `PostView` AS
SELECT p.`id`,  pg.`gradeId` as `approvedGradeId`, ps.`specialtyId` as `primarySpecialtyId`, pst.`siteId` as `primarySiteId`, prg.`programmeName`, pf.`fundingType`,
p.`nationalPostNumber`, p.`status`, p.`employingBodyId`, p.`trainingBodyId`, p.`oldPostId`, p.`newPostId`, p.`suffix`, p.`managingLocalOffice`, p.`postFamily`, p.`localPostNumber`, p.`programmeId`, p.`trainingDescription`, p.`intrepidId`
FROM `Post` p
LEFT JOIN `PostGrade` pg on p.`id` = pg.`postId` AND pg.`postGradeType` = 'APPROVED'
LEFT JOIN `PostSpecialty` ps on p.`id` = ps.`postId` AND ps.`postSpecialtyType` = 'PRIMARY'
LEFT JOIN `PostSite` pst on p.`id` = pst.`postId` AND pst.`postSiteType` = 'PRIMARY'
LEFT JOIN `PostFunding` pf on p.`id` = pf.`postId`
LEFT JOIN `Programme` prg on p.`programmeId` = prg.`id`;

