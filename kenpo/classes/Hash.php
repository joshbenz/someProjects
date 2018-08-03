<?php

//create hash based on a string
//NOTE: bcrypt automatically generates a salt

class Hash {
	public static function make($string) 
	{
		return password_hash($string, PASSWORD_BCRYPT);		//append salt to string and hash
	}

	public static function salt($length) 
	{
		return utf8_decode(mcrypt_create_iv($length, MCRYPT_DEV_URANDOM));	//generate salt
	}
	
	public static function unique() 
	{
		return self::make(uniqid());			//making a hash
	}
}
