package com.java.main.analysis.serviceimpl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.java.main.HomeController;
import com.java.main.analysis.service.AnalysisService;

@Component
public class AnalysisServiceImpl implements AnalysisService{
	private static final Logger logger = LoggerFactory.getLogger(AnalysisServiceImpl.class);
	
	public static final String IMAGE_CHANGE = "1";
 
	public Map<String,String> analysis(String text) {
		Map<String,String> result = null ;
		if ( isImageChange (text) ) { 
			logger.info("MATCH IMAGE CHANGE");
			
			result = getImageChangeParam(text) ;
		}
		
		return result ; 
	}
	
	private final String IMAGE_CHANGE_REGEXT = "[ㄱ-ㅎ가-힣 ]*([0-9]+).*[(그림)(이미지)]?.*[(보여)(틀어줘)(변경)].*";
	private boolean isImageChange(String text) {
		return Pattern.matches(IMAGE_CHANGE_REGEXT, text);
	}
	
	private Map<String,String> getImageChangeParam(String text) {
		Map<String,String> result = null ;
		Pattern p = Pattern.compile(IMAGE_CHANGE_REGEXT);
		Matcher m = p.matcher(text);
		
		
		if ( m.find() )  {
			System.out.println();
			for ( int i = 0 ; i < m.groupCount() + 1 ; i ++ ) {
				if ( i == 1 ) {
					logger.info("IMAGE_CHANGE NUMBER = {} ", m.group(1));
					result = new HashMap<String,String>() ; 
					result.put("number", m.group(1));
					result.put("method", IMAGE_CHANGE);
					result.put("methodName", "IMAGE_CHANGE");
				}
			}
		}
		
		return result ; 
	}
	
	public static void main(String[] args) {
		AnalysisService as = new AnalysisServiceImpl();
		Map<String,String> map = as.analysis("안녕 1번 으로 변경해줘");
		System.out.println(map);
	}
}