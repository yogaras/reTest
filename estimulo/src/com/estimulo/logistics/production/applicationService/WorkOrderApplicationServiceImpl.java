package com.estimulo.logistics.production.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.production.dao.WorkOrderDAO;
import com.estimulo.logistics.production.dao.WorkOrderDAOImpl;
import com.estimulo.logistics.production.to.ProductionPerformanceInfoTO;
import com.estimulo.logistics.production.to.WorkOrderInfoTO;

public class WorkOrderApplicationServiceImpl implements WorkOrderApplicationService {

		// SLF4J logger
		private static Logger logger = LoggerFactory.getLogger(WorkOrderApplicationServiceImpl.class);

		// 싱글톤
		private static WorkOrderApplicationService instance = new WorkOrderApplicationServiceImpl();

		private WorkOrderApplicationServiceImpl() {
		}

		public static WorkOrderApplicationService getInstance() {

			if (logger.isDebugEnabled()) {
				logger.debug("@ WorkOrderApplicationService 객체접근");
			}

			return instance;
		}

		// DAO 참조변수 선언
		// private static MpsDAO mpsDAO = MpsDAOImpl.getInstance();
		// private static MrpDAO mrpDAO = MrpDAOImpl.getInstance();
		private static WorkOrderDAO workOrderDAO = WorkOrderDAOImpl.getInstance();
		
		@Override
		public HashMap<String,Object> getWorkOrderableMrpList() {

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getWorkOrderableMrpList 시작");
			}

            HashMap<String,Object> resultMap = null;

			try {

				resultMap = workOrderDAO.getWorkOrderableMrpList();

			} catch (DataAccessException e) {
				logger.error(e.getMessage());
				throw e;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getWorkOrderableMrpList 종료");
			}
			return resultMap;
			
	}

		@Override
		public HashMap<String,Object> getWorkOrderSimulationList(String mrpNo) {

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getWorkOrderSimulationList 시작");
			}
			
            HashMap<String,Object> resultMap = null;

			try {

				resultMap = workOrderDAO.getWorkOrderSimulationList(mrpNo);

			} catch (DataAccessException e) {
				logger.error(e.getMessage());
				throw e;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getWorkOrderSimulationList 종료");
			}
			return resultMap;
			
		}

		@Override
		public HashMap<String,Object> workOrder(String workPlaceCode,String productionProcess) {

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : workOrder 시작");
			}

            HashMap<String,Object> resultMap = null;

			try {

				resultMap = workOrderDAO.workOrder(workPlaceCode,productionProcess);

			} catch (DataAccessException e) {
				logger.error(e.getMessage());
				throw e;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : workOrder 종료");
			}
			
        	return resultMap;			
			
		}

		@Override
		public ArrayList<WorkOrderInfoTO> getWorkOrderInfoList() {

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getWorkOrderInfoList 시작");
			}

			ArrayList<WorkOrderInfoTO> workOrderInfoList = null;

			try {

				workOrderInfoList = workOrderDAO.selectWorkOrderInfoList();

			} catch (DataAccessException e) {
				logger.error(e.getMessage());
				throw e;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getWorkOrderInfoList 종료");
			}
			return workOrderInfoList;
			
		}

		@Override
		public HashMap<String,Object> workOrderCompletion(String workOrderNo,String actualCompletionAmount) {

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : workOrderCompletion 시작");
			}
			
            HashMap<String,Object> resultMap = null;
            
			try {

				resultMap = workOrderDAO.workOrderCompletion(workOrderNo,actualCompletionAmount);

			} catch (DataAccessException e) {
				logger.error(e.getMessage());
				throw e;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : workOrderCompletion 종료");
			}
			
        	return resultMap;
			
		}
		
		@Override
		public ArrayList<ProductionPerformanceInfoTO> getProductionPerformanceInfoList() {

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getProductionPerformanceInfoList 시작");
			}
 
			ArrayList<ProductionPerformanceInfoTO> productionPerformanceInfoList = null;

			try {

				productionPerformanceInfoList = workOrderDAO.selectProductionPerformanceInfoList();

			} catch (DataAccessException e) {
				logger.error(e.getMessage());
				throw e;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getProductionPerformanceInfoList 종료");
			}
			return productionPerformanceInfoList;
			
		}

		@Override
		public HashMap<String,Object> showWorkSiteSituation(String workSiteCourse,String workOrderNo,String itemClassIfication) {
			
			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getProductionPerformanceInfoList 시작");
			}
 
			HashMap<String,Object> showWorkSiteSituation = null;

			try {

				showWorkSiteSituation = workOrderDAO.selectWorkSiteSituation(workSiteCourse,workOrderNo,itemClassIfication);

			} catch (DataAccessException e) {
				logger.error(e.getMessage());
				throw e;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getProductionPerformanceInfoList 종료");
			}
			return showWorkSiteSituation;
		}

		@Override
		public void workCompletion(String workOrderNo, String itemCode ,  ArrayList<String> itemCodeListArr) {
			
			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getProductionPerformanceInfoList 시작");
			}

			try {
				String itemCodeList=itemCodeListArr.toString().replace("[", "").replace("]", "");
				workOrderDAO.updateWorkCompletionStatus(workOrderNo,itemCode,itemCodeList);

			} catch (DataAccessException e) {
				logger.error(e.getMessage());
				throw e;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getProductionPerformanceInfoList 종료");
			}

		}

		@Override
		public HashMap<String, Object> workSiteLogList(String workSiteLogDate) {

			HashMap<String, Object> resultMap = new HashMap<>();
			
			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getProductionPerformanceInfoList 시작");
			}

			try {

				resultMap=workOrderDAO.workSiteLogList(workSiteLogDate);
				
			} catch (DataAccessException e) {
				logger.error(e.getMessage());
				throw e;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("WorkOrderApplicationService : getProductionPerformanceInfoList 종료");
			}
			return resultMap;
		}
		
}
