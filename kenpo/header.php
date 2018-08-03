<?php
require_once('core/init.php');

if(Input::exists()) {
	if(isset($_POST['signin'])) {
		if(Token::check(Input::get('token'))) {  
	
			$validate = new Validate();
			$validation = $validate->check($_POST, array(

				'username' => array('required' => true),
				'password' => array('required' => true)
			));

			if($validation->passed()) {
				$user = new User();
				$remember = (Input::get('rememberme') === 'on') ? true : false;
				$login = $user->login(Input::get('username'), Input::get('password'), $remember);

				if($login) 
					Redirect::to('index.php');
				else
					echo '<p>Sorry, login failed.</p>';
			} else {
				foreach($validation->errors() as $error) 
					echo $error, '<br>';
			}	
		} 
	}


	if(isset($_POST['confirmsignup'])) {
		//if(Token::check(Input::get('token'))) {	//??
			$validate = new Validate();
			$validation = $validate->check($_POST, array(
				'email'		=> array(
					'required'	=> true,
					'min'		=> 5,
					'validEmail'	=> true
				),
				'username'	=> array(
					'required'	=> true,
					'min'		=> 2,
					'max'		=> 20,
					'alphanumeric'	=> true,
					'unique'	=> 'Users'
				),
				
				'password'	=> array(
					'required'	=> true,
					'min'		=> 6
				),
				'reenterpassword'=>array(
					'required'	=> true,
					'matches'	=> 'password'
				), 
				'human'		=> array(
					'required'	=> true
				)
			));

			if($validation->passed()) {
				Session::flash('success', 'You registered successfully!');	//flash msg named success, msg to flash
				$user = new User();

				try {
					$user->create(array(
						'username'	=> Input::get('username'), 
						'password'	=> Hash::make(Input::get('password')),
						'email'		=> Input::get('email')
					));

					Session::flash('home', 'You have been registered!');
					Redirect::to('index.php');

				} catch(Exception $e) {
					die($e->getMessage());	//shud prob just die
				} 
			} else {
				foreach($validation->errors() as $error) 
					echo $error, '<br>';
			}
		//}  //end token check
	} 
}

?>



<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="..">
  <meta name="author" content="..">
  <title>Index</title>

  <!-- core CSS -->
  <link href="css/bootstrap.min.css" rel="stylesheet">
  <link href="css/font-awesome.min.css" rel="stylesheet">
  <link href="css/animate.min.css" rel="stylesheet">
  <link href="css/prettyPhoto.css" rel="stylesheet">
  <link href="css/main.css" rel="stylesheet">
  <link href="css/responsive.css" rel="stylesheet">
  <!--[if lt IE 9]>
  <script src="js/html5shiv.js"></script>
  <script src="js/respond.min.js"></script>
  <![endif]-->
  <link rel="shortcut icon" href="images/ico/favicon.ico">
  <link rel="apple-touch-icon-precomposed" sizes="144x144" href="images/ico/apple-touch-icon-144-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="114x114" href="images/ico/apple-touch-icon-114-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="72x72" href="images/ico/apple-touch-icon-72-precomposed.png">
  <link rel="apple-touch-icon-precomposed" href="images/ico/apple-touch-icon-57-precomposed.png">
</head><!--/head-->

