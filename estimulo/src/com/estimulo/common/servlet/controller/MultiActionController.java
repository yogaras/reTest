package com.estimulo.common.servlet.controller;

import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;

import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.AbstractController;

public class MultiActionController extends AbstractController{
	
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		
		//http://localhost:8282/SHRich/login.do?method=LoginCheak
	      
		
		  System.out.println("		@ MultiActionController handleRequestInternal");
	      String methodName = request.getParameter("method"); 
	      //loginCheck
	   
	      System.out.println("		@ methodName : "+methodName);
	      
	      
	      Class<?>[] parameters = new Class<?>[]{HttpServletRequest.class,HttpServletResponse.class};
          //   
	      
	      //searchCompanyList
	      
	      Class<?> cl = this.getClass();
	      	 //c1== MemberLoginController
	      try{        
	    	  
	         ModelAndView modelAndView=(ModelAndView)cl.getMethod(methodName, parameters).invoke(cl.newInstance(),request,response);
	         //모델앤드뷰= MemberLoginController의 LoginCheak 메서드실행.
	         // c1.getMehod() Method.invoke()
	         return modelAndView;
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      System.out.println("@@@@@@@@return null");
	      return null;
	   }

}
