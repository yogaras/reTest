package com.estimulo.hr.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.controller.CodeController;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;
import com.estimulo.hr.serviceFacade.HrServiceFacade;
import com.estimulo.hr.serviceFacade.HrServiceFacadeImpl;
import com.estimulo.hr.to.EmpInfoTO;
import com.estimulo.hr.to.EmployeeBasicTO;
import com.estimulo.hr.to.EmployeeDetailTO;
import com.estimulo.hr.to.EmployeeSecretTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class EmpController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(CodeController.class);

	// serviceFacade 참조변수 선언
	HrServiceFacade hrSF = HrServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	public ModelAndView searchAllEmpList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpController : searchAllEmpList 시작");
		}

		String searchCondition = request.getParameter("searchCondition");
		String companyCode = request.getParameter("companyCode");
		String workplaceCode = request.getParameter("workplaceCode");
		String deptCode = request.getParameter("deptCode");

		ArrayList<EmpInfoTO> empList = null;
		String[] paramArray = null;

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			switch (searchCondition) {

			case "ALL":

				paramArray = new String[] { companyCode };
				break;

			case "WORKPLACE":

				paramArray = new String[] { companyCode, workplaceCode };
				break;

			case "DEPT":

				paramArray = new String[] { companyCode, deptCode };
				break;

			case "RETIREMENT":

				paramArray = new String[] { companyCode };
				break;

			}

			empList = hrSF.getAllEmpList(searchCondition, paramArray);

			map.put("gridRowJson", empList);
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
			logger.debug("EmpController : searchAllEmpList 종료");
		}
		return null;
	}

	public ModelAndView searchEmpInfo(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpController : searchEmpInfo 시작");
		}

		String companyCode = request.getParameter("companyCode");
		String empCode = request.getParameter("empCode");

		EmpInfoTO empInfoTO = null;

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {

			out = response.getWriter();

			empInfoTO = hrSF.getEmpInfo(companyCode, empCode);

			map.put("empInfo", empInfoTO);
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
			logger.debug("EmpController : searchEmpInfo 종료");
		}
		return null;

	}

	public ModelAndView checkUserIdDuplication(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpController : checkUserIdDuplication 시작");
		}

		String companyCode = request.getParameter("companyCode");
		String newUserId = request.getParameter("newUseId");
		
		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			Boolean flag = hrSF.checkUserIdDuplication(companyCode, newUserId);

			map.put("result", flag);
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
			logger.debug("EmpController : checkUserIdDuplication 종료");
		}
		return null;
	}

	public ModelAndView checkEmpCodeDuplication(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpController : checkEmpCodeDuplication 시작");
		}

		String companyCode = request.getParameter("companyCode");
		String newEmpCode = request.getParameter("newEmpCode");

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			Boolean flag = hrSF.checkEmpCodeDuplication(companyCode, newEmpCode);

			map.put("result", flag);
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
			logger.debug("EmpController : checkEmpCodeDuplication 종료");
		}
		return null;
	}

	public ModelAndView getNewEmpCode(HttpServletRequest request, HttpServletResponse response) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("EmpController : getNewEmpCode 시작");
		}

		String companyCode = request.getParameter("companyCode");
		String newEmpCode = null;
		
		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			newEmpCode = hrSF.getNewEmpCode(companyCode);

			map.put("newEmpCode", newEmpCode);
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
			logger.debug("EmpController : getNewEmpCode 종료");
		}
		return null;
	}
	
	
	public ModelAndView batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpController : batchListProcess 시작");
		}

		String batchList = request.getParameter("batchList");
		String tableName = request.getParameter("tableName");

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<EmployeeBasicTO> empBasicList = null;
			ArrayList<EmployeeDetailTO> empDetailList = null;
			ArrayList<EmployeeSecretTO> empSecretList = null;

			HashMap<String, Object> resultMap = null;

			if (tableName.equals("BASIC")) {

				empBasicList = gson.fromJson(batchList, new TypeToken<ArrayList<EmployeeBasicTO>>() {
				}.getType());

				resultMap = hrSF.batchEmpBasicListProcess(empBasicList);

			} else if (tableName.equals("DETAIL")) {

				empDetailList = gson.fromJson(batchList, new TypeToken<ArrayList<EmployeeDetailTO>>() {
				}.getType());
				System.out.println(gson.toJson(empDetailList));
				
				resultMap = hrSF.batchEmpDetailListProcess(empDetailList);

			} else if (tableName.equals("SECRET")) {

				empSecretList = gson.fromJson(batchList, new TypeToken<ArrayList<EmployeeSecretTO>>() {
				}.getType());

				System.out.println(gson.toJson(empSecretList));

				
				resultMap = hrSF.batchEmpSecretListProcess(empSecretList);

			}

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
			logger.debug("EmpController : batchListProcess 종료");
		}
		return null;
	}

}
