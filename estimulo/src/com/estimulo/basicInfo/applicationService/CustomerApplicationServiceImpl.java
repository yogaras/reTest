package com.estimulo.basicInfo.applicationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.dao.CodeDetailDAO;
import com.estimulo.base.dao.CodeDetailDAOImpl;
import com.estimulo.base.to.CodeDetailTO;
import com.estimulo.basicInfo.dao.CustomerDAO;
import com.estimulo.basicInfo.dao.CustomerDAOImpl;
import com.estimulo.basicInfo.to.CustomerTO;
import com.estimulo.common.exception.DataAccessException;

public class CustomerApplicationServiceImpl implements CustomerApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(CustomerApplicationServiceImpl.class);

	// 싱글톤
	private static CustomerApplicationService instance = new CustomerApplicationServiceImpl();

	private CustomerApplicationServiceImpl() {
	}

	public static CustomerApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ CustomerApplicationServiceImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static CustomerDAO customerDAO = CustomerDAOImpl.getInstance();
	private static CodeDetailDAO codeDetailDAO = CodeDetailDAOImpl.getInstance();

	public ArrayList<CustomerTO> getCustomerList(String searchCondition, String companyCode, String workplaceCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerApplicationServiceImpl : getCustomerList 시작");
		}

		ArrayList<CustomerTO> customerList = null;

		try {

			switch (searchCondition) {

			case "ALL":

				customerList = customerDAO.selectCustomerListByCompany();
				break;

			case "WORKPLACE":

				customerList = customerDAO.selectCustomerListByWorkplace(workplaceCode);
				break;

			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerApplicationServiceImpl : getCustomerList 종료");
		}
		return customerList;
	}

	public String getNewCustomerCode(String companyCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerApplicationServiceImpl : getNewCustomerCode 시작");
		}

		ArrayList<CustomerTO> customerList = null;
		String newCustomerCode = null;

		try {

			customerList = customerDAO.selectCustomerListByCompany();

			TreeSet<Integer> customerCodeNoSet = new TreeSet<>();

			for (CustomerTO bean : customerList) {

				if (bean.getCustomerCode().startsWith("PTN-")) {

					try {

						Integer no = Integer.parseInt(bean.getCustomerCode().split("PTN-")[1]);
						customerCodeNoSet.add(no);

					} catch (NumberFormatException e) {

						// "PTN-" 다음 부분을 Integer 로 변환하지 못하는 경우 : 그냥 다음 반복문 실행

					}

				}

			}

			if (customerCodeNoSet.isEmpty()) {
				newCustomerCode = "PTN-" + String.format("%02d", 1);
			} else {
				newCustomerCode = "PTN-" + String.format("%02d", customerCodeNoSet.pollLast() + 1);
			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerApplicationServiceImpl : getNewCustomerCode 종료");
		}
		return newCustomerCode;
	}

	public HashMap<String, Object> batchCustomerListProcess(ArrayList<CustomerTO> customerList) {

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerApplicationServiceImpl : batchCustomerListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeBean = new CodeDetailTO();

			for (CustomerTO bean : customerList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					// 새로운 부서번호 생성 후 bean 에 set
					String newCustomerCode = getNewCustomerCode(bean.getWorkplaceCode());
					bean.setCustomerCode(newCustomerCode);

					// 부서 테이블에 insert
					customerDAO.insertCustomer(bean);
					insertList.add(bean.getCustomerCode());

					// CODE_DETAIL 테이블에 Insert
					detailCodeBean.setDivisionCodeNo("CL-01");
					detailCodeBean.setDetailCode(bean.getCustomerCode());
					detailCodeBean.setDetailCodeName(bean.getCustomerName());

					codeDetailDAO.insertDetailCode(detailCodeBean);

					break;

				case "UPDATE":

					customerDAO.updateCustomer(bean);
					updateList.add(bean.getCustomerCode());

					// CODE_DETAIL 테이블에 Update
					detailCodeBean.setDivisionCodeNo("CL-01");
					detailCodeBean.setDetailCode(bean.getCustomerCode());
					detailCodeBean.setDetailCodeName(bean.getCustomerName());

					codeDetailDAO.updateDetailCode(detailCodeBean);

					break;

				case "DELETE":

					customerDAO.deleteCustomer(bean);
					deleteList.add(bean.getCustomerCode());

					// CODE_DETAIL 테이블에 Delete
					detailCodeBean.setDivisionCodeNo("CL-01");
					detailCodeBean.setDetailCode(bean.getCustomerCode());
					detailCodeBean.setDetailCodeName(bean.getCustomerName());

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
			logger.debug("CustomerApplicationServiceImpl : batchCustomerListProcess 종료");
		}
		return resultMap;
	}

}
