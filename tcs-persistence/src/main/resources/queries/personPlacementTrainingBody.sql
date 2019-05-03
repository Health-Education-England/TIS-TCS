SELECT distinct p.id, po.trainingBodyId

FROM Person p
LEFT JOIN Placement pl
  ON p.id = pl.traineeId
LEFT JOIN Post po
  ON po.id = pl.postId

WHERE (p.id, po.trainingBodyId) > (:lastId,:lastTrainingBodyId)

AND po.trainingBodyId IS NOT NULL
ORDER BY p.id ASC, po.trainingBodyId ASC

LIMIT :pageSize