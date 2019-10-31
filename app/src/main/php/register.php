<?php
/*
$db = new PDO("mysql:dbname=lifting_goals; host=localhost","brouillardjr", "Roundpanda288!!");

$username = $_GET['username'];
$password = $_GET['password'];

//make db connection first
$sql = "INSERT INTO Users (username, password) VALUES ('$username', '$password')";
$result = $db->exec($sql);

echo "$result";
*/

$servername = "localhost";
$username = "brouillardjr";
$password = "Roundpanda288!!";
$dbname = "lifting_goals";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$username = $_GET['username'];
$password = $_GET['password'];

// prepare and bind
$stmt = $conn->prepare("INSERT INTO Users (username, password) VALUES (?, ?)");
$stmt->bind_param("ss", $username, $password);

if ($stmt->execute() == TRUE)
{
  echo "1";
}
else {
  echo "0";
}

$stmt->close();
$conn->close();
?>