<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Voice</title>
	<script src="js/jquery.min.js"></script> 
	<script src="js/recorder.js"></script>
	<script src="js/main.js"></script>
</head>
<body>
	<form id="form1" enctype="multipart/form-data">
		<input type="file" id="file" name="name"/>
		<div id="controls">
		    <button id="recordButton">Record</button>
		    <button id="pauseButton" disabled>Pause</button>
		    <button id="stopButton" disabled>Stop</button>
		</div>
		<h3>Recordings</h3>
		<ol id="recordingsList"></ol>
	</form>
</body>
</html>