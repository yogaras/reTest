package com.estimulo.base.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.applicationService.AddressApplicationService;
import com.estimulo.base.applicationService.AddressApplicationServiceImpl;
import com.estimulo.base.applicationService.CodeApplicationService;
import com.estimulo.base.applicationService.CodeApplicationServiceImpl;
import com.estimulo.base.to.AddressTO;
import com.estimulo.base.to.CodeDetailTO;
import com.estimulo.base.to.CodeTO;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;

public class BaseServiceFacadeImpl implements BaseServiceFacade {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(BaseServiceFacadeImpl.class);

	// 싱글톤
	private static BaseServiceFacade instance = new BaseServiceFacadeImpl();

	private BaseServiceFacadeImpl() {
	}

	public static BaseServiceFacade getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ BaseServiceFacadeImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();
	private static CodeApplicationService codeAS = CodeApplicationServiceImpl.getInstance();
	private static AddressApplicationService addressAS = AddressApplicationServiceImpl.getInstance();

	@Override
	public ArrayList<CodeDetailTO> getDetailCodeList(String divisionCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("BaseServiceFacadeImpl : getDetailCodeList 시작");
		}

		dataSourceTransactionManager.beginTransaction();//오토 커밋 false작업.. 에러 안나야 커밋하지
		ArrayList<CodeDetailTO> codeDetailList = null;

		try {
			
			codeDetailList = codeAS.getDetailCodeList(divisionCode);
			dataSourceTransactionManager.commitTransaction();//받아 와서 정상적이면 커밋 시작.

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();//에러나모 롤백!!!
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : getDetailCodeList 종료");
		}

		return codeDetailList;
	}

	@Override
	public ArrayList<CodeTO> getCodeList() {

		if (logger.isDebugEnabled()) {
			logger.debug("BaseServiceFacadeImpl : getCodeList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<CodeTO> codeList = null;

		try {

			codeList = codeAS.getCodeList();
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : getCodeList 종료");
		}

		return codeList;
	}

	@Override
	public Boolean checkCodeDuplication(String divisionCode, String newDetailCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("BaseServiceFacadeImpl : checkCodeDuplication 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		Boolean flag = false;

		try {

			flag = codeAS.checkCodeDuplication(divisionCode, newDetailCode);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : checkCodeDuplication 종료");
		}
		return flag;
	}

	@Override
	public HashMap<String, Object> batchCodeListProcess(ArrayList<CodeTO> codeList) {

		if (logger.isDebugEnabled()) {
			logger.debug("BaseServiceFacadeImpl : batchCodeListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = codeAS.batchCodeListProcess(codeList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : batchCodeListProcess 종료");
		}
		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchDetailCodeListProcess(ArrayList<CodeDetailTO> detailCodeList) {

		if (logger.isDebugEnabled()) {
			logger.debug("BaseServiceFacadeImpl : batchDetailCodeListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = codeAS.batchDetailCodeListProcess(detailCodeList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : batchDetailCodeListProcess 종료");
		}
		return resultMap;
	}

	@Override
	public HashMap<String, Object> changeCodeUseCheckProcess(ArrayList<CodeDetailTO> detailCodeList) {

		if (logger.isDebugEnabled()) {
			logger.debug("BaseServiceFacadeImpl : changeCodeUseCheckProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = codeAS.changeCodeUseCheckProcess(detailCodeList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : changeCodeUseCheckProcess 종료");
		}
		return resultMap;
	}

	@Override
	public ArrayList<AddressTO> getAddressList(String sidoName, String searchAddressType, String searchValue, String mainNumber) {

		if (logger.isDebugEnabled()) {
			logger.debug("BaseServiceFacadeImpl : getAddressList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<AddressTO> addressList = null;

		try {

			addressList = addressAS.getAddressList(sidoName, searchAddressType, searchValue, mainNumber);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogisticsInfoServiceFacadeImpl : getAddressList 종료");
		}
		return addressList;
	}

}
