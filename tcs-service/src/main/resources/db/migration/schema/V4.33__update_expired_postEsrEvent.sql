SET SQL_SAFE_UPDATES = 0;
UPDATE PostEsrEvent
SET status = 'RECONCILED_EXP'
WHERE id NOT IN
(
  select DISTINCT id FROM (
	  SELECT pee.id, latestEvent.positionNumber, pee.postId, pee.status, latestEvent.latestEventDateTime FROM
	  (
      SELECT positionNumber, MAX(eventDateTime) latestEventDateTime
      FROM PostEsrEvent
      WHERE status = 'RECONCILED'
      GROUP BY positionNumber
      ) latestEvent
      JOIN PostEsrEvent pee ON latestEvent.positionNumber = pee.positionNumber
        AND latestEvent.latestEventDateTime = pee.eventDateTime
        AND pee.status = 'RECONCILED'
  ) AS tmp
);
SET SQL_SAFE_UPDATES = 1;
