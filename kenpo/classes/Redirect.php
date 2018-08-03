<?php

class Redirect {
	public static function to($location = null) 
	{

		if($location) {					//if there is a location					
			if(is_numeric($location)) {		//will do this if a number is the location
				
				switch($location) {
					case 404:
						header('HTTP/1.0 404 Not Found');		//generates 404 error in the browser
						include('includes/errors/404.php');		//does not redirect! but displays the correct page
						exit();
					break;
				}

			}
			header('Location: ' . $location);	
			exit();
		}
	}
}
