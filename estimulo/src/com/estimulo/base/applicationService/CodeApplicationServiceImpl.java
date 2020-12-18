package com.estimulo.base.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.dao.CodeDAO;
import com.estimulo.base.dao.CodeDAOImpl;
import com.estimulo.base.dao.CodeDetailDAO;
import com.estimulo.base.dao.CodeDetailDAOImpl;
import com.estimulo.base.to.CodeDetailTO;
import com.estimulo.base.to.CodeTO;
import com.estimulo.common.exception.DataAccessException;

public class CodeApplicationServiceImpl implements CodeApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(CodeApplicationServiceImpl.class);

	// 싱글톤
	private static CodeApplicationService instance = new CodeApplicationServiceImpl();

	private CodeApplicationServiceImpl() {
	}

	public static CodeApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ CodeApplicationServiceImpl 객체접근");
		}

		return instance;
	}

	// DAO 참조변수
	private static CodeDAO codeDAO = CodeDAOImpl.getInstance();
	private static CodeDetailDAO codeDetailDAO = CodeDetailDAOImpl.getInstance();

	@Override
	public ArrayList<CodeTO> getCodeList() {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : getCodeList 시작");
		}

		ArrayList<CodeTO> codeList = null;

		try {

			codeList = codeDAO.selectCodeList();

			for (CodeTO bean : codeList) {

				bean.setCodeDetailTOList(codeDetailDAO.selectDetailCodeList(bean.getDivisionCodeNo()));

			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : getCodeList 종료");
		}
		return codeList;
	}

	@Override
	public ArrayList<CodeDetailTO> getDetailCodeList(String divisionCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : getDetailCodeList 시작");
		}

		ArrayList<CodeDetailTO> codeDetailList = null;

		try {

			codeDetailList = codeDetailDAO.selectDetailCodeList(divisionCode);
			

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : getDetailCodeList 종료");
		}

		return codeDetailList;

	}

	public Boolean checkCodeDuplication(String divisionCode, String newDetailCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : checkCodeDuplication 시작");
		}

		ArrayList<CodeDetailTO> detailCodeList = null;
		Boolean duplicated = false;

		try {

			detailCodeList = codeDetailDAO.selectDetailCodeList(divisionCode);

			for (CodeDetailTO bean : detailCodeList) {

				if (bean.getDetailCode().equals(newDetailCode)) {

					duplicated = true; // 입력받은 newDetailCode 와 같은 값이 있으면 중복된 코드임

				}

			}

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : checkCodeDuplication 종료");
		}
		return duplicated; // 중복된 코드이면 true 반환

	}

	@Override
	public HashMap<String, Object> batchCodeListProcess(ArrayList<CodeTO> codeList) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : batchCodeListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			for (CodeTO bean : codeList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					codeDAO.insertCode(bean);

					insertList.add(bean.getDivisionCodeNo());

					break;

				case "UPDATE":

					codeDAO.updateCode(bean);

					updateList.add(bean.getDivisionCodeNo());

					break;

				case "DELETE":

					codeDAO.deleteCode(bean);

					deleteList.add(bean.getDivisionCodeNo());

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
			logger.debug("CodeApplicationServiceImpl : batchCodeListProcess 종료");
		}
		return resultMap;

	}

	@Override
	public HashMap<String, Object> batchDetailCodeListProcess(ArrayList<CodeDetailTO> detailCodeList) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : batchDetailCodeListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			for (CodeDetailTO bean : detailCodeList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					codeDetailDAO.insertDetailCode(bean);

					insertList.add(bean.getDivisionCodeNo() + " / " + bean.getDetailCode());

					break;

				case "UPDATE":

					codeDetailDAO.updateDetailCode(bean);

					updateList.add(bean.getDivisionCodeNo() + " / " + bean.getDetailCode());

					break;

				case "DELETE":

					codeDetailDAO.deleteDetailCode(bean);

					deleteList.add(bean.getDivisionCodeNo() + " / " + bean.getDetailCode());

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
			logger.debug("CodeApplicationServiceImpl : batchDetailCodeListProcess 종료");
		}
		return resultMap;

	}

	@Override
	public HashMap<String, Object> changeCodeUseCheckProcess(ArrayList<CodeDetailTO> detailCodeList) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : changeCodeUseCheckProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		try {

			ArrayList<String> codeUseList = new ArrayList<>();
			ArrayList<String> codeNotUseList = new ArrayList<>();

			for (CodeDetailTO bean : detailCodeList) {

				String codeUseCheck = ((bean.getCodeUseCheck() == null) ? "" : bean.getCodeUseCheck().toUpperCase());

				switch (codeUseCheck) {

				case "N":

					codeDetailDAO.changeCodeUseCheck(bean.getDivisionCodeNo(), bean.getDetailCode(), "N");

					codeNotUseList.add(bean.getDivisionCodeNo() + " / " + bean.getDetailCode());

					break;

				default:

					codeDetailDAO.changeCodeUseCheck(bean.getDivisionCodeNo(), bean.getDetailCode(), "");

					codeUseList.add(bean.getDivisionCodeNo() + " / " + bean.getDetailCode());

					break;

				}

			}

			resultMap.put("USE", codeUseList);
			resultMap.put("NOT_USE", codeNotUseList);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("CodeApplicationServiceImpl : changeCodeUseCheckProcess 종료");
		}
		return resultMap;
	}

}
