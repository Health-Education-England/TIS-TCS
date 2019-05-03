SELECT distinct p.id, po.employingBodyId

FROM Person p
LEFT JOIN Placement pl
  ON p.id = pl.traineeId
LEFT JOIN Post po
  ON po.id = pl.postId

WHERE (p.id, po.employingBodyId) > (:lastId,:lastEmployingBodyId)

AND po.employingBodyId IS NOT NULL
ORDER BY p.id ASC, po.employingBodyId ASC

LIMIT :pageSize