package com.estimulo.common.servlet.context;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;


public class ApplicationContext {
	private HashMap<String,Object> map;
	
	public ApplicationContext(ServletConfig config, ServletContext application){
		System.out.println("		@ ApplicationContext");
		map=new HashMap<String, Object>();
		String path=config.getInitParameter("configFile");
		/// WEB-INF/config/properties/main.properties
		System.out.println("		@ configFile:"+path);
		
		String rPath = application.getRealPath(path);
		//절대경로를 구해주는 메서드


		Properties properties = new Properties();
		try{
			properties.load(new FileReader(rPath));
			
		}catch (FileNotFoundException e){ 
			e.printStackTrace();
		}catch (IOException e){ 
			e.printStackTrace();		
		}
		
		Set<String> set=properties.stringPropertyNames(); 
		for(String key:set){
			String value = properties.getProperty(key);
			
			System.out.println("		@ TO : "+value);
			
			Object obj=null;
			try{
				obj = Class.forName(value).newInstance();				
			}catch(ClassNotFoundException e){ 
				e.printStackTrace();
			}catch(InstantiationException e){
				e.printStackTrace();
			}catch(IllegalAccessException e){
				e.printStackTrace();
			}
			map.put(key, obj);
		}
	}

	public Object getTO(String TOName) {
		// TODO Auto-generated method stub
		System.out.println("		@ TO : "+TOName);
		return map.get(TOName);
		// return com.Justmusic.common.servlet.controller.UrlFilenameViewController
	}
	
}
