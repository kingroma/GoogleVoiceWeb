package com.java.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.java.main.analysis.service.AnalysisService;
import com.java.main.google.service.GoogleService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Value("#{config['wav.path']}") 
	private String WAV_PATH = null;
	
	@Value("#{config['image.storage']}")
	private String IMAGE_STORAGE = null ; 
	
	private final String defalutImage = "default.png" ;
	
	private final String[] imgExtension = {
			".png" , ".jpg" , ".jpeg"
	};

	@Resource
	private GoogleService googleService ;
	
	@Resource
	private AnalysisService analysisService ; 
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}
	
	@RequestMapping(value = "/wav/upload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> upload(MultipartHttpServletRequest request) {
		logger.info("/wav/upload");
		logger.info("WAV_PATH = {}",WAV_PATH);
		Map<String,String> result = new HashMap<String,String>();
		Map<String,String> analysisMap = null ; 
		String text = null ; 

		try {
			File file = null;
	        MultipartFile audiofile = request.getFile("audio");
	        byte[] audioData = audiofile.getBytes();

	        if (!audiofile.isEmpty()) {
	            if (audiofile.getSize() > 0) { 
	            	file = new File(WAV_PATH);
	                OutputStream out2 = new FileOutputStream(file);
	                out2.write(audioData);
	                out2.close();
	            }
	        }
	        
	        text = googleService.wavToText(file.getAbsolutePath());
			logger.info("TEXT = {}",text);
			
			if ( text != null && !text.isEmpty() ) {
				analysisMap = analysisService.analysis(text);
			}
	        
	        if ( analysisMap != null ) {
	        	for ( String key : analysisMap.keySet() ) { 
	        		result.put(key, analysisMap.get(key));
	        	}
	        } else { 
	        	result.put("method", "0");
	        	result.put("methodName", "NOT FOUND");
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result.put("text", text);
		return result ; 
	}
	
	
	@RequestMapping(value = "/get/{IMAGE_ID}", method = RequestMethod.GET , produces = "image/png" )
	public void getImage( HttpServletResponse response ,@PathVariable("IMAGE_ID") String imageId ) {
		logger.info("Image Storage = {} , Image ID = {}" , IMAGE_STORAGE,  imageId);
		
		ServletOutputStream sos = null ; 
		FileInputStream fis = null;
		
		File file = null;
		
		for ( String e : imgExtension ){
			file = new File(IMAGE_STORAGE + imageId + e);
			
			if ( file.exists() ){
				switch(e){
				case ".png":
					response.setContentType("image/png");
					break;
				case ".jpg":
					response.setContentType("image/jpeg");
					break;
				case ".jpeg":
					response.setContentType("image/jpeg");
					break;
				}
				break;
			}
			else 
				file = null ;
		}
		
		try {
			if ( file == null ){
				file = new File ( IMAGE_STORAGE + defalutImage );
				response.setContentType("image/jpeg");
			}
			
			sos = response.getOutputStream();
			
			fis = new FileInputStream(file);
			int length ; 
			
			byte[] buffer = new byte[128];
			while ( (length = fis.read(buffer)) != -1 ){
				sos.write(buffer,0,length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if ( sos != null ) 
					sos.close();
				if ( fis != null )
					fis.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
