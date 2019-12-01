<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$routineId = $_GET["routineId"];

$stmt = $conn->prepare("INSERT INTO Workouts (workout_name, description, duration, number_exercises) Values ('Untitled Workout', 'Untitled Description', 0, 0)");
$stmt->execute();

if ($stmt->affected_rows > 0)
{
    $ids = $conn->insert_id;
    $workoutId = $conn->insert_id;

    $stmt = $conn->prepare("INSERT INTO RoutineWorkouts (routine_id, workout_id) Values (?, ?)");
    $stmt->bind_param("ss", $routineId, $workoutId);
    $stmt->execute();

    if ($stmt->affected_rows > 0)
    {
        $ids .= "," . $conn->insert_id;
        echo $ids;
    }
    else
    {
        $stmt = $conn->prepare("DELETE FROM Workouts WHERE workout_id = ?");
        $stmt->bind_param("s", $workoutId);
        $stmt->execute();
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