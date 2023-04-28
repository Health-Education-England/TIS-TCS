CREATE TABLE ConditionsOfJoining (
  `programmeMembershipUuid` varchar(36) NOT NULL,
  `signedAt` datetime(3) NOT NULL,
  `version` enum('GG9') NOT NULL,
  PRIMARY KEY (`programmeMembershipUuid`),
  CONSTRAINT `fk_conditions_of_joining_programme_membership_uuid` FOREIGN KEY (`programmeMembershipUuid`) REFERENCES `ProgrammeMembership` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
