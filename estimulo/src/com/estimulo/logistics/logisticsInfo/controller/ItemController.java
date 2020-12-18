package com.estimulo.logistics.logisticsInfo.controller;

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
import com.estimulo.logistics.logisticsInfo.serviceFacade.LogisticsInfoServiceFacade;
import com.estimulo.logistics.logisticsInfo.serviceFacade.LogisticsInfoServiceFacadeImpl;
import com.estimulo.logistics.logisticsInfo.to.ItemInfoTO;
import com.estimulo.logistics.logisticsInfo.to.ItemTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ItemController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(ItemController.class);

	// serviceFacade 참조변수 선언
	LogisticsInfoServiceFacade logisticsSF = LogisticsInfoServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	public ModelAndView searchItem(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ItemController : searchItem 시작");
		}

		String searchCondition = request.getParameter("searchCondition");
		String itemClassification = request.getParameter("itemClassification");
		String itemGroupCode = request.getParameter("itemGroupCode");
		String minPrice = request.getParameter("minPrice");
		String maxPrice = request.getParameter("maxPrice");

		ArrayList<ItemInfoTO> itemInfoList = null;
		String[] paramArray = null;

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			switch (searchCondition) {

			case "ALL":

				paramArray = null;
				break;

			case "ITEM_CLASSIFICATION":

				paramArray = new String[] { itemClassification };
				break;

			case "ITEM_GROUP_CODE":

				paramArray = new String[] { itemGroupCode };
				break;

			case "STANDARD_UNIT_PRICE":

				paramArray = new String[] { minPrice, maxPrice };
				break;

			}

			itemInfoList = logisticsSF.getItemInfoList(searchCondition, paramArray);

			map.put("gridRowJson", itemInfoList);
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
			logger.debug("ItemController : searchItem 종료");
		}
		return null;
	}
	
	public ModelAndView getStandardUnitPrice(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ItemController : getStandardUnitPrice 시작");
		}

		String itemCode = request.getParameter("itemCode");
			System.out.println("itemCode = "+itemCode);

		int price = 0;
		
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();
			price = logisticsSF.getStandardUnitPrice(itemCode);

			map.put("gridRowJson", price);
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
			logger.debug("ItemController : searchItem 종료");
		}
		return null;
	}
	
	public ModelAndView getStandardUnitPriceBox(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ItemController : getStandardUnitPrice 시작");
		}

		String itemCode = request.getParameter("itemCode");
	

		int price = 0;
		
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();
			price = logisticsSF.getStandardUnitPriceBox(itemCode);

			map.put("gridRowJson", price);
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
			logger.debug("ItemController : searchItem 종료");
		}
		return null;
	}
	
	public ModelAndView batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ItemController : batchListProcess 시작");
		}

		String batchList = request.getParameter("batchList");

		ArrayList<ItemTO> itemTOList = gson.fromJson(batchList, new TypeToken<ArrayList<ItemTO>>() {
		}.getType());

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			HashMap<String, Object> resultMap = logisticsSF.batchItemListProcess(itemTOList);

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
			logger.debug("ItemController : batchListProcess 종료");
		}
		return null;
	}

}
