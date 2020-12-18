package com.estimulo.logistics.sales.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.sales.applicationService.ContractApplicationService;
import com.estimulo.logistics.sales.applicationService.ContractApplicationServiceImpl;
import com.estimulo.logistics.sales.applicationService.DeliveryApplicationService;
import com.estimulo.logistics.sales.applicationService.DeliveryApplicationServiceImpl;
import com.estimulo.logistics.sales.applicationService.EstimateApplicationService;
import com.estimulo.logistics.sales.applicationService.EstimateApplicationServiceImpl;
import com.estimulo.logistics.sales.applicationService.SalesPlanApplicationService;
import com.estimulo.logistics.sales.applicationService.SalesPlanApplicationServiceImpl;
import com.estimulo.logistics.sales.to.ContractDetailTO;
import com.estimulo.logistics.sales.to.ContractInfoTO;
import com.estimulo.logistics.sales.to.ContractTO;
import com.estimulo.logistics.sales.to.DeliveryInfoTO;
import com.estimulo.logistics.sales.to.EstimateDetailTO;
import com.estimulo.logistics.sales.to.EstimateTO;
import com.estimulo.logistics.sales.to.SalesPlanTO;

public class SalesServiceFacadeImpl implements SalesServiceFacade {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(SalesServiceFacadeImpl.class);

	// 싱글톤
	private static SalesServiceFacade instance = new SalesServiceFacadeImpl();

	private SalesServiceFacadeImpl() {
	}

	public static SalesServiceFacade getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ SalesServiceFacadeImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	private static EstimateApplicationService estimateAS = EstimateApplicationServiceImpl.getInstance();
	private static ContractApplicationService contractAS = ContractApplicationServiceImpl.getInstance();
	private static SalesPlanApplicationService salesPlanAS = SalesPlanApplicationServiceImpl.getInstance();
	private static DeliveryApplicationService deliveryAS = DeliveryApplicationServiceImpl.getInstance();

	@Override
	public ArrayList<EstimateTO> getEstimateList(String dateSearchCondition, String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getEstimateList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<EstimateTO> estimateTOList = null;

		try {

			estimateTOList = estimateAS.getEstimateList(dateSearchCondition, startDate, endDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getEstimateList 종료");
		}
		return estimateTOList;
	}

	@Override
	public ArrayList<EstimateDetailTO> getEstimateDetailList(String estimateNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getEstimateDetailList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<EstimateDetailTO> estimateDetailTOList = null;

		try {

			estimateDetailTOList = estimateAS.getEstimateDetailList(estimateNo);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getEstimateDetailList 종료");
		}
		return estimateDetailTOList;
	}

	@Override
	public HashMap<String, Object> addNewEstimate(String estimateDate, EstimateTO newEstimateTO) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : addNewEstimate 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = estimateAS.addNewEstimate(estimateDate, newEstimateTO);
			System.out.println("              SalesSF에서resultMap값은 : " + resultMap);
			// SalesSF에서resultMap값은 : {DELETE=[], newEstimateNo=ES2018100901,
			// INSERT=[ES2018100901-01], UPDATE=[]}

			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : addNewEstimate 종료");
		}
		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchEstimateDetailListProcess(ArrayList<EstimateDetailTO> estimateDetailTOList) {
		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : batchEstimateDetailListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = estimateAS.batchEstimateDetailListProcess(estimateDetailTOList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : batchEstimateDetailListProcess 종료");
		}
		return resultMap;
	}

	@Override
	public ArrayList<ContractInfoTO> getContractList(String searchCondition, String[] paramArray) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getContractList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<ContractInfoTO> contractInfoTOList = null;

		try {

			contractInfoTOList = contractAS.getContractList(searchCondition, paramArray);

			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getContractList 종료");
		}
		return contractInfoTOList;
	}

	@Override
	public ArrayList<ContractInfoTO> getDeliverableContractList(String searchCondition, String[] paramArray) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getDeliverableContractList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<ContractInfoTO> deliverableContractList = null;

		try {

			deliverableContractList = contractAS.getDeliverableContractList(searchCondition, paramArray);

			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getDeliverableContractList 종료");
		}
		return deliverableContractList;
	}
	
	@Override
	public ArrayList<ContractDetailTO> getContractDetailList(String estimateNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getContractDetailList 시작");
		}

		dataSourceTransactionManager.beginTransaction();

		ArrayList<ContractDetailTO> contractDetailTOList = null;

		try {

			contractDetailTOList = contractAS.getContractDetailList(estimateNo);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getContractDetailList 종료");
		}
		return contractDetailTOList;
	}

	@Override
	public ArrayList<EstimateTO> getEstimateListInContractAvailable(String startDate, String endDate) {
		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getEstimateListInContractAvailable 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<EstimateTO> estimateListInContractAvailable = null;

		try {

			estimateListInContractAvailable = contractAS.getEstimateListInContractAvailable(startDate, endDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getEstimateListInContractAvailable 종료");
		}
		return estimateListInContractAvailable;
	}

	@Override
	public HashMap<String, Object> addNewContract(String contractDate, String personCodeInCharge,
			ContractTO workingContractTO) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : addNewContract 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = contractAS.addNewContract(contractDate, personCodeInCharge, workingContractTO);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : addNewContract 종료");
		}
		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchContractDetailListProcess(ArrayList<ContractDetailTO> contractDetailTOList) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : batchContractDetailListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = contractAS.batchContractDetailListProcess(contractDetailTOList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : batchContractDetailListProcess 종료");
		}
		return resultMap;
	}

	@Override
	public void changeContractStatusInEstimate(String estimateNo, String contractStatus) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : changeContractStatusInEstimate 시작");
		}

		dataSourceTransactionManager.beginTransaction();

		try {
													 //estimateNo, "N"
			contractAS.changeContractStatusInEstimate(estimateNo, contractStatus);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : changeContractStatusInEstimate 종료");
		}
	}

	@Override
	public ArrayList<SalesPlanTO> getSalesPlanList(String dateSearchCondition, String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getSalesPlanList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<SalesPlanTO> salesPlanTOList = null;

		try {

			salesPlanTOList = salesPlanAS.getSalesPlanList(dateSearchCondition, startDate, endDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getSalesPlanList 종료");
		}
		return salesPlanTOList;
	}

	@Override
	public HashMap<String, Object> batchSalesPlanListProcess(ArrayList<SalesPlanTO> salesPlanTOList) {
		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : batchSalesPlanListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = salesPlanAS.batchSalesPlanListProcess(salesPlanTOList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : batchSalesPlanListProcess 종료");
		}
		return resultMap;
	}

	@Override
	public ArrayList<DeliveryInfoTO> getDeliveryInfoList() {
		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getDeliveryInfoList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<DeliveryInfoTO> deliveryInfoList = null;

		try {

			deliveryInfoList = deliveryAS.getDeliveryInfoList();
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : getDeliveryInfoList 종료");
		}
		return deliveryInfoList;
	}

	@Override
	public HashMap<String, Object> batchDeliveryListProcess(List<DeliveryInfoTO> deliveryTOList) {
		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : batchDeliveryListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {

			resultMap = deliveryAS.batchDeliveryListProcess(deliveryTOList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : batchDeliveryListProcess 종료");
		}
		return resultMap;
	}

	@Override
	public HashMap<String, Object> deliver(String contractDetailNo) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : deliver 시작");
		}

		dataSourceTransactionManager.beginTransaction();
        HashMap<String,Object> resultMap = null;

		try {

			resultMap = deliveryAS.deliver(contractDetailNo);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SalesServiceFacadeImpl : deliver 종료");
		}
		return resultMap;
	}
	
}
