CREATE TABLE WorkoutExercises (
	workout_exercise_id int NOT NULL AUTO_INCREMENT,
	routine_workout_id int NOT NULL,
	exercise_id int NOT NULL,
	minimum_sets int DEFAULT NULL,
	minimum_reps int DEFAULT NULL,
	maximum_sets int DEFAULT NULL,
	maximum_reps int DEFAULT NULL,
	PRIMARY KEY (workout_exercise_id),
	FOREIGN KEY (routine_workout_id) REFERENCES RoutineWorkouts(routine_workout_id),
	FOREIGN KEY (exercise_id) REFERENCES Exercises(exercise_id)
);