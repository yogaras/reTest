package com.estimulo.logistics.sales.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.exception.DataAccessException;
import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;
import com.estimulo.logistics.sales.serviceFacade.SalesServiceFacade;
import com.estimulo.logistics.sales.serviceFacade.SalesServiceFacadeImpl;
import com.estimulo.logistics.sales.to.SalesPlanTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SalesPlanController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(SalesPlanController.class);

	// serviceFacade 참조변수 선언
	SalesServiceFacade salesSF = SalesServiceFacadeImpl.getInstance();

	// gson 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	public ModelAndView searchSalesPlanInfo(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanController : searchSalesPlanInfo 시작");
		}

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String dateSearchCondition = request.getParameter("dateSearchCondition");

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<SalesPlanTO> salesPlanTOList = salesSF.getSalesPlanList(dateSearchCondition, startDate, endDate);

			map.put("gridRowJson", salesPlanTOList);
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
			logger.debug("SalesPlanController : searchSalesPlanInfo 종료");
		}
		return null;
	}

	public ModelAndView batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanController : batchListProcess 시작");
		}

		String batchList = request.getParameter("batchList");

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<SalesPlanTO> salesPlanTOList = gson.fromJson(batchList, new TypeToken<ArrayList<SalesPlanTO>>() {
			}.getType());

			HashMap<String, Object> resultMap = salesSF.batchSalesPlanListProcess(salesPlanTOList);

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
			logger.debug("SalesPlanController : batchListProcess 종료");
		}
		return null;
	}

}
