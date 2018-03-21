update Gender set status = 'INACTIVE';

update Gender set status = 'CURRENT' where code in ('Male','Female','I prefer not to specify');
