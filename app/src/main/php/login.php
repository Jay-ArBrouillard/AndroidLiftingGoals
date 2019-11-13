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
    $row = $result->fetch_object();
    if ($row->password == $password)  //Password matches
    {
      $arr[] = $row;
      //check if its the first login
      if ($row->last_login == null)  //First Login
      {
        array_push($arr, array('Status'=>'firstLogin'));
      }
      else 
      {
        array_push($arr, array('Status'=>'NotFirstLogin'));
      }
      echo json_encode($arr);
      //Update last_login time
      date_default_timezone_set('US/Central');
      $current_date = date("Y-m-d h:i:s");
      $stmt = $conn->prepare("UPDATE Users SET last_login = '$current_date' WHERE username = '$username'");
      $stmt->execute();
    }
    else //Password doesn't match
    {
      $empty[] = array('Status'=>'PasswordIncorrect');
      echo json_encode($empty);
    }
}
else //Username doesn't exist
{
    $empty[] = array('Status'=>'UserNonExisting');
    echo json_encode($empty);
}

$stmt->close();
$conn->close();
?>