<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$routineId = $_GET["routineId"];
$userId = $_GET["userId"];

$stmt = $conn->prepare("SELECT * FROM RoutineWorkouts WHERE routine_id = ?");
$stmt->bind_param("s", $routineId);
$stmt->execute();
$routineWorkouts = $stmt->get_result();
$exec = true;
while ($routineWorkoutRow = mysqli_fetch_object($routineWorkouts))    // fetch all rows
{
    $workoutId = $routineWorkoutRow->workout_id;
    $stmt = $conn->prepare("SELECT * FROM `WorkoutExercises` WHERE `workout_id` = ?");
    $stmt->bind_param("s", $workoutId);
    $stmt->execute();
    $WorkoutExercisesResult = $stmt->get_result(); 
    while ($workoutExerciseRow = mysqli_fetch_object($WorkoutExercisesResult))    // fetch all rows
    {
        $workoutExerciseId = $workoutExerciseRow->workout_exercise_id;
        $exercise_id = $workoutExerciseRow->exercise_id;
        $stmt = $conn->prepare("SELECT * FROM `Exercises` WHERE `exercise_id` = ?");
        $stmt->bind_param("s", $exercise_id);
        $stmt->execute();
        $exerciseResults = $stmt->get_result();

        $stmt = $conn->prepare("SELECT * FROM `UserRoutines` WHERE `user_id` = ? AND `routine_id` = ?");
        $stmt->bind_param("ss", $userId, $routineId);
        $stmt->execute();
        $userRoutineResults = $stmt->get_result();

        if (mysqli_num_rows($userRoutineResults) > 0)
        {
            $row = mysqli_fetch_object($userRoutineResults);
            $userRoutineId = $roiw->user_routine_id;

            $stmt = $conn->prepare("SELECT * FROM `UserExerciseLog` WHERE `user_routine_id` = ? AND `workout_exercise_id` = ?");
            $stmt->bind_param("ss", $userRoutineId, $workoutExerciseId);
            $stmt->execute();
            $UserExerciseLogResults = $stmt->get_result();

            if (mysqli_num_rows($UserExerciseLogResults) > 0)
            {
                $stmt = $conn->prepare("DELETE FROM `Exercises` WHERE `exercise_id` = ?");
                $stmt->bind_param("s", $exercise_id);
                $exec = $stmt->execute();
            }
        }
    }
}

if (mysqli_num_rows($routineWorkouts) == 0 || $exec == true)
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