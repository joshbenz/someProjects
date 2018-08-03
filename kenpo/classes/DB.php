<?php
//database wrapper yo...using PDO
//will use singleton patter -> main static method, allows us to get instance of database once
//connected once...use database on the fly, not have to reconnect everytime we query

class DB { 
	private static $_instance = null; //store instance of database
	private $_pdo, 			//store pdo object
		$_query, 		//last query executed
		$_error = false, 	//if there was an error
		$_results, 		//store result set
		$_count = 0;		//count of results


	private function __construct() 
	{
		try {
			$this->_pdo = new PDO('mysql:host=' . Config::get('mysql/host') . ';dbname=' . Config::get('mysql/db'), Config::get('mysql/username'), Config::get('mysql/password'));

		} catch(PDOExcetion $e) {
			die($e->getMessage());
		}
	}

	public static function getInstance() 	//saves the instance of the db connection, or returns if already set
	{
		if(!isset(self::$_instance)) {
			self::$_instance = new DB();
		}

		return self::$_instance;
	}

	public function query($sql, $params = array()) 
	{

		$this->_error = false; //gonna have to reset this to make sure it is false at the start
		
		if($this->_query = $this->_pdo->prepare($sql)) { //if the query prepares successfully
			$x = 1;
			if(count($params)) {			
				foreach($params as $param) {	//assign first param to first ?
					$this->_query->bindValue($x, $param);
					$x++;
				}
			}

			if($this->_query->execute()) { 	//execute even if there are no parameters
				$this->_results = $this->_query->fetchAll(PDO::FETCH_OBJ); //save results
				$this->_count = $this->_query->rowCount(); //keep track of how many results
			} else {
				$this->_error = true; //there was a stupid ass error
			}
		}

			return $this; //return current object we are working with 
	}
	
	public function action($action, $table, $where = array()) 
	{
		if(count($where) === 3) { 				//3 = field,operator,value
			$operators = array('=', '>', '<', '>=', '<='); //operators allowed to be used
			
			$field		= $where[0];
			$operator	= $where[1];
			$value		= $where[2];

			if(in_array($operator, $operators)) { 		//checks if specified operator is allowed
				$sql = "{$action} FROM {$table} WHERE {$field} {$operator} ?"; //general construction
				
				if(!$this->query($sql, array($value))->error()) {
					return $this;			//return current object if there is no error
				}
			}
		}

		return false;
	}

	public function get($table, $where) 
	{
		return $this->action('SELECT *', $table, $where);
	}

	public function delete($table, $where) 
	{
		return $this->action('DELETE', $table, $where);
	}

	public function results() 	//returns all resutls
	{
		return $this->_results;
	}

	public function first()		//returns first result
	{
		return $this->results()[0];
	}
	
	public function insert($table, $fields = array()) 
	{
		if(count($fields)) {
			$keys = array_keys($fields);	//keys ..eq username
			$values = '';
			$x = 1;

			foreach($fields as $field) {
				$values .= '?';
				if($x < count($fields)) {	//if not at the end, add comma
					$values .= ', ';
				}
				$x++;
			}
                        $sql = "INSERT INTO {$table} (`" . implode('`, `', $keys) . "`) VALUES ({$values})"; //create string with ` separator
			
			if(!$this->query($sql, $fields)->error()) {	//bind and execute using query() 
				return true;
			}

		}
		return false;
	}

	public function update($table, $id, $fields) 
	{
		$set = '';
		$x = 1;
	
		foreach($fields as $name => $value) {	//place in ? for later binding
			$set .= "{$name} = ?";
			if($x < count($fields)) {
				$set .= ',';
			}
			$x++;
		}	
		$sql = "UPDATE {$table} SET {$set} WHERE id = {$id}";  //the final string before binding

		if(!$this->query($sql, $fields)->error()) { 	//bind and execute using query() function above
			return true;
		}

		return false;
	}

	public function count()		//gets the number of results
	{
		return $this->_count;
	}

        public function error()
	{
		return $this->_error; //returns the error
        }
}
