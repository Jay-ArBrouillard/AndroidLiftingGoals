CREATE TABLE Workouts (
	workout_id int NOT NULL AUTO_INCREMENT,
	workout_name varchar(255) NOT NULL,
	description varchar(255) DEFAULT NULL,
	duration double DEFAULT '0',
	PRIMARY KEY (workout_id)
);