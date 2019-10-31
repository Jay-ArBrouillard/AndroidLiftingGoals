CREATE TABLE Routines (
	routine_id int NOT NULL AUTO_INCREMENT,
	user_id int NOT NULL,
	routine_name varchar(255) NOT NULL,
	description varchar(255) DEFAULT NULL,
	PRIMARY KEY (routine_id)
);