SELECT distinct p.id, p.employingBodyId

FROM Post p

WHERE (p.id, p.employingBodyId) > (:lastId,:lastEmployingBodyId)

AND p.employingBodyId IS NOT NULL
ORDER BY p.id ASC, p.employingBodyId ASC

LIMIT :pageSize