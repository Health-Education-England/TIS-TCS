delimiter //		 
drop function if exists get_localoffice//
create function get_localoffice (v_traineeId INT, v_result_type char(2))
returns varchar(255)
begin
	declare l_localoffice varchar(255);
	declare l_rule varchar(2);
	
	call get_localoffice_sp(v_traineeId, l_localoffice, l_rule);
	
	if (v_result_type = 'LO') THEN
		return l_localoffice;
	else
		return l_rule;
	end if;
end//
delimiter ;