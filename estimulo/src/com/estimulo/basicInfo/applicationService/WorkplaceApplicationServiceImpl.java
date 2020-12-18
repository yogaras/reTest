package com.estimulo.basicInfo.applicationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.dao.CodeDetailDAO;
import com.estimulo.base.dao.CodeDetailDAOImpl;
import com.estimulo.base.to.CodeDetailTO;
import com.estimulo.basicInfo.dao.WorkplaceDAO;
import com.estimulo.basicInfo.dao.WorkplaceDAOImpl;
import com.estimulo.basicInfo.to.WorkplaceTO;
import com.estimulo.common.exception.DataAccessException;

public class WorkplaceApplicationServiceImpl implements WorkplaceApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(WorkplaceApplicationServiceImpl.class);

	// 싱글톤
	private static WorkplaceApplicationService instance = new WorkplaceApplicationServiceImpl();

	private WorkplaceApplicationServiceImpl() {
	}

	public static WorkplaceApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ WorkplaceApplicationServiceImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static WorkplaceDAO workplaceDAO = WorkplaceDAOImpl.getInstance();
	private static CodeDetailDAO codeDetailDAO = CodeDetailDAOImpl.getInstance();

	public ArrayList<WorkplaceTO> getWorkplaceList(String companyCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceApplicationServiceImpl : getWorkplaceList 시작");
		}

		ArrayList<WorkplaceTO> workplaceList = null;

		try {

			workplaceList = workplaceDAO.selectWorkplaceList(companyCode);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceApplicationServiceImpl : getWorkplaceList 종료");
		}
		return workplaceList;
	}

	public String getNewWorkplaceCode(String companyCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceApplicationServiceImpl : getNewWorkplaceCode 시작");
		}

		ArrayList<WorkplaceTO> workplaceList = null;
		String newWorkplaceCode = null;

		try {

			workplaceList = workplaceDAO.selectWorkplaceList(companyCode);

			TreeSet<Integer> workplaceCodeNoSet = new TreeSet<>();

			for (WorkplaceTO bean : workplaceList) {

				if (bean.getWorkplaceCode().startsWith("BRC-")) {

					try {

						Integer no = Integer.parseInt(bean.getWorkplaceCode().split("BRC-")[1]);
						workplaceCodeNoSet.add(no);

					} catch (NumberFormatException e) {

						// "BRC-" 다음 부분을 Integer 로 변환하지 못하는 경우 : 그냥 다음 반복문 실행

					}

				}

			}

			if (workplaceCodeNoSet.isEmpty()) {
				newWorkplaceCode = "BRC-" + String.format("%02d", 1);
			} else {
				newWorkplaceCode = "BRC-" + String.format("%02d", workplaceCodeNoSet.pollLast() + 1);
			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceApplicationServiceImpl : getNewWorkplaceCode 시작");
		}
		return newWorkplaceCode;
	}


	public HashMap<String, Object> batchWorkplaceListProcess(ArrayList<WorkplaceTO> workplaceList) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceApplicationServiceImpl : batchWorkplaceListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeBean = new CodeDetailTO();

			for (WorkplaceTO bean : workplaceList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					// 새로운 사업장번호 생성 후 bean 에 set
					String newWorkplaceCode = getNewWorkplaceCode(bean.getCompanyCode());
					bean.setWorkplaceCode(newWorkplaceCode);

					// 사업장 테이블에 insert
					workplaceDAO.insertWorkplace(bean);
					insertList.add(bean.getWorkplaceCode());

					// CODE_DETAIL 테이블에 Insert
					detailCodeBean.setDivisionCodeNo("CO-02");
					detailCodeBean.setDetailCode(bean.getWorkplaceCode());
					detailCodeBean.setDetailCodeName(bean.getWorkplaceName());

					codeDetailDAO.insertDetailCode(detailCodeBean);

					break;

				case "UPDATE":

					workplaceDAO.updateWorkplace(bean);
					updateList.add(bean.getWorkplaceCode());

					// CODE_DETAIL 테이블에 Update
					detailCodeBean.setDivisionCodeNo("CO-02");
					detailCodeBean.setDetailCode(bean.getWorkplaceCode());
					detailCodeBean.setDetailCodeName(bean.getWorkplaceName());

					codeDetailDAO.updateDetailCode(detailCodeBean);

					break;

				case "DELETE":

					workplaceDAO.deleteWorkplace(bean);
					deleteList.add(bean.getWorkplaceCode());

					// CODE_DETAIL 테이블에 Delete
					detailCodeBean.setDivisionCodeNo("CO-02");
					detailCodeBean.setDetailCode(bean.getWorkplaceCode());
					detailCodeBean.setDetailCodeName(bean.getWorkplaceName());

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
			logger.debug("WorkplaceApplicationServiceImpl : batchWorkplaceListProcess 종료");
		}
		return resultMap;
	}
	
}
