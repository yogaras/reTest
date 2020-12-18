package com.estimulo.basicInfo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.basicInfo.serviceFacade.CooperatorServiceFacade;
import com.estimulo.basicInfo.serviceFacade.CooperatorServiceFacadeImpl;
import com.estimulo.basicInfo.to.CustomerTO;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class CustomerController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(CustomerController.class);

	// serviceFacade 참조변수 선언
	CooperatorServiceFacade cooperatorSF = CooperatorServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	public ModelAndView searchCustomerList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerController :  searchCustomerList 시작");
		}

		String searchCondition = request.getParameter("searchCondition");

		String companyCode = request.getParameter("companyCode");

		String workplaceCode = request.getParameter("workplaceCode");

		ArrayList<CustomerTO> customerList = null;

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			customerList = cooperatorSF.getCustomerList(searchCondition, companyCode, workplaceCode);

			map.put("gridRowJson", customerList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");

		} catch (IOException e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());

		} catch (DataAccessException e2) {
			e2.printStackTrace();
			map.put("errorCode", -2);
			map.put("errorMsg", e2.getMessage());

		} finally {
			out.println(gson.toJson(map));
			out.close();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerController :  searchCustomerList 종료");
		}
		return null;
	}

	public ModelAndView batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerController : batchListProcess 시작");
		}

		String batchList = request.getParameter("batchList");

		ArrayList<CustomerTO> customerList = gson.fromJson(batchList, new TypeToken<ArrayList<CustomerTO>>() {
		}.getType());

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			HashMap<String, Object> resultMap = cooperatorSF.batchCustomerListProcess(customerList);

			map.put("result", resultMap);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");

		} catch (IOException e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());

		} catch (DataAccessException e2) {
			e2.printStackTrace();
			map.put("errorCode", -2);
			map.put("errorMsg", e2.getMessage());

		} finally {
			out.println(gson.toJson(map));
			out.close();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerController : batchListProcess 종료");
		}
		return null;
	}

}
