<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$routineId = $_POST["routineId"];
$routineName = $_POST["routineName"];
$routineDesc = $_POST["routineDesc"];

$stmt = $conn->prepare("UPDATE Routines SET routine_name = ?, description = ? WHERE routine_id = ?");
$stmt->bind_param("sss", $routineName, $routineDesc, $routineId);
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