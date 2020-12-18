package com.estimulo.authorityManager.serviceFacade;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.authorityManager.applicationService.LogInApplicationService;
import com.estimulo.authorityManager.applicationService.LogInApplicationServiceImpl;
import com.estimulo.authorityManager.applicationService.UserMenuApplicationService;
import com.estimulo.authorityManager.applicationService.UserMenuApplicationServiceImpl;
import com.estimulo.authorityManager.exception.IdNotFoundException;
import com.estimulo.authorityManager.exception.PwMissMatchException;
import com.estimulo.authorityManager.exception.PwNotFoundException;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.hr.to.EmpInfoTO;

public class AuthorityManagerServiceFacadeImpl implements AuthorityManagerServiceFacade {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(AuthorityManagerServiceFacadeImpl.class);

	// 싱글톤
	private static AuthorityManagerServiceFacade instance = new AuthorityManagerServiceFacadeImpl();

	private AuthorityManagerServiceFacadeImpl() {
	}

	public static AuthorityManagerServiceFacade getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ AuthorityManagerServiceFacadeImpl");
		}

		return instance;
	}

	// AS 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

	private static LogInApplicationService logInAS = LogInApplicationServiceImpl.getInstance();
	private static UserMenuApplicationService userMenuAS = UserMenuApplicationServiceImpl.getInstance();

	@Override
	public EmpInfoTO accessToAuthority(String companyCode, String workplaceCode, String inputId, String inputPassWord)
			throws IdNotFoundException, PwMissMatchException, PwNotFoundException {

		if (logger.isDebugEnabled()) {
			logger.debug("LogInServiceFacadeImpl : accessToAuthority 객체접근");
		}

		dataSourceTransactionManager.beginTransaction();	//Auto Commit 막아주는 아이   
		EmpInfoTO TO = null;

		try {

			TO = logInAS.accessToAuthority(companyCode, workplaceCode, inputId, inputPassWord);		//문제가 없을 경우 로그인 한 사람의 정보가 담겨짐
			dataSourceTransactionManager.commitTransaction();			// Commit 해주고 Close 해주는 메서드 

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogInServiceFacadeImpl : accessToAuthority 종료");
		}
		return TO;
	}

	@Override
	public String getUserMenuCode(String workplaceCode, String deptCode, String positionCode,
			ServletContext application) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("LogInServiceFacadeImpl : getUserMenuCode 시작");
		}
		
		dataSourceTransactionManager.beginTransaction();	//DB 연동 메서드 인거 같다
		System.out.println("		@ DB 연동 : getUserMenuCode");

		String userMenuCode = null;

		try {
		
			userMenuCode = userMenuAS.getUserMenuCode(workplaceCode, deptCode, positionCode, application);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		} 
		
		if (logger.isDebugEnabled()) {
			logger.debug("LogInServiceFacadeImpl : getUserMenuCode 종료");
		}
		return userMenuCode;
	}

}
