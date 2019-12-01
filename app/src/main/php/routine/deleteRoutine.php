<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$routineId = $_GET["routineId"];

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


$stmt->close();
$conn->close();
?>