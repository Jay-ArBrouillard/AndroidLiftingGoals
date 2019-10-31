CREATE TABLE Users (
	user_id int NOT NULL AUTO_INCREMENT,
	username varchar(50) NOT NULL,
	password varchar(50) NOT NULL,
	admin tinyint(1) NOT NULL DEFAULT '0',
	last_login	datetime DEFAULT NULL,
	PRIMARY KEY (user_id)
);