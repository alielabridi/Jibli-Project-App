<?php

$connection = mysqli_connect("localhost","root","","jibli",3306) or die("Error " . mysqli_error($connection));
if (!$connection) {
    die('Could not connect: ' . mysql_error());
}


    //read the json file contents
    $jsondata = file_get_contents('php://input');
   
    //convert json object to php associative array
    $data = json_decode($jsondata, true);
    $u_email = $data[0]['U_EMAIL'];
    $u_lname = $data[0]['U_LNAME'];
    $u_password = $data[0]['U_PASSWORD'];
    $u_phone = $data[0]['U_PHONE'];
    

    //insert into mysql table
    $sql = "INSERT INTO users VALUES
    ('$u_email','$u_password', '$u_phone', '$u_lname')";
    if(!mysqli_query($connection, $sql))
    {
        die('Error : ' . mysql_error());
    }
    
?>