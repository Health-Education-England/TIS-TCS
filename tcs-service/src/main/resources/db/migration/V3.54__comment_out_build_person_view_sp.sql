delimiter //		 
drop procedure if exists build_person_localoffice//
create procedure build_person_localoffice()
begin

-- This procedure is no longer used as the real time query works fast enough
-- It is kept just in case we need to revert back to this solution
--	truncate table PersonOwner;
--
--	insert into PersonOwner(id,owner,rule)
--	select p.id, get_localoffice(id, 'LO') localoffice, get_localoffice(id, 'R') which_rule
--	from Person p;
--
--  truncate table PersonView;
--
--  INSERT INTO `PersonView` (`id`, `intrepidId`, `surname`, `forenames`, `gmcNumber`, `gdcNumber`, `publicHealthNumber`, `programmeId`, `programmeName`, `programmeNumber`, `trainingNumber`, `gradeAbbreviation`, `siteCode`, `placementType`, `role`, `status`, `currentOwner`, `currentOwnerRule`, `siteId`, `gradeId`)
--  select p.id,
--         p.intrepidId,
--         cd.surname,
--         cd.forenames,
--         gmc.gmcNumber,
--         gdc.gdcNumber,
--         p.publicHealthNumber,
--         pm.programmeId,
--         pm.programmeName,
--         pm.programmeNumber,
--         pm.TrainingNumber as trainingNumber,
--         pl.gradeAbbreviation,
--         pl.siteCode,
--         pl.placementType,
--         p.role,
--         p.status,
--         lo.owner as currentOwner,
--         lo.rule as currentOwnerRule,
--         pl.siteId,
--         pl.gradeId
--  from Person p
--  left join ContactDetails cd on (cd.id = p.id)
--  left join GmcDetails gmc on (gmc.id = p.id)
--  left join GdcDetails gdc on (gdc.id = p.id)
--  left join (select pm.personid,
--            pm.programmeStartDate,
--            pm.programmeEndDate,
--                    pm.programmeId,
--            prg.programmeName,
--            prg.programmeNumber,
--                    tn.number as TrainingNumber,
--            prg.owner as lo
--         from ProgrammeMembership pm
--             join Programme prg on (prg.id = pm.programmeId)
--         join TrainingNumber tn on (tn.programmeId = pm.programmeId)) pm on (pm.personId = p.id
--                                                             and curdate() between pm.programmeStartDate and pm.programmeEndDate)
--  left join (select pl.traineeId,
--            pl.dateFrom,
--            pl.dateTo,
--            pl.gradeAbbreviation,
--            pl.siteCode,
--            pl.placementType,
--            pst.owner as lo,
--            pl.siteId,
--            pl.gradeId
--         from Placement pl
--         join Post pst on (pst.id = pl.postId) limit 1) pl on (pl.traineeId = p.id
--                             and curdate() between pl.dateFrom and pl.dateTo)
--  left join PersonOwner lo on (lo.id = p.id);

end//
delimiter ;