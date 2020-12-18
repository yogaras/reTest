package com.estimulo.logistics.purchase.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.purchase.applicationService.BomApplicationService;
import com.estimulo.logistics.purchase.applicationService.BomApplicationServiceImpl;
import com.estimulo.logistics.purchase.applicationService.OrderApplicationService;
import com.estimulo.logistics.purchase.applicationService.OrderApplicationServiceImpl;
import com.estimulo.logistics.purchase.applicationService.StockApplicationService;
import com.estimulo.logistics.purchase.applicationService.StockApplicationServiceImpl;
import com.estimulo.logistics.purchase.to.BomDeployTO;
import com.estimulo.logistics.purchase.to.BomInfoTO;
import com.estimulo.logistics.purchase.to.BomTO;
import com.estimulo.logistics.purchase.to.OrderInfoTO;
import com.estimulo.logistics.purchase.to.StockLogTO;
import com.estimulo.logistics.purchase.to.StockTO;

public class PurchaseServiceFacadeImpl implements PurchaseServiceFacade {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(PurchaseServiceFacadeImpl.class);

	// 싱글톤
	private static PurchaseServiceFacade instance = new PurchaseServiceFacadeImpl();

	private PurchaseServiceFacadeImpl() {
	}

	public static PurchaseServiceFacade getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ PurchaseServiceFacadeImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();
	private static BomApplicationService bomAS = BomApplicationServiceImpl.getInstance();
	private static OrderApplicationService orderAS = OrderApplicationServiceImpl.getInstance();
	private static StockApplicationService stockAS = StockApplicationServiceImpl.getInstance();

	@Override
	public ArrayList<BomDeployTO> getBomDeployList(String deployCondition, String itemCode,
			String itemClassificationCondition) {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getBomDeployList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<BomDeployTO> bomDeployList = null;

		try {

			bomDeployList = bomAS.getBomDeployList(deployCondition, itemCode, itemClassificationCondition);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getBomDeployList 시작");
		}

		return bomDeployList;
	}

	@Override
	public ArrayList<BomInfoTO> getBomInfoList(String parentItemCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getBomInfoList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<BomInfoTO> bomInfoList = null;

		try {

			bomInfoList = bomAS.getBomInfoList(parentItemCode);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getBomInfoList 시작");
		}

		return bomInfoList;
	}

	@Override
	public HashMap<String,Object> getOrderList(String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getOrderList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
        HashMap<String,Object> resultMap = null;

		try {

			resultMap = orderAS.getOrderList(startDate, endDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getOrderList 끝");
		}

		return resultMap;
	}

	@Override
	public ArrayList<BomInfoTO> getAllItemWithBomRegisterAvailable() {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getAllItemWithBomRegisterAvailable 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<BomInfoTO> allItemWithBomRegisterAvailable = null;

		try {

			allItemWithBomRegisterAvailable = bomAS.getAllItemWithBomRegisterAvailable();
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getAllItemWithBomRegisterAvailable 시작");
		}

		return allItemWithBomRegisterAvailable;
	}

	@Override
	public HashMap<String, Object> batchBomListProcess(ArrayList<BomTO> batchBomList) {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : batchBomListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = bomAS.batchBomListProcess(batchBomList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : batchBomListProcess 시작");
		}

		return resultMap;

	}

	@Override
	public HashMap<String,Object> getOrderDialogInfo(ArrayList<String> mrpNoArr) {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : batchBomListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
        HashMap<String,Object> resultMap = null;
        
		try {

			resultMap = orderAS.getOrderDialogInfo(mrpNoArr);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : batchBomListProcess 시작");
		}

		return resultMap;

	}

	@Override
	public HashMap<String,Object> order(ArrayList<String> mrpGaNoArr) {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : order 시작");
		}

		dataSourceTransactionManager.beginTransaction();
        HashMap<String,Object> resultMap = null;
		
		try {

			resultMap = orderAS.order(mrpGaNoArr);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : order 끝");
		}

    	return resultMap;
		
	}

	@Override
	public HashMap<String,Object> optionOrder(String itemCode, String itemAmount) {
		// TODO Auto-generated method stub
		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : optionOrder 시작");
		}

		dataSourceTransactionManager.beginTransaction();
        HashMap<String,Object> resultMap = null;
		
		try {

			resultMap = orderAS.optionOrder(itemCode, itemAmount);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : optionOrder 시작");
		}
		
    	return resultMap;
		
	}

	@Override
	public ArrayList<StockTO> getStockList() {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getStockList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<StockTO> stockList = null;

		try {

			stockList = stockAS.getStockList();
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getStockList 끝");
		}

		return stockList;
	}

	@Override
	public ArrayList<StockLogTO> getStockLogList(String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getStockLogList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<StockLogTO> stockLogList = null;

		try {

			stockLogList = stockAS.getStockLogList(startDate, endDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getStockLogList 끝");
		}

		return stockLogList;
	}

	@Override
	public ArrayList<OrderInfoTO> getOrderInfoListOnDelivery() {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getOrderInfoListOnDelivery 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<OrderInfoTO> orderInfoListOnDelivery = null;

		try {

			orderInfoListOnDelivery = orderAS.getOrderInfoListOnDelivery();
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getOrderInfoListOnDelivery 시작");
		}

		return orderInfoListOnDelivery;

	}

	@Override
	public HashMap<String,Object> warehousing(ArrayList<String> orderNoArr) {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : warehousing 시작");
		}

		dataSourceTransactionManager.beginTransaction();
        HashMap<String,Object> resultMap = null;
		
		try {

			resultMap = stockAS.warehousing(orderNoArr);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : warehousing 끝");
		}
    	return resultMap;
	}

	@Override
	public ArrayList<OrderInfoTO> getOrderInfoList(String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getOrderInfoList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<OrderInfoTO> orderInfoList  = null;

		try {

			orderInfoList = orderAS.getOrderInfoList(startDate,endDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PurchaseServiceFacadeImpl : getOrderInfoList 시작");
		}

		return orderInfoList;

	}

}
