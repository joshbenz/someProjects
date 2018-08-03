<?php

class Config {

	public static function get($path = null) 
	{
		if($path) {
			$config = $GLOBALS['config'];
			$path = explode('/', $path);

							//for each one if it is set, the set it to config
							//eg...does mysql exist in config, if it does set mysql to config
								// if host exists in config(which is now mysql) then return
								//127.0.0.1
			foreach($path as $bit) {
				if(isset($config[$bit])) {
					$config = $config[$bit];
				}
			}
			return $config;
		}
		return false; //if nothing exists then do nothing...literally nothing
	}
}

