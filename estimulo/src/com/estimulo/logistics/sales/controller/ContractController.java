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
import com.estimulo.logistics.sales.to.ContractDetailTO;
import com.estimulo.logistics.sales.to.ContractInfoTO;
import com.estimulo.logistics.sales.to.ContractTO;
import com.estimulo.logistics.sales.to.EstimateTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ContractController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(ContractController.class);

	// serviceFacade 참조변수 선언
	SalesServiceFacade salesSF = SalesServiceFacadeImpl.getInstance();

	// GSON �씪�씠釉뚮윭由�
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // �냽�꽦媛믪씠 null �씤 �냽�꽦�룄 蹂��솚

	public ModelAndView searchContract(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractController : searchContract");
		}

		String searchCondition = request.getParameter("searchCondition");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String customerCode = request.getParameter("customerCode");

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			ArrayList<ContractInfoTO> contractInfoTOList = null;

			if (searchCondition.equals("searchByDate")) {

				String[] paramArray = { startDate, endDate };
				contractInfoTOList = salesSF.getContractList("searchByDate", paramArray);

			} else if (searchCondition.equals("searchByCustomer")) {

				String[] paramArray = { customerCode };
				contractInfoTOList = salesSF.getContractList("searchByCustomer", paramArray);

			}

			map.put("gridRowJson", contractInfoTOList);
			map.put("errorCode", 1);
			map.put("errorMsg", "�꽦怨�");

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
			logger.debug("ContractController : searchContract");
		}
		return null;
	}

//	�옉�뾽以�
	public ModelAndView searchContractNO(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractController : searchContractNO");
		}

		String searchCondition = request.getParameter("searchCondition");

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			ArrayList<ContractInfoTO> contractInfoTOList = null;
			if (searchCondition.equals("searchByDate")) {
				String customerCode = "";
				String[] paramArray = { customerCode };
				contractInfoTOList = salesSF.getContractList("searchByCustomer", paramArray);

			}

			map.put("gridRowJson", contractInfoTOList);
			map.put("errorCode", 1);
			map.put("errorMsg", "�꽦怨�");

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
			logger.debug("ContractController : searchContractNO");
		}
		return null;
	}

//	�옉�뾽�걹
	public ModelAndView searchContractDetail(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractController : searchContractDetail");
		}

		String contractNo = request.getParameter("contractNo");

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			ArrayList<ContractDetailTO> contractDetailTOList = salesSF.getContractDetailList(contractNo);

			map.put("gridRowJson", contractDetailTOList);
			map.put("errorCode", 1);
			map.put("errorMsg", "�꽦怨�");

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
			logger.debug("ContractController : searchContractDetail");
		}
		return null;
	}

	public ModelAndView searchEstimateInContractAvailable(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractController : searchEstimateInContractAvailable 시작");
		}

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			ArrayList<EstimateTO> estimateListInContractAvailable = salesSF
					.getEstimateListInContractAvailable(startDate, endDate);

			map.put("gridRowJson", estimateListInContractAvailable);
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
			logger.debug("ContractController : searchEstimateInContractAvailable 종료");
		}
		return null;
	}

	public ModelAndView addNewContract(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractController : addNewContract");
		}

		String batchList = request.getParameter("batchList");
		String contractDate = request.getParameter("contractDate");//오늘날짜
		String personCodeInCharge = request.getParameter("personCodeInCharge");//EMP-01
		System.out.println("@batchList="+batchList+"@contractDate"+contractDate+"@personCodeInCharge"+personCodeInCharge);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		PrintWriter out = null;

		try {
			
			out = response.getWriter();

			ContractTO workingContractTO = gson.fromJson(batchList, ContractTO.class);

			resultMap = salesSF.addNewContract(contractDate, personCodeInCharge,workingContractTO);

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
			logger.debug("ContractController : addNewContract");
		}
		
		return null;
		
	}

	public ModelAndView cancleEstimate(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractController : cancleEstimate");
		}
		String estimateNo = request.getParameter("estimateNo");

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {

			out = response.getWriter();

			salesSF.changeContractStatusInEstimate(estimateNo, "N");

			map.put("errorCode", 1);
			map.put("errorMsg", "�꽦怨�");
			map.put("cancledEstimateNo", estimateNo);

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
			logger.debug("ContractController : cancleEstimate");
		}
		return null;
	}

}
