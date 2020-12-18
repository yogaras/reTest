package com.estimulo.logistics.production.controller;

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
import com.estimulo.logistics.production.serviceFacade.ProductionServiceFacade;
import com.estimulo.logistics.production.serviceFacade.ProductionServiceFacadeImpl;
import com.estimulo.logistics.production.to.ContractDetailInMpsAvailableTO;
import com.estimulo.logistics.production.to.MpsTO;
import com.estimulo.logistics.production.to.SalesPlanInMpsAvailableTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MpsController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(MpsController.class);

	// serviceFacade 참조변수 선언
	ProductionServiceFacade productionSF = ProductionServiceFacadeImpl.getInstance();

	// gson 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	public ModelAndView searchMpsInfo(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsController : searchMpsInfo 시작");
		}

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String includeMrpApply = request.getParameter("includeMrpApply"); 
								// 포함 = includeMrpApply, 미포함 = excludeMrpApply;
   System.out.println("MPS 컨트롤러 값확인  - stDate : "+startDate+", endDate : "+endDate+", includeMrpApply:" +includeMrpApply);
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<MpsTO> mpsTOList = productionSF.getMpsList(startDate, endDate, includeMrpApply);

			map.put("gridRowJson", mpsTOList);
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
			logger.debug("MpsController : searchMpsInfo 종료");
		}
		return null;
	}

	public ModelAndView searchContractDetailListInMpsAvailable(HttpServletRequest request,
			HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsController : searchContractDetailListInMpsAvailable 시작");
		}

		String searchCondition = request.getParameter("searchCondition");//수주일자 = contractDate, 납기일 = dueDateOfContract
				System.out.println(searchCondition);
		String startDate = request.getParameter("startDate");
				System.out.println(startDate);
		String endDate = request.getParameter("endDate");
				System.out.println(endDate);
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList = productionSF
					.getContractDetailListInMpsAvailable(searchCondition, startDate, endDate);
													   //contractDate, 2019-07-01, 2019-07-31
			map.put("gridRowJson", contractDetailInMpsAvailableList);
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
			logger.debug("MpsController : searchContractDetailListInMpsAvailable 종료");
		}
		return null;
	}

	public ModelAndView searchSalesPlanListInMpsAvailable(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsController : searchSalesPlanListInMpsAvailable 시작");
		}

		String searchCondition = request.getParameter("searchCondition");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList = productionSF
					.getSalesPlanListInMpsAvailable(searchCondition, startDate, endDate);

			map.put("gridRowJson", salesPlanInMpsAvailableList);
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
			logger.debug("MpsController : searchSalesPlanListInMpsAvailable 종료");
		}
		return null;
	}

	public ModelAndView convertContractDetailToMps(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsController : convertContractDetailToMps 시작");
		}

		String batchList = request.getParameter("batchList");
				System.out.println("batchList = "+batchList);
		ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList = gson.fromJson(batchList,
				new TypeToken<ArrayList<ContractDetailInMpsAvailableTO>>() {
				}.getType());
				//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용

				System.out.println("contractDetailInMpsAvailableList = "+contractDetailInMpsAvailableList);
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			HashMap<String, Object> resultMap = productionSF
					.convertContractDetailToMps(contractDetailInMpsAvailableList);

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
			logger.debug("MpsController : convertContractDetailToMps 종료");
		}
		return null;
	}

	public ModelAndView convertSalesPlanToMps(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsController : convertSalesPlanToMps 시작");
		}

		String batchList = request.getParameter("batchList");

		ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList = gson.fromJson(batchList,
				new TypeToken<ArrayList<SalesPlanInMpsAvailableTO>>() {
				}.getType());

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			HashMap<String, Object> resultMap = productionSF.convertSalesPlanToMps(salesPlanInMpsAvailableList);

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
			logger.debug("MpsController : convertSalesPlanToMps 종료");
		}
		return null;
	}

}
