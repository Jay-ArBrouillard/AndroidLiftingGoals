<?php
require_once 'credentials.php';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$stmt = $conn->prepare("SELECT * FROM Routines WHERE default_routine = 1");
$stmt->execute();

$routinesResult = $stmt->get_result();
if (mysqli_num_rows($routinesResult) > 0) 
{

    while ($row1 = mysqli_fetch_object($routinesResult))
    {
        //$arr[]; //= $row1;
        $routine_id = $row1->routine_id;
        $stmt = $conn->prepare("SELECT * FROM RoutineWorkouts WHERE routine_id = ?");
        $stmt->bind_param("s", $routine_id);
        $stmt->execute();

        $routineWorkoutsResult = $stmt->get_result();
        if (mysqli_num_rows($routineWorkoutsResult) > 0) 
        {
            while ($row2 = mysqli_fetch_object($routineWorkoutsResult))
            {
                $workout_id = $row2->workout_id;
                $stmt = $conn->prepare("SELECT * FROM Workouts WHERE workout_id = ?");
                $stmt->bind_param("s", $workout_id);
                $stmt->execute();
    
                $workoutResults = $stmt->get_result();
                if (mysqli_num_rows($workoutResults) > 0) 
                {
                    while ($row3 = mysqli_fetch_object($workoutResults))
                    {
                        $arr[] = $row3;
                    }
                    print json_encode($arr);
    
                }

            }

        }

    }
    /*
    for ($i = 0; $i < sizeof($arr); $i++)   //Iterating 1,3 routine ids
    {
        $routine_id = $arr[$i]->routine_id;
        $stmt = $conn->prepare("SELECT * FROM RoutineWorkouts WHERE routine_id = ?");
        $stmt->bind_param("s", $routine_id);
        $stmt->execute();
    
        $routineWorkoutsResult = $stmt->get_result();
        if (mysqli_num_rows($routineWorkoutsResult) > 0) 
        {
            $workoutKeys [] = array();  //Tested works
    
            while ($row = mysqli_fetch_object($routineWorkoutsResult))
            {
                array_push($workoutKeys, $row->workout_id);

            }
            $stmt = $conn->prepare("SELECT * FROM Workouts");
            $stmt->execute();

            $workoutResults = $stmt->get_result();
            if (mysqli_num_rows($workoutResults) > 0) 
            {
                while ($row = mysqli_fetch_object($workoutResults))
                {
                    $test = json_decode($arr, true);
                    array_push($arr, $row);
                    $test = json_encode($arr);
                }

                print json_encode($test);
            }
            else 
            {
                print "workouts query failed";
            }
        }
        else
        {
            print "routineWorkouts Query failed";
        }
    }*/


    //echo json_encode($arr);

}


$stmt->close();
$conn->close();
?>