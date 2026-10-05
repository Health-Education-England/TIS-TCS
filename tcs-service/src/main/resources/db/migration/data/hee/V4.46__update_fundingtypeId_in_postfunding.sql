SET SQL_SAFE_UPDATES = 0;

UPDATE PostFunding
SET fundingTypeId =
  CASE fundingType
    WHEN 'Tariff' THEN 1
    WHEN 'MADEL' THEN 2
    WHEN 'Trust' THEN 3
    WHEN 'Other' THEN 4
    WHEN 'Funded - Tariff' THEN 5
    WHEN 'Funded - Non-tariff' THEN 6
    WHEN 'Trust Funded' THEN 7
    WHEN 'Academic - NIHR' THEN 8
    WHEN 'Academic - HEE' THEN 9
    WHEN 'Academic - Trust' THEN 10
    WHEN 'Supernumerary' THEN 11
    WHEN 'Long Term Plan' THEN 12
    WHEN 'Military' THEN 13
    WHEN 'Academic - University' THEN 14
    WHEN '100% Tariff Funded' THEN 15
    WHEN 'Academic' THEN 16
    ELSE fundingTypeId
  END
WHERE fundingType IN (
  'Tariff',
  'MADEL',
  'Trust',
  'Other',
  'Funded - Tariff',
  'Funded - Non-tariff',
  'Trust Funded',
  'Academic - NIHR',
  'Academic - HEE',
  'Academic - Trust',
  'Supernumerary',
  'Long Term Plan',
  'Military',
  'Academic - University',
  '100% Tariff Funded',
  'Academic'
);

SET SQL_SAFE_UPDATES = 1;
