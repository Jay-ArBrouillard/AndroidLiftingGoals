<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$userId = $_GET["userId"];

$stmt = $conn->prepare("SELECT * FROM Routines WHERE default_routine = 1 AND `user_id` = ?");
$stmt->bind_param("s", $userId);
$stmt->execute();

$routinesResult = $stmt->get_result();
if (mysqli_num_rows($routinesResult) == 0)
{
    $stmt = $conn->prepare("INSERT INTO Routines (`user_id`, routine_name, description, number_workouts, default_routine) Values (?, 'StrongLifts 5x5', 'Beginner strength training program consisting of only two alternating workouts.', 2, 1)");
    $stmt->bind_param("s", $userId);
    $stmt->execute();
    $routineId = $conn->insert_id;
    $stmt = $conn->prepare("INSERT INTO Workouts (workout_name, description, duration, number_exercises) Values ('Workout A', 'Squat, Bench, Row (Barbell)', 40, 3)");
    $stmt->execute();
    $workoutId1 = $conn->insert_id;
    $stmt = $conn->prepare("INSERT INTO RoutineWorkouts (routine_id, workout_id) Values (?, ?)");
    $stmt->bind_param("ss", $routineId, $workoutId1);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO Workouts (workout_name, description, duration, number_exercises) Values ('Workout B', 'Squat, Press, Deadlift', 30, 3)");
    $stmt->execute();
    $workoutId2 = $conn->insert_id;
    $stmt = $conn->prepare("INSERT INTO RoutineWorkouts (routine_id, workout_id) Values (?, ?)");
    $stmt->bind_param("ss", $routineId, $workoutId2);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO UserRoutines (`user_id`, routine_id) Values (?, ?)");
    $stmt->bind_param("ss", $userId, $routineId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO Exercises (exercise_name, `user_id`) Values ('Barbell Squat', ?)");
    $stmt->bind_param("s", $userId);
    $stmt->execute();
    $exerciseId = $conn->insert_id;
    $stmt = $conn->prepare("INSERT INTO WorkoutExercises(`workout_id`, `exercise_id`, `minimum_sets`, `minimum_reps`, `maximum_sets`, `maximum_reps`, `intensity`) VALUES (?,?,5,5,null,null,225)");
    $stmt->bind_param("ss", $workoutId1, $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO WorkoutExercises(`workout_id`, `exercise_id`, `minimum_sets`, `minimum_reps`, `maximum_sets`, `maximum_reps`, `intensity`) VALUES (?,?,5,5,null,null,225)");
    $stmt->bind_param("ss", $workoutId2, $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Quadriceps')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Hamstrings')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Glutes')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO Exercises (exercise_name, `user_id`) Values ('Barbell Bench', ?)");
    $stmt->bind_param("s", $userId);
    $stmt->execute();
    $exerciseId = $conn->insert_id;
    $stmt = $conn->prepare("INSERT INTO WorkoutExercises(`workout_id`, `exercise_id`, `minimum_sets`, `minimum_reps`, `maximum_sets`, `maximum_reps`, `intensity`) VALUES (?,?,5,5,null,null,135)");
    $stmt->bind_param("ss", $workoutId1, $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Chest')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Triceps')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Arms')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO Exercises (exercise_name, `user_id`) Values ('Barbell Row', ?)");
    $stmt->bind_param("s", $userId);
    $stmt->execute();
    $exerciseId = $conn->insert_id;
    $stmt = $conn->prepare("INSERT INTO WorkoutExercises(`workout_id`, `exercise_id`, `minimum_sets`, `minimum_reps`, `maximum_sets`, `maximum_reps`, `intensity`) VALUES (?,?,5,5,null,null,135)");
    $stmt->bind_param("ss", $workoutId1, $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Back')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Biceps')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Hamstrings')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO Exercises (exercise_name, `user_id`) Values ('Barbell Deadlift', ?)");
    $stmt->bind_param("s", $userId);
    $stmt->execute();
    $exerciseId = $conn->insert_id;
    $stmt = $conn->prepare("INSERT INTO WorkoutExercises(`workout_id`, `exercise_id`, `minimum_sets`, `minimum_reps`, `maximum_sets`, `maximum_reps`, `intensity`) VALUES (?,?,5,5,null,null,315)");
    $stmt->bind_param("ss", $workoutId2, $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Hamstrings')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Erector Spinae')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Glutes')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO Exercises (exercise_name, `user_id`) Values ('Barbell Overhead Press', ?)");
    $stmt->bind_param("s", $userId);
    $stmt->execute();
    $exerciseId = $conn->insert_id;
    $stmt = $conn->prepare("INSERT INTO WorkoutExercises(`workout_id`, `exercise_id`, `minimum_sets`, `minimum_reps`, `maximum_sets`, `maximum_reps`, `intensity`) VALUES (?,?,5,5,null,null,135)");
    $stmt->bind_param("ss", $workoutId2, $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Shoulders')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();
    $stmt = $conn->prepare("INSERT INTO MusclesTrained (exercise_id, muscle_group) Values (?, 'Triceps')");
    $stmt->bind_param("s", $exerciseId);
    $stmt->execute();

    echo json_encode(array());
}
else
{
    $arr = array();
    while ($row1 = mysqli_fetch_object($routinesResult))    // fetch all rows
    {
        array_push($arr, $row1);
        $routine_id = $row1->routine_id;
        $user_id = $row1->user_id;
    
        //Added
        $stmt = $conn->prepare("SELECT * FROM UserRoutines WHERE user_id = ? AND routine_id = ?");
        $stmt->bind_param("ss", $user_id, $routine_id);
        $stmt->execute();
        $userRoutineResult = $stmt->get_result();
    
        if (mysqli_num_rows($userRoutineResult) == 0)
        {
            $stmt = $conn->prepare("INSERT INTO UserRoutines (user_routine_id, user_id, routine_id) Values (null, ?, ?)");
            $stmt->bind_param("ss", $user_id, $routine_id);
            $stmt->execute();
        }
        //End Added
    
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
    
    echo json_encode($arr);
}



$stmt->close();
$conn->close();
?>