UPDATE ContactDetails
SET email = REPLACE(TRIM(email), UNHEX('C2A0'),'')
WHERE email LIKE CONCAT('%', UNHEX('C2A0'), '%') OR TRIM(email) <> email;
