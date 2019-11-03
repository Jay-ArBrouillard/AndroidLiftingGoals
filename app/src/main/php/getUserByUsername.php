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
if (mysqli_num_rows($result) > 0)
{
    while($row = $result->fetch_object()) {
        $arr[] = $row;
      }
      echo json_encode($arr);
}
else {
    $empty = array();
    echo json_encode($empty);
}


$stmt->close();
$conn->close();
?>