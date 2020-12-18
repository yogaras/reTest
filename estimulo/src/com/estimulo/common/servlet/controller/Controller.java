package com.estimulo.common.servlet.controller;



import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;

import com.estimulo.common.servlet.ModelAndView;

public interface Controller {
	ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response);
}
