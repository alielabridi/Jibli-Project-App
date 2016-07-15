<?php

$connection = mysqli_connect("localhost","root","","jibli",3306) or die("Error " . mysqli_error($connection));

if (!$connection) {
    die('Could not connect: ' . mysql_error());
}

    //read the json file contents
    $jsondata = file_get_contents('php://input');
   
    //convert json object to php associative array
    $data = json_decode($jsondata, true);
    $A_ID =      $data[0]['A_ID'];
    $A_PRODUCT =      $data[0]['A_PRODUCT'];
    $A_POSTTIME =   $data[0]['A_POSTTIME'];
    $A_PRICE =      $data[0]['A_PRICE'];
    $A_LOCATION =      $data[0]['A_LOCATION'];
    $A_COMMENT =      $data[0]['A_COMMENT'];
    $A_PROFIT =      $data[0]['A_PROFIT'];
    $BU_ID =      $data[0]['BU_ID'];
    
    //insert into mysql table
    $sql = "INSERT INTO announce VALUES
    ('$A_ID','$A_PRODUCT', '$A_POSTTIME', '$A_PRICE','$A_LOCATION','$A_COMMENT','$A_PROFIT','$BU_ID')";
    if(!mysqli_query($connection, $sql))
    {
        die('Error : ' . mysql_error());
    }
?>