package com.estimulo.logistics.sales.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.sales.dao.SalesPlanDAO;
import com.estimulo.logistics.sales.dao.SalesPlanDAOImpl;
import com.estimulo.logistics.sales.to.SalesPlanTO;

public class SalesPlanApplicationServiceImpl implements SalesPlanApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(SalesPlanApplicationServiceImpl.class);

	// 싱글톤
	private static SalesPlanApplicationService instance = new SalesPlanApplicationServiceImpl();

	private SalesPlanApplicationServiceImpl() {
	}

	public static SalesPlanApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ SalesPlanApplicationService 객체접근");
		}

		return instance;
	}

	// DAO 참조변수 선언
	private static SalesPlanDAO salesPlanDAO = SalesPlanDAOImpl.getInstance();

	public ArrayList<SalesPlanTO> getSalesPlanList(String dateSearchCondition, String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanApplicationService : getSalesPlanList 시작");
		}

		ArrayList<SalesPlanTO> salesPlanTOList = null;

		try {

			salesPlanTOList = salesPlanDAO.selectSalesPlanList(dateSearchCondition, startDate, endDate);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanApplicationService : getSalesPlanList 종료");
		}
		return salesPlanTOList;
	}

	public String getNewSalesPlanNo(String salesPlanDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanApplicationService : getNewSalesPlanNo 시작");
		}

		StringBuffer newEstimateNo = null;

		try {

			int newNo = salesPlanDAO.selectSalesPlanCount(salesPlanDate);

			newEstimateNo = new StringBuffer();

			newEstimateNo.append("SA");
			newEstimateNo.append(salesPlanDate.replace("-", ""));
			newEstimateNo.append(String.format("%02d", newNo)); // 2자리 숫자

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanApplicationService : getNewSalesPlanNo 종료");
		}
		return newEstimateNo.toString();
	}

	@Override
	public HashMap<String, Object> batchSalesPlanListProcess(ArrayList<SalesPlanTO> salesPlanTOList) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanApplicationService : batchSalesPlanListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			for (SalesPlanTO bean : salesPlanTOList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					// 새로운 판매계획일련번호 생성
					String newSalesPlanNo = getNewSalesPlanNo(bean.getSalesPlanDate());

					// Bean 에 새로운 판매계획일련번호 세팅
					bean.setSalesPlanNo(newSalesPlanNo);

					salesPlanDAO.insertSalesPlan(bean);

					insertList.add(newSalesPlanNo);

					break;

				case "UPDATE":

					salesPlanDAO.updateSalesPlan(bean);

					updateList.add(bean.getSalesPlanNo());

					break;

				case "DELETE":

					salesPlanDAO.deleteSalesPlan(bean);

					deleteList.add(bean.getSalesPlanNo());

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
			logger.debug("SalesPlanApplicationService : batchSalesPlanListProcess 종료");
		}
		return resultMap;

	}

}
