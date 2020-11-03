CREATE OR REPLACE VIEW `PostView` AS
SELECT p.`id`, pl.`traineeId` as `currentTraineeId`, gmcd.`gmcNumber` as `currentTraineeGmcNumber`, cd.`surname` as `currentTraineeSurname`, cd.`forenames` as `currentTraineeForenames`,  pg.`gradeId` as `approvedGradeCode`, ps.`specialtyId` as `primarySpecialtyId`, sp.`specialtyCode` as `primarySpecialtyCode`, sp.`name` as `primarySpecialtyName`, pst.`siteId` as `primarySiteCode`, prg.`programmeName`, pf.`fundingType`,
p.`nationalPostNumber`, p.`status`, p.`employingBodyId`, p.`trainingBodyId`, p.`oldPostId`, p.`newPostId`, p.`suffix`, p.`managingLocalOffice`, p.`postFamily`, p.`localPostNumber`, p.`programmeId`, p.`trainingDescription`, p.`intrepidId`
FROM `Post` p
LEFT JOIN `PostGrade` pg on p.`id` = pg.`postId` AND pg.`postGradeType` = 'APPROVED'
LEFT JOIN `PostSpecialty` ps on p.`id` = ps.`postId` AND ps.`postSpecialtyType` = 'PRIMARY'
LEFT JOIN `Specialty` sp on sp.`id` = ps.`specialtyId`
LEFT JOIN `PostSite` pst on p.`id` = pst.`postId` AND pst.`postSiteType` = 'PRIMARY'
LEFT JOIN `PostFunding` pf on p.`id` = pf.`postId`
LEFT JOIN `Programme` prg on p.`programmeId` = prg.`id`
LEFT JOIN `Placement` pl on pl.`postId` = p.`id` AND pl.`dateFrom` < CURDATE() AND pl.`dateTo` > CURDATE()
LEFT JOIN `ContactDetails` cd on pl.`traineeId` = cd.`id`
LEFT JOIN `GmcDetails` gmcd on pl.`traineeId` = gmcd.`id`;


