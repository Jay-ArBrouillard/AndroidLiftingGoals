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

$stmt = $conn->prepare("SELECT * FROM Users WHERE username = ?");
$stmt->bind_param("s", $username);
$stmt->execute();

$result = $stmt->get_result();
if (mysqli_num_rows($result) == 1)  //username exists
{
    echo "UserExists";
}
else //Username doesn't exist
{
    // prepare and bind
    $stmt = $conn->prepare("INSERT INTO Users (username, password) VALUES (?, ?)");
    $stmt->bind_param("ss", $username, $password);

    if ($stmt->execute() == TRUE)
    {
      $id = $conn->insert_id;
      echo strval($id);;
    }
    else {
      echo "error";
    }

}

$stmt->close();
$conn->close();
?>