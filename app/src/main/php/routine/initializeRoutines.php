<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$userId = $_GET['userId'];

$stmt;
$userRoutinesResult;
if ($userId == '1') //is Admin can see all routines
{
    $stmt = $conn->prepare("SELECT * FROM UserRoutines");
    $stmt->execute();
    $userRoutinesResult = $stmt->get_result();
}
else
{
    $stmt = $conn->prepare("SELECT * FROM UserRoutines WHERE user_id = ?");
    $stmt->bind_param("s", $userId);
    $stmt->execute();
    $userRoutinesResult = $stmt->get_result();
}


$arr = array();
while ($row0 = mysqli_fetch_object($userRoutinesResult))
{
    array_push($arr, $row0);
    $routineNum = $row0->routine_id;
    $stmt = $conn->prepare("SELECT * FROM Routines WHERE routine_id = ?");
    $stmt->bind_param("s", $routineNum);
    $stmt->execute();

    $routinesResult = $stmt->get_result();
    while ($row1 = mysqli_fetch_object($routinesResult))    // fetch all rows
    {
        $row1->routine_name = utf8_encode($row1->routine_name);
        $row1->description = utf8_encode($row1->description);
        $row1->routine_name = mb_convert_encoding($row1->routine_name, "HTML-ENTITIES");
        $row1->description = mb_convert_encoding($row1->description, "HTML-ENTITIES");

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
            $row3->workout_name = utf8_encode($row3->workout_name);
            $row3->description = utf8_encode($row3->description);
            $row3->workout_name = mb_convert_encoding($row3->workout_name, "HTML-ENTITIES");
            $row3->description = mb_convert_encoding($row3->description, "HTML-ENTITIES");
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

                $stmt = $conn->prepare("SELECT * FROM MusclesTrained WHERE exercise_id = ?");
                $stmt->bind_param("s", $exercise_id);
                $stmt->execute();

                $MuscleResults = $stmt->get_result();  
                while ($row6 = mysqli_fetch_object($MuscleResults))    // fetch all rows
                {
                    array_push($arr, $row6);
                }
            }
        }

    }
    
}

echo json_encode($arr);

$stmt->close();
$conn->close();
?>