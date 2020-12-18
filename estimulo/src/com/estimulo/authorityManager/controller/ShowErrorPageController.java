package com.estimulo.authorityManager.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.basicInfo.controller.CustomerController;
import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;

public class ShowErrorPageController extends MultiActionController {

	private static Logger logger = LoggerFactory.getLogger(CustomerController.class);

	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ShowErrorPageController :  handleRequestInternal");
		}

		String viewName = "redirect:" + request.getContextPath() + "/hello.html";

		HashMap<String, Object> model = new HashMap<String, Object>();

		if (request.getRequestURI().contains("accessDenied")) {
			model.put("errorCode", -1);
			model.put("errorTitle", "Access Denied");
			model.put("errorMsg", "액세스 거부되었습니다");
			viewName = "errorPage";
		}

		ModelAndView modelAndView = new ModelAndView(viewName, model);

		if (logger.isDebugEnabled()) {
			logger.debug("ShowErrorPageController :  handleRequestInternal");
		}
		return modelAndView;
	}

}
