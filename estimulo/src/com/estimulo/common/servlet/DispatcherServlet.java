package com.estimulo.common.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.context.ApplicationContext;
import com.estimulo.common.servlet.controller.Controller;
import com.estimulo.common.servlet.mapper.SimpleUrlHandlerMapping;
import com.estimulo.common.servlet.view.InternalResourceViewResolver;

/**
 * Servlet implementation class DispatcherServlet
 */
public class DispatcherServlet extends HttpServlet {

	private ApplicationContext applicationContext;
	private SimpleUrlHandlerMapping simpleUrlHandlerMapping;
	private InternalResourceViewResolver internalResourceViewResolver;

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		System.out.println("		@ init");
		ServletContext application = this.getServletContext();
		applicationContext = new ApplicationContext(config, application);
		System.out.println("		@ simpleUrlHandlerMapping, internalResourceViewResolver");
		simpleUrlHandlerMapping = SimpleUrlHandlerMapping.getInstance(application);
		internalResourceViewResolver = InternalResourceViewResolver.getInstance(application);
	}

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DispatcherServlet() {

		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		System.out.println("		@getController");
		Controller controller = simpleUrlHandlerMapping.getController(applicationContext, request);
//    /login.do

		System.out.println("		@handleRequest");
		ModelAndView modelAndView = controller.handleRequest(request, response);
		// MemberLogInController .handleRequest

		if (modelAndView != null) {

			System.out.println("		@resolveView");
			internalResourceViewResolver.resolverView(modelAndView, request, response);
		}

	}

}
