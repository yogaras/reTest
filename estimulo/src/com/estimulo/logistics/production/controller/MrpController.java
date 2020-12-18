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
import com.estimulo.logistics.production.to.MrpGatheringTO;
import com.estimulo.logistics.production.to.MrpTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MrpController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(MrpController.class);
	
	// serviceFacade 참조변수 선언
	ProductionServiceFacade productionSF = ProductionServiceFacadeImpl.getInstance();
	
	// gson 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환
	
	
	
	public ModelAndView getMrpList(HttpServletRequest request, HttpServletResponse response) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("MrpController : getMrpList 시작");
		}
		
		String mrpGatheringStatusCondition = request.getParameter("mrpGatheringStatusCondition");
		String dateSearchCondition = request.getParameter("dateSearchCondition");
		String mrpStartDate = request.getParameter("mrpStartDate");
		String mrpEndDate = request.getParameter("mrpEndDate");
		String mrpGatheringNo = request.getParameter("mrpGatheringNo");
	
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;
		
		try {
			out = response.getWriter();

			ArrayList<MrpTO> mrpList = null;
			
		
			if(mrpGatheringStatusCondition != null ) {
				//여기 null이라는 스트링값이 담겨저왔으니 null은 아님. 객체가있는상태.
				
				mrpList = productionSF.searchMrpList(mrpGatheringStatusCondition);
				
			} else if (dateSearchCondition != null) {
				
				mrpList = productionSF.searchMrpList(dateSearchCondition, mrpStartDate, mrpEndDate);
				
			} else if (mrpGatheringNo != null) {
				
				mrpList = productionSF.searchMrpListAsMrpGatheringNo(mrpGatheringNo);
				
			}
			
			map.put("gridRowJson", mrpList);
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
			System.out.println("getMrpList - 취합대기 검색/취합 클릭시 값확인 : "+mrpGatheringStatusCondition);
			out.println(gson.toJson(map));
			out.close();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("MrpController : getMrpList 종료");
		}
		return null;
	}
	
	
	public ModelAndView openMrp(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpController : openMrp 시작");
		}
		
		String mpsNoListStr = request.getParameter("mpsNoList");
		System.out.println("mpsNoListStr 값확인 : "+mpsNoListStr);
		
		ArrayList<String> mpsNoArr = gson.fromJson(mpsNoListStr,
				new TypeToken<ArrayList<String>>() { }.getType());		
		//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
		System.out.println("mpsNoArr 값확인 : "+mpsNoArr);
		HashMap<String, Object> resultMap = new HashMap<>();
				PrintWriter out = null;

		try {
			
			out = response.getWriter();

			resultMap = productionSF.openMrp(mpsNoArr);

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
			logger.debug("MrpController : openMrp 종료");
		}
		
		return null;
	}

	
	public ModelAndView registerMrp(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpController : registerMrp 시작");
		}
		
		String batchList = request.getParameter("batchList");
		String mrpRegisterDate = request.getParameter("mrpRegisterDate");

		ArrayList<MrpTO> newMrpList	= gson.fromJson(batchList, 
				new TypeToken<ArrayList<MrpTO>>() { }.getType());	
		//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
		System.out.println("결과 MPS에 등록  batchList -> newMrpList : "+batchList+"->"+newMrpList);
		System.out.println("mrpRegisterDate"+mrpRegisterDate);
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			HashMap<String, Object> resultMap = productionSF.registerMrp(mrpRegisterDate, newMrpList);	 
			System.out.println("resultMap : "+resultMap);
			
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
			logger.debug("MrpController : registerMrp 종료");
		}
		return null;
	}
	
	
	
	public ModelAndView getMrpGatheringList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpController : getMrpGatheringList 시작");
		}
		
		String mrpNoList = request.getParameter("mrpNoList");
		
		ArrayList<String> mrpNoArr = gson.fromJson(mrpNoList,
				new TypeToken<ArrayList<String>>() { }.getType());				
			//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<MrpGatheringTO> mrpGatheringList = productionSF.getMrpGathering(mrpNoArr);
			
			map.put("gridRowJson", mrpGatheringList);
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
			logger.debug("MrpController : getMrpGatheringList 종료");
		}
		return null;
	}
	
	

	public ModelAndView registerMrpGathering(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpController : registerMrpGathering 시작");
		}
		
		String mrpGatheringRegisterDate = request.getParameter("mrpGatheringRegisterDate"); //선택한날짜
		String batchList = request.getParameter("batchList"); //소요량 취합결과 그리드에 뿌려진 데이터값
		String mrpNoAndItemCodeList = request.getParameter("mrpNoAndItemCodeList"); //mprNO : ItemCode 
		System.out.println("mrpRegisterAndGather.jsp -> 넘어온 파라미터값 확인 ↓ ");
		System.out.println("mrpGatheringRegisterDate : "+mrpGatheringRegisterDate);
		System.out.println("batchList : "+batchList);
		System.out.println("mrpNoAndItemCodeList : "+mrpNoAndItemCodeList);
		System.out.println(" TYPE Token 값 확인  ↓");
		
		
		ArrayList<MrpGatheringTO> newMrpGatheringList
			= gson.fromJson(batchList, //소요량 취합결과 그리드에 뿌려진 데이터값
					new TypeToken<ArrayList<MrpGatheringTO>>() { }.getType());	
			//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
		HashMap<String, String> mrpNoAndItemCodeMap 
			=  gson.fromJson(mrpNoAndItemCodeList, //mprNO : ItemCode 
					new TypeToken<HashMap<String, String>>() { }.getType());	
		System.out.println("newMrpGatheringList : "+newMrpGatheringList);	
		System.out.println("mrpNoAndItemCodeMap : "+mrpNoAndItemCodeMap);
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			HashMap<String, Object> resultMap 
				= productionSF.registerMrpGathering(mrpGatheringRegisterDate, newMrpGatheringList, mrpNoAndItemCodeMap);	 
			
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
			logger.debug("MrpController : registerMrpGathering 종료");
		}
		return null;
	}
	

	public ModelAndView searchMrpGathering(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpController : searchMrpGathering 시작");
		}
		
		String searchDateCondition = request.getParameter("searchDateCondition");
		String startDate = request.getParameter("mrpGatheringStartDate");
		String endDate = request.getParameter("mrpGatheringEndDate");
		
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<MrpGatheringTO> mrpGatheringList = 
					productionSF.searchMrpGatheringList(searchDateCondition, startDate, endDate);
			
			map.put("gridRowJson", mrpGatheringList);
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
			logger.debug("MrpController : searchMrpGathering 종료");
		}
		return null;
	}
	
	
}
