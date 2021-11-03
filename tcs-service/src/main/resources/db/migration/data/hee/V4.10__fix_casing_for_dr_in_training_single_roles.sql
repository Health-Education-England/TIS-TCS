UPDATE Person person
SET person.role = 'DR in Training'
WHERE UPPER(person.role) = 'DR IN TRAINING';
