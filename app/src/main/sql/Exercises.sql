CREATE TABLE Exercises (
	exercise_id int NOT NULL AUTO_INCREMENT,
	exercise_name varchar(255) NOT NULL,
	muscle_group enum('quadriceps', 'hamstrings', 'calves', 'chest', 'back', 'shoulders', 'biceps', 'forearms', 'trapezius', 'abs') NOT NULL,
	PRIMARY KEY (exercise_id)
);