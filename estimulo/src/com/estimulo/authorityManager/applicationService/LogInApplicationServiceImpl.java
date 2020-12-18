package com.estimulo.authorityManager.applicationService;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.authorityManager.exception.IdNotFoundException;
import com.estimulo.authorityManager.exception.PwMissMatchException;
import com.estimulo.authorityManager.exception.PwNotFoundException;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.hr.dao.EmpSearchingDAO;
import com.estimulo.hr.dao.EmpSearchingDAOImpl;
import com.estimulo.hr.dao.EmployeeSecretDAO;
import com.estimulo.hr.dao.EmployeeSecretDAOImpl;
import com.estimulo.hr.to.EmpInfoTO;
import com.estimulo.hr.to.EmployeeSecretTO;

public class LogInApplicationServiceImpl implements LogInApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(LogInApplicationServiceImpl.class);

	// 싱글톤
	private static LogInApplicationService instance = new LogInApplicationServiceImpl();

	private LogInApplicationServiceImpl() {
	}

	public static LogInApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ LogInApplicationServiceImpl 객체접근");
		}

		return instance;
	}

	// DAO 참조변수 선언
	private static EmpSearchingDAO empSearchDAO = EmpSearchingDAOImpl.getInstance();
	private static EmployeeSecretDAO empSecretDAO = EmployeeSecretDAOImpl.getInstance();

	public EmpInfoTO accessToAuthority(String companyCode, String workplaceCode, String inputId, String inputPassWord)
			throws IdNotFoundException, PwMissMatchException, PwNotFoundException, DataAccessException {

		if (logger.isDebugEnabled()) {
			logger.debug("LogInApplicationServiceImpl : accessToAuthority 시작");
		}

		EmpInfoTO bean = null;

		try {

			bean = checkEmpInfo(companyCode, workplaceCode, inputId);	// 데이터 베이스 에서 우리가 로그인 화면에서 입력한 값을 보내줘서 비교한후 있으면 들고와서 그사람의 정보를 bean 에 담는다
			checkPassWord(companyCode, bean.getEmpCode(), inputPassWord); // 비밀번호를 확인 해주는 메서드 

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogInApplicationServiceImpl : accessToAuthority 종료");
		}
		return bean;
	}

	private EmpInfoTO checkEmpInfo(String companyCode, String workplaceCode, String inputId)
			throws IdNotFoundException {

		if (logger.isDebugEnabled()) {
			logger.debug("LogInApplicationServiceImpl : checkEmpInfo 시작");
		}

		EmpInfoTO bean = null;
		ArrayList<EmpInfoTO> empInfoTOList = null;

		try {

			empInfoTOList = empSearchDAO.getTotalEmpInfo(companyCode, workplaceCode, inputId);

			if (empInfoTOList.size() == 1) {

				for (EmpInfoTO e : empInfoTOList) {
					bean = e;
				}

			} else if (empInfoTOList.size() == 0) {
				throw new IdNotFoundException("입력된 정보에 해당하는 사원은 없습니다.");
			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogInApplicationServiceImpl : checkEmpInfo 종료");
		}
		return bean;
	}

	private void checkPassWord(String companyCode, String empCode, String inputPassWord)
			throws PwMissMatchException, PwNotFoundException {

		if (logger.isDebugEnabled()) {
			logger.debug("LogInApplicationServiceImpl : checkPassWord 시작");
		}

		try {

			EmployeeSecretTO bean = empSecretDAO.selectUserPassWord(companyCode, empCode);

			StringBuffer userPassWord = new StringBuffer();
			if (bean != null) {
				userPassWord.append(bean.getUserPassword());

				// 회원ID 는 있으나 passWord Data 가 없는 경우
			} else if (bean == null || bean.getUserPassword().equals("") || bean.getUserPassword() == null) {
				throw new PwNotFoundException("비밀번호 정보를 찾을 수 없습니다.");
			}

			if (!inputPassWord.equals(userPassWord.toString())) {
				throw new PwMissMatchException("비밀번호가 가입된 정보와 같지 않습니다.");
			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("LogInApplicationServiceImpl : checkPassWord 종료");
		}

	}

}