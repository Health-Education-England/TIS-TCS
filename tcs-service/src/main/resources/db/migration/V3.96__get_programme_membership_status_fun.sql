delimiter //
drop function if exists get_programmeMembershipStatus//
create function get_programmeMembershipStatus (programmeStartDate DATE, programmeEndDate DATE) RETURNS varchar(10) CHARSET utf8
BEGIN
  DECLARE currentDate DATE;

  IF (programmeStartDate IS NULL OR programmeEndDate IS NULL) THEN
    RETURN "PAST";
  END IF;

  SET currentDate = CURDATE();

  IF (programmeStartDate > currentDate) THEN
    RETURN "FUTURE";
  ELSE
    IF (programmeEndDate < currentDate) THEN
      RETURN "PAST";
    ELSE
      RETURN "CURRENT";
    END IF;
  END IF;

RETURN "UNKNOWN";
END//
delimiter ;
