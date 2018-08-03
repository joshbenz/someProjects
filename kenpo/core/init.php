<?php

session_start();

//global config settings for easy access
$GLOBALS['config'] = array(
	'mysql'		=> array(
				'host' 		=> '127.0.0.1', 
				'username' 	=> 'root',
				'password' 	=> 'jB092713313327',
				'db' 		=> 'Kenpo'
			),
	'remember' 	=> array(
				'cookie_name' 	=> 'hash',
				'cookie_expiry' => 604800
			),
	'session' 	=> array(
				'session_name'	=> 'user',
				'token_name'	=> 'token'
			)
);

//autoloading classes when required using standart php library
spl_autoload_register(function($class) 
{
	require_once 'classes/' . $class . '.php';
});

//not a class here so cant use above fake
require_once('functions/sanitize.php');


if(Cookie::exists(Config::get('remember/cookie_name')) && !Session::exists(Config::get('session/session_name'))) {	//check for a session and a cookie in session
	$hash = Cookie::get(Config::get('remember/cookie_name'));							//get the hash from session
	$hashCheck = DB::getInstance()->get('users_session', array('hash', '=', $hash));				//get hash from db

	if($hashCheck->count()) {											//check if the match
		$user = new User($hashCheck->first()->user_id);
		$user->login();
	}

}
