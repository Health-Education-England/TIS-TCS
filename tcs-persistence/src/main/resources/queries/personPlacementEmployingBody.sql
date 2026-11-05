SELECT distinct p.id, po.employingBodyId,
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

WHERE (p.id, po.employingBodyId) > (:lastId,:lastEmployingBodyId)

AND po.employingBodyId IS NOT NULL
ORDER BY p.id ASC, po.employingBodyId ASC

LIMIT :pageSize
