package com.estimulo.logistics.logisticsInfo.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.logisticsInfo.applicationService.ItemApplicationService;
import com.estimulo.logistics.logisticsInfo.applicationService.ItemApplicationServiceImpl;
import com.estimulo.logistics.logisticsInfo.applicationService.WarehouseApplicationService;
import com.estimulo.logistics.logisticsInfo.applicationService.WarehouseApplicationServiceImpl;
import com.estimulo.logistics.logisticsInfo.to.ItemInfoTO;
import com.estimulo.logistics.logisticsInfo.to.ItemTO;
import com.estimulo.logistics.logisticsInfo.to.WarehouseTO;

public class LogisticsInfoServiceFacadeImpl implements LogisticsInfoServiceFacade {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(LogisticsInfoServiceFacadeImpl.class);

	// 싱글톤
	private static LogisticsInfoServiceFacade instance = new LogisticsInfoServiceFacadeImpl();

	private LogisticsInfoServiceFacadeImpl() {
	}

	public static LogisticsInfoServiceFacade getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ LogisticsInfoServiceFacadeImpl 객체접근");
		}

		return instance;
	}
	
	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();
	private static ItemApplicationService itemAS = ItemApplicationServiceImpl.getInstance();
	private static WarehouseApplicationService warehouseAS = WarehouseApplicationServiceImpl.getInstance();
	
	@Override
	public ArrayList<ItemInfoTO> getItemInfoList(String searchCondition, String[] paramArray) {

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : getItemInfoList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<ItemInfoTO> itemInfoList = null;

		try {

			itemInfoList = itemAS.getItemInfoList(searchCondition, paramArray);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : getItemInfoList 종료");
		}

		return itemInfoList;
	}

	@Override
	public HashMap<String, Object> batchItemListProcess(ArrayList<ItemTO> itemTOList) {
		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : batchItemListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = itemAS.batchItemListProcess(itemTOList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : batchItemListProcess 종료");
		}
		return resultMap;
	}

	@Override
	public ArrayList<WarehouseTO> getWarehouseInfoList() {
		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : getWarehouseInfoList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<WarehouseTO> warehouseInfoList = null;

		try {

			warehouseInfoList = warehouseAS.getWarehouseInfoList();
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : batchItemListProcess 종료");
		}
		return warehouseInfoList;
	}

	@Override
	public void modifyWarehouseInfo(WarehouseTO warehouseTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String findLastWarehouseCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStandardUnitPrice(String itemCode) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : getStandardUnitPrice 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		int price = 0;
		
		try {

			price = itemAS.getStandardUnitPrice(itemCode);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : getStandardUnitPrice 종료");
		}
		return price;
		
	}
	
	@Override
	public int getStandardUnitPriceBox(String itemCode) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : getStandardUnitPrice 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		int price = 0;
		
		try {

			price = itemAS.getStandardUnitPriceBox(itemCode);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : getStandardUnitPrice 종료");
		}
		return price;
		
	}

}
