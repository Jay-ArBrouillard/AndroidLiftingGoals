CREATE TABLE RoutineWorkouts (
	routine_workout_id int NOT NULL AUTO_INCREMENT,
	routine_id int NOT NULL,
	workout_id int NOT NULL,
	PRIMARY KEY (routine_workout_id),
	FOREIGN KEY (routine_id) REFERENCES Routines(routine_id),
	FOREIGN KEY (workout_id) REFERENCES Workouts(workout_id)
);