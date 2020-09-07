-- Person
INSERT INTO `acl_class` (`class`)
VALUES ('com.transformuk.hee.tis.tcs.service.model.Person');

INSERT IGNORE INTO `acl_sid` (`principal`, `sid`)
VALUES (0, 'HEE');

INSERT INTO `acl_object_identity` (`object_id_class`, `object_id_identity`, `owner_sid`,
                                   `entries_inheriting`)
SELECT (
           SELECT `id`
           FROM `acl_class`
           WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Person'
       ),
       `id`,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1
FROM `Person`;

INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       0,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Person'
      );

INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       1,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       2,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Person'
      );


-- Programme
INSERT INTO `acl_class` (`class`)
VALUES ('com.transformuk.hee.tis.tcs.service.model.Programme');

INSERT IGNORE INTO `acl_sid` (`principal`, `sid`)
VALUES (0, 'HEE');

INSERT INTO `acl_object_identity` (`object_id_class`, `object_id_identity`, `owner_sid`,
                                   `entries_inheriting`)
SELECT (
           SELECT `id`
           FROM `acl_class`
           WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Programme'
       ),
       `id`,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1
FROM `Programme`;

INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       0,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Programme'
      );

INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       1,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       2,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Programme'
      );


-- ProgrammeMembership
INSERT INTO `acl_class` (`class`)
VALUES ('com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership');

INSERT IGNORE INTO `acl_sid` (`principal`, `sid`)
VALUES (0, 'HEE');

INSERT INTO `acl_object_identity` (`object_id_class`, `object_id_identity`, `owner_sid`,
                                   `entries_inheriting`)
SELECT (
           SELECT `id`
           FROM `acl_class`
           WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership'
       ),
       `id`,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1
FROM `ProgrammeMembership`;

INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       0,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership'
      );

INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       1,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       2,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership'
      );


-- Post
INSERT INTO `acl_class` (`class`)
VALUES ('com.transformuk.hee.tis.tcs.service.model.Post');

INSERT IGNORE INTO `acl_sid` (`principal`, `sid`)
VALUES (0, 'HEE');

INSERT INTO `acl_object_identity` (`object_id_class`, `object_id_identity`, `owner_sid`,
                                   `entries_inheriting`)
SELECT (
           SELECT `id`
           FROM `acl_class`
           WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Post'
       ),
       `id`,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1
FROM `Post`;

INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       0,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Post'
      );

INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       1,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       2,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Post'
      );


-- Placement
INSERT INTO `acl_class` (`class`)
VALUES ('com.transformuk.hee.tis.tcs.service.model.Placement');

INSERT IGNORE INTO `acl_sid` (`principal`, `sid`)
VALUES (0, 'HEE');

INSERT INTO `acl_object_identity` (`object_id_class`, `object_id_identity`, `owner_sid`,
                                   `entries_inheriting`)
SELECT (
           SELECT `id`
           FROM `acl_class`
           WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Placement'
       ),
       `id`,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1
FROM `Placement`;

INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       0,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Placement'
      );

INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       1,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       2,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.tcs.service.model.Placement'
      );
