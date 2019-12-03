<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$event = $_POST["event"];
$userId = $_POST["userId"];
$time = $_POST["time"];
$date = $_POST["date"];
$month = $_POST["month"];
$year = $_POST["year"];
$fullDate = $_POST["fullDate"];
$exercises = $_POST["exercises"];

$stmt = $conn->prepare("INSERT INTO Events (`event`,`user_id`,`time`,`date`,`month`,`year`,`full_date`,`exercises`) Values (?, ?, ?, ?, ?, ?, ?, ?)");
$stmt->bind_param("ssssssss", $event, $userId, $time, $date, $month, $year, $fullDate, $exercises);
$stmt->execute();

if ($stmt->affected_rows > 0)
{
    echo $conn->insert_id;
}
else
{
    echo -1;
}

$stmt->close();
$conn->close();
?>