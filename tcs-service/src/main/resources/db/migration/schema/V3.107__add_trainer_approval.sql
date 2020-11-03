CREATE TABLE TrainerApproval (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    startDate Date,
    endDate Date,
    trainerType VARCHAR(255),
    approvalStatus VARCHAR(255) DEFAULT 'CURRENT',
    personId bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT fk_person_trainerapproval_id FOREIGN KEY (personId) REFERENCES Person (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE  TrainerApproval ADD INDEX(approvalStatus);
