package com.estimulo.logistics.production.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.production.to.ProductionPerformanceInfoTO;
import com.estimulo.logistics.production.to.WorkOrderInfoTO;
import com.estimulo.logistics.production.to.WorkOrderSimulationTO;
import com.estimulo.logistics.production.to.WorkOrderableMrpListTO;
import com.estimulo.logistics.production.to.WorkSiteLog;
import com.estimulo.logistics.production.to.WorkSiteSimulationTO;

import oracle.jdbc.internal.OracleTypes;

public class WorkOrderDAOImpl implements WorkOrderDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(WorkOrderDAOImpl.class);

	// 싱글톤
	private static WorkOrderDAO instance = new WorkOrderDAOImpl();

	private WorkOrderDAOImpl() {
	}

	public static WorkOrderDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ WorkOrderDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	@Override
	public HashMap<String,Object> getWorkOrderableMrpList() {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderDAOImpl : getWorkOrderableMrpList 시작");
		}

		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		HashMap<String,Object> resultMap = new HashMap<>();
		ArrayList<WorkOrderableMrpListTO> workOrderableMrpList = new ArrayList<WorkOrderableMrpListTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			query.append(" {call P_WORK_ORDERABLE_MRP_LIST(?,?,?)} ");
			System.out.println("      @ 프로시저 호출");

			cs = conn.prepareCall(query.toString());

			cs.registerOutParameter(1, OracleTypes.NUMBER);
			cs.registerOutParameter(2, OracleTypes.VARCHAR);
			cs.registerOutParameter(3, OracleTypes.CURSOR);
			cs.executeUpdate();
			
            int errorCode = cs.getInt(1);
            String errorMsg = cs.getString(2);
			rs = (ResultSet) cs.getObject(3);
			
			WorkOrderableMrpListTO bean = null;

			while (rs.next()) {

				bean = new WorkOrderableMrpListTO();
				bean.setMrpNo(rs.getString("MRP_NO"));
				bean.setMpsNo(rs.getString("MPS_NO"));
				bean.setMrpGatheringNo(rs.getString("MRP_GATHERING_NO"));
				bean.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setUnitOfMrp(rs.getString("UNIT_OF_MRP"));
				bean.setRequiredAmount(rs.getInt("REQUIRED_AMOUNT"));
				bean.setOrderDate(rs.getString("ORDER_DATE"));
				bean.setRequiredDate(rs.getString("REQUIRED_DATE"));

				workOrderableMrpList.add(bean);
			}

			resultMap.put("gridRowJson", workOrderableMrpList);
			resultMap.put("errorCode",errorCode);
			resultMap.put("errorMsg",errorMsg);
			
		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(cs, rs);

		}

		return resultMap;

	}

	@Override
	public HashMap<String,Object> getWorkOrderSimulationList(String mrpNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderDAOImpl : getWorkOrderSimulationList 시작");
		}

		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		HashMap<String,Object> resultMap = new HashMap<>();		
		ArrayList<WorkOrderSimulationTO> workOrderSimulationList = new ArrayList<WorkOrderSimulationTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			query.append("{call P_WORK_ORDER_SIMULATION(?,?,?,?)}");
			System.out.println("      @ 프로시저 호출");

			cs = conn.prepareCall(query.toString());

			cs.setString(1, mrpNo);
			cs.registerOutParameter(2, OracleTypes.NUMBER);
			cs.registerOutParameter(3, OracleTypes.VARCHAR);
			cs.registerOutParameter(4, OracleTypes.CURSOR);
			cs.executeUpdate();
			
            int errorCode = cs.getInt(2);
            String errorMsg = cs.getString(3);
			rs = (ResultSet) cs.getObject(4);

			WorkOrderSimulationTO bean = null;

			while (rs.next()) {

				bean = new WorkOrderSimulationTO();

				bean.setMrpNo(rs.getString("MRP_NO"));
				bean.setMpsNo(rs.getString("MPS_NO"));
				bean.setMrpGatheringNo(rs.getString("MRP_GATHERING_NO"));
				bean.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setUnitOfMrp(rs.getString("UNIT_OF_MRP"));
				bean.setInputAmount(rs.getString("INPUT_AMOUNT"));
				bean.setRequiredAmount(rs.getString("REQUIRED_AMOUNT"));
				bean.setStockAfterWork(rs.getString("STOCK_AFTER_WORK"));
				bean.setOrderDate(rs.getString("ORDER_DATE"));
				bean.setRequiredDate(rs.getString("REQUIRED_DATE"));

				workOrderSimulationList.add(bean);
			}

			resultMap.put("gridRowJson", workOrderSimulationList);
			resultMap.put("errorCode",errorCode);
			resultMap.put("errorMsg",errorMsg);

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(cs, rs);

		}

		return resultMap;
	}

	@Override
	public HashMap<String,Object> workOrder(String workPlaceCode,String productionProcess) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderDAOImpl : workOrder 시작");
		}

		Connection conn = null;
		CallableStatement cs = null;
		HashMap<String,Object> resultMap = new HashMap<>();
		
		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			query.append("{call P_WORK_ORDER(?,?,?,?)}");
			System.out.println("      @ 프로시저 호출");
			System.out.println(productionProcess);
			cs = conn.prepareCall(query.toString());

			cs.setString(1, workPlaceCode);
			cs.setString(2, productionProcess);
			cs.registerOutParameter(3, OracleTypes.NUMBER);
			cs.registerOutParameter(4, OracleTypes.VARCHAR);
			cs.executeUpdate();
			
            int errorCode = cs.getInt(3);
            String errorMsg = cs.getString(4);
            
        	resultMap.put("errorCode",errorCode);
        	resultMap.put("errorMsg",errorMsg);            
            
        	return resultMap;
        	
		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(cs);

		}

	}

	@Override
	public ArrayList<WorkOrderInfoTO> selectWorkOrderInfoList() {
		
		 if (logger.isDebugEnabled()) { 
			 logger.debug("WorkOrderDAOImpl : selectWorkOrderInfoList 시작");
		  }
		  
		  Connection conn = null; 
		  PreparedStatement pstmt = null; 
		  ResultSet rs = null;
		  
		  ArrayList<WorkOrderInfoTO> workOrderInfoList = new ArrayList<WorkOrderInfoTO>();
		  
		  try { 
			  
		  conn = dataSourceTransactionManager.getConnection(); 
		  
		  StringBuffer query= new StringBuffer();
		  
		    /*SELECT WDI.COMPLETION_STATUS,WDI.INSPECTION_STATUS,WDI.ITEM_CLASSIFICATION,WDI.ITEM_CODE,WDI.ITEM_NAME,WDI.MPS_NO,
	  		WDI.MRP_GATHERING_NO,WDI.MRP_NO,WDI.PRODUCTION_PROCESS_CODE,WDI.PRODUCTION_PROCESS_NAME,WDI.PRODUCTION_STATUS, 
	  		WDI.REQUIRED_AMOUNT,WDI.REQUIRED_AMOUNT,WDI.UNIT_OF_MRP,WDI.WORK_ORDER_NO,WDI.WORK_SITE_CODE,WDI.WORK_SITE_NAME FROM 
	  		WORK_ORDER_INFO WDI ,MPS MP,CONTRACT_DETAIL CD
	  		WHERE MP.CONTRACT_DETAIL_NO = CD.CONTRACT_DETAIL_NO
	  		AND MP.MPS_NO = WDI.MPS_NO
	  		AND WDI.OPERATION_COMPLETED IS NULL*/
		  
		  query.append("SELECT WDI.COMPLETION_STATUS,WDI.INSPECTION_STATUS,WDI.ITEM_CLASSIFICATION,WDI.ITEM_CODE,WDI.ITEM_NAME,WDI.MPS_NO,\r\n" + 
		  		" WDI.MRP_GATHERING_NO,WDI.MRP_NO,WDI.PRODUCTION_PROCESS_CODE,WDI.PRODUCTION_PROCESS_NAME,WDI.PRODUCTION_STATUS,\r\n" + 
		  		" WDI.REQUIRED_AMOUNT,WDI.REQUIRED_AMOUNT,WDI.UNIT_OF_MRP,WDI.WORK_ORDER_NO,WDI.WORK_SITE_CODE,WDI.WORK_SITE_NAME FROM \r\n" + 
		  		" WORK_ORDER_INFO WDI ,MPS MP,CONTRACT_DETAIL CD\r\n" + 
		  		" WHERE MP.CONTRACT_DETAIL_NO = CD.CONTRACT_DETAIL_NO\r\n" + 
		  		" AND MP.MPS_NO = WDI.MPS_NO\r\n" + 
		  		" AND WDI.OPERATION_COMPLETED IS NULL ");
		  	 	  
		  pstmt = conn.prepareStatement(query.toString());
		  
		  rs = pstmt.executeQuery();
		  
		  WorkOrderInfoTO bean = null;
		  
		  while (rs.next()) {
		  
		  bean = new WorkOrderInfoTO();
		  
		  bean.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
		  bean.setMrpNo(rs.getString("MRP_NO"));
		  bean.setMpsNo(rs.getString("MPS_NO"));
		  bean.setMrpGatheringNo(rs.getString("MRP_GATHERING_NO"));
		  bean.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
		  bean.setItemCode(rs.getString("ITEM_CODE"));
		  bean.setItemName(rs.getString("ITEM_NAME"));
		  bean.setUnitOfMrp(rs.getString("UNIT_OF_MRP"));
		  bean.setRequiredAmount(rs.getString("REQUIRED_AMOUNT"));
		  bean.setProductionProcessCode(rs.getString("PRODUCTION_PROCESS_CODE"));
		  bean.setProductionProcessName(rs.getString("PRODUCTION_PROCESS_NAME"));
		  bean.setWorkSiteCode(rs.getString("WORK_SITE_CODE"));
		  bean.setWorkStieName(rs.getString("WORK_SITE_NAME"));
		  bean.setInspectionStatus(rs.getString("INSPECTION_STATUS"));
		  bean.setProductionStatus(rs.getString("PRODUCTION_STATUS"));
		  bean.setCompletionStatus(rs.getString("COMPLETION_STATUS"));
		  
		  workOrderInfoList.add(bean);
		  
		  }
		  
		  return workOrderInfoList;
		  
		  } catch (Exception sqle) {
		  
		  throw new DataAccessException(sqle.getMessage());
		  
		  } finally {
		  
		  dataSourceTransactionManager.close(pstmt, rs);
		  
		  }
	
	}

	@Override
	public HashMap<String,Object> workOrderCompletion(String workOrderNo,String actualCompletionAmount) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkOrderDAOImpl : workOrderCompletion 시작");
		}

		Connection conn = null;
		CallableStatement cs = null;
		HashMap<String,Object> resultMap = new HashMap<>();
		
		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			
			query.append("{call P_WORK_ORDER_COMPLETION(?,?,?,?)}");
			System.out.println("      @ 프로시저 호출");

			cs = conn.prepareCall(query.toString());
			
			cs.setString(1, workOrderNo); //작업지시일련번호
			cs.setString(2, actualCompletionAmount); //작업완료수량
			cs.registerOutParameter(3, OracleTypes.NUMBER);
			cs.registerOutParameter(4, OracleTypes.VARCHAR);
			cs.executeUpdate();

            int errorCode = cs.getInt(3);
            String errorMsg = cs.getString(4);
        	resultMap.put("errorCode",errorCode);
        	resultMap.put("errorMsg",errorMsg);

            return resultMap;
            
		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(cs);

		}

	}
	
	@Override
	public ArrayList<ProductionPerformanceInfoTO> selectProductionPerformanceInfoList() {
		
		 if (logger.isDebugEnabled()) { 
			 logger.debug("WorkOrderDAOImpl : selectProductionPerformanceInfoList 시작");
		  }
		  
		  Connection conn = null; 
		  PreparedStatement pstmt = null; 
		  ResultSet rs = null;
		  
		  ArrayList<ProductionPerformanceInfoTO> productionPerformanceInfoList = new ArrayList<ProductionPerformanceInfoTO>();
		  
		  try { 
			  
		  conn = dataSourceTransactionManager.getConnection(); 
		  
		  StringBuffer query= new StringBuffer();
		  
		  query.append("SELECT * \r\n" + 
		  		"FROM PRODUCTION_PERFORMANCE \r\n" + 
		  		"order by work_order_completion_date desc");
		  	 	  
		  pstmt = conn.prepareStatement(query.toString());
		  
		  rs = pstmt.executeQuery();
		  
		  ProductionPerformanceInfoTO bean = null;
		  
		  while (rs.next()) {
		  
		  bean = new ProductionPerformanceInfoTO();
		  
		  bean.setWorkOrderCompletionDate(rs.getString("WORK_ORDER_COMPLETION_DATE"));
		  bean.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
		  bean.setMpsNo(rs.getString("MPS_NO"));
		  bean.setContractDetailNo(rs.getString("CONTRACT_DETAIL_NO"));
		  bean.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
		  bean.setItemCode(rs.getString("ITEM_CODE"));
		  bean.setItemName(rs.getString("ITEM_NAME"));
		  bean.setUnit(rs.getString("UNIT"));
		  bean.setWorkOrderAmount(rs.getString("WORK_ORDER_AMOUNT"));
		  bean.setActualCompletionAmount(rs.getString("ACTUAL_COMPLETION_AMOUNT"));
		  bean.setWorkSuccessRate(rs.getString("WORK_SUCCESS_RATE"));
		  
		  productionPerformanceInfoList.add(bean);
		  
		  }
		  
		  return productionPerformanceInfoList;
		  
		  } catch (Exception sqle) {
		  
		  throw new DataAccessException(sqle.getMessage());
		  
		  } finally {
		  
		  dataSourceTransactionManager.close(pstmt, rs);
		  
		  }
	
	}
	

	@Override
	public HashMap<String,Object> selectWorkSiteSituation(String workSiteCourse,String workOrderNo,String itemClassIfication) {
		
		if (logger.isDebugEnabled()) { 
			 logger.debug("WorkOrderDAOImpl : selectWorkSiteSituation 시작");
		  }
		  
		  Connection conn = null; 
		  CallableStatement cs= null;
		  ResultSet rs = null;
		  HashMap<String,Object> resultMap = new HashMap<>();
		  ArrayList<WorkSiteSimulationTO> list = new ArrayList<>();
		  WorkSiteSimulationTO bean = null;
		  System.out.println(workSiteCourse);
		  System.out.println(workOrderNo);
		  System.out.println(itemClassIfication);
		  try { 
			  
		  conn = dataSourceTransactionManager.getConnection(); 
		  
		  StringBuffer query= new StringBuffer();
		  System.out.println("프로시저 호출");
		  query.append("{call P_WORK_SITE_SITUATION(?,?,?,?,?,?)}");  
		  
		  cs = conn.prepareCall(query.toString());
		  
		  cs.setString(1, workOrderNo);	
		  cs.setString(2, workSiteCourse);	 	   	  	   	  
		  cs.setString(3, itemClassIfication);	 	   	  	   	  
		  cs.registerOutParameter(4, OracleTypes.NUMBER);	 	  
		  cs.registerOutParameter(5, OracleTypes.VARCHAR);	 	  
		  cs.registerOutParameter(6, OracleTypes.CURSOR);	 	  
		  	 	  
		  
		  cs.executeUpdate();
		  
		  int errorCode = cs.getInt(4);
          String errorMsg = cs.getString(5);		
          rs = (ResultSet) cs.getObject(6);
		  
      	  while(rs.next()){
      	  bean = new WorkSiteSimulationTO();
      	  bean.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
      	  bean.setMrpNo(rs.getString("MRP_NO"));
      	  bean.setMpsNo(rs.getString("MPS_NO"));
      	  bean.setWorkSieteName(rs.getString("WORK_SITE_NAME"));
      	  bean.setWdItem(rs.getString("WD_ITEM"));
      	  bean.setParentItemCode(rs.getString("PARENT_ITEM_CODE"));
       	  bean.setParentItemName(rs.getString("PARENT_ITEM_NAME"));
      	  bean.setItemClassIfication(rs.getString("ITEM_CLASSIFICATION"));
      	  bean.setItemCode(rs.getString("ITEM_CODE"));
      	  bean.setItemName(rs.getString("ITEM_NAME"));
      	  bean.setRequiredAmount(rs.getString("REQUIRED_AMOUNT"));
      	  list.add(bean);
      	  }
      	  
      	  resultMap.put("gridRowJson",list);
      	  resultMap.put("errorCode",errorCode);
    	  resultMap.put("errorMsg",errorMsg);
		  return resultMap;
		  
		  } catch (Exception sqle) {
		  
		  throw new DataAccessException(sqle.getMessage());
		  
		  } finally {
		  
		  dataSourceTransactionManager.close(cs,rs);
		  
		  }
	}

	@Override
	public void updateWorkCompletionStatus(String workOrderNo, String itemCode,String itemCodeList) {
		
		if (logger.isDebugEnabled()) { 
			 logger.debug("WorkOrderDAOImpl : selectProductionPerformanceInfoList 시작");
		  }
		  
		  Connection conn = null; 
		  CallableStatement cs= null;
		  HashMap<String,Object> resultMap = new HashMap<>();
		  System.out.println(workOrderNo);
		  System.out.println(itemCode);
		  System.out.println(itemCodeList);
		  try { 
			  
		  conn = dataSourceTransactionManager.getConnection(); 
		  
		  StringBuffer query= new StringBuffer();
		  System.out.println(workOrderNo);
		  query.append("{call P_WORK_SITE_COMPLETION(?,?,?,?,?)}");
		  		 	  
		  cs = conn.prepareCall(query.toString());
		  cs.setString(1, workOrderNo);	
		  cs.setString(2, itemCode); 
		  cs.setString(3, itemCodeList);
		  cs.registerOutParameter(4, OracleTypes.NUMBER);	 	  
		  cs.registerOutParameter(5, OracleTypes.VARCHAR);	
		  		
		  cs.executeUpdate();
		  
		  int errorCode = cs.getInt(4);
          String errorMsg = cs.getString(5);
          resultMap.put("errorCode",errorCode);
    	  resultMap.put("errorMsg",errorMsg);
		  System.out.println(errorMsg);
		  
		  } catch (Exception sqle) {
		  
		  throw new DataAccessException(sqle.getMessage());
		  
		  } finally {
		  
		  dataSourceTransactionManager.close(cs);
		  
		  }
	}

	@Override
	public HashMap<String, Object> workSiteLogList(String workSiteLogDate) {

		if (logger.isDebugEnabled()) { 
			 logger.debug("WorkOrderDAOImpl : selectWorkSiteSituation 시작");
		  }
		  
		  Connection conn = null; 
		  PreparedStatement pstmt = null; 
		  ResultSet rs = null;
		  HashMap<String,Object> resultMap = new HashMap<>();
		  ArrayList<WorkSiteLog> list = new ArrayList<>();
		  WorkSiteLog bean = null;

		  try { 
			  
		  conn = dataSourceTransactionManager.getConnection(); 
		  
		  StringBuffer query= new StringBuffer();
		  
		  query.append(" select * from work_site_log where WORK_DATE LIKE TO_DATE(?,'YYYY-MM-DD') order by work_date desc ");  
		   
		  pstmt = conn.prepareCall(query.toString());	  
		  pstmt.setString(1,workSiteLogDate);	 	  
		  
		  rs=pstmt.executeQuery();
		  
     	  while(rs.next()){
     		  
     	  bean = new WorkSiteLog();
     	  bean.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
     	  bean.setItemCode(rs.getString("ITEM_CODE"));
     	  bean.setItemName(rs.getString("ITEM_NAME"));
     	  bean.setReaeson(rs.getString("REAESON"));
     	  bean.setWorkSiteName(rs.getString("WORK_SITE_NAME"));
     	  bean.setWorkDate(rs.getString("WORK_DATE"));
     	  bean.setProductionProcessCode(rs.getString("PRODUCTION_PROCESS_CODE"));
     	  bean.setProductionProcessName(rs.getString("PRODUCTION_PROCESS_NAME"));
     	  
     	  list.add(bean);
     	  
     	  }
     	  
     	  resultMap.put("gridRowJson",list);

		  return resultMap;
		  
		  } catch (Exception sqle) {
		  
		  throw new DataAccessException(sqle.getMessage());
		  
		  } finally {
		  
		  dataSourceTransactionManager.close(pstmt,rs);
		  
		  }
	}
	
}
