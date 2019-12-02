<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$content = $_POST['exerciseLogsList'];
$json = json_decode($content, true);
$remove = array();
foreach ($json as $key => $value) {
    //$userExerciseLogId = $value["userExerciseLogId"];
    $userRoutineId = $value["userRoutineId"];
    $workoutExerciseId = $value["workoutExerciseId"];
    $setPerformed = $value["setPerformed"];
    $repsPerformed = $value["repsPerformed"];
    $intensity = $value["intensity"];
    $rpe = $value["rpe"];
    $restDuration = $value["restDuration"];
    $tempo = $value["tempo"];
    $date = $value["date"];

    
    $stmt = $conn->prepare("INSERT INTO UserExerciseLog (user_routine_id, workout_exercise_id, set_performed, reps_performed, intensity, rating_perceived_exertion, rest_duration, tempo, date_performed) Values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("sssssssss", $userRoutineId, $workoutExerciseId, $setPerformed, $repsPerformed, $intensity, $rpe, $restDuration, $tempo, $date);
    $stmt->execute();

    if ($stmt->affected_rows > 0)
    {
        $id = $conn->insert_id;
        array_push($remove, $id);
        echo " " . $id;
    }
    else
    {
        for ($i = 0; $i < count($remove); $i++) 
        {
            $stmt = $conn->prepare("DELETE FROM UserExerciseLog WHERE user_exercise_log_id = ?");
            $stmt->bind_param("s", $remove[$i]);
            $stmt->execute();
        }
        echo -1;
    }

    $stmt->close();
}

$conn->close();
?>