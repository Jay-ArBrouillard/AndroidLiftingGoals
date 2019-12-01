<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$workoutId = $_GET["workoutId"];

$stmt = $conn->prepare("DELETE FROM Workouts WHERE workout_id = ?");
$stmt->bind_param("s", $workoutId);
$exec = $stmt->execute();

if ($exec === true)
{
    echo 0;
}
else
{
    echo -1;
}


$stmt->close();
$conn->close();
?>