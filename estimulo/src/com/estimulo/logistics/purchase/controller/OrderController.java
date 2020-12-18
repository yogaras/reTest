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
import com.estimulo.logistics.purchase.to.OrderInfoTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class OrderController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(OrderController.class);

	// serviceFacade 참조변수 선언
	PurchaseServiceFacade purchaseSF = PurchaseServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	public ModelAndView getOrderList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderController : getOrderList 시작");
		}

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		HashMap<String, Object> resultMap = new HashMap<>();
		PrintWriter out = null;

		try {
			
			out = response.getWriter();

			resultMap = purchaseSF.getOrderList(startDate, endDate);

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
			logger.debug("OrderController : getOrderList 종료");
		}
		
		return null;
		
	}

	public ModelAndView openOrderDialog(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderController : openOrderDialog 시작");
		}

		String mrpNoListStr = request.getParameter("mrpGatheringNoList");
		ArrayList<String> mrpNoArr = gson.fromJson(mrpNoListStr, new TypeToken<ArrayList<String>>() {
		}.getType()); //제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
		HashMap<String,Object> resultMap = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			resultMap = purchaseSF.getOrderDialogInfo(mrpNoArr);

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
			logger.debug("OrderController : openOrderDialog 종료");
		}
		
		return null;
		
	}

	public ModelAndView showOrderInfo(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderController : showOrderInfo 시작");
		}

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		try {

			out = response.getWriter();

			ArrayList<OrderInfoTO> orderInfoList = purchaseSF.getOrderInfoList(startDate,endDate);
			map.put("gridRowJson", orderInfoList);
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
			logger.debug("OrderController : showOrderInfo 종료");
		}
		return null;
	}
	
	public ModelAndView searchOrderInfoListOnDelivery(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderController : openOrderDialog 시작");
		}

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {

			out = response.getWriter();

			ArrayList<OrderInfoTO> orderInfoListOnDelivery = purchaseSF.getOrderInfoListOnDelivery();
			map.put("gridRowJson", orderInfoListOnDelivery);
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
			logger.debug("OrderController : openOrderDialog 종료");
		}
		return null;
	}

	public ModelAndView order(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderController : order 시작");
		}
		String mrpGatheringNoListStr = request.getParameter("mrpGatheringNoList");
		HashMap<String, Object> resultMap = new HashMap<>();

		PrintWriter out = null;
		 ArrayList<String> mrpGaNoArr = gson.fromJson(mrpGatheringNoListStr , new TypeToken<ArrayList<String>>() {
	      }.getType());
		 	//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
		try {
			out = response.getWriter();
///////////////////////////////시작부
			resultMap = purchaseSF.order(mrpGaNoArr);

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
			logger.debug("OrderController : order 종료");
		}
		return null;
	}

	public ModelAndView optionOrder(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderController : optionOrder 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			String itemCode = request.getParameter("itemCode");
			String itemAmount = request.getParameter("itemAmount");

			resultMap = purchaseSF.optionOrder(itemCode, itemAmount);

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
			logger.debug("OrderController : optionOrder 종료");
		}
		return null;
	}
	
	

}
