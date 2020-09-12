var URL ; 
var gumStream;
var rec;
var input;

var AudioContext;
var audioContext;

var recordButton;
var stopButton;
var pauseButton;

$(document).ready(function(){
	URL = window.URL || window.webkitURL;
	
	AudioContext = window.AudioContext || window.webkitAudioContext;
	audioContext = new AudioContext;
	recordButton = document.getElementById("recordButton");
	stopButton = document.getElementById("stopButton");
	pauseButton = document.getElementById("pauseButton");
	
	recordButton.addEventListener("click", startRecording);
	stopButton.addEventListener("click", stopRecording);
	pauseButton.addEventListener("click", pauseRecording);
})

function startRecording() { 
	console.log("recordButton clicked");
	
	var constraints = {
		    audio: true,
		    video: false
		} 
		/* Disable the record button until we get a success or fail from getUserMedia() */

		recordButton.disabled = true;
		stopButton.disabled = false;
		pauseButton.disabled = false

		/* We're using the standard promise based getUserMedia()

		https://developer.mozilla.org/en-US/docs/Web/API/MediaDevices/getUserMedia */
		
		navigator.mediaDevices.getUserMedia(constraints).then(function(stream) {
		    console.log("getUserMedia() success, stream created, initializing Recorder.js ..."); 
		    /* assign to gumStream for later use */
		    gumStream = stream;
		    /* use the stream */
		    input = audioContext.createMediaStreamSource(stream);
		    /* Create the Recorder object and configure to record mono sound (1 channel) Recording 2 channels will double the file size */
		    rec = new Recorder(input, {
		        numChannels: 1
		    }) 
		    //start the recording process 
		    rec.record()
		    console.log("Recording started");
		}).catch(function(err) {
		    //enable the record button if getUserMedia() fails 
		    recordButton.disabled = false;
		    stopButton.disabled = true;
		    pauseButton.disabled = true
		});
}


function pauseRecording() {
    console.log("pauseButton clicked rec.recording=", rec.recording);
    if (rec.recording) {
        //pause 
        rec.stop();
        pauseButton.innerHTML = "Resume";
    } else {
        //resume 
        rec.record()
        pauseButton.innerHTML = "Pause";
    }
}

function stopRecording() {
    console.log("stopButton clicked");
    //disable the stop button, enable the record too allow for new recordings 
    stopButton.disabled = true;
    recordButton.disabled = false;
    pauseButton.disabled = true;
    //reset button just in case the recording is stopped while paused 
    pauseButton.innerHTML = "Pause";
    //tell the recorder to stop the recording 
    rec.stop(); //stop microphone access 
    gumStream.getAudioTracks()[0].stop();
    //create the wav blob and pass it on to createDownloadLink 
    rec.exportWAV(sendDataToServer);
}


function sendDataToServer(blob) {
	var url = "wav/upload";
	var form = new FormData();
	var req = new XMLHttpRequest();
	
	form.append("audio", blob);    
	
    req.onreadystatechange = function() {
        if (req.readyState == XMLHttpRequest.DONE  ) {
        	console.log("REQUEST DONE")
        	console.log(req.responseText);
        	var jsonString = req.responseText ;
        	var obj = JSON.parse(jsonString);
        	
        	if (obj.method == "0"){
        		$('#img').attr("src","");
        	} else if ( obj.method == "1") { 
        		$('#img').attr("src","get/"+obj.number);
        	}
        	
        	var text = obj.text ;
        	
        	if ( obj.analysisText != null && obj.analysisText != undefined ){
        		text = obj.analysisText ;
        	}
        	
        	$('#after').html("result : [" + obj.methodName + "] " + text);
        }
    };
    
    req.open("POST", url);
    req.send(form);
}


function uploadFile(){
	var filename = new Date().toISOString();
	//filename to send to server without extension 
	//upload link 
	var upload = document.createElement('a');
	upload.href = "#";
	upload.innerHTML = "Upload";
	upload.addEventListener("click", function(event) {
	    var xhr = new XMLHttpRequest();
	    xhr.onload = function(e) {
	        if (this.readyState === 4) {
	            console.log("Server returned: ", e.target.responseText);
	        }
	    };
	    var fd = new FormData();
	    fd.append("audio_data", blob, filename);
	    xhr.open("POST", "upload.php", true);
	    xhr.send(fd);
	})
	li.appendChild(document.createTextNode(" ")) //add a space in between 
	li.appendChild(upload) //add the upload link to li

}