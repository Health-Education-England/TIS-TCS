ALTER TABLE `Curriculum`
ADD COLUMN uuid varchar(36);

UPDATE `Curriculum` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_Curriculum
BEFORE INSERT ON `Curriculum`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `Curriculum` (uuid);

ALTER TABLE `Specialty`
ADD COLUMN uuid varchar(36);

UPDATE `Specialty` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_Specialty
BEFORE INSERT ON `Specialty`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `Specialty` (uuid);

ALTER TABLE `SpecialtyGroup`
ADD COLUMN uuid varchar(36);

UPDATE `SpecialtyGroup` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_SpecialtyGroup
BEFORE INSERT ON `SpecialtyGroup`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `SpecialtyGroup` (uuid);
