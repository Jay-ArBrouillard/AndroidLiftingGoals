<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$workoutId = $_POST["workoutId"];
$workoutName = $_POST["workoutName"];
$workoutDesc = $_POST["workoutDesc"];
$duration = $_POST["duration"];
$numExercises = $_POST["numExercises"];

$stmt = $conn->prepare("UPDATE Workouts SET workout_name = ?, description = ?, duration = ?, number_exercises = ? WHERE workout_id = ?");
$stmt->bind_param("sssss", $workoutName, $workoutDesc, $duration, $numExercises, $workoutId);
$stmt->execute();

if ($stmt->affected_rows > 0)
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