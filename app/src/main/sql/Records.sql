CREATE TABLE Records (
	record_id int NOT NULL AUTO_INCREMENT,
	user_id int NOT NULL,
	exercise_id int NOT NULL,
	intensity double DEFAULT 0,
	reps_performed int DEFAULT 0,
	PRIMARY KEY (record_id),
	FOREIGN KEY (user_id) REFERENCES Users(user_id),
	FOREIGN KEY (exercise_id) REFERENCES Exercises(exercise_id)
);