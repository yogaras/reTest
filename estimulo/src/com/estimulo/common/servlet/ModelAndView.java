package com.estimulo.common.servlet;

import java.util.HashMap;

public class ModelAndView {
	private String viewName;//견적 등록기준으로 sales/estimateRegister게 들어옴
	private HashMap<String, Object> modelObject;//견적 등록기준으로 null이 들어옴

	public ModelAndView(String viewName, HashMap<String, Object> modelObject) {
		this.viewName = viewName;
		this.modelObject = modelObject;
	}

	public String getViewName() {
		
		
		
		return viewName;
	}

	public HashMap<String, Object> getModelObject() {
		return modelObject;
	}
}
