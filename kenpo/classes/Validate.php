<?php
// check if something is being passed, check for errors and save errors

class Validate {

	private $_passed = false,	//assume it has not yet passed
		$_errors = array(),	//the errors
		$_db = null;		//database connection


	public function __construct() 
	{
		$this->_db = DB::getInstance();
	}

	public function check($source, $items = array())  					//items are for exame username password 
	{
		foreach($items as $item => $rules) {						//list through rules defined, for each of them list through the rules inside of them
			foreach($rules as $rule => $rule_value) {				//check them against the source provided and save errors as we go
			
				$value = trim($source[$item]);					//get value from the source 
				$value = escape($value);	
	
				if($rule === 'required' && empty($value)) {
					$this->addError("{$item} is required");
				} else if(!empty($value)) {
					switch($rule) {						//checks the rest of the rules
						case 'min':
							if(strlen($value) < $rule_value) {
								$this->addError("{$item} must be a minimum of {$rule_value} characters.");
							}
						break;
						case 'max':
                                                        if(strlen($value) > $rule_value) {
                                                                $this->addError("{$item} must be a maximum of {$rule_value} characters.");
                                                        }
						break;
						case 'matches':
							if($value != $source[$rule_value]) {
								$this->addError("{$rule_value} must match {$rule_value}.");
							}
						break;
						case 'unique':
							$check = $this->_db->get($rule_value, array($item,'=',$value));		//checks via database
							if($check->count()) {
								$this->addError("{$item} already exists.");
							}
						break;
						case 'alpha':
							if(!ctype_alpha($value)) {
								$this->addError("{$rule_value} must only contain alphabetic characters");
							}
						break;
						case 'alphanumeric':
							if(!ctype_alnum($value)) {
								$this->addError("{$rule_value} must only contain letters and numbers");
							}
						break;
						case 'validEmail':
							if(filter_var($value, FILTER_VALIDATE_EMAIL) != true)
								$this->addError("Must be a valid email!");
						break;
					}	
				}
			}
		}

		if(empty($this->_errors)) { 							//if the error array is empty then there are no errors so set passed to true
			$this->_passed = true;
		}

		return $this;
	}

	private function addError($error) 
	{
		$this->_errors[] = $error;
	}

	public function errors() 
	{
		return $this->_errors;
	}

	public function passed() 
	{
		return $this->_passed;
	}

}
