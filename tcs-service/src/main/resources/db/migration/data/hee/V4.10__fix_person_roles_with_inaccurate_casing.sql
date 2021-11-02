create temporary table Numbers as
  select 1 as n
  union select 2 as n
  union select 3 as n
  union select 4 as n
  union select 5 as n
  union select 6 as n
  union select 7 as n
  union select 8 as n
  union select 9 as n
  union select 10 as n;

-- Create table where each row has a Person id and one role
create temporary table PersonPerRole as
select id,
substring_index(substring_index(role, ',', n), ',', -1) as role
from Person
join Numbers on char_length(role) - char_length(replace(role, ',', '')) >= n - 1;

-- Update each row's role with the role taken from the reference Role table (with correct casing)
update PersonPerRole
inner join reference.Role ref_role on PersonPerRole.role = ref_role.code
set PersonPerRole.role = ref_role.code;

-- Remove duplicates
create temporary table PersonPerRoleDistinct as
select distinct * from PersonPerRole;

-- Merge rows with same id back into one, where role is concatenated
create temporary table ConcatenatedRoles as
select id, group_concat(role separator ',') as role
from PersonPerRoleDistinct
group by id;

update Person
inner join ConcatenatedRoles on Person.id = ConcatenatedRoles.id
set Person.role = ConcatenatedRoles.role;

drop table Numbers;
drop table PersonPerRole;
drop table PersonPerRoleDistinct;
drop table ConcatenatedRoles;
