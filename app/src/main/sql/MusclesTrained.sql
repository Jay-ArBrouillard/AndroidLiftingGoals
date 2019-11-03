CREATE TABLE MusclesTrained (
	muscles_trained_id int NOT NULL AUTO_INCREMENT,
	exercise_id int NOT NULL,
	muscle_group enum('quadriceps', 'hamstrings', 'calves', 'chest', 'back', 'shoulders', 'biceps', 'forearms'),
	PRIMARY KEY (muscles_trained_id),
	FOREIGN KEY (exercise_id) REFERENCES Exercises(exercise_id)
	ON DELETE CASCADE
);