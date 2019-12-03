CREATE TABLE UserExerciseLog (
	user_exercise_log_id int NOT NULL AUTO_INCREMENT,
	user_routine_id int NOT NULL,
	workout_exercise_id int NOT NULL,
	set_performed int NOT NULL DEFAULT '0',
	reps_performed int DEFAULT '0',
	intensity double DEFAULT '0',
	rating_perceived_exertion double DEFAULT NULL,
	rest_duration double DEFAULT '0',
	tempo varchar(50) DEFAULT NULL,
	date_performed varchar(50) NOT NULL,
	PRIMARY KEY (user_exercise_log_id),
	FOREIGN KEY (user_routine_id) REFERENCES UserRoutines(user_routine_id),
	FOREIGN KEY (workout_exercise_id) REFERENCES WorkoutExercises(workout_exercise_id)
);