package com.estimulo.basicInfo.applicationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.dao.CodeDetailDAO;
import com.estimulo.base.dao.CodeDetailDAOImpl;
import com.estimulo.base.to.CodeDetailTO;
import com.estimulo.basicInfo.dao.FinancialAccountAssociatesDAO;
import com.estimulo.basicInfo.dao.FinancialAccountAssociatesDAOImpl;
import com.estimulo.basicInfo.to.FinancialAccountAssociatesTO;
import com.estimulo.common.exception.DataAccessException;

public class FinancialAccountAssociatesApplicationServiceImpl implements FinancialAccountAssociatesApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(FinancialAccountAssociatesApplicationServiceImpl.class);

	// 싱글톤
	private static FinancialAccountAssociatesApplicationService instance = new FinancialAccountAssociatesApplicationServiceImpl();

	private FinancialAccountAssociatesApplicationServiceImpl() {
	}

	public static FinancialAccountAssociatesApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ FinancialAccountAssociatesApplicationServiceImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static FinancialAccountAssociatesDAO associatesDAO = FinancialAccountAssociatesDAOImpl.getInstance();
	private static CodeDetailDAO codeDetailDAO = CodeDetailDAOImpl.getInstance();

	public ArrayList<FinancialAccountAssociatesTO> getFinancialAccountAssociatesList(String searchCondition,
			String workplaceCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("FinancialAccountAssociatesApplicationServiceImpl : getFinancialAccountAssociatesList 시작");
		}

		ArrayList<FinancialAccountAssociatesTO> financialAccountAssociatesList = null;

		try {

			switch (searchCondition) {

			case "ALL":

				financialAccountAssociatesList = associatesDAO.selectFinancialAccountAssociatesListByCompany();
				break;

			case "WORKPLACE":

				financialAccountAssociatesList = associatesDAO
						.selectFinancialAccountAssociatesListByWorkplace(workplaceCode);
				break;

			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("FinancialAccountAssociatesApplicationServiceImpl : getFinancialAccountAssociatesList 종료");
		}
		return financialAccountAssociatesList;
	}

	public String getNewFinancialAccountAssociatesCode() {

		if (logger.isDebugEnabled()) {
			logger.debug("FinancialAccountAssociatesApplicationServiceImpl : getNewFinancialAccountAssociatesCode 시작");
		}

		ArrayList<FinancialAccountAssociatesTO> financialAccountAssociatesList = null;
		String newFinancialAccountAssociatesCode = null;

		try {

			financialAccountAssociatesList = associatesDAO
					.selectFinancialAccountAssociatesListByCompany();

			TreeSet<Integer> financialAccountAssociatesCodeNoSet = new TreeSet<>();

			for (FinancialAccountAssociatesTO bean : financialAccountAssociatesList) {

				if (bean.getAccountAssociatesCode().startsWith("FPT-")) {

					try {

						Integer no = Integer.parseInt(bean.getAccountAssociatesCode().split("FPT-")[1]);
						financialAccountAssociatesCodeNoSet.add(no);

					} catch (NumberFormatException e) {

						// "FPT-" 다음 부분을 Integer 로 변환하지 못하는 경우 : 그냥 다음 반복문 실행

					}

				}

			}

			if (financialAccountAssociatesCodeNoSet.isEmpty()) {
				newFinancialAccountAssociatesCode = "FPT-" + String.format("%02d", 1);
			} else {
				newFinancialAccountAssociatesCode = "FPT-"
						+ String.format("%02d", financialAccountAssociatesCodeNoSet.pollLast() + 1);
			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("FinancialAccountAssociatesApplicationServiceImpl : getNewFinancialAccountAssociatesCode 종료");
		}
		return newFinancialAccountAssociatesCode;
	}

	public HashMap<String, Object> batchFinancialAccountAssociatesListProcess(
			ArrayList<FinancialAccountAssociatesTO> financialAccountAssociatesList) {

		if (logger.isDebugEnabled()) {
			logger.debug(
					"FinancialAccountAssociatesApplicationServiceImpl : batchFinancialAccountAssociatesListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeBean = new CodeDetailTO();

			for (FinancialAccountAssociatesTO bean : financialAccountAssociatesList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					// 새로운 금융거래처번호 생성 후 bean 에 set
					String newFinancialAccountAssociatesCode = getNewFinancialAccountAssociatesCode();
					bean.setAccountAssociatesCode(newFinancialAccountAssociatesCode);

					// 금융거래처 테이블에 insert
					associatesDAO.insertFinancialAccountAssociates(bean);
					insertList.add(bean.getAccountAssociatesCode());

					// CODE_DETAIL 테이블에 Insert
					detailCodeBean.setDivisionCodeNo("CL-02");
					detailCodeBean.setDetailCode(bean.getAccountAssociatesCode());
					detailCodeBean.setDetailCodeName(bean.getAccountAssociatesName());

					codeDetailDAO.insertDetailCode(detailCodeBean);

					break;

				case "UPDATE":

					associatesDAO.updateFinancialAccountAssociates(bean);
					updateList.add(bean.getAccountAssociatesCode());

					// CODE_DETAIL 테이블에 Update
					detailCodeBean.setDivisionCodeNo("CL-02");
					detailCodeBean.setDetailCode(bean.getAccountAssociatesCode());
					detailCodeBean.setDetailCodeName(bean.getAccountAssociatesName());

					codeDetailDAO.updateDetailCode(detailCodeBean);

					break;

				case "DELETE":

					associatesDAO.deleteFinancialAccountAssociates(bean);
					deleteList.add(bean.getAccountAssociatesCode());

					// CODE_DETAIL 테이블에 Delete
					detailCodeBean.setDivisionCodeNo("CL-02");
					detailCodeBean.setDetailCode(bean.getAccountAssociatesCode());
					detailCodeBean.setDetailCodeName(bean.getAccountAssociatesName());

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
			logger.debug(
					"FinancialAccountAssociatesApplicationServiceImpl : batchFinancialAccountAssociatesListProcess 종료");
		}
		return resultMap;
	}

}
