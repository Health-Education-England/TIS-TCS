delimiter //
drop function if exists get_personRoleCategory//
create function get_personRoleCategory (personRole text) RETURNS varchar(500) CHARSET utf8
DETERMINISTIC
BEGIN
  DECLARE roleCategory varchar(500);
  SET roleCategory = "";

  IF (personRole like '%Educational Supervisor%') THEN
    SET roleCategory = "Educational Supervisors,";
  END IF;

  IF (personRole like '%Clinical Supervisor%') THEN
    SET roleCategory = CONCAT (roleCategory, "Clinical Supervisors,");
  END IF;

  IF (personRole like '%Leave.Approver.NonAdministrator%') THEN
      SET roleCategory = CONCAT (roleCategory, "Leave Approvers");
  END IF;

RETURN TRIM(TRAILING ',' FROM roleCategory);
END //
