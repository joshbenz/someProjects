<?php

class User {
	private $_db, 
		$_data,
		$_sessionName,
		$_cookieName,
		$_isLoggedIn;

	public function __construct($user = null) 
	{

		$this->_db = DB::getInstance();
		$this->_sessionName = Config::get('session/session_name');
		$this->_cookieName = Config::get('remember/cookie_name');

		if(!$user) {							//grab user based off of id or username
			if(Session::exists($this->_sessionName)) {
				$user = Session::get($this->_sessionName);
				
				if($this->find($user)) {			//checks if user is logged in
					$this->_isLoggedIn = true;
				} else {
					//logout
				}
			}
		} else {
			$this->find($user);
		}
	}

	public function update($fields = array(), $id = null) 
	{

		if(!$id && $this->isLoggedIn()) {
			$id = $this->data()->id;
		}

		if(!$this->_db->update('Users', $id, $fields)) {
			throw new Exception('There was a problem updating.');
		}
	}

	public function create($fields = array()) 
	{

		if(!$this->_db->insert('Users', $fields)) {
			throw new Exception('There was a problem creating the account.');
		}
	}

	public function find($user = null) 
	{

		if($user) {
			$field = (is_numeric($user)) ? 'id' : 'username';			//if the parameter is a number then seach via id, other wise we will search via username string
			$data = $this->_db->get('Users', array($field, '=', $user));

			if($data->count()) {
				$this->_data = $data->first();					//save first result from query
				return true;
			}
		}

		return false;
	}


//Note: for using remember....generate hash, check inside database to see if user id exists in table, if it does, then it assumes we have been
//assigned a session before already....if it doesnt exist then insert it, making sure each user id only has one hash...otherwise grab the 
//database hash and id for use




	public function login($username = null, $password = null, $remember = false) 
	{

		if(!$username && !$password && $this->exists()) {
			Session::put($this->_sessionName, $this->data()->id);
		} else {

			$user = $this->find($username);								//gets all user data of the user
		
			if($user) {
				if(password_verify($password, $this->data()->password)) {		//check if passwords match
					Session::put($this->_sessionName, $this->data()->id);	 		//place id in the current session

					if($remember) {
						$hash = Hash::unique();													//generate unique hash
						$hashCheck = $this->_db->get('users_session', array('user_id', '=', $this->data()->id));				//check if it is in the database or not

						if(!$hashCheck->count()) {
							$this->_db->insert('users_session',array(									//if not, then store locally and insert hash and id into db
								'user_id'	=> $this->data()->id,
								'hash'		=> $hash
							));
						} else {
							$hash = $hashCheck->first()->hash;										//if it is, grab it from the database
						}

						Cookie::put($this->_cookieName, $hash, Config::get('remember/cookie_expiry'));


					}
					return true;
				}
			}
		}
	
		return false;
	}

	public function hasPermission($key) 					//json--javascript object
	{
		$group = $this->_db->get('Groups', array('id', '=', $this->data()->the_group));
		
		if($group->count()) {
			$permissions = json_decode($group->first()->permissions, true);		//thanks php
		}

		if($permissions[$key] == true) {
			return true;
		}

		return false;
	}	


	public function exists() 
	{
		return(!empty($this->_data)) ? true : false;
	}

	public function logout()
	{

		$this->_db->delete('users_session', array('user_id', '=', $this->data()->id));

		Session::delete($this->_sessionName);
		Cookie::delete($this->_cookieName);
	}

	public function data() 
	{
		return $this->_data;
	}

	public function isLoggedIn() 
	{
		return $this->_isLoggedIn;
	}
}
