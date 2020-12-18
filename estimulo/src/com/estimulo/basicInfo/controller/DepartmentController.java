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
import com.estimulo.basicInfo.to.DepartmentTO;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DepartmentController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(DepartmentController.class);

	// serviceFacade 참조변수 선언
	OrganizationServiceFacade orgSF = OrganizationServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	public ModelAndView searchDepartmentList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentController :  searchDepartmentList 시작");
		}

		String searchCondition = request.getParameter("searchCondition");

		String companyCode = request.getParameter("companyCode");

		String workplaceCode = request.getParameter("workplaceCode");

		
		ArrayList<DepartmentTO> departmentList = null;

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			departmentList = orgSF.getDepartmentList(searchCondition, companyCode, workplaceCode);

			map.put("gridRowJson", departmentList);
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
			logger.debug("DepartmentController :  searchDepartmentList 종료");
		}
		return null;
	}

	public ModelAndView batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentController : batchListProcess 시작");
		}

		String batchList = request.getParameter("batchList");

		ArrayList<DepartmentTO> departmentList = gson.fromJson(batchList, new TypeToken<ArrayList<DepartmentTO>>() {
		}.getType());

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			HashMap<String, Object> resultMap = orgSF.batchDepartmentListProcess(departmentList);

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
			logger.debug("DepartmentController : batchListProcess 종료");
		}
		return null;
	}
}
