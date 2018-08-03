<?php
class Input {
	public static function exists($type = 'post')  
	{

		switch($type) {
			case 'post':
				return(!empty($_POST)) ? true : false;	//true when post contains something
			break;
			case 'get':
				return(!empty($_GET)) ? true : false; 	//true when get has something
			break;
			default:
				return false;
			break;
		}
	}
	
	public static function get($item) 
	{

		if(isset($_POST[$item])) {		//if post has is set
			return $_POST[$item];		//return it
		} else if(isset($_GET[$item])) {	//same with get
			return $_GET[$item];
		} 
		return '';				//otherwise return nothing
	}
}
