-- ProgrammeMembership

delimiter |

create trigger trg_person_owner_insert_ProgrammeMembership
after insert on `ProgrammeMembership`
for each row
begin

	delete from `PersonOwner` where `id` = NEW.`personId`;

	insert into `PersonOwner` (`id`, `owner`, `rule`)
	select p.`id`, get_localoffice(`id`, 'LO') localoffice, get_localoffice(`id`, 'R') which_rule
	from `Person` p where `id` = NEW.`personId`;

end;
|

create trigger trg_person_owner_update_ProgrammeMembership
after update on `ProgrammeMembership`
for each row
begin

	delete from `PersonOwner` where `id` = NEW.`personId`;

	insert into `PersonOwner` (`id`, `owner`, `rule`)
	select p.`id`, get_localoffice(`id`, 'LO') localoffice, get_localoffice(`id`, 'R') which_rule
	from `Person` p where `id` = NEW.`personId`;

end;
|

-- Placement

create trigger trg_person_owner_insert_Placement
after insert on `Placement`
for each row
begin

	delete from `PersonOwner` where `id` = NEW.`traineeId`;

	insert into `PersonOwner` (`id`, `owner`, `rule`)
	select p.`id`, get_localoffice(`id`, 'LO') localoffice, get_localoffice(`id`, 'R') which_rule
	from `Person` p where `id` = NEW.`traineeId`;

end;
|

create trigger trg_person_owner_update_Placement
after update on `Placement`
for each row
begin

	delete from `PersonOwner` where `id` = NEW.`traineeId`;

	insert into `PersonOwner` (`id`, `owner`, `rule`)
	select p.`id`, get_localoffice(`id`, 'LO') localoffice, get_localoffice(`id`, 'R') which_rule
	from `Person` p where `id` = NEW.`traineeId`;

end;
|
