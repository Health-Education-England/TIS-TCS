delimiter //
drop procedure if exists build_person_localoffice//
create procedure build_person_localoffice()
begin

	truncate table PersonOwner;

	insert into PersonOwner(id,owner,rule)
	select p.id, get_localoffice(id, 'LO') localoffice, get_localoffice(id, 'R') which_rule
	from Person p
	on duplicate key update
	  owner = get_localoffice(id, 'PO'),
	  rule = get_localoffice(id, 'R');

end//
delimiter ;
