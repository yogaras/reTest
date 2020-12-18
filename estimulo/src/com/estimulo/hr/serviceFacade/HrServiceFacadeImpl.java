package com.estimulo.hr.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.hr.applicationService.EmpApplicationService;
import com.estimulo.hr.applicationService.EmpApplicationServiceImpl;
import com.estimulo.hr.to.EmpInfoTO;
import com.estimulo.hr.to.EmployeeBasicTO;
import com.estimulo.hr.to.EmployeeDetailTO;
import com.estimulo.hr.to.EmployeeSecretTO;

public class HrServiceFacadeImpl implements HrServiceFacade {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(HrServiceFacadeImpl.class);

	// 싱글톤
	private static HrServiceFacade instance = new HrServiceFacadeImpl();

	private HrServiceFacadeImpl() {
	}

	public static HrServiceFacade getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ HrServiceFacadeImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();
	private static EmpApplicationService empAS = EmpApplicationServiceImpl.getInstance();

	@Override
	public ArrayList<EmpInfoTO> getAllEmpList(String searchCondition, String[] paramArray) {

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : getAllEmpList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<EmpInfoTO> empList = null;

		try {

			empList = empAS.getAllEmpList(searchCondition, paramArray);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : getAllEmpList 시작");
		}

		return empList;
	}

	@Override
	public EmpInfoTO getEmpInfo(String companyCode, String empCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : getAllEmpList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		EmpInfoTO TO = null;

		try {

			TO = empAS.getEmpInfo(companyCode, empCode);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : getAllEmpList 시작");
		}

		return TO;
	}


	@Override
	public String getNewEmpCode(String companyCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : getNewEmpCode 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		String newEmpCode = null;

		try {
			newEmpCode = empAS.getNewEmpCode(companyCode);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : getNewEmpCode 시작");
		}
		
		return newEmpCode;
	}
	
	@Override
	public HashMap<String, Object> batchEmpBasicListProcess(ArrayList<EmployeeBasicTO> empBasicList) {

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : batchEmpBasicListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = empAS.batchEmpBasicListProcess(empBasicList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : batchEmpBasicListProcess 시작");
		}

		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchEmpDetailListProcess(ArrayList<EmployeeDetailTO> empDetailList) {
		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : batchEmpDetailListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = empAS.batchEmpDetailListProcess(empDetailList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : batchEmpDetailListProcess 시작");
		}

		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchEmpSecretListProcess(ArrayList<EmployeeSecretTO> empSecretList) {
		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : batchEmpSecretListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = empAS.batchEmpSecretListProcess(empSecretList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : batchEmpSecretListProcess 시작");
		}

		return resultMap;
	}

	@Override
	public Boolean checkUserIdDuplication(String companyCode, String newUserId) {
		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : checkUserIdDuplication 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		Boolean duplicated = false;

		try {
			duplicated = empAS.checkUserIdDuplication(companyCode, newUserId);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : checkUserIdDuplication 시작");
		}

		return duplicated;
	}

	@Override
	public Boolean checkEmpCodeDuplication(String companyCode, String newEmpCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : checkEmpCodeDuplication 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		Boolean duplicated = false;

		try {
			duplicated = empAS.checkEmpCodeDuplication(companyCode, newEmpCode);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("HrServiceFacadeImpl : checkEmpCodeDuplication 시작");
		}

		return duplicated;
	}


}
