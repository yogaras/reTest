package com.estimulo.common.servlet.controller;

import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;

import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.AbstractController;

public class UrlFilenameViewController extends AbstractController{

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response){
		// TODO Auto-generated method stub
		System.out.println("		@ UrlFilenameViewController handleRequestInternal");
		String uri=request.getRequestURI();
		System.out.println("		uri : "+uri);
		// -/프로젝트명/loginform.html
	
		String contextPath=request.getContextPath();
		// /프로젝트명
	
		int sIndex = contextPath.length()+1;
	
		int eIndex = uri.lastIndexOf(".");
		
		
		String viewName = uri.substring(sIndex, eIndex);
	 
		System.out.println("		viewName "+viewName);
		//loginform
		ModelAndView modelAndView = new ModelAndView(viewName,null);
		
		System.out.println("		return mav");
		return modelAndView;
	}

}
