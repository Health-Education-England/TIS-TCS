-- clear down the tables, remove the relationships between tables and alter previous join columns to be varchars instead of numbers

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE Curriculum;
DROP TABLE CurriculumGrade;
DROP TABLE Funding;
DROP TABLE FundingComponents;
DROP TABLE Grade;
DROP TABLE Placement;
DROP TABLE PlacementFunder;
DROP TABLE Post;
DROP TABLE PostFunding;
DROP TABLE Programme;
DROP TABLE ProgrammeMembership;
DROP TABLE Specialty;
DROP TABLE SpecialtyGroup;
DROP TABLE TariffFundingTypeFields;
DROP TABLE TariffRate;
DROP TABLE TrainingNumber;
SET FOREIGN_KEY_CHECKS = 1;


