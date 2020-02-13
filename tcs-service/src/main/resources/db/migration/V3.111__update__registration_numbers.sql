-- update emptystring to NULL
update GmcDetails
set gmcNumber = NULL
where gmcNumber REGEXP '^\s*$';

update GdcDetails
set gdcNumber = NULL
where gdcNumber REGEXP '^\s*$';

update Person
set publicHealthNumber = NULL
where publicHealthNumber REGEXP '^\s*$';

-- remove whitespaces
update GmcDetails
set gmcNumber = replace(gmcNumber, ' ', '')
where gmcNumber like '% %';

update GdcDetails
set gdcNumber = replace(gdcNumber, ' ', '')
where gdcNumber like '% %';

update Person
set publicHealthNumber = replace(publicHealthNumber, ' ', '')
where publicHealthNumber like '% %';
