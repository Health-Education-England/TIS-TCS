UPDATE PostFunding
SET fundingType = 'HEE Funded - Tariff' WHERE fundingType = 'TARIFF';

UPDATE PostFunding
SET fundingType = 'HEE Funded - Non-Tariff' WHERE fundingType = 'MADEL';

UPDATE PostFunding
SET fundingType = 'Trust Funded' WHERE fundingType = 'TRUST';

UPDATE PostFunding
SET fundingType = 'Other' WHERE fundingType = 'OTHER';