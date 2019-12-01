<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$workoutId = $_GET["workoutId"];
$exerciseId = $_GET["exerciseId"];

$stmt = $conn->prepare("INSERT INTO WorkoutExercises (workout_id, exercise_id) Values (?, ?)");
$stmt->bind_param("ss", $workoutId, $exerciseId);
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