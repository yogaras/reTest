package com.estimulo.logistics.purchase.controller;

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
import com.estimulo.logistics.purchase.serviceFacade.PurchaseServiceFacade;
import com.estimulo.logistics.purchase.serviceFacade.PurchaseServiceFacadeImpl;
import com.estimulo.logistics.purchase.to.StockLogTO;
import com.estimulo.logistics.purchase.to.StockTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class StockController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(StockController.class);

	// serviceFacade 참조변수 선언
	PurchaseServiceFacade purchaseSF = PurchaseServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	public ModelAndView searchStockList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("StockController : searchStockList 시작");
		}

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<StockTO> stockList = purchaseSF.getStockList();

			map.put("gridRowJson", stockList);
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
			logger.debug("StockController : searchStockList 종료");
		}
		return null;
	}

	public ModelAndView searchStockLogList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("StockController : searchStockList 시작");
		}

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		try {
			out = response.getWriter();

			ArrayList<StockLogTO> stockLogList = purchaseSF.getStockLogList(startDate,endDate);

			map.put("gridRowJson", stockLogList);
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
			logger.debug("StockController : searchStockList 종료");
		}
		return null;
	}
	
	public ModelAndView warehousing(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("StockController : warehousing 시작");
		}

		String orderNoListStr = request.getParameter("orderNoList");

		ArrayList<String> orderNoArr = gson.fromJson(orderNoListStr,new TypeToken<ArrayList<String>>(){}.getType());	

		HashMap<String, Object> resultMap = new HashMap<>();
		
		PrintWriter out = null;

		try {
			out = response.getWriter();

			resultMap = purchaseSF.warehousing(orderNoArr);

		} catch (IOException e1) {
			e1.printStackTrace();
			resultMap.put("errorCode", -1);
			resultMap.put("errorMsg", e1.getMessage());

		} catch (DataAccessException e2) {
			e2.printStackTrace();
			resultMap.put("errorCode", -2);
			resultMap.put("errorMsg", e2.getMessage());

		} finally {
			out.println(gson.toJson(resultMap));
			out.close();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("StockController : warehousing 종료");
		}
		return null;
	}
	
	
}
