truncate table `PostSite`;
truncate table `PostGrade`;

ALTER TABLE `PostSite`
MODIFY COLUMN `siteId` bigint(10);

ALTER TABLE `PostGrade`
MODIFY COLUMN `gradeId` bigint(10);

---

ALTER TABLE `Placement`
ADD COLUMN `siteId` bigint(10);

ALTER TABLE `Placement`
ADD COLUMN `gradeId` bigint(10);

---

ALTER TABLE `PersonView`
ADD COLUMN `siteId` bigint(10);

ALTER TABLE `PersonView`
ADD COLUMN `gradeId` bigint(10);

---

ALTER TABLE `PostView`
ADD COLUMN `primarySiteId` bigint(10);

ALTER TABLE `PostView`
ADD COLUMN `approvedGradeId` bigint(10);



