package com.estimulo.logistics.production.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.production.to.MrpTO;
import com.estimulo.logistics.production.to.OpenMrpTO;

import oracle.jdbc.internal.OracleTypes;

public class MrpDAOImpl implements MrpDAO {

   // SLF4J logger
   private static Logger logger = LoggerFactory.getLogger(MrpDAOImpl.class);   
   
   // 싱글톤
   private static MrpDAO instance = new MrpDAOImpl();

   private MrpDAOImpl() {
   }

   public static MrpDAO getInstance() {
      
      if (logger.isDebugEnabled()) {
         logger.debug("@ MrpDAOImpl 객체접근");
      }
      
      return instance;
   }
   
   // 참조변수 선언
   private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
         .getInstance();

   
   
   public ArrayList<MrpTO> selectMrpList(String mrpGatheringStatusCondition) { 
      
      if (logger.isDebugEnabled()) {
         logger.debug("MrpDAOImpl : selectMrpList 시작");
      }
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      ArrayList<MrpTO> mrpList = new ArrayList<MrpTO>();

      try {
         conn = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
/*
SELECT * FROM MRP WHERE MRP_GATHERING_STATUS IS NULL
*/
         query.append("SELECT * FROM MRP ");
         
         if(mrpGatheringStatusCondition.equals("null") || mrpGatheringStatusCondition == null) {
            query.append(
                  "WHERE MRP_GATHERING_STATUS IS NULL ORDER BY MRP_NO");
                        
         } else if(mrpGatheringStatusCondition.equals("notNull")) {
            query.append(
                  "WHERE MRP_GATHERING_STATUS IS NOT NULL ORDER BY MRP_NO");
            
         }
         
         pstmt = conn.prepareStatement(query.toString());

         rs = pstmt.executeQuery();

         MrpTO bean = null;
         
         while (rs.next()) {
            
            bean = new MrpTO();

            bean.setMrpNo(rs.getString("MRP_NO"));
            bean.setMpsNo(rs.getString("MPS_NO"));
            bean.setMrpGatheringNo(rs.getString("MRP_GATHERING_NO"));
            bean.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
            bean.setItemCode(rs.getString("ITEM_CODE"));
            bean.setItemName(rs.getString("ITEM_NAME"));
            bean.setOrderDate(rs.getString("ORDER_DATE"));
            bean.setRequiredDate(rs.getString("REQUIRED_DATE"));
            bean.setRequiredAmount(rs.getInt("REQUIRED_AMOUNT"));
            bean.setUnitOfMrp(rs.getString("UNIT_OF_MRP"));
            bean.setMrpGatheringStatus(rs.getString("MRP_GATHERING_STATUS"));

            mrpList.add(bean);
            
         }

         return mrpList;

      } catch (Exception sqle) {

         throw new DataAccessException(sqle.getMessage());

      } finally {

         dataSourceTransactionManager.close(pstmt, rs);

      }
      
   }

   
   public ArrayList<MrpTO> selectMrpList(String dateSearchCondtion, String startDate, String endDate) { 

      if (logger.isDebugEnabled()) {
         logger.debug("MrpDAOImpl : selectMrpList 시작");
      }
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      ArrayList<MrpTO> mrpList = new ArrayList<MrpTO>();

      try {
         conn = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
/*
SELECT * FROM MRP WHERE ( CASE ? WHEN 'orderDate' THEN
TO_DATE(ORDER_DATE, 'YYYY-MM-DD') WHEN 'requiredDate' THEN
TO_DATE(REQUIRED_DATE, 'YYYY-MM-DD') END ) 
BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')
*/
         query.append(
               "SELECT * FROM MRP WHERE ( CASE ? WHEN 'orderDate' THEN\r\n" + 
               "TO_DATE(ORDER_DATE, 'YYYY-MM-DD') WHEN 'requiredDate' THEN\r\n" + 
               "TO_DATE(REQUIRED_DATE, 'YYYY-MM-DD') END ) \r\n" + 
               "BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
         
         pstmt = conn.prepareStatement(query.toString());
         pstmt.setString(1, dateSearchCondtion);
         pstmt.setString(2, startDate);
         pstmt.setString(3, endDate);

         rs = pstmt.executeQuery();

         MrpTO bean = null;
         
         while (rs.next()) {
            
            bean = new MrpTO();

            bean.setMrpNo(rs.getString("MRP_NO"));
            bean.setMpsNo(rs.getString("MPS_NO"));
            bean.setMrpGatheringNo(rs.getString("MRP_GATHERING_NO"));
            bean.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
            bean.setItemCode(rs.getString("ITEM_CODE"));
            bean.setItemName(rs.getString("ITEM_NAME"));
            bean.setOrderDate(rs.getString("ORDER_DATE"));
            bean.setRequiredDate(rs.getString("REQUIRED_DATE"));
            bean.setRequiredAmount(rs.getInt("REQUIRED_AMOUNT"));
            bean.setUnitOfMrp(rs.getString("UNIT_OF_MRP"));
            bean.setMrpGatheringStatus(rs.getString("MRP_GATHERING_STATUS"));
            
            mrpList.add(bean);
            
         }

         return mrpList;

      } catch (Exception sqle) {

         throw new DataAccessException(sqle.getMessage());

      } finally {

         dataSourceTransactionManager.close(pstmt, rs);

      }
      
   }
   
   

   public ArrayList<MrpTO> selectMrpListAsMrpGatheringNo(String mrpGatheringNo) { 

      if (logger.isDebugEnabled()) {
         logger.debug("MrpDAOImpl : selectMrpListAsMrpGatheringNo 시작");
      }
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      ArrayList<MrpTO> mrpList = new ArrayList<MrpTO>();

      try {
         conn = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
/*
SELECT * FROM MRP WHERE MRP_GATHERING_NO =?
*/
         query.append(
               "SELECT * FROM MRP WHERE MRP_GATHERING_NO =?");
         
         pstmt = conn.prepareStatement(query.toString());
         pstmt.setString(1, mrpGatheringNo);

         rs = pstmt.executeQuery();

         MrpTO bean = null;
         
         while (rs.next()) {
            
            bean = new MrpTO();

            bean.setMrpNo(rs.getString("MRP_NO"));
            bean.setMpsNo(rs.getString("MPS_NO"));
            bean.setMrpGatheringNo(rs.getString("MRP_GATHERING_NO"));
            bean.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
            bean.setItemCode(rs.getString("ITEM_CODE"));
            bean.setItemName(rs.getString("ITEM_NAME"));
            bean.setOrderDate(rs.getString("ORDER_DATE"));
            bean.setRequiredDate(rs.getString("REQUIRED_DATE"));
            bean.setRequiredAmount(rs.getInt("REQUIRED_AMOUNT"));
            bean.setUnitOfMrp(rs.getString("UNIT_OF_MRP"));
            bean.setMrpGatheringStatus(rs.getString("MRP_GATHERING_STATUS"));
            
            mrpList.add(bean);
         }

         return mrpList;

      } catch (Exception sqle) {

         throw new DataAccessException(sqle.getMessage());

      } finally {

         dataSourceTransactionManager.close(pstmt, rs);

      }
      
   }
   
   @Override
   public HashMap<String,Object> openMrp(String mpsNoList) {
      if (logger.isDebugEnabled()) {
         logger.debug("MrpDAOImpl : openMrp 시작");
      }
      
      Connection conn = null;
      CallableStatement cs = null;
      ResultSet rs = null;

      ArrayList<OpenMrpTO> openMrpList = new ArrayList<OpenMrpTO>();
      HashMap<String,Object> resultMap = new HashMap<>();
      
      try {
         conn = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
         query.append(" {call P_MRP_OPEN(?,?,?,?)} ");
         System.out.println("      @ 프로시저 호출");

          cs = conn.prepareCall(query.toString());
            cs.setString(1,mpsNoList);
            cs.registerOutParameter(2,OracleTypes.NUMBER);
            cs.registerOutParameter(3,OracleTypes.VARCHAR);			//https://docs.oracle.com/cd/E11882_01/appdev.112/e13995/index.html?oracle/jdbc/OracleTypes.html
            cs.registerOutParameter(4,OracleTypes.CURSOR);
            cs.executeUpdate();
            int errorCode = cs.getInt(2);
            String errorMsg = cs.getString(3);
            rs = (ResultSet) cs.getObject(4);

         OpenMrpTO bean = null;
         
         while (rs.next()) {

            bean = new OpenMrpTO();					//MRP_OPEN_TEMP 에 저장된 값이 담기게 되고 여기서 세팅을 해준다.

            bean.setMpsNo(rs.getString("MPS_NO"));
            bean.setBomNo(rs.getString("BOM_NO"));
            bean.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
            bean.setItemCode(rs.getString("ITEM_CODE"));
            bean.setItemName(rs.getString("ITEM_NAME"));
            bean.setUnitOfMrp(rs.getString("UNIT_OF_MRP"));
            bean.setPlanAmount(rs.getString("PLAN_AMOUNT"));
            bean.setOrderDate(rs.getString("ORDER_DATE"));
            bean.setRequiredDate(rs.getString("REQUIRED_DATE"));
            bean.setTotalLossRate(rs.getString("TOTAL_LOSS_RATE"));
            bean.setCaculatedAmount(rs.getString("CACULATED_AMOUNT"));
            bean.setRequiredAmount(rs.getInt("REQUIRED_AMOUNT"));
            
            openMrpList.add(bean);
         }
         
         resultMap.put("gridRowJson", openMrpList);
         resultMap.put("errorCode",errorCode);
         resultMap.put("errorMsg",errorMsg);
         
      } catch (Exception sqle) {

         throw new DataAccessException(sqle.getMessage());

      } finally {

         dataSourceTransactionManager.close(cs, rs);

      }

      return resultMap;
   }
   
   
   
   public int selectMrpCount(String mrpRegisterDate) {

      if (logger.isDebugEnabled()) {
         logger.debug("MrpDAOImpl : selectMrpCount 시작");
      }
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      try {
         conn = dataSourceTransactionManager.getConnection();

         StringBuffer query = new StringBuffer();
         /*
          * SELECT * FROM MRP WHERE INSTR(MRP_NO, REPLACE( ? , '-' , '' ) ) > 0
          */
         query.append("SELECT * FROM MRP WHERE INSTR(MRP_NO, REPLACE( ? , '-' , '' ) ) > 0");
         pstmt = conn.prepareStatement(query.toString());
         pstmt.setString(1, mrpRegisterDate);

         rs = pstmt.executeQuery();

         TreeSet<Integer> intSet = new TreeSet<>();

         while (rs.next()) {
            String mrpNo = rs.getString("MRP_NO");

            // MRP 일련번호에서 마지막 3자리만 가져오기
            int no = Integer.parseInt(mrpNo.substring(mrpNo.length()-3, mrpNo.length()));
  
            intSet.add(no);
         }

         if (intSet.isEmpty()) {
            return 1;
         } else {
            return intSet.pollLast() + 1;   // 가장 높은 번호 + 1
         }

      } catch (Exception sqle) {

         throw new DataAccessException(sqle.getMessage());

      } finally {

         dataSourceTransactionManager.close(pstmt, rs);

      }
   }
   

   public void insertMrp(MrpTO bean) {
      
      if (logger.isDebugEnabled()) {
         logger.debug("MrpDAOImpl : insertMrp 시작");
      }
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      try {
         conn = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
/*
Insert into MRP
(MRP_NO, MPS_NO, MRP_GATHERING_NO, ITEM_CLASSIFICATION, 
ITEM_CODE, ITEM_NAME, UNIT_OR_MRP, 
REQUIRED_AMOUNT, LEAD_TIME, SCHEDULED_COMPLETE_DATE,
MRP_GATHERING_STATUS)
values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
*/
         query.append(
               "Insert into MRP\r\n" + 
               "(MRP_NO, MPS_NO, MRP_GATHERING_NO, ITEM_CLASSIFICATION, \r\n" + 
               "ITEM_CODE, ITEM_NAME, UNIT_OF_MRP, \r\n" + 
               "REQUIRED_AMOUNT, ORDER_DATE, REQUIRED_DATE,\r\n" + 
               "MRP_GATHERING_STATUS)\r\n" + 
               "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         
         pstmt = conn.prepareStatement(query.toString());

         pstmt.setString(1, bean.getMrpNo());
         pstmt.setString(2, bean.getMpsNo());
         pstmt.setString(3, bean.getMrpGatheringNo());
         pstmt.setString(4, bean.getItemClassification());
         pstmt.setString(5, bean.getItemCode());
         pstmt.setString(6, bean.getItemName());
         pstmt.setString(7, bean.getUnitOfMrp());
         pstmt.setInt(8, bean.getRequiredAmount());
         pstmt.setString(9, bean.getOrderDate());
         pstmt.setString(10, bean.getRequiredDate());
         pstmt.setString(11, bean.getMrpGatheringStatus());
         
         rs = pstmt.executeQuery();

      } catch (Exception sqle) {

         throw new DataAccessException(sqle.getMessage());

      } finally {

         dataSourceTransactionManager.close(pstmt, rs);

      }
      
   }
   

   public void updateMrp(MrpTO bean) {

      if (logger.isDebugEnabled()) {
         logger.debug("MrpDAOImpl : updateMrp 시작");
      }
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      try {
         conn = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
         /*
          * UPDATE MRP SET MPS_NO = ? , MRP_GATHERING_NO = ? , ITEM_CLASSIFICATION = ? ,
          * ITEM_CODE = ? , ITEM_NAME = ? , UNIT_OF_MRP = ? , REQUIRED_AMOUNT = ? ,
          * ORDER_DATE = ? , REQUIRED_DATE = ? , MRP_GATHERING_STATUS = ? WHERE
          * MRP_NO = ?
          */
         query.append("UPDATE MRP SET MPS_NO = ? , MRP_GATHERING_NO = ? ,\r\n"
               + "ITEM_CLASSIFICATION = ? , ITEM_CODE = ? ,\r\n" + "ITEM_NAME = ? , UNIT_OR_MRP = ? ,\r\n"
               + "REQUIRED_AMOUNT = ? , ORDER_DATE = ? ,\r\n"
               + "REQUIRED_DATE = ? , MRP_GATHERING_STATUS = ? \r\n" + "WHERE MRP_NO = ?");
         
         pstmt = conn.prepareStatement(query.toString());

         pstmt.setString(1, bean.getMpsNo());
         pstmt.setString(2, bean.getMrpGatheringNo());
         pstmt.setString(3, bean.getItemClassification());
         pstmt.setString(4, bean.getItemCode());
         pstmt.setString(5, bean.getItemName());
         pstmt.setString(6, bean.getUnitOfMrp());
         pstmt.setInt(7, bean.getRequiredAmount());
         pstmt.setString(8, bean.getOrderDate());
         pstmt.setString(9, bean.getRequiredDate());
         pstmt.setString(10, bean.getMrpGatheringStatus());
         pstmt.setString(11, bean.getMrpNo());

         rs = pstmt.executeQuery();

      } catch (Exception sqle) {

         throw new DataAccessException(sqle.getMessage());

      } finally {

         dataSourceTransactionManager.close(pstmt, rs);

      }
      
   }
   


   public void changeMrpGatheringStatus(String mrpNo, String mrpGatheringNo, String mrpGatheringStatus) {

      if (logger.isDebugEnabled()) {
         logger.debug("MrpDAOImpl : changeMrpGatheringStatus 시작");
      }
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      try {
         conn = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
/*
UPDATE MRP SET MRP_GATHERING_NO = ? , MRP_GATHERING_STATUS = ? WHERE MRP_NO = ?
*/
         query.append(
               "UPDATE MRP SET MRP_GATHERING_NO = ? , MRP_GATHERING_STATUS = ? WHERE MRP_NO = ?\r\n" + 
               "");
         
         pstmt = conn.prepareStatement(query.toString());

         pstmt.setString(1, mrpGatheringNo);
         pstmt.setString(2, mrpGatheringStatus);
         pstmt.setString(3, mrpNo);
         
         rs = pstmt.executeQuery();

      } catch (Exception sqle) {

         throw new DataAccessException(sqle.getMessage());

      } finally {

         dataSourceTransactionManager.close(pstmt, rs);

      }
      
   }

   
   
   public void deleteMrp(MrpTO bean) {
      
      if (logger.isDebugEnabled()) {
         logger.debug("MrpDAOImpl : deleteMrp 시작");
      }
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      try {
         conn = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
         
         query.append("DELETE FROM MRP WHERE MRP_NO = ?");
         
         pstmt = conn.prepareStatement(query.toString());

         pstmt.setString(1, bean.getMrpNo());

         rs = pstmt.executeQuery();

      } catch (Exception sqle) {

         throw new DataAccessException(sqle.getMessage());

      } finally {

         dataSourceTransactionManager.close(pstmt, rs);

      }
      
   }
   
}