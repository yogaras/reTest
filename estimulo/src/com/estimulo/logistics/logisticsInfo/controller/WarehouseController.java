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
import com.estimulo.logistics.logisticsInfo.to.WarehouseTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class WarehouseController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(WarehouseController.class);

	// serviceFacade 참조변수 선언
	LogisticsInfoServiceFacade logisticsSF = LogisticsInfoServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	PrintWriter out = null;

	public ModelAndView getWarehouseList(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("WarehouseController : getWarehouseList 시작");
		}
		HashMap<String, Object> map = new HashMap<>();	
		PrintWriter out = null;
		try {
			out = response.getWriter();
			ArrayList<WarehouseTO> WarehouseTOList = logisticsSF.getWarehouseInfoList();
			map.put("gridRowJson", WarehouseTOList);
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
			logger.debug("WarehouseController : getWarehouseList 종료");
		}
		return null;
	}

	
	public ModelAndView modifyWarehouseInfo(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("WarehouseController : modifyWarehouseInfo 시작");
		}
		String batchList = request.getParameter("batchList");
		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			WarehouseTO WarehouseTO = gson.fromJson(batchList, WarehouseTO.class);
			logisticsSF.modifyWarehouseInfo(WarehouseTO);
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
			logger.debug("WarehouseController : modifyWarehouseInfo 종료");
		}
		return null;
	}
	
	
	public ModelAndView findLastWarehouseCode(HttpServletRequest request, HttpServletResponse response){
		if (logger.isDebugEnabled()) {
			logger.debug("WarehouseController : findLastWarehouseCode 시작");
		}
		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;
		try {
			out=response.getWriter();
			String warehouseCode = logisticsSF.findLastWarehouseCode();
			map.put("lastWarehouseCode", warehouseCode);
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
			logger.debug("WarehouseController : findLastWarehouseCode 종료");
		}
		return null;
	}
	

}
