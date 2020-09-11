<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Voice</title>
	<script src="resources/js/jquery.min.js"></script> 
	<script src="resources/js/recorder.js"></script>
	<script src="resources/js/home.js"></script>
</head>
<body>
	<div style="border: 0px solid #e2e2e2;margin:15px"></div>
	<h3>Recordings</h3>
	<div id="controls">
	    <button id="recordButton">Start</button>
	    <button id="pauseButton" disabled style="display:none">Pause</button>
	    <button id="stopButton" disabled>Stop</button>
	</div>
	
	<div style="border: 1px solid #e2e2e2;margin-top:15px;margin-bottom:15px;"></div>
	
	<!-- <ol id="recordingsList"></ol> -->
	<!-- <input id="text" type="text" style="display:none"/> -->
	
	<div>
		<h3 id="after">result  : </h3>
	</div>
	
	<div style="border: 1px solid #e2e2e2;margin-top:15px;margin-bottom:15px;"></div>
	
	<div>
		<img id="img" width="250" height="250" style="border:3px solid #e2e2e2" src="get/default"/>
	</div>
</body>
</html>