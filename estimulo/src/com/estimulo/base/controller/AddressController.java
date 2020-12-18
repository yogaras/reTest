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
import com.estimulo.base.to.AddressTO;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AddressController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(AddressController.class);

	// serviceFacade 참조변수 선언
	private static BaseServiceFacade baseSF = BaseServiceFacadeImpl.getInstance();

	// GSON 라이브러리F
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	public ModelAndView searchAddressList(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("AddressController : searchAddressList 시작");
		}

		String sidoName = request.getParameter("sidoName");
		String searchAddressType = request.getParameter("searchAddressType");
		String searchValue = request.getParameter("searchValue");
		String mainNumber = request.getParameter("mainNumber");

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		try {
			out = response.getWriter();

			ArrayList<AddressTO> addressList = baseSF.getAddressList(sidoName, searchAddressType, searchValue,
					mainNumber);

			map.put("addressList", addressList);
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
			logger.debug("AddressController : searchAddressList 종료");
		}
		return null;

	}

}
