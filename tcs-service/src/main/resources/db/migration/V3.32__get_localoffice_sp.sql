delimiter //
drop procedure if exists get_localoffice_sp//
create procedure get_localoffice_sp (IN v_traineeId bigint, OUT o_lo VARCHAR(255) , OUT o_which_rule varchar(2))
  get_localoffice_sp:begin

    /*P1*/
    select prg.managingDeanery, 'P1'
    into o_lo, o_which_rule
    from ProgrammeMembership pm
      join Programme prg on (prg.id = pm.programmeId)
    where pm.personid = v_traineeId
          and curdate() between pm.programmeStartDate and pm.programmeEndDate
    order by pm.programmeStartDate
    limit 1;

    if o_lo is not null then
      leave get_localoffice_sp;
    end if;

    /*P2*/
    select pst.managingLocalOffice, 'P2'
    into o_lo, o_which_rule
    from Placement pl
      join Post pst on (pst.id = pl.postId)
    where pl.traineeId = v_traineeId
          and curdate() between pl.dateFrom and pl.dateTo
    order by pl.dateFrom
    limit 1;

    if o_lo is not null then
      leave get_localoffice_sp;
    end if;

    /*P5*/
    select prg.managingDeanery, 'P5'
    into o_lo, o_which_rule
    from ProgrammeMembership pm
      join Programme prg on (prg.id = pm.programmeId)
    where pm.personid = v_traineeId
          and curdate() < pm.programmeStartDate
    order by pm.programmeStartDate
    limit 1;

    if o_lo is not null then
      leave get_localoffice_sp;
    end if;

    /*P6*/
    select pst.managingLocalOffice, 'P6'
    into o_lo, o_which_rule
    from Placement pl
      join Post pst on (pst.id = pl.postId)
    where pl.traineeId = v_traineeId
          and curdate() < pl.dateFrom
    order by pl.dateFrom
    limit 1;

    if o_lo is not null then
      leave get_localoffice_sp;
    end if;

    /*P7*/
    select prg.managingDeanery, 'P7'
    into o_lo, o_which_rule
    from ProgrammeMembership pm
      join Programme prg on (prg.id = pm.programmeId)
    where pm.personid = v_traineeId
          and curdate() > pm.programmeEndDate
    order by pm.programmeEndDate desc
    limit 1;

    if o_lo is not null then
      leave get_localoffice_sp;
    end if;

    /*P8*/
    select pst.managingLocalOffice, 'P8'
    into o_lo, o_which_rule
    from Placement pl
      join Post pst on (pst.id = pl.postId)
    where pl.traineeId = v_traineeId
          and curdate() > pl.dateTo
    order by pl.dateto desc
    limit 1;

  end//
delimiter ;
