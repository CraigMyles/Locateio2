<?php
$response = array();
include 'db_connect.php';
include 'functions.php';
 
//Get the input request parameters
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array
 
//Check for Mandatory parameters
if(isset($input['username']) && isset($input['password']) && isset($input['fName']) && isset($input['lName']) && isset($input['email'])){
	$username = $input['username'];
	$password = $input['password'];
	$fName = $input['fName'];
	$lName = $input['lName'];
	$email = $input['email'];
	
	//Check if user already exist
	if(!userExists($username)){
 
		//Get a unique Salt
		//$salt         = getSalt();
		
		//Generate a unique password Hash
		//$passwordHash = password_hash(concatPasswordWithSalt($password,$salt),PASSWORD_DEFAULT);
		
		//Query to register new user
		global $con;
		$query = "INSERT INTO users (users.userID, users.username, users.password, users.fName, users.lName, users.email) VALUES (NULL, :username, :password, :fName, :lName, :email)";
		if($query = $con->prepare($query)){
			$query->execute(array(
			':username' => $username,
			':password' => $password,
			':fName' => $fName,
			':lName' => $lName,
			':email' => $email,

		));

		
		




			$response["status"] = 0;
			$response["message"] = "User created";
			//$stmt->close();
		}
	}
	else{
		$response["status"] = 1;
		$response["message"] = "User exists";
	}
}
else{
	$response["status"] = 2;
	$response["message"] = "Missing mandatory parameters";
}
echo json_encode($response);
?>