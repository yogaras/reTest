package com.estimulo.basicInfo.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.basicInfo.applicationService.CustomerApplicationService;
import com.estimulo.basicInfo.applicationService.CustomerApplicationServiceImpl;
import com.estimulo.basicInfo.applicationService.FinancialAccountAssociatesApplicationService;
import com.estimulo.basicInfo.applicationService.FinancialAccountAssociatesApplicationServiceImpl;
import com.estimulo.basicInfo.to.CustomerTO;
import com.estimulo.basicInfo.to.FinancialAccountAssociatesTO;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;

public class CooperatorServiceFacadeImpl implements CooperatorServiceFacade {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(CooperatorServiceFacadeImpl.class);

	// 싱글톤
	private static CooperatorServiceFacade instance = new CooperatorServiceFacadeImpl();

	private CooperatorServiceFacadeImpl() {
	}

	public static CooperatorServiceFacade getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ CooperatorServiceFacadeImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();
	private static CustomerApplicationService customerAS = CustomerApplicationServiceImpl.getInstance();
	private static FinancialAccountAssociatesApplicationService associatsAS = FinancialAccountAssociatesApplicationServiceImpl
			.getInstance();

	@Override
	public ArrayList<CustomerTO> getCustomerList(String searchCondition, String companyCode, String workplaceCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("CooperatorServiceFacadeImpl : getCustomerList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<CustomerTO> customerList = null;

		try {

			customerList = customerAS.getCustomerList(searchCondition, companyCode, workplaceCode);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CooperatorServiceFacadeImpl : getCustomerList 종료");
		}

		return customerList;
	}

	@Override
	public HashMap<String, Object> batchCustomerListProcess(ArrayList<CustomerTO> customerList) {

		if (logger.isDebugEnabled()) {
			logger.debug("CooperatorServiceFacadeImpl : batchCustomerListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = customerAS.batchCustomerListProcess(customerList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CooperatorServiceFacadeImpl : batchCustomerListProcess 시작");
		}
		return resultMap;

	}

	@Override
	public ArrayList<FinancialAccountAssociatesTO> getFinancialAccountAssociatesList(String searchCondition,
			String workplaceCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("CooperatorServiceFacadeImpl : getFinancialAccountAssociatesList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<FinancialAccountAssociatesTO> financialAccountAssociatesList = null;

		try {

			financialAccountAssociatesList = associatsAS.getFinancialAccountAssociatesList(searchCondition,
					workplaceCode);

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CooperatorServiceFacadeImpl : getFinancialAccountAssociatesList 종료");
		}
		return financialAccountAssociatesList;

	}

	@Override
	public HashMap<String, Object> batchFinancialAccountAssociatesListProcess(
			ArrayList<FinancialAccountAssociatesTO> financialAccountAssociatesList) {
		if (logger.isDebugEnabled()) {
			logger.debug("CooperatorServiceFacadeImpl : batchFinancialAccountAssociatesListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = associatsAS.batchFinancialAccountAssociatesListProcess(financialAccountAssociatesList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CooperatorServiceFacadeImpl : batchFinancialAccountAssociatesListProcess 시작");
		}
		return resultMap;
	}

}
