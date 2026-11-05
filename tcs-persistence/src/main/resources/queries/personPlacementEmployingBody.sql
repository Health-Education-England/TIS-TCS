SELECT p.id,
       po.employingBodyId,
       CASE
         WHEN SUM(CASE
                    WHEN (pl.dateFrom <= CURDATE() AND pl.dateTo >= CURDATE())
                      OR (pl.dateFrom > CURDATE() AND
                          pl.dateFrom <= DATE_ADD(CURDATE(), INTERVAL 13 WEEK))
                      THEN 1
                    ELSE 0
           END) > 0 THEN 'CURRENT'
         ELSE 'INACTIVE'
         END AS placementStatus

FROM Person p
LEFT JOIN Placement pl
  ON p.id = pl.traineeId
LEFT JOIN Post po
  ON po.id = pl.postId

WHERE po.employingBodyId IS NOT NULL
GROUP BY p.id, po.employingBodyId
HAVING (p.id, po.employingBodyId) > (:lastId, :lastEmployingBodyId)
ORDER BY p.id ASC, po.employingBodyId ASC
LIMIT :pageSize
