package com.estimulo.common.servlet.view;

import java.io.FileNotFoundException; 
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.view.InternalResourceViewResolver;

public class InternalResourceViewResolver {
	private String prefix, postfix;
	private static InternalResourceViewResolver instance;
	public static InternalResourceViewResolver getInstance(ServletContext application){
		if(instance ==null){
			instance = new InternalResourceViewResolver(application);
			System.out.println("		@ InternalResourceViewResolver");
		}
		return instance;
	}
	private InternalResourceViewResolver(ServletContext application){
		String path = application.getInitParameter("pathFile");
		String rPath = application.getRealPath(path);
		Properties properties = new Properties();
		try{
			properties.load(new FileReader(rPath));			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		prefix = properties.getProperty("prefix");
		postfix = properties.getProperty("postfix");
	}
	public void resolverView(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String viewName = modelAndView.getViewName();
		//loginform
		System.out.println("		@ viewResolver viewName : "+viewName); 
		
		if(viewName.lastIndexOf("redirect:")==-1){
			//loginform
			System.out.println("		@ forward");
			HashMap<String,Object> modelObject = modelAndView.getModelObject();
			System.out.println("		@ forward modelobj : "+modelObject);
			
			if(modelObject!=null){
				for(String key:modelObject.keySet()){			
					request.setAttribute(key, modelObject.get(key));
					System.out.println("		viewResolver setAttrKey : "+key);
				}
			}
			RequestDispatcher rd = request.getRequestDispatcher(prefix+viewName+postfix);
			
			System.out.println("		@ forward page viewName : "+prefix+viewName+postfix);
			rd.forward(request, response);
			// 패키지명/loginform.jsp
		}else{
			System.out.println("		@ redirect");
			int index = viewName.indexOf(":");
			String path = viewName.substring(index+1);
			
			System.out.println("		@ redirect page path : "+path);
			response.sendRedirect(path);
		}
	}	
}
