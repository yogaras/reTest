package com.estimulo.hr.applicationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.dao.CodeDetailDAO;
import com.estimulo.base.dao.CodeDetailDAOImpl;
import com.estimulo.base.to.CodeDetailTO;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.hr.dao.EmpSearchingDAO;
import com.estimulo.hr.dao.EmpSearchingDAOImpl;
import com.estimulo.hr.dao.EmployeeBasicDAO;
import com.estimulo.hr.dao.EmployeeBasicDAOImpl;
import com.estimulo.hr.dao.EmployeeDetailDAO;
import com.estimulo.hr.dao.EmployeeDetailDAOImpl;
import com.estimulo.hr.dao.EmployeeSecretDAO;
import com.estimulo.hr.dao.EmployeeSecretDAOImpl;
import com.estimulo.hr.to.EmpInfoTO;
import com.estimulo.hr.to.EmployeeBasicTO;
import com.estimulo.hr.to.EmployeeDetailTO;
import com.estimulo.hr.to.EmployeeSecretTO;

public class EmpApplicationServiceImpl implements EmpApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(EmpApplicationServiceImpl.class);

	// 싱글톤
	private static EmpApplicationService instance = new EmpApplicationServiceImpl();

	private EmpApplicationServiceImpl() {
	}

	public static EmpApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ EmpApplicationService 객체접근");
		}

		return instance;

	}

	// DAO 참조변수 선언
	private static EmployeeBasicDAO empBasicDAO = EmployeeBasicDAOImpl.getInstance();
	private static EmployeeDetailDAO empDetailDAO = EmployeeDetailDAOImpl.getInstance();
	private static EmployeeSecretDAO empSecretDAO = EmployeeSecretDAOImpl.getInstance();
	private static EmpSearchingDAO empSearchDAO = EmpSearchingDAOImpl.getInstance();
	private static CodeDetailDAO codeDetailDAO = CodeDetailDAOImpl.getInstance();

	public ArrayList<EmpInfoTO> getAllEmpList(String searchCondition, String[] paramArray) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : getAllEmpList 시작");
		}

		ArrayList<EmpInfoTO> empList = null;

		try {

			empList = empSearchDAO.selectAllEmpList(searchCondition, paramArray);

			for (EmpInfoTO bean : empList) {

				bean.setEmpDetailTOList(
						empDetailDAO.selectEmployeeDetailList(bean.getCompanyCode(), bean.getEmpCode()));

				bean.setEmpSecretTOList(
						empSecretDAO.selectEmployeeSecretList(bean.getCompanyCode(), bean.getEmpCode()));

			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : getAllEmpList 종료");
		}
		return empList;

	}

	public EmpInfoTO getEmpInfo(String companyCode, String empCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : getEmpInfo 시작");
		}

		EmpInfoTO bean = new EmpInfoTO();

		try {

			ArrayList<EmployeeDetailTO> empDetailTOList = empDetailDAO.selectEmployeeDetailList(companyCode, empCode);

			ArrayList<EmployeeSecretTO> empSecretTOList = empSecretDAO.selectEmployeeSecretList(companyCode, empCode);

			bean.setEmpDetailTOList(empDetailTOList);
			bean.setEmpSecretTOList(empSecretTOList);

			EmployeeBasicTO basicBean = empBasicDAO.selectEmployeeBasicTO(companyCode, empCode);

			if (basicBean != null) {

				bean.setCompanyCode(companyCode);
				bean.setEmpCode(empCode);
				bean.setEmpName(basicBean.getEmpName());
				bean.setEmpEngName(basicBean.getEmpEngName());
				bean.setSocialSecurityNumber(basicBean.getSocialSecurityNumber());
				bean.setHireDate(basicBean.getHireDate());
				bean.setRetirementDate(basicBean.getRetirementDate());
				bean.setUserOrNot(basicBean.getUserOrNot());
				bean.setBirthDate(basicBean.getBirthDate());
				bean.setGender(basicBean.getGender());

			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : getAllEmpList 종료");
		}
		return bean;

	}

	public String getNewEmpCode(String companyCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : getNewEmpCode 시작");
		}

		ArrayList<EmployeeBasicTO> empBasicList = null;
		String newEmpCode = null;

		try {

			empBasicList = empBasicDAO.selectEmployeeBasicList(companyCode);

			TreeSet<Integer> empCodeNoSet = new TreeSet<>();

			for (EmployeeBasicTO TO : empBasicList) {

				if (TO.getEmpCode().startsWith("EMP-")) {

					try {

						Integer no = Integer.parseInt(TO.getEmpCode().split("EMP-")[1]);
						empCodeNoSet.add(no);

					} catch (NumberFormatException e) {

						// "EMP-" 다음 부분을 Integer 로 변환하지 못하는 경우 : 그냥 다음 반복문 실행

					}

				}

			}

			if (empCodeNoSet.isEmpty()) {
				newEmpCode = "EMP-" + String.format("%03d", 1);
			} else {
				newEmpCode = "EMP-" + String.format("%03d", empCodeNoSet.pollLast() + 1);
			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : getNewEmpCode 종료");
		}
		return newEmpCode;
	}

	public HashMap<String, Object> batchEmpBasicListProcess(ArrayList<EmployeeBasicTO> empBasicList) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : batchEmpBasicListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			// ArrayList<String> updateList = new ArrayList<>();
			// ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeTO = new CodeDetailTO();

			for (EmployeeBasicTO TO : empBasicList) {

				String status = TO.getStatus();

				switch (status) {

				case "INSERT":

					empBasicDAO.insertEmployeeBasic(TO);

					insertList.add(TO.getEmpCode());

					// CODE_DETAIL 테이블에 Insert
					detailCodeTO.setDivisionCodeNo("HR-02");
					detailCodeTO.setDetailCode(TO.getEmpCode());
					detailCodeTO.setDetailCodeName(TO.getEmpEngName());

					codeDetailDAO.insertDetailCode(detailCodeTO);

					break;

				}

			}

			resultMap.put("INSERT", insertList);
			// resultMap.put("UPDATE", updateList);
			// resultMap.put("DELETE", deleteList);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : batchEmpBasicListProcess 종료");
		}
		return resultMap;

	}

	public HashMap<String, Object> batchEmpDetailListProcess(ArrayList<EmployeeDetailTO> empDetailList) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : batchEmpDetailListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			// ArrayList<String> updateList = new ArrayList<>();
			// ArrayList<String> deleteList = new ArrayList<>();

			for (EmployeeDetailTO bean : empDetailList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					empDetailDAO.insertEmployeeDetail(bean);
					insertList.add(bean.getEmpCode());

					// 사원 계정 정지 => EMPLOYEE_BASIC 테이블의 USER_OR_NOT 컬럼을 "N" 으로 변경
					// 새로운 userPassWord 를 null 로 입력
					if (bean.getUpdateHistory().equals("계정 정지")) {

						changeEmpAccountUserStatus(bean.getCompanyCode(), bean.getEmpCode(), "N");

						// 사원 계정 정지 => EMPLOYEE_SECRET 테이블에 userPassWord 가 null 인 새로운 EmployeeSecretTO
						// 생성, Insert
						int newSeq = empSecretDAO.selectUserPassWordCount(bean.getCompanyCode(), bean.getEmpCode());

						EmployeeSecretTO newSecretBean = new EmployeeSecretTO();

						newSecretBean.setCompanyCode(bean.getCompanyCode());
						newSecretBean.setEmpCode(bean.getEmpCode());
						newSecretBean.setSeq(newSeq);

						empSecretDAO.insertEmployeeSecret(newSecretBean);

					}

					break;

				}

			}

			resultMap.put("INSERT", insertList);
			// resultMap.put("UPDATE", updateList);
			// resultMap.put("DELETE", deleteList);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : batchCodeListProcess 종료");
		}
		return resultMap;

	}

	public HashMap<String, Object> batchEmpSecretListProcess(ArrayList<EmployeeSecretTO> empSecretList) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : batchEmpSecretListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			// ArrayList<String> updateList = new ArrayList<>();
			// ArrayList<String> deleteList = new ArrayList<>();

			for (EmployeeSecretTO TO : empSecretList) {

				String status = TO.getStatus();

				switch (status) {

				case "INSERT":

					empSecretDAO.insertEmployeeSecret(TO);

					insertList.add(TO.getEmpCode());

					break;

				}

			}

			resultMap.put("INSERT", insertList);
			// resultMap.put("UPDATE", updateList);
			// resultMap.put("DELETE", deleteList);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : batchCodeListProcess 종료");
		}
		return resultMap;

	}

	@Override
	public Boolean checkUserIdDuplication(String companyCode, String newUserId) {
		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : checkUserIdDuplication 시작");
		}

		ArrayList<EmployeeDetailTO> empDetailList = null;
		Boolean duplicated = false;

		try {

			empDetailList = empDetailDAO.selectUserIdList(companyCode);

			for (EmployeeDetailTO TO : empDetailList) {

				if (TO.getUserId().equals(newUserId)) {

					duplicated = true;

				}

			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : checkUserIdDuplication 종료");
		}
		return duplicated; // 중복된 코드이면 true 반환
	}

	@Override
	public Boolean checkEmpCodeDuplication(String companyCode, String newEmpCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : checkEmpCodeDuplication 시작");
		}

		ArrayList<EmployeeBasicTO> empBasicList = null;
		Boolean duplicated = false;

		try {

			empBasicList = empBasicDAO.selectEmployeeBasicList(companyCode);

			for (EmployeeBasicTO TO : empBasicList) {

				if (TO.getEmpCode().equals(newEmpCode)) {

					duplicated = true;

				}

			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : checkEmpCodeDuplication 종료");
		}
		return duplicated; // 중복된 코드이면 true 반환
	}

	@Override
	public void changeEmpAccountUserStatus(String companyCode, String empCode, String userStatus) {
		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : changeEmpAccountUserOrNot 시작");
		}

		try {

			empBasicDAO.changeUserAccountStatus(companyCode, empCode, userStatus);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EmpApplicationServiceImpl : changeEmpAccountUserOrNot 종료");
		}

	}

}
