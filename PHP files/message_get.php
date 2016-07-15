<?php

$connection = mysqli_connect("localhost","root","","jibli",3306) or die("Error " . mysqli_error($connection));
if (!$connection) {
    die('Could not connect: ' . mysql_error());
}


    //read the json file contents
    $jsondata = file_get_contents('php://input');
   
    //convert json object to php associative array
    $data = json_decode($jsondata, true);
    $BU_ID = $data[0]['BU_ID'];
    $M_MESSAGE = $data[0]['M_MESSAGE'];
    $M_To = $data[0]['M_To'];
    $M_DATE = $data[0]['M_DATE'];

    //insert into mysql table
    $sql = "INSERT INTO message VALUES
    ('$BU_IDl','$M_MESSAGE', '$M_To', '$M_DATE')";
    if(!mysqli_query($connection, $sql))
    {
        die('Error : ' . mysql_error());
    }
?>