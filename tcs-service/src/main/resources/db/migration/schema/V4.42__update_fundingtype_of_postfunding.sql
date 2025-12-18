UPDATE `PostFunding`
SET `fundingType` = REPLACE(`fundingType`, 'HEE ', '')
WHERE `fundingType` IN (
  'HEE Funded - Tariff',
  'HEE Funded - Non-Tariff'
);
