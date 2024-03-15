ALTER TABLE `Post` ADD COLUMN `fundingStatus` varchar(255) NOT NULL;

UPDATE Post p
LEFT JOIN (SELECT f.postId, MAX(f.endDate) AS endDate FROM PostFunding f GROUP BY f.postId) latestpf ON p.id = latestpf.postId
SET p.fundingStatus = 'CURRENT'
WHERE latestpf.endDate >= current_date()
OR latestpf.endDate IS NULL;

UPDATE Post p
LEFT JOIN (SELECT f.postId, MAX(f.endDate) AS endDate FROM PostFunding f GROUP BY f.postId) latestpf ON p.id = latestpf.postId
SET p.fundingStatus = 'INACTIVE'
WHERE latestpf.endDate < current_date();