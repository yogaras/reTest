package com.estimulo.common.servlet.mapper;

import java.io.FileNotFoundException; 
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.estimulo.common.servlet.context.ApplicationContext;
import com.estimulo.common.servlet.controller.Controller;
import com.estimulo.common.servlet.mapper.SimpleUrlHandlerMapping;

public class SimpleUrlHandlerMapping {
	private HashMap<String, String> map;
	private static SimpleUrlHandlerMapping instance;
	public static SimpleUrlHandlerMapping getInstance(ServletContext application){
		if(instance == null){
			instance = new SimpleUrlHandlerMapping(application);
			System.out.println("		@ SimpleUrlHandlerMapping");
		}
			return instance;
	}
	private SimpleUrlHandlerMapping(ServletContext application){
		map=new HashMap<String, String>();
		String path = application.getInitParameter("urlmappingFile");
		String rPath = application.getRealPath(path);
		Properties properties = new Properties();
		try{
			properties.load(new FileReader(rPath));
	
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		Set<String> set = properties.stringPropertyNames();
		for(String key:set){
			String value = properties.getProperty(key);
			map.put(key,value);
		}
	}
	public Controller getController(ApplicationContext applicationContext, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String uri=request.getRequestURI();
		System.out.println("		@ uri : "+uri);
		//- /프로젝트명/SHRich/login.do

	   String contextPath = request.getContextPath();
	   //- /프로젝트명	
		int sIndex = contextPath.length();
		
		String key = uri.substring(sIndex);
		//-/login.do
		String TOName = map.get(key); 
		System.out.println("		@ TOName : "+TOName);
		
		return (Controller)(applicationContext.getTO(TOName));
		//MemberLogInController
		//return (Controller)com.Justmusic.common.servlet.controller.UrlFilenameViewController
	}
	
	
}
