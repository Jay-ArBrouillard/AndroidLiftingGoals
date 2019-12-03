<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$username = $_GET['username'];

$stmt = $conn->prepare("SELECT * FROM Users WHERE username = ?");
$stmt->bind_param("s", $username);
$stmt->execute();

$result = $stmt->get_result();
if (mysqli_num_rows($result) == 1)  //username exists
{
    $row = $result->fetch_object();
    //Update last_login to null
    $stmt = $conn->prepare("UPDATE Users SET last_login = null WHERE username = '$username'");
    $stmt->execute();
    echo json_encode(array('Status'=>'Success'));
}
else //Username doesn't exist
{
    echo json_encode(array('Status'=>'Error'));
}

$stmt->close();
$conn->close();
?>