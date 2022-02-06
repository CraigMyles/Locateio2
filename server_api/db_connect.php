<?php
//db connection
try {
	$db_host = '';
	$db_dbname = '';
	$db_username = '';
	$db_password = '';

	$con = new PDO('mysql:host='.$db_host.';dbname='.$db_dbname.';charset=utf8',$db_username,$db_password);

} catch (PDOException $e) {
	$error  = "Error!: " . $e->getMessage() . "<br/>";
	die("Connection Failed");
}
?>