UPDATE `RightToWork`
SET `permitToWork` = 'UK ancestry'
WHERE `permitToWork` = 'ANCESTRY_VISA';

UPDATE `RightToWork`
SET `permitToWork` = 'Dependant/Spouse visa'
WHERE `permitToWork` = 'DEPENDENT_OF_HMSP'
   OR `permitToWork` = 'DEPENDENT_OF_HSMP'
   OR `permitToWork` = 'DEPENDENT_OF_WORK_PERMIT'
   OR `permitToWork` = 'SPOUSE_OF_HSMP_HOLDER';

UPDATE `RightToWork`
SET `permitToWork` = 'EU EEA National (started prior to June 2021)'
WHERE `permitToWork` = 'EC_EEA_NATIONAL';

UPDATE `RightToWork`
SET `permitToWork` = 'Evidence of entitlement'
WHERE `permitToWork` = 'EVIDENCE_OF_ENTITLEMENT';

UPDATE `RightToWork`
SET `permitToWork` = 'HSMP'
WHERE `permitToWork` = 'HSMP';

UPDATE `RightToWork`
SET `permitToWork` = 'Indefinite Leave to Remain'
WHERE `permitToWork` = 'INDEFINATE_LEAVE'
   OR `permitToWork` = 'INDEFINITE_LEAVE'
   OR `permitToWork` = 'INDEFINITE_LEAVE_TO_REMAIN';

UPDATE `RightToWork`
SET `permitToWork` = 'Limited LTR'
WHERE `permitToWork` = 'LIMITED_LTR';

UPDATE `RightToWork`
SET `permitToWork` = 'Other'
WHERE `permitToWork` = 'OTHER';

UPDATE `RightToWork`
SET `permitToWork` = 'Permit-free'
WHERE `permitToWork` = 'PERMIT_FREE';

UPDATE `RightToWork`
SET `permitToWork` = 'Post graduate visa'
WHERE `permitToWork` = 'POSTGRADUATE_VISA';

UPDATE `RightToWork`
SET `permitToWork` = 'Refugee in the UK'
WHERE `permitToWork` = 'REFUGEE_DOCTOR'
   OR `permitToWork` = 'REFUGEE_IN_THE_UK';

UPDATE `RightToWork`
SET `permitToWork` = 'Resident permit'
WHERE `permitToWork` = 'RESIDENT_PERMIT';

UPDATE `RightToWork`
SET `permitToWork` = 'Spouse of EEA National (started prior to June 2021)'
WHERE `permitToWork` = 'SPOUSE_OF_EEA_NATIONAL';

UPDATE `RightToWork`
SET `permitToWork` = 'Tier 1'
WHERE `permitToWork` = 'TIER_1';

UPDATE `RightToWork`
SET `permitToWork` = 'Tier 2'
WHERE `permitToWork` = 'TIER_2'
   OR `permitToWork` = 'TIER_2_POINTS_BASED_SYSTEM';

UPDATE `RightToWork`
SET `permitToWork` = 'Tier 4'
WHERE `permitToWork` = 'TIER_4'
   OR `permitToWork` = 'TIER_4_GENERALS_STUDENT'
   OR  `permitToWork` = 'STUDENT_VISA';

UPDATE `RightToWork`
SET `permitToWork` = 'Tier 5'
WHERE `permitToWork` = 'TIER_5';

UPDATE `RightToWork`
SET `permitToWork` = 'TWES MTI'
WHERE `permitToWork` = 'TWES_MTI';

UPDATE `RightToWork`
SET `permitToWork` = 'UK/Irish National'
WHERE `permitToWork` = 'UK_NATIONAL'
   OR `permitToWork` IS NULL;

UPDATE `RightToWork`
SET `permitToWork` = 'Unspecified'
WHERE `permitToWork` = 'UNSPECIFIED';

UPDATE `RightToWork`
SET `permitToWork` = 'Work permit'
WHERE `permitToWork` = 'WORK_PERMIT';

UPDATE `RightToWork`
SET `permitToWork` = 'Yes'
WHERE `permitToWork` = 'YES';
