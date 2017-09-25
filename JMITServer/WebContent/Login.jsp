<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=yes">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css">
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-2.2.1.min.js"></script>
<!-- Compiled and minified JavaScript -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js"></script>

<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<link href="css/materialize.css" type="text/css" rel="stylesheet"
	media="screen,projection" />
<link href="css/style.css" type="text/css" rel="stylesheet"
	media="screen,projection" />
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link rel="icon" href="favicon.ico" type="image/x-icon">
<script>
	$(document).ready(function() {
		$(".button-collapse").sideNav();
		$('.parallax').parallax();
		$(".dropdown-button").dropdown({
			hover : false
		});
	});
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<nav class="light-blue lighten-1" role="navigation">
	<div class="nav-wrapper container">
		<div
			style="position: absolute; color: #fff; display: inline-block; font-size: 2.1rem; padding-left: 10px; white-space: nowrap;">
			<div id="logo-container" href="#" style="float: left; width: 50%">
				<img src="ic_launcher.png" />
			</div>
			<div id="title-container" href="#" style="float: right; width: 50%">Login</div>
		</div>
	</div>
	</nav>
	<div class="row" style="margin-top: 80px;">
		<form class="col s12" action="user_login" method="post">
			<div class="row">
				<div class="input-field col s6 offset-s3">
					<input id="roll_no" name="roll_no" type="text" class="validate">
					<label for="roll_no">Username</label>
				</div>
			</div>
			<div class="row">
				<div class="input-field col s6 offset-s3">
					<input id="password" name="password" type="password"
						class="validate"> <label for="password">Password</label>
				</div>
			</div>
			<input type="hidden" name="device_id" value="web" maxlength="50">
			<div class="row">
				<div class="input-field col s4 offset-s4">
					<button class="btn waves-effect waves-light" type="submit"
						name="action">
						Submit <i class="material-icons right">send</i>
					</button>
				</div>
			</div>
			<input type="hidden" name="action" value="register">
		</form>
	</div>
</body>
</html>