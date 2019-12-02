CREATE TABLE Events (
	event_id int NOT NULL AUTO_INCREMENT,
	event varchar(255) NOT NULL,
    user_id int(11) NOT NULL,
	time varchar(50) NOT NULL,
	date varchar(50) NOT NULL,
	month varchar(50) NOT NULL,
	year varchar(50) NOT NULL,
	full_date varchar(255) NOT NULL,
	exercises varchar(255) NOT NULL,
	PRIMARY KEY (event_id),
	FOREIGN KEY (user_id) REFERENCES Users(user_id)
);