<?php
$connect = mysqli_connect('localhost', 'root', '', 'projectandroid');
mysqli_query($connect, "SET NAMES 'utf8'");
$name = $_POST['name'];
$phone = $_POST['phone'];
$sql = "INSERT INTO profile ( name, phone) VALUES('quan1', '15315')";
mysqli_query($connect, $sql);
?>