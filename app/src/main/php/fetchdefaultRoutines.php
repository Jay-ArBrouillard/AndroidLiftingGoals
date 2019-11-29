<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


$stmt = $conn->prepare("SELECT * FROM Routines WHERE default_routine = 1");
$stmt->execute();

$arr = array();

$routinesResult = $stmt->get_result();
if (mysqli_num_rows($routinesResult) > 0)   //1 row
{
    while ($row1 = mysqli_fetch_object($routinesResult))    // fetch all rows
    {
        array_push($arr, $row1);
        $routine_id = $row1->routine_id;
        $stmt = $conn->prepare("SELECT * FROM RoutineWorkouts WHERE routine_id = ?");
        $stmt->bind_param("s", $routine_id);
        $stmt->execute();

        $routineWorkoutsResult = $stmt->get_result();
        while ($row2 = mysqli_fetch_object($routineWorkoutsResult))    // fetch all rows
        {
            array_push($arr, $row2);
            $workout_id = $row2->workout_id;
            $stmt = $conn->prepare("SELECT * FROM Workouts WHERE workout_id = ?");
            $stmt->bind_param("s", $workout_id);
            $stmt->execute();
            $WorkoutsResult = $stmt->get_result();  
            $row3 = mysqli_fetch_object($WorkoutsResult);//Only returns one row
            array_push($arr, $row3);

            $stmt = $conn->prepare("SELECT * FROM WorkoutExercises WHERE workout_id = ?");
            $stmt->bind_param("s", $workout_id);
            $stmt->execute();
            $WorkoutExercisesResult = $stmt->get_result(); 
            while ($row4 = mysqli_fetch_object($WorkoutExercisesResult))    // fetch all rows
            {
                array_push($arr, $row4);
                $exercise_id = $row4->exercise_id;
                $stmt = $conn->prepare("SELECT * FROM Exercises WHERE exercise_id = ?");
                $stmt->bind_param("s", $exercise_id);
                $stmt->execute();
                $ExerciseResults = $stmt->get_result();  
                $row5 = mysqli_fetch_object($ExerciseResults);//Only returns one row
                array_push($arr, $row5);
            }
        }

    }


}

echo json_encode($arr);

$stmt->close();
$conn->close();
?>