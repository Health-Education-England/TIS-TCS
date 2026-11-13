SELECT p.id,
       po.employingBodyId,
       CASE
         WHEN SUM(CASE
                    WHEN (pl.dateFrom <= CURDATE() AND pl.dateTo >= CURDATE())
                      OR (pl.dateFrom > CURDATE() AND
                          pl.dateFrom <= DATE_ADD(CURDATE(), INTERVAL 13 WEEK))
                      THEN 1
                    ELSE 0
           END) > 0 THEN 'ACTIVE'
         ELSE 'INACTIVE'
         END AS status

FROM Person p
LEFT JOIN Placement pl
  ON p.id = pl.traineeId
LEFT JOIN Post po
  ON po.id = pl.postId

WHERE po.employingBodyId IS NOT NULL
  AND p.id >= :lastId
  AND (p.id > :lastId OR po.employingBodyId > :lastEmployingBodyId)
GROUP BY p.id, po.employingBodyId
ORDER BY p.id ASC, po.employingBodyId ASC
LIMIT :pageSize
