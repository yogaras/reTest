package com.estimulo.logistics.production.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.production.applicationService.MpsApplicationService;
import com.estimulo.logistics.production.applicationService.MpsApplicationServiceImpl;
import com.estimulo.logistics.production.applicationService.MrpApplicationService;
import com.estimulo.logistics.production.applicationService.MrpApplicationServiceImpl;
import com.estimulo.logistics.production.applicationService.WorkOrderApplicationService;
import com.estimulo.logistics.production.applicationService.WorkOrderApplicationServiceImpl;
import com.estimulo.logistics.production.to.ContractDetailInMpsAvailableTO;
import com.estimulo.logistics.production.to.MpsTO;
import com.estimulo.logistics.production.to.MrpGatheringTO;
import com.estimulo.logistics.production.to.MrpTO;
import com.estimulo.logistics.production.to.ProductionPerformanceInfoTO;
import com.estimulo.logistics.production.to.SalesPlanInMpsAvailableTO;
import com.estimulo.logistics.production.to.WorkOrderInfoTO;

public class ProductionServiceFacadeImpl implements ProductionServiceFacade {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(ProductionServiceFacadeImpl.class);

	// 싱글톤
	private static ProductionServiceFacade instance = new ProductionServiceFacadeImpl();

	private ProductionServiceFacadeImpl() {
	}

	public static ProductionServiceFacade getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ ProductionServiceFacadeImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();
	private static MpsApplicationService mpsAS = MpsApplicationServiceImpl.getInstance();
	private static MrpApplicationService mrpAS = MrpApplicationServiceImpl.getInstance();
	private static WorkOrderApplicationService workOrderAS = WorkOrderApplicationServiceImpl.getInstance();

	@Override
	public ArrayList<MpsTO> getMpsList(String startDate, String endDate, String includeMrpApply) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getMpsList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<MpsTO> mpsTOList = null;

		try {
			mpsTOList = mpsAS.getMpsList(startDate, endDate, includeMrpApply);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getMpsList 종료");
		}

