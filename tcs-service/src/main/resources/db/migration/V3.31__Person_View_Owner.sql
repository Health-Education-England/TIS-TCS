CREATE TABLE `PersonOwner` (
  `id` bigint(20) NOT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `rule` varchar(255) DEFAULT NULL,
  UNIQUE KEY `person_owner_pk` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `PersonOwner` ADD CONSTRAINT `fk_person_owner_person_id` FOREIGN KEY (`id`) REFERENCES `Person` (`id`);

create or replace view `PersonView` as
  select p.id,
    p.intrepidId,
    cd.surname,
    cd.forenames,
    gmc.gmcNumber,
    gdc.gdcNumber,
    p.publicHealthNumber,
    pm.programmeId,
    pm.programmeName,
    pm.programmeNumber,
    pm.TrainingNumber,
    pl.gradeAbbreviation,
    pl.siteCode,
    pl.placementType,
    p.role,
    p.status,
    COALESCE(pm.lo, pl.lo) as currentOwner,
    CASE WHEN (pm.lo IS NOT NULL) THEN 'P1' ELSE 'P2' END as currentOwnerRule
  from Person p
    left join ContactDetails cd on (cd.id = p.id)
    left join GmcDetails gmc on (gmc.id = p.id)
    left join GdcDetails gdc on (gdc.id = p.id)
    left join (select pm.personid,
                 pm.programmeStartDate,
                 pm.programmeEndDate,
                 pm.programmeId,
                 prg.programmeName,
                 prg.programmeNumber,
                 tn.number as TrainingNumber,
                 prg.managingDeanery as lo
               from ProgrammeMembership pm
                 join Programme prg on (prg.id = pm.programmeId)
                 join TrainingNumber tn on (tn.programmeId = pm.programmeId)) pm on (pm.personId = p.id
                                                                                     and curdate() between pm.programmeStartDate and pm.programmeEndDate)
    left join (select pl.traineeId,
                 pl.dateFrom,
                 pl.dateTo,
                 pl.gradeAbbreviation,
                 pl.siteCode,
                 pl.placementType,
                 pst.managingLocalOffice as lo
               from Placement pl
                 join Post pst on (pst.id = pl.postId)) pl on (pl.traineeId = p.id
                                                               and curdate() between pl.dateFrom and pl.dateTo)