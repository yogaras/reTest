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
import com.estimulo.logistics.production.to.ProductionPerformanceInfoTO;
import com.estimulo.logistics.production.to.WorkOrderInfoTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class WorkOrderController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(WorkOrderController.class);

	// serviceFacade 참조변수 선언
	ProductionServiceFacade productionSF = ProductionServiceFacadeImpl.getInstance();

	// gson 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // serializeNulls() 속성값이 null 인 속성도 json 변환

	public ModelAndView getWorkOrderableMrpList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderController : getWorkOrderList 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			resultMap = productionSF.getWorkOrderableMrpList();

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
			logger.debug("WorkOrderController : getWorkOrderList 종료");
		}
		return null;
	}

	public ModelAndView showWorkOrderDialog(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderController : showWorkOrderDialog 시작");
		}

		PrintWriter out = null;

		String mrpNo = request.getParameter("mrpNo");
		
		HashMap<String,Object> resultMap = new HashMap<>();

		try {
			
			out = response.getWriter();
			resultMap = productionSF.getWorkOrderSimulationList(mrpNo);

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
			logger.debug("WorkOrderController : showWorkOrderDialog 종료");
		}
		return null;
	}

	public ModelAndView workOrder(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderController : workOrder 시작");
		}
		String workPlaceCode = request.getParameter("workPlaceCode"); //사업장코드
		String productionProcess = request.getParameter("productionProcessCode"); //생산공정코드:PP002
		System.out.println("사업장코드" + workPlaceCode + ":" + "생산공정코드" + productionProcess);
		HashMap<String,Object> resultMap = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			resultMap = productionSF.workOrder(workPlaceCode,productionProcess);

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
			logger.debug("WorkOrderController : workOrder 종료");
		}
		return null;
	}

	public ModelAndView showWorkOrderInfoList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderController : showWorkOrderInfoList 시작");
		}

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		ArrayList<WorkOrderInfoTO> workOrderInfoList = null;

		try {
			out = response.getWriter();

			workOrderInfoList = productionSF.getWorkOrderInfoList();

			map.put("gridRowJson", workOrderInfoList);
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
			logger.debug("WorkOrderController : showWorkOrderInfoList 종료");
		}
		return null;
	}
	
	public ModelAndView workOrderCompletion(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderController : workOrderCompletion 시작");
		}

		String workOrderNo=request.getParameter("workOrderNo");
		String actualCompletionAmount=request.getParameter("actualCompletionAmount");
		HashMap<String, Object> resultMap = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			resultMap = productionSF.workOrderCompletion(workOrderNo,actualCompletionAmount);

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
			logger.debug("WorkOrderController : workOrderCompletion 종료");
		}
		return null;
	}
	
	public ModelAndView getProductionPerformanceInfoList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderController : getProductionPerformanceInfoList 시작");
		}

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;
		ArrayList<ProductionPerformanceInfoTO> productionPerformanceInfoList = null;

		try {
			out = response.getWriter();

			productionPerformanceInfoList = productionSF.getProductionPerformanceInfoList();

			map.put("gridRowJson", productionPerformanceInfoList);
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
			logger.debug("WorkOrderController : getProductionPerformanceInfoList 종료");
		}
		return null;
	}
	
	public ModelAndView showWorkSiteSituation(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderController : showWorkSite 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		PrintWriter out = null;
		
		String workSiteCourse = request.getParameter("workSiteCourse");//원재료검사:RawMaterials,제품제작:Production,판매제품검사:SiteExamine
		String workOrderNo = request.getParameter("workOrderNo");//작업지시일련번호	
		String itemClassIfication = request.getParameter("itemClassIfication");//품목분류:완제품,반제품,재공품	

		try {
			out = response.getWriter();

			resultMap = productionSF.showWorkSiteSituation(workSiteCourse,workOrderNo,itemClassIfication);

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
			logger.debug("WorkOrderController : showWorkSite 종료");
		}
		return null;
	}
	
	public ModelAndView workCompletion(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderController : workCompletion 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		PrintWriter out = null;
		
		String workOrderNo = request.getParameter("workOrderNo"); //작업지시번호
		String itemCode = request.getParameter("itemCode"); //제작품목코드 DK-01 , DK-AP01
		String itemCodeList = request.getParameter("itemCodeList"); //작업품목코드 
		ArrayList<String> itemCodeListArr = gson.fromJson(itemCodeList,
				new TypeToken<ArrayList<String>>() {}.getType());
		//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
		try {
			out = response.getWriter();

			productionSF.workCompletion(workOrderNo,itemCode,itemCodeListArr);

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
			logger.debug("WorkOrderController : workCompletion 종료");
		}
		return null;
	}
	
	public ModelAndView workSiteLogList(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderController : workCompletion 시작");
		}
		
		String workSiteLogDate = request.getParameter("workSiteLogDate");
		
		HashMap<String, Object> resultMap = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			resultMap=productionSF.workSiteLogList(workSiteLogDate);
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
			logger.debug("WorkOrderController : workCompletion 종료");
		}
		return null;
	}
	
}
