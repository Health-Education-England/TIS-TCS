SET SQL_SAFE_UPDATES = 0;

UPDATE PostFunding
SET fundingTypeId =
  CASE fundingType
    WHEN 'Other' THEN 4
    WHEN 'Funded - Tariff' THEN 5
    WHEN 'Funded - Non-tariff' THEN 6
    WHEN 'Trust Funded' THEN 7
    WHEN 'Academic - NIHR' THEN 8
    WHEN 'Academic - HEE' THEN 9
    WHEN 'Academic - Trust' THEN 10
    WHEN 'Supernumerary' THEN 11
    WHEN 'Academic - University' THEN 12
    WHEN '100% Tariff Funded' THEN 13
    ELSE fundingTypeId
  END
WHERE fundingType IN (
  'Other',
  'Funded - Tariff',
  'Funded - Non-tariff',
  'Trust Funded',
  'Academic - NIHR',
  'Academic - HEE',
  'Academic - Trust',
  'Supernumerary',
  'Academic - University',
  '100% Tariff Funded'
);

SET SQL_SAFE_UPDATES = 1;
