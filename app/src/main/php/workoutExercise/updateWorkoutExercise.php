<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$minSets = $_POST["minSets"];
$minReps = $_POST["minReps"];
$maxSets = $_POST["maxSets"];
$maxReps = $_POST["maxReps"];
$intensity = $_POST["intensity"];
$workoutExerciseId = $_POST["workoutExerciseId"];

$stmt = $conn->prepare("UPDATE WorkoutExercises SET minimum_sets = ?, minimum_reps = ?, maximum_sets = ?, maximum_reps = ?, intensity = ? WHERE workout_exercise_id = ?");
$stmt->bind_param("ssssss", $minSets, $minReps, $maxSets, $maxReps, $intensity, $workoutExerciseId);
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