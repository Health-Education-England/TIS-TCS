CREATE TABLE Absence (
   id bigint(20) NOT NULL AUTO_INCREMENT,
   personId bigint(20) NOT NULL,
   startDate datetime NULL DEFAULT NULL,
   endDate datetime NULL DEFAULT NULL,
   durationInDays int NULL DEFAULT NULL,
   absenceAttendanceId varchar(10) NOT NULL,
   PRIMARY KEY (`id`),
   CONSTRAINT fk_absence_person_id FOREIGN KEY (personId) REFERENCES Person (id) ON DELETE CASCADE,
   CONSTRAINT abs_atten_id UNIQUE (absenceAttendanceId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



