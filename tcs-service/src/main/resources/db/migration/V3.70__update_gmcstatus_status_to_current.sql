
INSERT INTO GmcStatus (code, label)
SELECT 'Temporary registration with Licence', 'Temporary registration with Licence' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM GmcStatus
      WHERE code='Temporary registration with Licence')
LIMIT 1;

INSERT INTO GmcStatus (code, label)
SELECT 'Temporary registration without a Licence', 'Temporary registration without a Licence' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM GmcStatus
      WHERE code='Temporary registration without a Licence')
LIMIT 1;

INSERT INTO GmcStatus (code, label)
SELECT 'Not Registered – Erased for false declaration', 'Not Registered – Erased for false declaration' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM GmcStatus
      WHERE code='Not Registered – Erased for false declaration')
LIMIT 1;

INSERT INTO GmcStatus (code, label)
SELECT 'Not Registered – Erased for fraudulent application', 'Not Registered – Erased for fraudulent application' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM GmcStatus
      WHERE code='Not Registered – Erased for fraudulent application')
LIMIT 1;

INSERT INTO GmcStatus (code, label)
SELECT 'Not Registered – Provisional registration expired', 'Not Registered – Provisional registration expired' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM GmcStatus
      WHERE code='Not Registered – Provisional registration expired')
LIMIT 1;

update GmcStatus set status = 'INACTIVE';

update GmcStatus set status = 'CURRENT' where code in ('Not Registered - Deceased',
'Not Registered - Erased after Fitness to Practise panel hearing',
'Not Registered - Having relinquished registration',
'Provisionally registered with Licence',
'Provisionally registered without a Licence',
'Registered with Licence',
'Registered without a Licence',
'Suspended',
'Temporary registration with Licence',
'Temporary registration without a Licence',
'Not Registered – Erased for false declaration',
'Not Registered – Erased for fraudulent application',
'Not Registered – Provisional registration expired');