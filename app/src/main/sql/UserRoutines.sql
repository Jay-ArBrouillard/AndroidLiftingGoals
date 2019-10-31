CREATE TABLE UserRoutines (
	user_routine_id int NOT NULL AUTO_INCREMENT,
	user_id int NOT NULL,
	password varchar(50) NOT NULL,
	creation_date datetime NOT NULL,
	last_performed datetime DEFAULT NULL,
	PRIMARY KEY (user_routine_id),
	FOREIGN KEY (user_id) REFERENCES Users(user_id)
	ON DELETE CASCADE
);