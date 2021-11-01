update Person person
inner join reference.Role role ON person.role = role.code
set person.role = role.code;
