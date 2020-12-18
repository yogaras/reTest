package com.estimulo.logistics.sales.applicationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.sales.dao.DeliveryDAO;
import com.estimulo.logistics.sales.dao.DeliveryDAOImpl;
import com.estimulo.logistics.sales.to.DeliveryInfoTO;

public class DeliveryApplicationServiceImpl implements DeliveryApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(EstimateApplicationServiceImpl.class);

	// 싱글톤
	private static DeliveryApplicationService instance = new DeliveryApplicationServiceImpl();

	private DeliveryApplicationServiceImpl() {
	}

	public static DeliveryApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ DeliveryApplicationService 객체접근");
		}

		return instance;
	}

	private static DeliveryDAO deliveryDAO = DeliveryDAOImpl.getInstance();

	@Override
	public ArrayList<DeliveryInfoTO> getDeliveryInfoList() {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryApplicationServiceImpl : getDeliveryInfoList 시작");
		}

		ArrayList<DeliveryInfoTO> deliveryInfoList = null;

		try {

			deliveryInfoList = deliveryDAO.selectDeliveryInfoList();

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryApplicationServiceImpl : getDeliveryInfoList 종료");
		}
		return deliveryInfoList;
	}

	@Override
	public HashMap<String, Object> batchDeliveryListProcess(List<DeliveryInfoTO> deliveryTOList) {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryApplicationServiceImpl : batchDeliveryListProcess 시작");
		}
		
		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			for (DeliveryInfoTO bean : deliveryTOList) {

				String status = bean.getStatus();

				switch (status.toUpperCase()) {

				case "INSERT":

					// 새로운 일련번호 생성
					String newDeliveryNo = "새로운";

					// Bean 에 새로운 일련번호 세팅
					bean.setDeliveryNo(newDeliveryNo);
					deliveryDAO.insertDeliveryResult(bean);
					insertList.add(newDeliveryNo);

					break;

				case "UPDATE":

					deliveryDAO.updateDeliveryResult(bean);

					updateList.add(bean.getDeliveryNo());

					break;

				case "DELETE":

					deliveryDAO.deleteDeliveryResult(bean);

					deleteList.add(bean.getDeliveryNo());

					break;

				}

			}

			resultMap.put("INSERT", insertList);
			resultMap.put("UPDATE", updateList);
			resultMap.put("DELETE", deleteList);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		
		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryApplicationServiceImpl : batchDeliveryListProcess 종료");
		}
		return resultMap;
	}

	@Override
	public HashMap<String,Object> deliver(String contractDetailNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryApplicationServiceImpl : deliver 시작");
		}

        HashMap<String,Object> resultMap = null;

		try {

			resultMap = deliveryDAO.deliver(contractDetailNo);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryApplicationServiceImpl : deliver 종료");
		}
		return resultMap;
	}
	
}
