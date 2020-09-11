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
	<div id="controls">
	    <button id="recordButton">Start</button>
	    <button id="pauseButton" disabled style="display:none">Pause</button>
	    <button id="stopButton" disabled>Stop</button>
	</div>
	<h3>Recordings</h3>
	<!-- <ol id="recordingsList"></ol> -->
	<input id="text" type="text"/>
	
	<div>
		<h3 id="after">result  : </h3>
	</div>
	
	<div>
		<img id="img" src="get/default"/>
	</div>
</body>
</html>