package com.estimulo.logistics.purchase.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.purchase.dao.OrderDAO;
import com.estimulo.logistics.purchase.dao.OrderDAOImpl;
import com.estimulo.logistics.purchase.to.OrderInfoTO;
public class OrderApplicationServiceImpl implements OrderApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(BomApplicationServiceImpl.class);

	// 싱글톤
	private static OrderApplicationService instance = new OrderApplicationServiceImpl();

	private OrderApplicationServiceImpl() {
	}

	public static OrderApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ OrderApplicationServiceImpl 객체접근");
		}

		return instance;
	}

	// DAO 참조변수 선언
	private static OrderDAO orderDAO = OrderDAOImpl.getInstance();

	@Override
	public HashMap<String,Object> getOrderList(String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : getOrderList 시작");
		}

        HashMap<String,Object> resultMap = null;

		try {

			resultMap = orderDAO.getOrderList(startDate, endDate);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : getOrderList 종료");
		}
		return resultMap;
	}

	@Override
	public HashMap<String,Object> getOrderDialogInfo(ArrayList<String> mrpNoArr) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : getOrderDialogInfo 시작");
		}

        HashMap<String,Object> resultMap = null;

		try {

			String mrpNoList = mrpNoArr.toString().replace("[", "").replace("]", "");
			System.out.println("mrpNoList = "+mrpNoList);
			resultMap = orderDAO.getOrderDialogInfo(mrpNoList);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : getOrderDialogInfo 종료");
		}
		return resultMap;
	}

	@Override
	public HashMap<String,Object> order(ArrayList<String> mrpGaNoArr) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : order 시작");
		}
        
		HashMap<String,Object> resultMap = null;
        
		try {
			String mpsNoList = mrpGaNoArr.toString().replace("[", "").replace("]", "");
			resultMap = orderDAO.order(mpsNoList);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : order 종료");
		}

    	return resultMap;
		
	}

	@Override
	public HashMap<String,Object> optionOrder(String itemCode, String itemAmount) {
		// TODO Auto-generated method stub
		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : optionOrder 시작");
		}
		
        HashMap<String,Object> resultMap = null;
        
		try {

			resultMap = orderDAO.optionOrder(itemCode, itemAmount);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : optionOrder 종료");
		}
		
    	return resultMap;
		
	}

	@Override
	public ArrayList<OrderInfoTO> getOrderInfoListOnDelivery() {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : getOrderInfoListOnDelivery 시작");
		}

		ArrayList<OrderInfoTO> orderInfoListOnDelivery = null;

		try {

			orderInfoListOnDelivery = orderDAO.getOrderInfoListOnDelivery();

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : getOrderInfoListOnDelivery 종료");
		}
		return orderInfoListOnDelivery;
	}

	@Override
	public ArrayList<OrderInfoTO> getOrderInfoList(String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : getOrderInfoList 시작");
		}

		ArrayList<OrderInfoTO> orderInfoList  = null;

		try {

			orderInfoList = orderDAO.getOrderInfoList(startDate,endDate);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrderApplicationServiceImpl : getOrderInfoList 종료");
		}
		return orderInfoList;
		
	}

}
