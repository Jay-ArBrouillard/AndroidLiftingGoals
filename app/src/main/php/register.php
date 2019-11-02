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

// prepare and bind
$stmt = $conn->prepare("INSERT INTO Users (username, password) VALUES (?, ?)");
$stmt->bind_param("ss", $username, $password);

if ($stmt->execute() == TRUE)
{
  echo "1";
}
else {
  echo "0";
}

$stmt->close();
$conn->close();
?>