<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    echo 'ConnectionError';
}

$content = $_POST['recordsList'];
$json = json_decode($content, true);
$remove = array();
foreach ($json as $key => $value) {
    $userId = $value["userId"];
    $exerciseId = $value["exerciseId"];
    $intensity = $value["intensity"];
    $repsPerformed = $value["repsPerformed"];
    $date = $value["date"];

    //record id doesnt matter
    $stmt = $conn->prepare("SELECT * FROM Records WHERE exercise_id = ? AND reps_performed = ?");
    $stmt->bind_param("ss", $exerciseId, $repsPerformed);
    $stmt->execute();

    $recordResult = $stmt->get_result();

    if (mysqli_num_rows($recordResult) > 0)
    {
        $row = mysqli_fetch_object($recordResult);
        $recordId = $row->record_id;
        $dbIntensity = $row->intensity;
        echo $recordId;
        
        if ($ntensity > $dbIntensity)
        {
            $stmt = $conn->prepare("UPDATE Records SET `intensity`= ?,`date_performed`= ? WHERE record_id = ?");
            $stmt->bind_param("sss", $intensity, $date, $recordId);
            $stmt->execute();
        }
    }
    else
    {
        $stmt = $conn->prepare("INSERT INTO Records(`user_id`, `exercise_id`, `intensity`, `reps_performed`, `date_performed`) VALUES (?, ?, ?, ?, ?)");
        $stmt->bind_param("sssss", $userId, $exerciseId, $intensity, $repsPerformed, $date);
        $stmt->execute();

        if ($stmt->affected_rows > 0)
        {
            $id = $conn->insert_id;
            array_push($remove, $id);
            echo " " . $id;
        }
        else
        {
            for ($i = 0; $i < count($remove); $i++) 
            {
                $stmt = $conn->prepare("DELETE FROM Records WHERE record_id = ?");
                $stmt->bind_param("s", $remove[$i]);
                $stmt->execute();
            }
            echo -1;
            break;
        }
    }
}

$stmt->close();
$conn->close();
?>