<body class="homepage">

  <header id="header">
    <div class="top-bar">
      <div class="container">
        <div class="row">
          <div class="col-sm-6 col-xs-4">
            <div class="top-number"><p><i class="fa fa-phone-square"></i>  +0123 456 70 90</p></div>
          </div>
          <div class="col-sm-6 col-xs-8">
            <div class="social">
              <ul class="social-share">
                <li><a href="#"><i class="fa fa-facebook"></i></a></li>
                <li><a href="#"><i class="fa fa-twitter"></i></a></li>
                <li><a href="#"><i class="fa fa-linkedin"></i></a></li>
                <li><a href="#"><i class="fa fa-dribbble"></i></a></li>
                <li><a href="#"><i class="fa fa-skype"></i></a></li>
              </ul>
              <div class="search">
                <form role="form">
                  <input type="text" class="search-form" autocomplete="off" placeholder="Search">
                  <i class="fa fa-search"></i>

		<?php //checks if logged in and displays stuff
			if(Session::exists('home'))
				echo Session::flash('home');		//not really needed..i put it here for later reference

			$user = new User();
				
			if($user->isLoggedIn()) {
				//DO STUFF LIKE SHOW USERS NAME AND RANK IDK
		?>

			<!-- logout thing or change password buttons or something -->

		<?php } else { ?>

                  <button class="btn" href="#signup" data-toggle="modal" data-target=".bs-modal-sm"><b>Sign In/Register</b></button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div><!--/.container-->
    </div><!--/.top-bar-->

    <nav class="navbar navbar-inverse" role="banner">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="index.html"><img src="images/logo.pn" alt="logo"></a>
        </div>

        <div class="collapse navbar-collapse navbar-right">
          <ul class="nav navbar-nav">
            <li class="active"><a href="index.html">Home</a></li>

            <li><a href="contact.php">Contact</a></li>
          </ul>
        </div>
      </div><!--/.container-->
    </nav><!--/nav-->
    <!-- Modal -->
    <div class="modal fade bs-modal-sm" id="myModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-sm">
        <div class="modal-content">
          <br>
          <div class="bs-example bs-example-tabs">
            <ul id="myTab" class="nav nav-tabs">
              <li class="active"><a href="#signin" data-toggle="tab">Sign In</a></li>
              <li class=""><a href="#signup" data-toggle="tab">Register</a></li>
              <li class=""><a href="#why" data-toggle="tab">Why?</a></li>
            </ul>
          </div>
          <div class="modal-body">
            <div id="myTabContent" class="tab-content">
              <div class="tab-pane fade in" id="why">
                <p>We need this information so that you can receive access to the site and its content. Rest assured your information will not be sold, traded, or given to anyone.</p>
                <p></p><br> Please contact <a mailto:href="#"></a>sifu@kenpokicks.com</a> for any other inquiries.</p>
              </div>
              <div class="tab-pane fade active in" id="signin">
                <form class="form-horizontal" action="" method="post">
                  <fieldset>

                    <!-- Sign In Form -->
                    <!-- Text input-->
                    <div class="control-group">
                      <label class="control-label" for="userid">Alias:</label>
                      <div class="controls">
                        <input required="" id="username" name="username" type="text" class="form-control" placeholder="JoeSixpack" class="input-medium" required="">
                      </div>
                    </div>

                    <!-- Password input-->
                    <div class="control-group">
                      <label class="control-label" for="password">Password:</label>
                      <div class="controls">
                        <input required="" id="password" name="password" class="form-control" type="password" placeholder="********" class="input-medium">
                      </div>
                    </div>

                    <!-- Multiple Checkboxes (inline) -->
                    <div class="control-group">
                      <label class="control-label" for="rememberme"></label>
                      <div class="controls">
                        <label class="checkbox inline" for="rememberme">
                          <input type="checkbox" name="rememberme" id="rememberme" value="Remember me">
                          Remember me
                        </label>
                      </div>
                    </div>

                   <!-- Token -->
			<input type="hidden" name="token" id="token" value="<?php echo Token::generate(); ?>">
                    
		    <!-- Button -->
                    <div class="control-group">
                      <label class="control-label" for="signin"></label>
                      <div class="controls">
                        <button id="signin" name="signin" class="btn btn-success">Sign In</button>
                      </div>
                    </div>
                  </fieldset>
                </form>
              </div>
              <div class="tab-pane fade" id="signup">
                <form class="form-horizontal" action="" method="post">
                  <fieldset>

                    <!-- Sign Up Form -->
                    <!-- Text input-->
                    <div class="control-group">
                      <label class="control-label" for="email">Email:</label>
                      <div class="controls">
                        <input id="email" name="email" class="form-control" type="text" placeholder="JoeSixpack@sixpacksrus.com" class="input-large" required="">
                      </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                      <label class="control-label" for="username">Alias:</label>
                      <div class="controls">
                        <input id="username" name="username" class="form-control" type="text" placeholder="JoeSixpack" class="input-large" required="">
                      </div>
                    </div>

                    <!-- Password input-->
                    <div class="control-group">
                      <label class="control-label" for="password">Password:</label>
                      <div class="controls">
                        <input id="password" name="password" class="form-control" type="password" placeholder="********" class="input-large" required="">
                        <em>1-8 Characters</em>
                      </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                      <label class="control-label" for="reenterpassword">Re-Enter Password:</label>
                      <div class="controls">
                        <input id="reenterpassword" class="form-control" name="reenterpassword" type="password" placeholder="********" class="input-large" required="">
                      </div>
                    </div>

                    <!-- Multiple Radios (inline) -->
                    <br>
                    <div class="control-group">
                      <label class="control-label" for="humancheck">Humanity Check:</label>
                      <div class="controls">
                        <label class="radio inline" for="humancheck-0">
                          <input type="radio" name="humancheck-0" id="humancheck-0" value="robot" checked="checked">I'm a Robot</label>
                          <label class="radio inline" for="humancheck-1">
                            <input type="radio" name="humancheck-1" id="human" value="human">I'm Human</label>
                          </div>
                        </div>



                        <!-- Button -->
                        <div class="control-group">
                          <label class="control-label" for="confirmsignup"></label>
                          <div class="controls">
                            <button id="confirmsignup" name="confirmsignup" type="submit" class="btn btn-success">Sign Up</button>
                          </div>
                        </div>
                      </fieldset>
                    </form>
                  </div>
                </div>
              </div>

              <div class="modal-footer">
                <center>
                  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </center>
              </div>

		<?php } //not sure if this is the right place..i am going based off of the spacing?>
            </div>
          </div>
        </div>
  </header><!--/header-->
