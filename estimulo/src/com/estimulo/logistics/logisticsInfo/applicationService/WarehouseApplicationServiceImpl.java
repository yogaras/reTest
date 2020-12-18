package com.estimulo.logistics.logisticsInfo.applicationService;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.logisticsInfo.dao.WarehouseDAO;
import com.estimulo.logistics.logisticsInfo.dao.WarehouseDAOImpl;
import com.estimulo.logistics.logisticsInfo.to.WarehouseTO;

public class WarehouseApplicationServiceImpl implements WarehouseApplicationService{
	
	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(ItemApplicationServiceImpl.class);

	// 싱글톤
	private static WarehouseApplicationServiceImpl instance = new WarehouseApplicationServiceImpl();

	private WarehouseApplicationServiceImpl() {
	}

	public static WarehouseApplicationServiceImpl getInstance() {

	if (logger.isDebugEnabled()) {
		logger.debug("@ ItemApplicationService 객체접근");
	}
		
		return instance;

	}
	private static WarehouseDAO warehouseDAO = WarehouseDAOImpl.getInstance();
	
	@Override
	public ArrayList<WarehouseTO> getWarehouseInfoList(){

		if (logger.isDebugEnabled()) {
			logger.debug("WarehouseApplicationServiceImpl : getWarehouseList 시작");
		}

		ArrayList<WarehouseTO> warehouseList = null;

		try {

			warehouseList = warehouseDAO.selectWarehouseList();

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("WarehouseApplicationServiceImpl : getWarehouseList 종료");
		}
		return warehouseList;
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
}
