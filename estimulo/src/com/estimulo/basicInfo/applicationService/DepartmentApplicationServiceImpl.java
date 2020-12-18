package com.estimulo.basicInfo.applicationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.dao.CodeDetailDAO;
import com.estimulo.base.dao.CodeDetailDAOImpl;
import com.estimulo.base.to.CodeDetailTO;
import com.estimulo.basicInfo.dao.DepartmentDAO;
import com.estimulo.basicInfo.dao.DepartmentDAOImpl;
import com.estimulo.basicInfo.to.DepartmentTO;
import com.estimulo.common.exception.DataAccessException;

public class DepartmentApplicationServiceImpl implements DepartmentApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(DepartmentApplicationServiceImpl.class);

	// 싱글톤
	private static DepartmentApplicationService instance = new DepartmentApplicationServiceImpl();

	private DepartmentApplicationServiceImpl() {
	}

	public static DepartmentApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ DepartmentApplicationServiceImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DepartmentDAO deptDAO = DepartmentDAOImpl.getInstance();
	private static CodeDetailDAO codeDetailDAO = CodeDetailDAOImpl.getInstance();

	public ArrayList<DepartmentTO> getDepartmentList(String searchCondition, String companyCode,
			String workplaceCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentApplicationServiceImpl : getDepartmentList 시작");
		}

		ArrayList<DepartmentTO> departmentList = null;

		try {

			switch (searchCondition) {

			case "ALL":

				departmentList = deptDAO.selectDepartmentListByCompany(companyCode);
				break;

			case "WORKPLACE":

				departmentList = deptDAO.selectDepartmentListByWorkplace(workplaceCode);
				break;
				
			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentApplicationServiceImpl : getDepartmentList 종료");
		}
		return departmentList;
	}

	public String getNewDepartmentCode(String companyCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentApplicationServiceImpl : getNewDepartmentCode 시작");
		}

		ArrayList<DepartmentTO> departmentList = null;
		String newDepartmentCode = null;

		try {

			departmentList = deptDAO.selectDepartmentListByCompany(companyCode);

			TreeSet<Integer> departmentCodeNoSet = new TreeSet<>();

			for (DepartmentTO bean : departmentList) {

				if (bean.getDeptCode().startsWith("DPT-")) {

					try {

						Integer no = Integer.parseInt(bean.getDeptCode().split("DPT-")[1]);
						departmentCodeNoSet.add(no);

					} catch (NumberFormatException e) {

						// "DPT-" 다음 부분을 Integer 로 변환하지 못하는 경우 : 그냥 다음 반복문 실행

					}

				}

			}

			if (departmentCodeNoSet.isEmpty()) {
				newDepartmentCode = "DPT-" + String.format("%02d", 1);
			} else {
				newDepartmentCode = "DPT-" + String.format("%02d", departmentCodeNoSet.pollLast() + 1);
			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentApplicationServiceImpl : getNewDepartmentCode 종료");
		}
		return newDepartmentCode;
	}


	public HashMap<String, Object> batchDepartmentListProcess(ArrayList<DepartmentTO> departmentList) {

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentApplicationServiceImpl : batchDepartmentListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeBean = new CodeDetailTO();

			for (DepartmentTO bean : departmentList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					// 새로운 부서번호 생성 후 bean 에 set
					String newDepartmentCode = getNewDepartmentCode(bean.getCompanyCode());
					bean.setDeptCode(newDepartmentCode);

					// 부서 테이블에 insert
					deptDAO.insertDepartment(bean);
					insertList.add(bean.getDeptCode());

					// CODE_DETAIL 테이블에 Insert
					detailCodeBean.setDivisionCodeNo("CO-03");
					detailCodeBean.setDetailCode(bean.getDeptCode());
					detailCodeBean.setDetailCodeName(bean.getDeptName());

					codeDetailDAO.insertDetailCode(detailCodeBean);

					break;

				case "UPDATE":

					deptDAO.updateDepartment(bean);
					updateList.add(bean.getDeptCode());

					// CODE_DETAIL 테이블에 Update
					detailCodeBean.setDivisionCodeNo("CO-03");
					detailCodeBean.setDetailCode(bean.getDeptCode());
					detailCodeBean.setDetailCodeName(bean.getDeptName());

					codeDetailDAO.updateDetailCode(detailCodeBean);

					break;

				case "DELETE":

					deptDAO.deleteDepartment(bean);
					deleteList.add(bean.getDeptCode());

					// CODE_DETAIL 테이블에 Delete
					detailCodeBean.setDivisionCodeNo("CO-03");
					detailCodeBean.setDetailCode(bean.getDeptCode());
					detailCodeBean.setDetailCodeName(bean.getDeptName());

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
			logger.debug("DepartmentApplicationServiceImpl : batchDepartmentListProcess 종료");
		}
		return resultMap;
	}
	
}
