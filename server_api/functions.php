<?php
$random_salt_length = 32;
/**
* Queries the database and checks whether the user already exists
* 
* @param $username
* 
* @return
*/
function userExists($username){
	//$query = "SELECT username FROM users WHERE username = ?";
	global $con;
		$query = $con->prepare('SELECT * FROM users
			WHERE users.username = :username');
		$query->execute(array(
			':username' => $username,
		));


		if ($query->rowCount() > 0) {
			return true;
		}
		else{return false;}
}
 
/**
* Creates a unique Salt for hashing the password
* 
* @return
*/
function getSalt(){
	global $random_salt_length;
	return bin2hex(openssl_random_pseudo_bytes($random_salt_length));
}
 
/**
* Creates password hash using the Salt and the password
* 
* @param $password
* @param $salt
* 
* @return
*/
function concatPasswordWithSalt($password,$salt){
	global $random_salt_length;
	if($random_salt_length % 2 == 0){
		$mid = $random_salt_length / 2;
	}
	else{
		$mid = ($random_salt_length - 1) / 2;
	}
 
	return
	substr($salt,0,$mid - 1).$password.substr($salt,$mid,$random_salt_length - 1);
 
}
?>