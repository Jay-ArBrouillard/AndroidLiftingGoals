<?php

$db = new PDO("mysql:dbname=lifting_goals; host=localhost","brouillardjr", "Roundpanda288!!");

$username = $db->quote($_GET['username']);
$password = $db->quote($_GET['password']);

//make db connection first
$sql = "SELECT * FROM Users";
$result = $db->query($sql)
  ->fetchAll(PDO::FETCH_ASSOC);

$printMe = [];

foreach($result as $row){
  $printMe[] = $row;
}
echo json_encode($printMe);

?>