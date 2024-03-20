ALTER TABLE `Post` MODIFY `fundingStatus` varchar(255);

SET SQL_SAFE_UPDATES = 0;
UPDATE Post p
LEFT JOIN PostFunding pf ON p.id = pf.postId
SET p.fundingStatus = "INACTIVE"
WHERE pf.id IS NULL;
SET SQL_SAFE_UPDATES = 1;