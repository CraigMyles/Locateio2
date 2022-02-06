<?php
$response = array();
include 'db_connect.php';
include 'functions.php';

//Get the input request parameters
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array

//Check for Mandatory parameters
if(isset($input['username']) && isset($input['stringLongitude']) && isset($input['stringLatitude']) && isset($input['locationTitle']) && isset($input['locationDesc']) && isset($input['extraLocationInfo']) && isset($input['rating'])){
	$username = $input['username'];
	$lat = $input['stringLongitude'];
	$lng = $input['stringLatitude'];
	$title = $input['locationTitle'];
	$description = $input['locationDesc'];
	$extraInfo = $input['extraLocationInfo'];
	$rating = $input['rating'];


	//Query to add to location table
	global $con;
	$query = "INSERT INTO locations (`locationID`, `username`, `title`, `description`, `extraInfo` ,`lat`, `lng`, `rating`, `posted`) VALUES (NULL, :username, :title, :description, :extraInfo, :lat, :lng, :rating, NOW());";
	if($query = $con->prepare($query)){
		$query->execute(array(
			':username' => $username,
			':title' => $title,
			':description' => $description,
			':extraInfo' => $extraInfo,
			':lat' => $lat,
			':lng' => $lng,
			':rating' => $rating,

		));

		$response["status"] = 0;
		$response["message"] = "Location Shared";


	}
}
else{
	$response["status"] = 2;
	$response["message"] = "Missing mandatory parameters";
}
echo json_encode($response);
?>