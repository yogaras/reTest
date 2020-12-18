package com.estimulo.common.sl;

import java.util.Collections; 
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.estimulo.common.sl.ServiceLocator;
import com.estimulo.common.sl.ServiceLocatorException;

public class ServiceLocator {
	
	private Map<String, DataSource> cache;
	
	private Context envCtx;  
	private static ServiceLocator instance;  
	static {
		try{
		instance = new ServiceLocator();
		System.out.println("		@ ServiceLocator");
		}catch(ServiceLocatorException e){
			e.printStackTrace();
		}
		
	}
	private ServiceLocator(){
		try{
			envCtx = new InitialContext(); //JNDI 네이밍서비스와 통신하는 객체이다.
			cache = Collections.synchronizedMap(new HashMap<String,DataSource>()); //동기화 처리를 해준다.(동시에 HASHMAP 에 접근하지 못하게 한다.)
			
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceLocatorException(e.getMessage()); 		
		}
	}
	public static ServiceLocator getInstance() {
		return instance;
	}
	
	public DataSource getDataSource(String jndiName) {
		// TODO Auto-generated method stub
		DataSource dataSource;
		System.out.println("		@ getDataSource");
		try{
			if(cache.containsKey(jndiName)){ 
				dataSource=cache.get(jndiName);
			}else{
				dataSource = (DataSource)envCtx.lookup("java:comp/env/"+jndiName); 
				cache.put(jndiName, dataSource); 
			}
			System.out.println("		@ jndi: "+jndiName);
		}catch(NamingException e){
			throw new ServiceLocatorException(e.getMessage());
			
		}
		return dataSource;
	}
	
}

