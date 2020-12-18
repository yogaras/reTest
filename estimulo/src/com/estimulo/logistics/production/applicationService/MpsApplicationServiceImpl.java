package com.estimulo.logistics.production.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.production.dao.MpsDAO;
import com.estimulo.logistics.production.dao.MpsDAOImpl;
import com.estimulo.logistics.production.to.ContractDetailInMpsAvailableTO;
import com.estimulo.logistics.production.to.MpsTO;
import com.estimulo.logistics.production.to.SalesPlanInMpsAvailableTO;
import com.estimulo.logistics.sales.dao.ContractDetailDAO;
import com.estimulo.logistics.sales.dao.ContractDetailDAOImpl;
import com.estimulo.logistics.sales.dao.SalesPlanDAO;
import com.estimulo.logistics.sales.dao.SalesPlanDAOImpl;

public class MpsApplicationServiceImpl implements MpsApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(MpsApplicationServiceImpl.class);

	// 싱글톤
	private static MpsApplicationService instance = new MpsApplicationServiceImpl();

	private MpsApplicationServiceImpl() {
	}

	public static MpsApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ MpsApplicationServiceImpl 객체접근");
		}

		return instance;
	}

	// DAO 참조변수 선언
	private static MpsDAO mpsDAO = MpsDAOImpl.getInstance();
	private static ContractDetailDAO contractDetailDAO = ContractDetailDAOImpl.getInstance();
	private static SalesPlanDAO salesPlanDAO = SalesPlanDAOImpl.getInstance();

	public ArrayList<MpsTO> getMpsList(String startDate, String endDate, String includeMrpApply) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : getMpsList 시작");
		}

		ArrayList<MpsTO> mpsTOList = null;

		try {

			mpsTOList = mpsDAO.selectMpsList(startDate, endDate, includeMrpApply);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : getMpsList 종료");
		}
		return mpsTOList;
	}

	public ArrayList<ContractDetailInMpsAvailableTO> getContractDetailListInMpsAvailable(String searchCondition,
			String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : getContractDetailListInMpsAvailable 시작");
		}

		ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList = null;

		try {

			contractDetailInMpsAvailableList = contractDetailDAO.selectContractDetailListInMpsAvailable(searchCondition,
					startDate, endDate);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : getContractDetailListInMpsAvailable 종료");
		}
		return contractDetailInMpsAvailableList;
	}

	public ArrayList<SalesPlanInMpsAvailableTO> getSalesPlanListInMpsAvailable(String searchCondition, String startDate,
			String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : getSalesPlanListInMpsAvailable 시작");
		}

		ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList = null;

		try {

			salesPlanInMpsAvailableList = salesPlanDAO.selectSalesPlanListInMpsAvailable(searchCondition, startDate,
					endDate);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : getSalesPlanListInMpsAvailable 종료");
		}
		return salesPlanInMpsAvailableList;
	}

	public String getNewMpsNo(String mpsPlanDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : getNewMpsNo 시작");
		}

		StringBuffer newEstimateNo = null;

		try {

			int i = mpsDAO.selectMpsCount(mpsPlanDate);
				System.out.println("mpsPlanDate = "+mpsPlanDate);
				System.out.println("i = "+i);

			newEstimateNo = new StringBuffer();
			newEstimateNo.append("PS");
			newEstimateNo.append(mpsPlanDate.replace("-", ""));
			newEstimateNo.append(String.format("%02d", i)); //PS2020042401

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : getNewMpsNo 종료");
		}
		return newEstimateNo.toString();
	}

	public HashMap<String, Object> convertContractDetailToMps(
			ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : convertContractDetailToMps 시작");
		}

		HashMap<String, Object> resultMap = null;

		try {

			ArrayList<MpsTO> mpsTOList = new ArrayList<>();

			MpsTO newMpsBean = null;

			// MPS 에 등록할 수주상세 Bean 의 정보를 새로운 MPS Bean 에 세팅, status : "INSERT"
			for (ContractDetailInMpsAvailableTO bean : contractDetailInMpsAvailableList) {

				newMpsBean = new MpsTO();

				newMpsBean.setStatus("INSERT");

				newMpsBean.setMpsPlanClassification(bean.getPlanClassification());
				newMpsBean.setContractDetailNo(bean.getContractDetailNo());
				newMpsBean.setItemCode(bean.getItemCode());
				newMpsBean.setItemName(bean.getItemName());
				newMpsBean.setUnitOfMps(bean.getUnitOfContract());
				newMpsBean.setMpsPlanDate(bean.getMpsPlanDate());
				newMpsBean.setMpsPlanAmount(bean.getProductionRequirement());
				newMpsBean.setDueDateOfMps(bean.getDueDateOfContract());
				newMpsBean.setScheduledEndDate(bean.getScheduledEndDate());
				newMpsBean.setDescription(bean.getDescription());

				mpsTOList.add(newMpsBean);

			}

			resultMap = batchMpsListProcess(mpsTOList); //batchMpsListProcess 메소드 호출

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : convertContractDetailToMps 종료");
		}
		return resultMap;

	}

	public HashMap<String, Object> convertSalesPlanToMps(
			ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : convertSalesPlanToMps 시작");
		}

		HashMap<String, Object> resultMap = null;

		try {

			ArrayList<MpsTO> mpsTOList = new ArrayList<>();

			MpsTO newMpsBean = null;

			// MPS 에 등록할 판매계획 TO 의 정보를 새로운 MPS TO 에 세팅, status : "INSERT"
			for (SalesPlanInMpsAvailableTO bean : salesPlanInMpsAvailableList) {

				newMpsBean = new MpsTO();

				newMpsBean.setStatus("INSERT");

				newMpsBean.setMpsPlanClassification(bean.getPlanClassification());
				newMpsBean.setSalesPlanNo(bean.getSalesPlanNo());
				newMpsBean.setItemCode(bean.getItemCode());
				newMpsBean.setItemName(bean.getItemName());
				newMpsBean.setUnitOfMps(bean.getUnitOfSales());
				newMpsBean.setMpsPlanDate(bean.getMpsPlanDate());
				newMpsBean.setMpsPlanAmount(bean.getSalesAmount());
				newMpsBean.setDueDateOfMps(bean.getDueDateOfSales());
				newMpsBean.setScheduledEndDate(bean.getScheduledEndDate());
				newMpsBean.setDescription(bean.getDescription());

				mpsTOList.add(newMpsBean);

			}

			resultMap = batchMpsListProcess(mpsTOList);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : convertSalesPlanToMps 종료");
		}
		return resultMap;
	}

	public HashMap<String, Object> batchMpsListProcess(ArrayList<MpsTO> mpsTOList) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : batchMpsListProcess 시작");
		}

		HashMap<String, Object> resultMap = null;

		try {

			resultMap = new HashMap<>();

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			for (MpsTO bean : mpsTOList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					// 새로운 판매계획일련번호 생성
					String newMpsNo = getNewMpsNo(bean.getMpsPlanDate());
						System.out.println("newMpsNo = "+newMpsNo);

					// MPS TO 에 새로운 판매계획일련번호 세팅
					bean.setMpsNo(newMpsNo);

					// MPS TO Insert
					mpsDAO.insertMps(bean);

					// 생성된 새로운 MPS 번호를 ArrayList 에 추가
					insertList.add(newMpsNo);

					// MPS TO 의 수주상세번호가 존재하면, 수주상세 테이블에서 해당 번호의 MPS 적용상태를 'Y' 로 변경
					if (bean.getContractDetailNo() != null) {

						changeMpsStatusInContractDetail(bean.getContractDetailNo(), "Y");

						// MPS TO 의 판매계획번호가 존재하면, 판매계획 테이블에서 해당 번호의 MPS 적용상태를 'Y' 로 변경
					} else if (bean.getSalesPlanNo() != null) {

						changeMpsStatusInSalesPlan(bean.getSalesPlanNo(), "Y");

					}

					break;

				case "UPDATE":

					mpsDAO.updateMps(bean);

					updateList.add(bean.getMpsNo());

					break;

				case "DELETE":

					mpsDAO.deleteMps(bean);

					deleteList.add(bean.getMpsNo());

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
			logger.debug("MpsApplicationServiceImpl : batchMpsListProcess 종료");
		}
		return resultMap;
	}

	public void changeMpsStatusInContractDetail(String contractDetailNo, String mpsStatus) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : changeMpsStatusInContractDetail 시작");
		}

		try {

			contractDetailDAO.changeMpsStatusOfContractDetail(contractDetailNo, mpsStatus);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : changeMpsStatusInContractDetail 종료");
		}

	}

	public void changeMpsStatusInSalesPlan(String salesPlanNo, String mpsStatus) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : changeMpsStatusInSalesPlan 시작");
		}

		try {

			salesPlanDAO.changeMpsStatusOfSalesPlan(salesPlanNo, mpsStatus);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("MpsApplicationServiceImpl : changeMpsStatusInSalesPlan 종료");
		}

	}

}
