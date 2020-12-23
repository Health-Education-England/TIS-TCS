ALTER TABLE `SpecialtyTypes`
    ADD COLUMN uuid varchar(36);

UPDATE `SpecialtyTypes` SET uuid =(SELECT uuid()) WHERE uuid IS NULL;

CREATE UNIQUE index idx_uuid_unique on `SpecialtyTypes` (uuid);

DROP TRIGGER IF EXISTS before_insert_SpecialtyTypes;
CREATE TRIGGER before_insert_SpecialtyTypes
    BEFORE INSERT ON `SpecialtyTypes`
    FOR EACH ROW
    SET new.uuid = ifnull(new.uuid,uuid());
