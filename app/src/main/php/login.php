<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$username = $_GET['username'];
$password = $_GET['password'];

$stmt = $conn->prepare("SELECT * FROM Users WHERE username = ? AND password = ?");
$stmt->bind_param("ss", $username, $password);
$stmt->execute();

$result = $stmt->get_result();
while($row = $result->fetch_object()) {
  $arr[] = $row;
}
echo json_encode($arr);

$stmt->close();
$conn->close();
?>