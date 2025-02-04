SET SQL_SAFE_UPDATES = 0;

update Post p
set p.fundingStatus = 'INACTIVE';

ALTER TABLE `Post` MODIFY `fundingStatus` varchar(255) not null default 'INACTIVE';

UPDATE Post p
LEFT JOIN (SELECT f.postId, MAX(f.endDate) AS endDate FROM PostFunding f GROUP BY f.postId) latestPf ON p.id = latestPf.postId
SET p.fundingStatus = 'CURRENT'
WHERE latestPf.endDate >= current_date();

UPDATE Post p
INNER JOIN (SELECT f.postId, f.endDate FROM PostFunding f WHERE startDate is not null and endDate IS NULL) latestPf ON p.id = latestPf.postId
SET p.fundingStatus = 'CURRENT';

SET SQL_SAFE_UPDATES = 1;
