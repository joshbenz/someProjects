<?php

//cross site request forgery protection
//1 generate a token
//2 check if a token is valid and exists and then delete it
//generate a token with each refresh of the page and only that page then knows
//and a user cannot direct you to that page becuase that token will always be checked
//want name of token to be the same though --specified in the GLOBALS in init.php
//attacker wont know token becuase it is generated uniquely by the user
ini_set ("display_errors", "1");


class Token {
	public static function generate() 
	{
		return Session::put(Config::get('session/token_name'), md5(uniqid()));
	}

	public static function check($token) 
	{	
			
		$tokenName = Config::get('session/token_name');

		if(Session::exists($tokenName) && $token === Session::get($tokenName)) {		//if token = token of session then delete it
			Session::delete($tokenName);
			return true;
		}

		return false;
	}
}

