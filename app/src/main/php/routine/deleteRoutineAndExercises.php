<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$routineId = $_GET["routineId"];

$stmt = $conn->prepare("SELECT * FROM RoutineWorkouts WHERE routine_id = ?");
$stmt->bind_param("s", $routineId);
$stmt->execute();
$routineWorkouts = $stmt->get_result();

while ($routineWorkoutRow = mysqli_fetch_object($routineWorkouts))    // fetch all rows
{
    $workoutId = $routineWorkoutRow->workout_id;
    $stmt = $conn->prepare("SELECT * FROM `WorkoutExercises` WHERE `workout_id` = ?");
    $stmt->bind_param("s", $workoutId);
    $stmt->execute();
    $WorkoutExercisesResult = $stmt->get_result(); 
    while ($workoutExerciseRow = mysqli_fetch_object($WorkoutExercisesResult))    // fetch all rows
    {
        $exercise_id = $workoutExerciseRow->exercise_id;
        $stmt = $conn->prepare("DELETE FROM `Exercises` WHERE `exercise_id` = ?");
        $stmt->bind_param("s", $exercise_id);
        $exec = $stmt->execute();
    }
}

if (mysqli_num_rows($routineWorkouts) == 0 || $exec === true)
{
    $stmt = $conn->prepare("DELETE FROM Routines WHERE routine_id = ?");
    $stmt->bind_param("s", $routineId);
    $exec = $stmt->execute();

    if ($exec === true)
    {
        echo 0;
    }
    else
    {
        echo -1;
    }
}
else
{
    echo -1;
}

$stmt->close();
$conn->close();
?>