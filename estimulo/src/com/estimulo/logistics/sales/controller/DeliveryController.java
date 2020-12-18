package com.estimulo.logistics.sales.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.exception.DataAccessException;
import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;
import com.estimulo.logistics.sales.serviceFacade.SalesServiceFacade;
import com.estimulo.logistics.sales.serviceFacade.SalesServiceFacadeImpl;
import com.estimulo.logistics.sales.to.ContractInfoTO;
import com.estimulo.logistics.sales.to.DeliveryInfoTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DeliveryController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(DeliveryController.class);

	// serviceFacade 참조변수 선언
	SalesServiceFacade salesSF = SalesServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 변환

	public ModelAndView searchDeliveryInfoList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryController : searchDeliveryInfoList 시작");
		}

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			ArrayList<DeliveryInfoTO> deliveryInfoList = salesSF.getDeliveryInfoList();

			map.put("gridRowJson", deliveryInfoList);
			map.put("errorCode", 0);
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
			logger.debug("DeliveryController : searchDeliveryInfoList 종료");
		}
		return null;
	}

	// batchListProcess

	public ModelAndView batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryController : batchListProcess 시작");
		}

		String batchList = request.getParameter("batchList");

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			List<DeliveryInfoTO> deliveryTOList = gson.fromJson(batchList, new TypeToken<ArrayList<DeliveryInfoTO>>() {
			}.getType());

			System.out.println(deliveryTOList);

			HashMap<String, Object> resultMap = salesSF.batchDeliveryListProcess(deliveryTOList);

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
			logger.debug("DeliveryController : batchListProcess 종료");
		}
		return null;
	}

	public ModelAndView searchDeliverableContractList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryController : searchDeliverableContractList 시작");
		}

		HashMap<String, Object> map = new HashMap<>();

		String searchCondition = request.getParameter("searchCondition");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String customerCode = request.getParameter("customerCode");
		PrintWriter out = null;

		try {

			out = response.getWriter();

			ArrayList<ContractInfoTO> deliverableContractList = null;

			if (searchCondition.equals("searchByDate")) {//기간검색

				String[] paramArray = { startDate, endDate };
				deliverableContractList = salesSF.getDeliverableContractList("searchByDate", paramArray);

			} else if (searchCondition.equals("searchByCustomer")) {//거래처검색

				String[] paramArray = { customerCode };
				deliverableContractList = salesSF.getDeliverableContractList("searchByCustomer", paramArray);

			}

			map.put("gridRowJson", deliverableContractList);
			map.put("errorCode", 0);
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
			logger.debug("DeliveryController : searchDeliverableContractList 종료");
		}
		return null;
	}

	public ModelAndView deliver(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryController : deliver 시작");
		}

		HashMap<String,Object> resultMap = new HashMap<>();
		
		PrintWriter out = null;

		String contractDetailNo = request.getParameter("contractDetailNo");

		try {

			out = response.getWriter();

			resultMap = salesSF.deliver(contractDetailNo);
			
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
			logger.debug("DeliveryController : deliver 종료");
		}

		return null;
	}

}
