CREATE TABLE UserRoutines (
	user_routine_id int NOT NULL AUTO_INCREMENT,
	user_id int NOT NULL,
	routine_id int NOT NULL,
	PRIMARY KEY (user_routine_id),
	FOREIGN KEY (user_id) REFERENCES Users(user_id)
	ON DELETE CASCADE,
	FOREIGN KEY (routine_id) REFERENCES Routines(routine_id)
	ON DELETE CASCADE
);