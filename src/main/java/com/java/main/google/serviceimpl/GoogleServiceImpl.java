package com.java.main.google.serviceimpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.java.main.google.client.Google;
import com.java.main.google.service.GoogleService;

@Component
public class GoogleServiceImpl implements GoogleService {
	
	@Resource
	private Google google ; 
	
	public String wavToText(String path) {
		return google.wavToText(path);
	}
}
