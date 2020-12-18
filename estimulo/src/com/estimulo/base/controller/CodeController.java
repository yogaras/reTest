package com.estimulo.base.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.serviceFacade.BaseServiceFacade;
import com.estimulo.base.serviceFacade.BaseServiceFacadeImpl;
import com.estimulo.base.to.CodeDetailTO;
import com.estimulo.base.to.CodeTO;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class CodeController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(CodeController.class);

	// serviceFacade 참조변수 선언
	private static BaseServiceFacade baseSF = BaseServiceFacadeImpl.getInstance();

	// gson 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	public ModelAndView findCodeList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoController : findCodeList 시작");
		}

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<CodeTO> codeList = baseSF.getCodeList();

			map.put("codeList", codeList);
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
			out.println(gson.toJson(map));		// class -> Json 으로 변환 시켜줌 
			out.close();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoController : findCodeList 종료");
		}
		return null;
	}

	public ModelAndView findDetailCodeList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoController : findDetailCodeList 시작"); //이건 걍 디버그 찍는거 
		}

		String divisionCode = request.getParameter("divisionCode");	//estimateRegister.jsp 에서 날아온값으 받아줌 divisionCode = "CL-01"
		System.out.println(	"@"+divisionCode);
		
		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<CodeDetailTO> detailCodeList = baseSF.getDetailCodeList(divisionCode); 
			;

			map.put("detailCodeList", detailCodeList);
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
			//=> 자바 객체를 json 형식의 문자열로 바꿈 out이 sysout out이 아님ㅋㅋ
			out.close();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoController : findDetailCodeList 종료");
		}
		return null;
	}

	public ModelAndView checkCodeDuplication(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoController : checkCodeDuplication 시작");
		}

		String divisionCode = request.getParameter("divisionCode");
		String newDetailCode = request.getParameter("newCode");

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			Boolean flag = baseSF.checkCodeDuplication(divisionCode, newDetailCode);

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
			logger.debug("LogisticsInfoController : checkCodeDuplication 종료");
		}
		return null;
	}

	public ModelAndView batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoController : batchListProcess 시작");
		}

		String batchList = request.getParameter("batchList");
		String tableName = request.getParameter("tableName");

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<CodeTO> codeList = null;
			ArrayList<CodeDetailTO> detailCodeList = null;
			HashMap<String, Object> resultMap = null;

			if (tableName.equals("CODE")) {

				codeList = gson.fromJson(batchList, new TypeToken<ArrayList<CodeTO>>() {
				}.getType());
				//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
				resultMap = baseSF.batchCodeListProcess(codeList);

			} else if (tableName.equals("CODE_DETAIL")) {

				detailCodeList = gson.fromJson(batchList, new TypeToken<ArrayList<CodeDetailTO>>() {
				}.getType());
				//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
				resultMap = baseSF.batchDetailCodeListProcess(detailCodeList);

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
			logger.debug("LogisticsInfoController : batchListProcess 종료");
		}
		return null;
	}

	
	public ModelAndView changeCodeUseCheckProcess(HttpServletRequest request, HttpServletResponse response) {


		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoController : changeCodeUseCheckProcess 시작");
		}

		String batchList = request.getParameter("batchList");

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<CodeDetailTO> detailCodeList = null;
			HashMap<String, Object> resultMap = null;

			detailCodeList = gson.fromJson(batchList, new TypeToken<ArrayList<CodeDetailTO>>() {
			}.getType());
			//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
			resultMap = baseSF.changeCodeUseCheckProcess(detailCodeList);

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
			logger.debug("LogisticsInfoController : changeCodeUseCheckProcess 종료");
		}
		return null;
	}
	
}
