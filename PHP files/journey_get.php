<?php

$connection = mysqli_connect("localhost","root","","jibli",3306) or die("Error " . mysqli_error($connection));
if (!$connection) {
    die('Could not connect: ' . mysql_error());
}

    //read the json file contents
    $jsondata = file_get_contents('php://input');
   
    //convert json object to php associative array
    $data = json_decode($jsondata, true);
    $J_DATE =      $data[0]['J_DATE'];
    $J_DESTINATION =   $data[0]['J_DESTINATION'];
    $J_DEPARTURE =      $data[0]['J_DEPARTURE'];
    $BR_ID =      $data[0]['BR_ID'];
    
    //insert into mysql table
/*    $sql = "INSERT INTO journey VALUES
    (null,$J_DATE, $J_DESTINATION, $J_DEPARTURE,$BR_ID)";*/
    $sql = "INSERT INTO `journey` (`J_ID`, `J_DATE`, `J_DESTINATION`, `J_DEPARTURE`, `BR_ID`) VALUES (NULL,'$J_DATE','$J_DESTINATION', '$J_DEPARTURE', '$BR_ID');";
    if(!mysqli_query($connection, $sql))
    {
        die('Error : ' . mysqli_error($connection));
    }
?>