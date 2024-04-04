UPDATE Post p
INNER JOIN (SELECT f.postId, f.endDate, f.startDate FROM PostFunding f WHERE endDate IS NULL AND startDate IS NULL) latestpf ON p.id = latestpf.postId
SET p.fundingStatus = 'INACTIVE';