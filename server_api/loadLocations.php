<?php
$response = array();
include 'db_connect.php';
include 'functions.php';

//Get the input request parameters
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array

//Check for Mandatory parameters
if(isset($input['reqData'])){

	try{
		global $con;
		$query = $con->prepare('SELECT * FROM locations ORDER BY locationID DESC');
		$query->execute();


		if ($query->rowCount() > 0) {
			
			$rows = [];
			while($row = $query->fetch(PDO::FETCH_ASSOC)){
				$rows[] = $row;
			}

			$response["status"] = 0;
			$response["message"] = $rows;
		}
		elseif ($query->rowCount() == 0) {
			$response["status"] = 1;
			$response["message"] = "Data not found.";
		}
		else{
			$response["status"] = 1;
			$response["message"] = "An unknown error has occured.";
		}

	}catch(PDOException $e){
		$error = $e->getMessage();
		$response["status"] = 1;
		$response["message"] = $error;
	}


}
else{
	$response["status"] = 2;
	$response["message"] = "Missing mandatory parameters";
}
//Display the JSON response
echo json_encode($response);
?>