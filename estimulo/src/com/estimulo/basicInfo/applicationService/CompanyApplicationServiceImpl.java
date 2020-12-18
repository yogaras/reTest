package com.estimulo.basicInfo.applicationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.dao.CodeDetailDAO;
import com.estimulo.base.dao.CodeDetailDAOImpl;
import com.estimulo.base.to.CodeDetailTO;
import com.estimulo.basicInfo.dao.CompanyDAO;
import com.estimulo.basicInfo.dao.CompanyDAOImpl;
import com.estimulo.basicInfo.to.CompanyTO;
import com.estimulo.common.exception.DataAccessException;

public class CompanyApplicationServiceImpl implements CompanyApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(CompanyApplicationServiceImpl.class);

	// 싱글톤
	private static CompanyApplicationService instance = new CompanyApplicationServiceImpl();

	private CompanyApplicationServiceImpl() {
	}

	public static CompanyApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ CompanyApplicationServiceImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static CompanyDAO companyDAO = CompanyDAOImpl.getInstance();
	private static CodeDetailDAO codeDetailDAO = CodeDetailDAOImpl.getInstance();

	public ArrayList<CompanyTO> getCompanyList() {

		if (logger.isDebugEnabled()) {
			logger.debug("CompanyApplicationServiceImpl : getCompanyList 시작");
		}

		ArrayList<CompanyTO> companyList = null;

		try {

			companyList = companyDAO.selectCompanyList();

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CompanyApplicationServiceImpl : getCompanyList 종료");
		}
		return companyList;
	}

	public String getNewCompanyCode() {

		if (logger.isDebugEnabled()) {
			logger.debug("CompanyApplicationServiceImpl : getNewCompanyCode 시작");
		}

		ArrayList<CompanyTO> companyList = null;
		String newCompanyCode = null;

		try {

			companyList = companyDAO.selectCompanyList();

			TreeSet<Integer> companyCodeNoSet = new TreeSet<>();

			for (CompanyTO bean : companyList) {

				if (bean.getCompanyCode().startsWith("COM-")) {

					try {

						Integer no = Integer.parseInt(bean.getCompanyCode().split("COM-")[1]);
						companyCodeNoSet.add(no);

					} catch (NumberFormatException e) {

						// "COM-" 다음 부분을 Integer 로 변환하지 못하는 경우 : 그냥 다음 반복문 실행

					}

				}

			}

			if (companyCodeNoSet.isEmpty()) {
				newCompanyCode = "COM-" + String.format("%02d", 1);
			} else {
				newCompanyCode = "COM-" + String.format("%02d", companyCodeNoSet.pollLast() + 1);
			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CompanyApplicationServiceImpl : getNewCompanyCode 종료");
		}
		return newCompanyCode;
	}

	public HashMap<String, Object> batchCompanyListProcess(ArrayList<CompanyTO> companyList) {

		if (logger.isDebugEnabled()) {
			logger.debug("CompanyApplicationServiceImpl : batchCompanyListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeBean = new CodeDetailTO();

			
			// 2번의 반복문 실행 : 합쳐서 해도 상관없지만, 추가/수정/삭제가 겹치면 중간에 빠지는 번호가 생길 수 있음
			for (CompanyTO bean : companyList) {  // 1차 반복 : INSERT 만 실행 => 중간에 빠지는 번호가 없도록 하기 위해서..

				String status = bean.getStatus();

				switch (status) {   

				case "INSERT":

					// 새로운 회사번호 생성 후 bean 에 set
					String newCompanyCode = getNewCompanyCode();
					bean.setCompanyCode(newCompanyCode);

					// 회사 테이블에 insert
					companyDAO.insertCompany(bean);
					insertList.add(bean.getCompanyCode());

					// CODE_DETAIL 테이블에 Insert
					detailCodeBean.setDivisionCodeNo("CO-01");
					detailCodeBean.setDetailCode(bean.getCompanyCode());
					detailCodeBean.setDetailCodeName(bean.getCompanyName());

					codeDetailDAO.insertDetailCode(detailCodeBean);

					break;

				}

			}

			for (CompanyTO bean : companyList) {    // 2차 반복 : UPDATE , DELETE 만 실행

				String status = bean.getStatus();

				switch (status) {

				case "UPDATE":

					companyDAO.updateCompany(bean);
					updateList.add(bean.getCompanyCode());

					// CODE_DETAIL 테이블에 Update
					detailCodeBean.setDivisionCodeNo("CO-01");
					detailCodeBean.setDetailCode(bean.getCompanyCode());
					detailCodeBean.setDetailCodeName(bean.getCompanyName());

					codeDetailDAO.updateDetailCode(detailCodeBean);

					break;

				case "DELETE":

					companyDAO.deleteCompany(bean);
					deleteList.add(bean.getCompanyCode());

					// CODE_DETAIL 테이블에 Delete
					detailCodeBean.setDivisionCodeNo("CO-01");
					detailCodeBean.setDetailCode(bean.getCompanyCode());
					detailCodeBean.setDetailCodeName(bean.getCompanyName());

					codeDetailDAO.deleteDetailCode(detailCodeBean);

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
			logger.debug("CompanyApplicationServiceImpl : batchCompanyListProcess 종료");
		}
		return resultMap;
	}

}
