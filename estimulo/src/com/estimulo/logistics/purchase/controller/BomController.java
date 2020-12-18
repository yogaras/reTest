package com.estimulo.logistics.purchase.controller;

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
import com.estimulo.logistics.purchase.serviceFacade.PurchaseServiceFacade;
import com.estimulo.logistics.purchase.serviceFacade.PurchaseServiceFacadeImpl;
import com.estimulo.logistics.purchase.to.BomDeployTO;
import com.estimulo.logistics.purchase.to.BomInfoTO;
import com.estimulo.logistics.purchase.to.BomTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class BomController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(BomController.class);

	// serviceFacade 참조변수 선언
	PurchaseServiceFacade purchaseSF = PurchaseServiceFacadeImpl.getInstance();

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	public ModelAndView searchBomDeploy(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("BomController : searchBomDeploy 시작");
		}

		String deployCondition = request.getParameter("deployCondition");
		// System.out.println(deployCondition);
		// forward 정전개 || reverse 역전개
		String itemCode = request.getParameter("itemCode");
		// System.out.println(itemCode);
		// CodeController를 사용하여 검색한 후 선택하여 텍스트박스에 들어있던 값을 파라미터로 받아옴
		// ex ] DK-01
		String itemClassificationCondition = request.getParameter("itemClassificationCondition");
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<BomDeployTO> bomDeployList = purchaseSF.getBomDeployList(deployCondition, itemCode, itemClassificationCondition);

			map.put("gridRowJson", bomDeployList);
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
			logger.debug("BomController : searchBomDeploy 종료");
		}
		return null;
	}

	public ModelAndView searchBomInfo(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("BomController : searchBomInfo 시작");
		}

		String parentItemCode = request.getParameter("parentItemCode");

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<BomInfoTO> bomInfoList = purchaseSF.getBomInfoList(parentItemCode);

			map.put("gridRowJson", bomInfoList);
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
			logger.debug("BomController : searchBomInfo 종료");
		}
		return null;

	}

	public ModelAndView searchAllItemWithBomRegisterAvailable(HttpServletRequest request,
			HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("BomController : searchAllItemWithBomRegisterAvailable 시작");
		}

		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<BomInfoTO> allItemWithBomRegisterAvailable = purchaseSF.getAllItemWithBomRegisterAvailable();

			map.put("gridRowJson", allItemWithBomRegisterAvailable);
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
			logger.debug("BomController : searchAllItemWithBomRegisterAvailable 종료");
		}
		return null;

	}

	public ModelAndView batchBomListProcess(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("BomController : batchBomListProcess 시작");
		}

		String batchList = request.getParameter("batchList");
		// System.out.println(batchList);
		ArrayList<BomTO> batchBomList = gson.fromJson(batchList, new TypeToken<ArrayList<BomTO>>() {
		}.getType());
		//제너릭 클래스를 사용할경우 정해지지 않은 제너릭타입을  명시하기위해서 TypeToken을 사용
		HashMap<String, Object> map = new HashMap<>();

		PrintWriter out = null;

		try {
			out = response.getWriter();

			HashMap<String, Object> resultList = purchaseSF.batchBomListProcess(batchBomList);

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
			logger.debug("BomController : batchBomListProcess 종료");
		}
		return null;

	}

}
