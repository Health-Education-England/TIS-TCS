SELECT distinct p.id, p.trainingBodyId

FROM Post p

WHERE (p.id, p.trainingBodyId) > (:lastId,:lastTrainingBodyId)

AND p.trainingBodyId IS NOT NULL
ORDER BY p.id ASC, p.trainingBodyId ASC

LIMIT :pageSize