package com.java.main.google.client;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.protobuf.ByteString;

@Component
public class GoogleClient implements Google{
	
	private static final Logger logger = LoggerFactory.getLogger(GoogleClient.class);
	
	@Value("#{config['google.auth.json.path']}") 
	private String GOOGLE_AUTH_JSON_PATH = null ; 
	
	private SpeechSettings SPEECH_SETTINGS = null ;
	
	private RecognitionConfig RECOGNITION_CONFIG = null ; 
	
	private final String LANGUAGE = "ko-KR"; // en-US
	public GoogleClient() {
	}
	
	@PostConstruct
	public void auth() {
		try {
			logger.info("GOOGLE_AUTH_JSON_PATH = {} ",GOOGLE_AUTH_JSON_PATH);
			logger.info("LANGUAGE = {}" , LANGUAGE);
			FileInputStream credentialsStream = new FileInputStream(GOOGLE_AUTH_JSON_PATH);
			GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
			FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);

			SPEECH_SETTINGS = SpeechSettings.newBuilder()
				.setCredentialsProvider(credentialsProvider)
				.build();
			
			RECOGNITION_CONFIG = RecognitionConfig.newBuilder()
			              .setEncoding(AudioEncoding.LINEAR16)
//			              .setSampleRateHertz(16000) // 확인좀 필요할 듯 
			              .setLanguageCode(LANGUAGE)
			              .build();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public String wavToText(String filePath) {
		logger.info("Wav to Text File Parh = {}",filePath);
		String result = null ; 
		
		try {
			SpeechClient speechClient = SpeechClient.create(SPEECH_SETTINGS);
			
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			ByteString audioBytes = ByteString.copyFrom(data);
			
			RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();
			
			RecognizeResponse response = speechClient.recognize(RECOGNITION_CONFIG, audio);
			List<SpeechRecognitionResult> results = response.getResultsList();
			
			for (SpeechRecognitionResult r : results) {
		        // There can be several alternative transcripts for a given chunk of speech. Just use the
		        // first (most likely) one here.
		        SpeechRecognitionAlternative alternative = r.getAlternativesList().get(0);
		        logger.info("Transcription = {}" , alternative.getTranscript());
		        result = alternative.getTranscript() ; 
		        
		        break ; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result ; 
	}
}
