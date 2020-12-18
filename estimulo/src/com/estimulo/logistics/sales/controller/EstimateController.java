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
import com.estimulo.logistics.sales.to.EstimateDetailTO;
import com.estimulo.logistics.sales.to.EstimateTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class EstimateController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(EstimateController.class);

	// serviceFacade 참조변수 선언
	SalesServiceFacade salesSF = SalesServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	public ModelAndView searchEstimateInfo(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateController : searchEstimateInfo 시작");
		}

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String dateSearchCondition = request.getParameter("dateSearchCondition");

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<EstimateTO> estimateTOList = salesSF.getEstimateList(dateSearchCondition, startDate, endDate);

			map.put("gridRowJson", estimateTOList);
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
			logger.debug("EstimateController : searchEstimateInfo 종료");
		}
		return null;
	}

	public ModelAndView searchEstimateDetailInfo(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateController : searchEstimateDetailInfo 시작");
		}

		String estimateNo = request.getParameter("estimateNo");

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<EstimateDetailTO> estimateDetailTOList = salesSF.getEstimateDetailList(estimateNo);

			map.put("gridRowJson", estimateDetailTOList);
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
			logger.debug("EstimateController : searchEstimateDetailInfo 종료");
		}
		return null;
	}

	public ModelAndView addNewEstimate(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateController : addNewEstimate 시작");
		}

		String estimateDate = request.getParameter("estimateDate");
		System.out.println("            estimateDate : " + estimateDate); //견적일자
		String newEstimateInfo = request.getParameter("newEstimateInfo"); //견적과 견적상세의 데이터값의 배열
		System.out.println("            newEstimateInfo : " + newEstimateInfo);
		EstimateTO newEstimateTO = gson.fromJson(newEstimateInfo, EstimateTO.class);
		// newEstimateInfo 는 뷰단에서 보낸 json 문자열 => 문자열을 자바 객체로 변환

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			HashMap<String, Object> resultList = salesSF.addNewEstimate(estimateDate, newEstimateTO);
			//견적일자와 견적,견적상세의 json객체를 EstimateTO로 변환한 newEstimateTO를 map에 담음
			map.put("result", resultList);
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
			out.println(gson.toJson(map));// 자바 객체를 json 형식의 문자열로 바꿈
			out.close();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateController : addNewEstimate 종료");
		}
		return null;
	}

	public ModelAndView batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateController : batchListProcess 시작");
		}

		String batchList = request.getParameter("batchList");

		ArrayList<EstimateDetailTO> estimateDetailTOList = gson.fromJson(batchList,
				new TypeToken<ArrayList<EstimateDetailTO>>() {
				}.getType());

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			HashMap<String, Object> resultList = salesSF.batchEstimateDetailListProcess(estimateDetailTOList);

			map.put("result", resultList);
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
			logger.debug("EstimateController : batchListProcess 종료");
		}
		return null;
	}

}
