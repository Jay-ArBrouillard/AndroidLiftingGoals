CREATE TABLE WorkoutExercises (
	workout_exercise_id int NOT NULL AUTO_INCREMENT,
	workout_id int NOT NULL,
	exercise_id int NOT NULL,
	minimum_sets int DEFAULT NULL,
	minimum_reps int DEFAULT NULL,
	maximum_sets int DEFAULT NULL,
	maximum_reps int DEFAULT NULL,
	PRIMARY KEY (workout_exercise_id),
	FOREIGN KEY (workout_id) REFERENCES Workouts(workout_id),
	FOREIGN KEY (exercise_id) REFERENCES Exercises(exercise_id)
);