package com.estimulo.basicInfo.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.basicInfo.applicationService.CompanyApplicationService;
import com.estimulo.basicInfo.applicationService.CompanyApplicationServiceImpl;
import com.estimulo.basicInfo.applicationService.DepartmentApplicationService;
import com.estimulo.basicInfo.applicationService.DepartmentApplicationServiceImpl;
import com.estimulo.basicInfo.applicationService.WorkplaceApplicationService;
import com.estimulo.basicInfo.applicationService.WorkplaceApplicationServiceImpl;
import com.estimulo.basicInfo.to.CompanyTO;
import com.estimulo.basicInfo.to.DepartmentTO;
import com.estimulo.basicInfo.to.WorkplaceTO;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;

public class OrganizationServiceFacadeImpl implements OrganizationServiceFacade {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(OrganizationServiceFacadeImpl.class);

	// 싱글톤
	private static OrganizationServiceFacade instance = new OrganizationServiceFacadeImpl();

	private OrganizationServiceFacadeImpl() {
	}

	public static OrganizationServiceFacade getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ OrganizationServiceFacadeImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();
	private static CompanyApplicationService companyAS = CompanyApplicationServiceImpl.getInstance();
	private static WorkplaceApplicationService workplaceAS = WorkplaceApplicationServiceImpl.getInstance();
	private static DepartmentApplicationService deptAS = DepartmentApplicationServiceImpl.getInstance();

	@Override
	public ArrayList<CompanyTO> getCompanyList() {

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : getCompanyList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<CompanyTO> companyList = null;

		try {

			companyList = companyAS.getCompanyList();
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : getCompanyList 종료");
		}

		return companyList;
	}

	@Override
	public ArrayList<WorkplaceTO> getWorkplaceList(String companyCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : getWorkplaceList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<WorkplaceTO> workplaceList = null;

		try {

			workplaceList = workplaceAS.getWorkplaceList(companyCode);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : getWorkplaceList 종료");
		}

		return workplaceList;
	}

	@Override
	public ArrayList<DepartmentTO> getDepartmentList(String searchCondition, String companyCode,
			String workplaceCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : getDepartmentList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<DepartmentTO> departmentList = null;

		try {

			departmentList = deptAS.getDepartmentList(searchCondition, companyCode, workplaceCode);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : getDepartmentList 종료");
		}

		return departmentList;
	}

	@Override
	public HashMap<String, Object> batchCompanyListProcess(ArrayList<CompanyTO> companyList) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : batchCompanyListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = companyAS.batchCompanyListProcess(companyList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : batchCompanyListProcess 시작");
		}
		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchWorkplaceListProcess(ArrayList<WorkplaceTO> workplaceList) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : batchWorkplaceListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = workplaceAS.batchWorkplaceListProcess(workplaceList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : batchWorkplaceListProcess 시작");
		}
		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchDepartmentListProcess(ArrayList<DepartmentTO> departmentList) {

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : batchDepartmentListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = deptAS.batchDepartmentListProcess(departmentList);
			dataSourceTransactionManager.commitTransaction(); 

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("OrganizationServiceFacadeImpl : batchDepartmentListProcess 시작");
		}
		return resultMap;
	}

}
