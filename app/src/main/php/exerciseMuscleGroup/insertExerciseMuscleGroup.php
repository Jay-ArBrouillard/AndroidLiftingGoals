<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$exerciseName = $_POST['exerciseName'];
$userId = $_POST['userId'];
$musclesString = $_POST['muscleGroupsList']; //Arms Back Biceps
$arr = explode(" ", $musclesString);

$stmt = $conn->prepare("INSERT INTO Exercises (`exercise_name`, `user_id`) Values (?, ?)");
$stmt->bind_param("ss", $exerciseName, $userId);
$stmt->execute();

if ($stmt->affected_rows > 0)
{
    $exerciseId = $conn->insert_id;
    echo " " . $exerciseId;

    for ($i = 0; $i < count($arr); $i++) 
    {
        $stmt = $conn->prepare("INSERT INTO MusclesTrained (`exercise_id`, muscle_group) Values (?, ?)");
        $stmt->bind_param("ss", $exerciseId, $arr[$i]);
        $stmt->execute();

        if ($stmt->affected_rows > 0)
        {
            $muscleId = $conn->insert_id;
            echo " " . $muscleId;
        }
        else
        {
            $stmt = $conn->prepare("DELETE FROM Exercises (`exercise_id` Values (?)");
            $stmt->bind_param("s", $exerciseId);
            $stmt->execute();

            echo -1;
            break;
        }
    }

}
else
{
    echo -1;
}

$stmt->close();
$conn->close();
?>