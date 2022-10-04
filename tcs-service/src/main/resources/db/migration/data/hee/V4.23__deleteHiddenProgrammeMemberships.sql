delete from ProgrammeMembership
where uuid in("c844896b-c443-4363-b67e-f5d5a2109b3d", "cf7417c6-230d-4cdf-814e-5402cf85050a",
 "d8729460-a6ff-471f-89ba-0224e7d4044f", "e6a2f14c-d83d-11ec-9eb2-0638a616fc76",
  "eaad8f17-d83d-11ec-9eb2-0638a616fc76", "eadae647-d83d-11ec-9eb2-0638a616fc76",
   "eb74f516-d83d-11ec-9eb2-0638a616fc76");

--  near duplicates found using:
--  select personId, programmeId, count(*), group_concat(programmeStartDate separator ' vs. '), group_concat(programmeEndDate separator ' vs. ')
--  from ProgrammeMembership
--  where personId in(148944, 305133,170518,47571,261085,269060,302971)
--  group by personId, programmeId
--  having count(*) > 1
