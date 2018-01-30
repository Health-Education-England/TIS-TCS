delimiter //
drop procedure if exists build_post_view//
create procedure build_post_view()
begin

  truncate table PostView;

  INSERT INTO `PostView` (`id`,
  `approvedGradeId`,
  `primarySpecialtyId`,
  `primarySpecialtyCode`,
  `primarySpecialtyName`,
  `primarySiteId`,
  `programmeName`,
  `fundingType`,
  `nationalPostNumber`,
  `status`,
  `owner`,
  `intrepidId`)
  SELECT p.`id`,
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
  p.`intrepidId`
  FROM `Post` p
  LEFT JOIN `PostGrade` pg on p.`id` = pg.`postId` AND pg.`postGradeType` = 'APPROVED'
  LEFT JOIN `PostSpecialty` ps on p.`id` = ps.`postId` AND ps.`postSpecialtyType` = 'PRIMARY'
  LEFT JOIN `Specialty` sp on sp.`id` = ps.`specialtyId`
  LEFT JOIN `PostSite` pst on p.`id` = pst.`postId` AND pst.`postSiteType` = 'PRIMARY'
  LEFT JOIN `PostFunding` pf on p.`id` = pf.`postId`
  LEFT JOIN `Programme` prg on p.`programmeId` = prg.`id`;

end//
delimiter ;