		return mpsTOList;
	}

	@Override
	public ArrayList<ContractDetailInMpsAvailableTO> getContractDetailListInMpsAvailable(String searchCondition,
			String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getContractDetailListInMpsAvailable 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList = null;

		try {
			contractDetailInMpsAvailableList = mpsAS.getContractDetailListInMpsAvailable(searchCondition, startDate,
					endDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getContractDetailListInMpsAvailable 종료");
		}

		return contractDetailInMpsAvailableList;

	}

	@Override
	public ArrayList<SalesPlanInMpsAvailableTO> getSalesPlanListInMpsAvailable(String searchCondition,
			String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getSalesPlanListInMpsAvailable 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList = null;

		try {
			salesPlanInMpsAvailableList = mpsAS.getSalesPlanListInMpsAvailable(searchCondition, startDate, endDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getSalesPlanListInMpsAvailable 종료");
		}

		return salesPlanInMpsAvailableList;

	}

	@Override
	public HashMap<String, Object> convertContractDetailToMps(
			ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : convertContractDetailToMps 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {
			resultMap = mpsAS.convertContractDetailToMps(contractDetailInMpsAvailableList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : convertContractDetailToMps 종료");
		}

		return resultMap;

	}

	@Override
	public HashMap<String, Object> convertSalesPlanToMps(
			ArrayList<SalesPlanInMpsAvailableTO> contractDetailInMpsAvailableList) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : convertSalesPlanToMps 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {
			resultMap = mpsAS.convertSalesPlanToMps(contractDetailInMpsAvailableList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : convertSalesPlanToMps 종료");
		}

		return resultMap;

	}

	@Override
	public HashMap<String, Object> batchMpsListProcess(ArrayList<MpsTO> mpsTOList) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : batchMpsListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {
			resultMap = mpsAS.batchMpsListProcess(mpsTOList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : batchMpsListProcess 종료");
		}

		return resultMap;

	}

	@Override
	public ArrayList<MrpTO> searchMrpList(String mrpGatheringStatusCondition) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : searchMrpList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<MrpTO> mrpList = null;

		try {

			mrpList = mrpAS.searchMrpList(mrpGatheringStatusCondition);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : searchMrpList 종료");
		}

		return mrpList;

	}

	@Override
	public ArrayList<MrpTO> searchMrpList(String dateSearchCondtion, String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : searchMrpList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<MrpTO> mrpList = null;

		try {

			mrpList = mrpAS.searchMrpList(dateSearchCondtion, startDate, endDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : searchMrpList 종료");
		}

		return mrpList;
	}

	@Override
	public ArrayList<MrpTO> searchMrpListAsMrpGatheringNo(String mrpGatheringNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : searchMrpListAsMrpGatheringNo 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<MrpTO> mrpList = null;

		try {
			mrpList = mrpAS.searchMrpListAsMrpGatheringNo(mrpGatheringNo);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : searchMrpListAsMrpGatheringNo 종료");
		}

		return mrpList;
	}

	@Override
	public ArrayList<MrpGatheringTO> searchMrpGatheringList(String dateSearchCondtion, String startDate,
			String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : searchMrpGatheringList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<MrpGatheringTO> mrpGatheringList = null;

		try {
			mrpGatheringList = mrpAS.searchMrpGatheringList(dateSearchCondtion, startDate, endDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : searchMrpGatheringList 종료");
		}

		return mrpGatheringList;
	}

	@Override
	public HashMap<String, Object> openMrp(ArrayList<String> mpsNoArr) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : openMrp 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {
			resultMap = mrpAS.openMrp(mpsNoArr);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : openMrp 종료");
		}

		return resultMap;
	}
	
	@Override
	public HashMap<String, Object> registerMrp(String mrpRegisterDate, ArrayList<MrpTO> newMrpList) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : registerMrp 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {
			resultMap = mrpAS.registerMrp(mrpRegisterDate, newMrpList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : registerMrp 종료");
		}

		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchMrpListProcess(ArrayList<MrpTO> mrpTOList) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : batchMrpListProcess 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {
			resultMap = mrpAS.batchMrpListProcess(mrpTOList);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : batchMrpListProcess 종료");
		}

		return resultMap;
	}

	@Override
	public ArrayList<MrpGatheringTO> getMrpGathering(ArrayList<String> mrpNoArr) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getMrpGathering 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<MrpGatheringTO> mrpGatheringList = null;

		try {
			mrpGatheringList = mrpAS.getMrpGathering(mrpNoArr);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getMrpGathering 종료");
		}

		return mrpGatheringList;
	}

	@Override
	public HashMap<String, Object> registerMrpGathering(String mrpGatheringRegisterDate,
			ArrayList<MrpGatheringTO> newMrpGatheringList, HashMap<String, String> mrpNoAndItemCodeMap) {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : registerMrpGathering 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String, Object> resultMap = null;

		try {
			resultMap = mrpAS.registerMrpGathering(mrpGatheringRegisterDate, newMrpGatheringList, mrpNoAndItemCodeMap);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : registerMrpGathering 종료");
		}

		return resultMap;
	}

	@Override
	public HashMap<String, Object> getWorkOrderableMrpList() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getWorkOrderableMrpList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
        HashMap<String,Object> resultMap = null;

		try {
			resultMap = workOrderAS.getWorkOrderableMrpList();
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getWorkOrderableMrpList 종료");
		}

		return resultMap;
		
	}

	@Override
	public HashMap<String,Object> getWorkOrderSimulationList(String mrpNo) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getWorkOrderSimulationList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
        HashMap<String,Object> resultMap = null;

		try {
			resultMap = workOrderAS.getWorkOrderSimulationList(mrpNo);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getWorkOrderSimulationList 종료");
		}

		return resultMap;
	}

	@Override
	public HashMap<String,Object> workOrder(String workPlaceCode,String productionProcess) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : workOrder 시작");
		}

		dataSourceTransactionManager.beginTransaction();
        HashMap<String,Object> resultMap = null;
        
		try {
			
			resultMap = workOrderAS.workOrder(workPlaceCode,productionProcess);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : workOrder 종료");
		}
		
    	return resultMap;
		
	}

	@Override
	public ArrayList<WorkOrderInfoTO> getWorkOrderInfoList() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getWorkOrderInfoList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<WorkOrderInfoTO> workOrderInfoList = null;

		try {
			workOrderInfoList = workOrderAS.getWorkOrderInfoList();
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getWorkOrderInfoList 종료");
		}

		return workOrderInfoList;
		
	}

	@Override
	public HashMap<String,Object> workOrderCompletion(String workOrderNo,String actualCompletionAmount) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : workOrderCompletion 시작");
		}

		dataSourceTransactionManager.beginTransaction();
        HashMap<String,Object> resultMap = null;		
		
		try {
			resultMap = workOrderAS.workOrderCompletion(workOrderNo,actualCompletionAmount);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : workOrderCompletion 종료");
		}
		
    	return resultMap;
		
	}

	@Override
	public ArrayList<ProductionPerformanceInfoTO> getProductionPerformanceInfoList() {

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getProductionPerformanceInfoList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		ArrayList<ProductionPerformanceInfoTO> productionPerformanceInfoList = null;

		try {

			productionPerformanceInfoList = workOrderAS.getProductionPerformanceInfoList();
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getProductionPerformanceInfoList 종료");
		}

		return productionPerformanceInfoList;

	}

	@Override
	public HashMap<String,Object> showWorkSiteSituation(String workSiteCourse,String workOrderNo,String itemClassIfication) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getProductionPerformanceInfoList 시작");
		}

		dataSourceTransactionManager.beginTransaction();
		HashMap<String,Object> showWorkSiteSituation = null;

		try {

			showWorkSiteSituation = workOrderAS.showWorkSiteSituation(workSiteCourse,workOrderNo,itemClassIfication);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getProductionPerformanceInfoList 종료");
		}

		return showWorkSiteSituation;

	}

	@Override
	public void workCompletion(String workOrderNo, String itemCode ,  ArrayList<String> itemCodeListArr) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getProductionPerformanceInfoList 시작");
		}

		dataSourceTransactionManager.beginTransaction();


		try {

			workOrderAS.workCompletion(workOrderNo,itemCode,itemCodeListArr);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getProductionPerformanceInfoList 종료");
		}
		

	}

	@Override
	public HashMap<String, Object> workSiteLogList(String workSiteLogDate) {
		
		HashMap<String, Object> resultMap = new HashMap<>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getProductionPerformanceInfoList 시작");
		}

		dataSourceTransactionManager.beginTransaction();


		try {

			resultMap=workOrderAS.workSiteLogList(workSiteLogDate);
			dataSourceTransactionManager.commitTransaction();

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ProductionServiceFacadeImpl : getProductionPerformanceInfoList 종료");
		}
		return resultMap;
	}

	
}
