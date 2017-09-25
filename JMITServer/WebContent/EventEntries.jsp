<%@page import="database.DataManager"%>
<%@page import="database.CacheConnection"%>
<%@page import="java.util.ArrayList" import="pojo.Registration"%>
<%@page import="java.util.ArrayList" import="pojo.Event"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=yes">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css">
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-2.2.1.min.js"></script>
<!-- Compiled and minified JavaScript -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<link href="css/materialize.css" type="text/css" rel="stylesheet"
	media="screen,projection" />
<link href="css/style.css" type="text/css" rel="stylesheet"
	media="screen,projection" />
<script language="JavaScript">
	$(document).ready(function() {
		$(".button-collapse").sideNav();
		$('.parallax').parallax();
		$(".dropdown-button").dropdown({
			hover : false
		});
		$("#content").hide();
		$("#downloadExcel").hide();
		$("#Emptycontent").show();
		$("#Loadingcontent").hide();
		$("#downloadExcel").click(function(){
			 try{ 
				var form= $('<form action="downloadexcel" method="post">' + 
				    '<input type="hidden" name="event_id" value="' + $("#downloadExcel").data("id") + '">' +
				    '</form>');
				$('body').append(form);
				form.submit();
			 }catch(e){
				 alert(e);
			 }
			});
		var id=$.cookie("current_id");
		var name=$.cookie("current_name");
		if(name!=null && id!=null)
			onItemClick(id,name);
	});	

  function onItemClick(event_id,event_name){
	var xhttp;
	if (window.XMLHttpRequest) {
	    xhttp = new XMLHttpRequest();
	    } else {
	    // code for IE6, IE5
	    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}

	$("#Loadingcontent").show();
	$("#Emptycontent").hide();
	$("#content").hide();
	xhttp.open("POST", "get_registeration", true);
	xhttp.onreadystatechange = function() {
	        if (this.readyState == 4 && this.status == 200) {
	        	try{
	        		myObj = JSON.parse(this.responseText);
	        		
	        	document.getElementById("title").innerHTML=event_name;
	        	var table=document.getElementById("table1");
        		var rowCount1 = table.rows.length; 
        		while(--rowCount1>=0)table.deleteRow(rowCount1);
        		
	        	$("#content").show();
	        	$("#Emptycontent").hide();
	    		$("#Loadingcontent").hide();
	        	$("#downloadExcel").show();
	        	$("#table1").show();
        		$("#table").show();	
        		$("#noentries").hide();
        		$("#downloadExcel").removeData("id");
	        	$("#downloadExcel").data("id", event_id);
	        	$.cookie("current_id", event_id);
	        	$.cookie("current_name", event_name);
	        	if(myObj.success==0)
	        	{
	        		$("#downloadExcel").hide();
	        		$("#table1").hide();
	        		$("#table").hide();
	        		$("#noentries").show();

	        		
	        	}
	        	else{
	        		array=JSON.parse(myObj.registrations);
	        		for (x=0;x<array.length;x++) {
	        		   var rowCount = table.rows.length;
	        		   var row = table.insertRow(rowCount);
	        		   var newcell = row.insertCell(0);
	        		   newcell.innerHTML=x+1;
	        		   var newcell = row.insertCell(1);
	        		   newcell.innerHTML=array[x].roll_no;
	        		   var newcell1 = row.insertCell(2);
	        		   newcell1.innerHTML=array[x].user_name;
	        		   var newcell2 = row.insertCell(3);
	        		   newcell2.innerHTML=array[x].department;
	        		   var newcell3 = row.insertCell(4);
	        		   newcell3.innerHTML=array[x].email;
	        		   var newcell4 = row.insertCell(5);
	        		   newcell4.innerHTML=array[x].ph_no;	
	        	}   }
	       }catch(eee){
	    	alert(eee);   
	       }
	       }
	    };
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send("event_id="+event_id);
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JMIT</title>
</head>
<body>

	<%
		String userName = null;
		Cookie[] cookies = request.getCookies();
		String event_ids = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user"))
					userName = cookie.getValue();
				else if (cookie.getName().equals("event"))
					event_ids = cookie.getValue();
			}
		}
		if (userName == null)
			response.sendRedirect("Login.jsp");
	%>
	<ul id="dropdown1" class="dropdown-content">
		<%
			java.sql.Connection connection = CacheConnection.checkOut("web");
			ArrayList<Event> events = DataManager.getEventsByIds(connection, event_ids);
			for (Event e : events) {
		%>
		<li><a href="#!" id="link"
			onclick="onItemClick(<%=e.getEvent_id()%>,'<%=e.getEvent_name()%>')"><%=e.getEvent_name()%></a></li>

		<%
			}
		%>
	</ul>

	<nav class="red lighten-2" role="navigation">
	<div class="nav-wrapper">
		<a href="#!" class="brand-logo left" style="margin-left: 20px">JMIT</a>
		<ul class="right">
			<li><a class="dropdown-button" href="#!"
				data-activates="dropdown1">Select Event<i
					class="material-icons right">arrow_drop_down</i></a></li>
			<li class="hide-on-med-and-down"><a href="userlogout">Logout</a></li>
			<li class="hide-on-large-only"><a href="userlogout"
				style="float: left; position: relative; z-index: 1; height: 56px;"><i
					class="material-icons">exit_to_app</i></a></li>
		</ul>

	</div>
	</nav>
	<div id="Emptycontent">
		<h5 class="black-text text-lighten-2"
			style="margin: 0; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);"
			align="center">Select event from top to view entries.</h5>

	</div>
	<div id="Loadingcontent"
		style="margin: 0; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);">
		<div class="preloader-wrapper big active">
			<div class="spinner-layer spinner-blue-only">
				<div class="circle-clipper left">
					<div class="circle"></div>
				</div>
				<div class="gap-patch">
					<div class="circle"></div>
				</div>
				<div class="circle-clipper right">
					<div class="circle"></div>
				</div>
			</div>
		</div>
	</div>

	</div>
	<div id="main" class="col s14">
		<div class="row">
			<div class="col s2 offset-s1" style="margin-top: 20px;">
				<a class="waves-effect waves-light btn" id="downloadExcel">Download
					Excel Sheet</a>
			</div>
		</div>


		<div class="row">
			<div id="content" class="card blue-grey darken-1 col s10 offset-s1">

				<div class="card-content white-text">
					<span class="card-title" id="title">Card Title</span>

					<center class="middlecontent">
						<h6 id="noentries" class="white-text text-lighten-2">No
							entries found</h6>

						<TABLE id="table" class="responsive-table">
							<thead>
								<TR>
									<TH>Sno</TH>
									<TH>Roll no</TH>
									<TH>Name</TH>
									<TH>Dept</TH>
									<TH>Email</TH>
									<TH>Phone</TH>
								</TR>
							</thead>
							<tbody id="table1">

							</tbody>
						</TABLE>
					</center>
				</div>
			</div>
		</div>
	</div>

</body>
</html>