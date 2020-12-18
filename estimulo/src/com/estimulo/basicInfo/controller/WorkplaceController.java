package com.estimulo.basicInfo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.basicInfo.serviceFacade.OrganizationServiceFacade;
import com.estimulo.basicInfo.serviceFacade.OrganizationServiceFacadeImpl;
import com.estimulo.basicInfo.to.WorkplaceTO;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class WorkplaceController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(WorkplaceController.class);

	// serviceFacade 참조변수 선언
	OrganizationServiceFacade orgSF = OrganizationServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	public ModelAndView searchWorkplaceList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceController : searchWorkplaceList 시작");
		}

		String companyCode = request.getParameter("companyCode");

		ArrayList<WorkplaceTO> workplaceList = null;

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			workplaceList = orgSF.getWorkplaceList(companyCode);

			map.put("gridRowJson", workplaceList);
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
			logger.debug("WorkplaceController : searchWorkplaceList 종료");
		}
		return null;
	}

	public ModelAndView batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceController : batchListProcess 시작");
		}

		String batchList = request.getParameter("batchList");

		ArrayList<WorkplaceTO> workplaceList = gson.fromJson(batchList, new TypeToken<ArrayList<WorkplaceTO>>() {
		}.getType());

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			HashMap<String, Object> resultMap = orgSF.batchWorkplaceListProcess(workplaceList);

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
			logger.debug("WorkplaceController : batchListProcess 종료");
		}
		return null;
	}
}
