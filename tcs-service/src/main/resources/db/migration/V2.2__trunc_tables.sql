-- clear down the tables, remove the relationships between tables and alter previous join columns to be varchars instead of numbers

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Curriculum;
TRUNCATE TABLE Funding;
TRUNCATE TABLE FundingComponents;
TRUNCATE TABLE Placement;
TRUNCATE TABLE PlacementFunder;
TRUNCATE TABLE Post;
TRUNCATE TABLE PostFunding;
TRUNCATE TABLE Programme;
TRUNCATE TABLE ProgrammeMembership;
TRUNCATE TABLE Specialty;
TRUNCATE TABLE TariffFundingTypeFields;
TRUNCATE TABLE TariffRate;
TRUNCATE TABLE TrainingNumber;
SET FOREIGN_KEY_CHECKS = 1;
