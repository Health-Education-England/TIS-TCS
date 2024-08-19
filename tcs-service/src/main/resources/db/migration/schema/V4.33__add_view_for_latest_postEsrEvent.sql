-- create index if not exists
SET @index_exist = (SELECT COUNT(1) indexExists FROM INFORMATION_SCHEMA.STATISTICS
WHERE table_schema=DATABASE() AND table_name='PostEsrEvent' AND index_name='PostEvnt_byPosition');

SET @preparedStatement = IF(@index_exist <= 0, 'ALTER TABLE PostEsrEvent ADD INDEX PostEvnt_byPosition (positionId ASC, positionNumber ASC, eventDateTime ASC)',
                             'select 1');
prepare stmt from @preparedStatement;
execute stmt;

-- create the view
CREATE OR REPLACE VIEW v_PostEsrLatestEvent AS
SELECT pe.*
FROM (
	SELECT positionId, positionNumber, MAX(eventDateTime) lastDateTime
	FROM PostEsrEvent
	GROUP BY positionId, positionNumber
) latest
INNER JOIN PostEsrEvent pe
  ON latest.positionId = pe.positionId
  AND latest.positionNumber = pe.positionNumber
  AND latest.lastDateTime = pe.eventDateTime;
