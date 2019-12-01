<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$userId = $_GET["userId"];

$stmt = $conn->prepare("INSERT INTO Routines (user_id, routine_name, description, number_workouts, default_routine) Values (?, 'Untitled Routine', 'Untitled Description', 0, 0)");
$stmt->bind_param("s", $userId);
$stmt->execute();

if ($stmt->affected_rows > 0)
{
    $ids = $conn->insert_id;
    $routineId = $conn->insert_id;

    $stmt = $conn->prepare("INSERT INTO UserRoutines (user_id, routine_id) Values (?, ?)");
    $stmt->bind_param("ss", $userId, $routineId);
    $stmt->execute();

    if ($stmt->affected_rows > 0)
    {
        $ids .= "," . $conn->insert_id;
        echo $ids;
    }
    else
    {
        $stmt = $conn->prepare("DELETE FROM Routines WHERE routine_id = ?");
        $stmt->bind_param("s", $routineId);
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