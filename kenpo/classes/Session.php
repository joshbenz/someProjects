<?php
//flashing--flash a mesg to the user and have it disappear on the next refresh

class Session {
	
	public static function exists($name) 
	{
		return (isset($_SESSION[$name])) ? true : false; 	//if token is set then return true
	}	

	public static function delete($name) 
	{

		if(self::exists($name)) {
			unset($_SESSION[$name]);			//if token exists then unset it
		}
	}

	public static function put($name, $value) 
	{
		return $_SESSION[$name] = $value;		//return the value of the session
	}

	public static function get($name) 
	{
		return $_SESSION[$name];
	}

	public static function flash($name, $string = '') 
	{

		if(self::exists($name)) {		//if session exists
			$session = self::get($name);	//get the session
			self::delete($name);
			return $session;		//delete session
		} else {
			self::put($name, $string); 	//return the value of the session as the session...or something
		}
	}
}
