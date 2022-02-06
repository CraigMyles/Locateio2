<?php
$response = array();
include 'db_connect.php';
include 'functions.php';

//Get the input request parameters
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array

//Check for Mandatory parameters
if(isset($input['username']) && isset($input['password'])){
	$username = $input['username'];
	$password = $input['password'];
	//$query    = "SELECT full_name,password_hash, salt FROM member WHERE username = ?";


	try{
		global $con;
		$query = $con->prepare('SELECT * FROM users
			WHERE users.username = :username
			AND users.password = :password');
		$query->execute(array(
			':username' => $username,
			':password' => $password,
		));


		if ($query->rowCount() > 0) {
			$response["status"] = 0;
			$response["message"] = "Login successful";

	 		//get first name
			// $q = $conn->query("SELECT `$columnName` FROM `$tableName` WHERE $prop='".$value."'");
			// $f = $q->fetch();
			// $result = $f[$columnName];

			$query = $con->prepare('SELECT fName FROM users
				WHERE users.username = :username
				AND users.password = :password');
			$query->execute(array(
				':username' => $username,
				':password' => $password,
			));

			$fName = $query->fetch();
			$fName = $fName['fName'];





			$response["first_name"] = $fName;
		}
		elseif ($query->rowCount() == 0) {
			$response["status"] = 1;
			$response["message"] = "Username / Password does not match.";
		}
		else{
			$response["status"] = 1;
			$response["message"] = "An unknown login error has occured. Row Count: " .$query->rowCount();
		}

	}catch(PDOException $e){
		$error = $e->getMessage();
		$response["status"] = 1;
		$response["message"] = $error;
	}


	// if($stmt = $con->prepare($query)){
	// 	$stmt->bind_param("s",$username);
	// 	$stmt->execute();
	// 	$stmt->bind_result($fullName,$passwordHashDB,$salt);
	// 	if($stmt->fetch()){
	// 		//Validate the password
	// 		if(password_verify(concatPasswordWithSalt($password,$salt),$passwordHashDB)){
	// 			$response["status"] = 0;
	// 			$response["message"] = "Login successful";
	// 			$response["full_name"] = $fullName;
	// 		}
	// 		else{
	// 			$response["status"] = 1;
	// 			$response["message"] = "Invalid username and password combination";
	// 		}
	// 	}
	// 	else{
	// 		$response["status"] = 1;
	// 		$response["message"] = "Invalid username and password combination";
	// 	}

	// 	$stmt->close();
	// }
}
else{
	$response["status"] = 2;
	$response["message"] = "Missing mandatory parameters";
}
//Display the JSON response
echo json_encode($response);
?>