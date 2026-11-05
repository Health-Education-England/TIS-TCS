SELECT distinct p.id, po.trainingBodyId,
  CASE
    WHEN (pl.dateFrom <= CURDATE() AND pl.dateTo >= CURDATE())
      OR (pl.dateFrom > CURDATE() AND pl.dateFrom <= DATE_ADD(CURDATE(), INTERVAL 13 WEEK))
      THEN 'CURRENT'
    ELSE 'INACTIVE'
    END AS placementStatus

FROM Person p
LEFT JOIN Placement pl
  ON p.id = pl.traineeId
LEFT JOIN Post po
  ON po.id = pl.postId

WHERE (p.id, po.trainingBodyId) > (:lastId,:lastTrainingBodyId)

AND po.trainingBodyId IS NOT NULL
ORDER BY p.id ASC, po.trainingBodyId ASC

LIMIT :pageSize
