DROP TRIGGER IF EXISTS before_insert_Curriculum;
CREATE TRIGGER before_insert_Curriculum
BEFORE INSERT ON `Curriculum`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_Specialty;
CREATE TRIGGER before_insert_Specialty
BEFORE INSERT ON `Specialty`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_SpecialtyGroup;
CREATE TRIGGER before_insert_SpecialtyGroup
BEFORE INSERT ON `SpecialtyGroup`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());
