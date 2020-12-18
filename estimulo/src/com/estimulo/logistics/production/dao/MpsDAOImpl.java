package com.estimulo.logistics.production.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.production.to.MpsTO;


public class MpsDAOImpl implements MpsDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(MpsDAOImpl.class);	
		
	// 싱글톤
	private static MpsDAO instance = new MpsDAOImpl();

	private MpsDAOImpl() {
	}

	public static MpsDAO getInstance() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("@ MpsDAOImpl 객체접근");
		}
		
		return instance;
	}
	

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	
	
	public ArrayList<MpsTO> selectMpsList(String startDate, String endDate, String includeMrpApply) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsDAOImpl : selectMpsList 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<MpsTO> mpsTOList = new ArrayList<MpsTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
SELECT * FROM MPS 
WHERE TO_DATE(MPS_PLAN_DATE, 'YYYY-MM-DD') 
BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')
*/
			query.append(
					"SELECT * FROM MPS \r\n" + 
					"WHERE TO_DATE(MPS_PLAN_DATE, 'YYYY-MM-DD') \r\n" + 
					"BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD') ");
			
			if(includeMrpApply == null ) {  // MRP 적용된 주생산계획 포함
				
			} else if(includeMrpApply.equals("includeMrpApply")) {  // MRP 적용된 주생산계획 포함
				
			} else if(includeMrpApply.equals("excludeMrpApply")) {  // MRP 적용된 주생산계획 미포함
				query.append("AND MRP_APPLY_STATUS IS NULL");
				
			}
			
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);

			rs = pstmt.executeQuery();

			MpsTO bean = null;
			
			while (rs.next()) {
				
				bean = new MpsTO();
				
				bean.setMpsNo(rs.getString("MPS_NO"));
				bean.setMpsPlanClassification(rs.getString("MPS_PLAN_CLASSIFICATION"));
				bean.setContractDetailNo(rs.getString("CONTRACT_DETAIL_NO"));
				bean.setSalesPlanNo(rs.getString("SALES_PLAN_NO"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setUnitOfMps(rs.getString("UNIT_OF_MPS"));
				bean.setMpsPlanDate(rs.getString("MPS_PLAN_DATE"));
				bean.setMpsPlanAmount(rs.getString("MPS_PLAN_AMOUNT"));
				bean.setDueDateOfMps(rs.getString("DUE_DATE_OF_MPS"));
				bean.setScheduledEndDate(rs.getString("SCHEDULED_END_DATE"));
				bean.setMrpApplyStatus(rs.getString("MRP_APPLY_STATUS"));
				bean.setDescription(rs.getString("DESCRIPTION"));
				
				mpsTOList.add(bean);
			}

			return mpsTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
		
	}
	

	@Override
	public int selectMpsCount(String mpsPlanDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsDAOImpl : selectMpsCount 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * SELECT * FROM MPS WHERE MPS_PLAN_DATE = ?
			 */
			query.append("SELECT * FROM MPS WHERE MPS_PLAN_DATE = ?");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, mpsPlanDate);

			rs = pstmt.executeQuery();

			TreeSet<Integer> intSet = new TreeSet<>();

			while (rs.next()) {
				String mpsNo = rs.getString("MPS_NO");

				// MPS 일련번호에서 마지막 2자리만 가져오기
				int no = Integer.parseInt(mpsNo.substring(mpsNo.length()-2, mpsNo.length()));

				intSet.add(no);
			}
			System.out.println("@@@MPS_NO 결과@@@"+intSet.isEmpty());
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

	
	
	public void insertMps(MpsTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsDAOImpl : insertMps 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
Insert into MPS 
(MPS_NO, CONTRACT_DETAIL_NO, MPS_PLAN_CLASSIFICATION, 
SALES_PLAN_NO, ITEM_CODE, ITEM_NAME, MPS_PLAN_DATE, SCHEDULED_END_DATE, 
UNIT_OF_MPS, DUE_DATE_OF_MPS, MPS_PLAN_AMOUNT, MRP_APPLY_STATUS, DESCRIPTION) 
values ('PS2018070301','CO2018070301-01','수주',null,'DK-01','디지털카메라 NO.1','2018-07-03',null,'EA','2018-07-13',200,null,null)
*/
			query.append(
					"Insert into MPS \r\n" + 
					"(MPS_NO, CONTRACT_DETAIL_NO, MPS_PLAN_CLASSIFICATION, \r\n" + 
					"SALES_PLAN_NO, ITEM_CODE, ITEM_NAME, MPS_PLAN_DATE, SCHEDULED_END_DATE, \r\n" + 
					"UNIT_OF_MPS, DUE_DATE_OF_MPS, MPS_PLAN_AMOUNT, MRP_APPLY_STATUS, DESCRIPTION) \r\n" +  
					"values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getMpsNo());
			pstmt.setString(2, bean.getContractDetailNo());
			pstmt.setString(3, bean.getMpsPlanClassification());
			pstmt.setString(4, bean.getSalesPlanNo());
			pstmt.setString(5, bean.getItemCode());
			pstmt.setString(6, bean.getItemName());
			pstmt.setString(7, bean.getMpsPlanDate());
			pstmt.setString(8, bean.getScheduledEndDate());
			pstmt.setString(9, bean.getUnitOfMps());
			pstmt.setString(10, bean.getDueDateOfMps());
			pstmt.setString(11, bean.getMpsPlanAmount());
			pstmt.setString(12, bean.getMrpApplyStatus());
			pstmt.setString(13, bean.getDescription());
			
			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
		
	}
	

	public void updateMps(MpsTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsDAOImpl : updateMps 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
UPDATE MPS SET 
MPS_PLAN_CLASSIFICATION = ? , CONTRACT_DETAIL_NO = ? ,
SALES_PLAN_NO = ? , ITEM_CODE = ? , ITEM_NAME = ? ,
UNIT_OF_MPS = ? , MPS_PLAN_DATE = ? ,
MPS_PLAN_AMOUNT = ? , DUE_DATE_OF_MPS = ? ,
SCHEDULED_END_DATE = ? , MRP_APPLY_STATUS = ? ,
DESCRIPTION = ? 
WHERE MPS_NO = ?
*/
			query.append(
					"UPDATE MPS SET \r\n" + 
					"MPS_PLAN_CLASSIFICATION = ? , CONTRACT_DETAIL_NO = ? ,\r\n" + 
					"SALES_PLAN_NO = ? , ITEM_CODE = ? , ITEM_NAME = ? ,\r\n" + 
					"UNIT_OF_MPS = ? , MPS_PLAN_DATE = ? ,\r\n" + 
					"MPS_PLAN_AMOUNT = ? , DUE_DATE_OF_MPS = ? ,\r\n" + 
					"SCHEDULED_END_DATE = ? , MRP_APPLY_STATUS = ? ,\r\n" + 
					"DESCRIPTION = ? \r\n" + 
					"WHERE MPS_NO = ?");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getContractDetailNo());
			pstmt.setString(2, bean.getMpsPlanClassification());
			pstmt.setString(3, bean.getSalesPlanNo());
			pstmt.setString(4, bean.getItemCode());
			pstmt.setString(5, bean.getItemName());
			pstmt.setString(6, bean.getMpsPlanDate());
			pstmt.setString(7, bean.getScheduledEndDate());
			pstmt.setString(8, bean.getUnitOfMps());
			pstmt.setString(9, bean.getDueDateOfMps());
			pstmt.setString(10, bean.getMpsPlanAmount());
			pstmt.setString(11, bean.getMrpApplyStatus());
			pstmt.setString(12, bean.getDescription());
			pstmt.setString(13, bean.getMpsNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
		
	}
	

	public void  changeMrpApplyStatus(String mpsNo, String mrpStatus) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsDAOImpl : changeMrpApplyStatus 시작");
		}		

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
UPDATE MPS SET MRP_APPLY_STATUS = ? WHERE MPS_NO = ?
*/
			query.append(
					"UPDATE MPS SET MRP_APPLY_STATUS = ? WHERE MPS_NO = ?");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, mrpStatus);
			pstmt.setString(2, mpsNo);
			
			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
		
	}

	
	public void deleteMps(MpsTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("MpsDAOImpl : deleteMps 시작");
		}		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			
			query.append("DELETE FROM MPS WHERE MPS_NO = ?");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getMpsNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
		
	}
}
