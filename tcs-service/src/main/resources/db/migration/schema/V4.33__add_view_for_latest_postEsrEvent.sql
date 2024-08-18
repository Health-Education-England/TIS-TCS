ALTER TABLE tcs.PostEsrEvent
ADD INDEX PostEvnt_byPosition (positionId ASC, positionNumber ASC, eventDateTime ASC);

CREATE VIEW v_PostEsrLatestEvent AS
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
