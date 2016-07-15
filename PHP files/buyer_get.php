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
    $BU_TRANSACTION = $data[0]['BU_TRANSACTION'];
    $U_EMAIL = $data[0]['U_EMAIL'];
 

    //insert into mysql table
    $sql = "INSERT INTO buyer VALUES
    ('$BU_ID','$BU_TRANSACTION', '$U_EMAIL')";
    if(!mysqli_query($connection, $sql))
    {
        die('Error : ' . mysql_error());
    }
?>