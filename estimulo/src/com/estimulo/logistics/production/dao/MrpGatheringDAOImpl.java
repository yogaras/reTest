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
import com.estimulo.logistics.production.to.MrpGatheringTO;

public class MrpGatheringDAOImpl implements MrpGatheringDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(MrpGatheringDAOImpl.class);

	// 싱글톤
	private static MrpGatheringDAO instance = new MrpGatheringDAOImpl();

	private MrpGatheringDAOImpl() {
	}

	public static MrpGatheringDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ MrpGatheringDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	public ArrayList<MrpGatheringTO> selectMrpGatheringList(String searchDateCondition, String startDate,
			String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpGatheringDAOImpl : selectMrpGatheringList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<MrpGatheringTO> mrpGatheringList = new ArrayList<MrpGatheringTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * SELECT * FROM MRP_GATHERING WHERE ( CASE ? WHEN 'claimDate' THEN
			 * TO_DATE(CLAIM_DATE, 'YYYY-MM-DD') WHEN 'orderDate' THEN TO_DATE(ORDER_DATE,
			 * 'YYYY-MM-DD') END ) BETWEEN TO_DATE(?,'YYYY-MM-DD') AND
			 * TO_DATE(?,'YYYY-MM-DD')
			 */
			query.append("SELECT * FROM MRP_GATHERING WHERE ( CASE ? WHEN 'claimDate' THEN\r\n"
					+ "TO_DATE(CLAIM_DATE, 'YYYY-MM-DD') WHEN 'dueDate' THEN\r\n"
					+ "TO_DATE(DUE_DATE, 'YYYY-MM-DD') END ) \r\n"
					+ "BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')\r\n" + "");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, searchDateCondition);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);

			rs = pstmt.executeQuery();

			MrpGatheringTO bean = null;

			while (rs.next()) {

				bean = new MrpGatheringTO();

				bean.setMrpGatheringNo(rs.getString("MRP_GATHERING_NO"));
				bean.setOrderOrProductionStatus(rs.getString("ORDER_OR_PRODUCTION_STATUS"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setUnitOfMrpGathering(rs.getString("UNIT_OF_MRP_GATHERING"));
				bean.setClaimDate(rs.getString("CLAIM_DATE"));
				bean.setDueDate(rs.getString("DUE_DATE"));
				bean.setNecessaryAmount(rs.getInt("NECESSARY_AMOUNT"));

				mrpGatheringList.add(bean);

			}

			return mrpGatheringList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	public ArrayList<MrpGatheringTO> getMrpGathering(String mrpNoList) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpGatheringDAOImpl : getMrpGathering 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<MrpGatheringTO> mrpGatheringList = new ArrayList<MrpGatheringTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			/*
			 * WITH MRP_NO_STR AS ( SELECT ? FROM DUAL ) ,
			 * 
			 * MRP_NO_LIST AS ( SELECT TRIM( REGEXP_SUBSTR( (SELECT * FROM MRP_NO_STR )
			 * ,'[^,]+', 1, LEVEL ) ) AS MRP_NO FROM DUAL CONNECT BY REGEXP_SUBSTR( (SELECT
			 * * FROM MRP_NO_STR ) , '[^,]+', 1, LEVEL ) IS NOT NULL )
			 * 
			 * SELECT ( CASE TO_CHAR(ITEM_CLASSIFICATION) WHEN '원재료' THEN '구매' WHEN '반제품'
			 * THEN '생산' WHEN '완제품' THEN '생산' ELSE TO_CHAR(ITEM_CLASSIFICATION) END ) AS
			 * ORDER_OR_PRODUCTION_STATUS, ITEM_CODE, ITEM_NAME, UNIT_OF_MRP AS
			 * UNIT_OF_MRP_GATHERING, MIN(ORDER_DATE) AS CLAIM_DATE, MIN(REQUIRED_DATE) AS
			 * DUE_DATE, SUM(REQUIRED_AMOUNT) AS NECESSARY_AMOUNT FROM ( SELECT * FROM MRP 
			 * WHERE MRP_GATHERING_STATUS IS NULL AND MRP_NO IN ( SELECT MRP_NO FROM
			 * MRP_NO_LIST ) ) GROUP BY ITEM_CLASSIFICATION, ITEM_CODE, ITEM_NAME,
			 * UNIT_OF_MRP ORDER BY CLAIM_DATE, ORDER_OR_PRODUCTION_STATUS
			 */

			query.append("WITH MRP_NO_STR AS ( SELECT ? FROM DUAL ) ,\r\n" + "\r\n" + "MRP_NO_LIST AS (\r\n"
					+ "SELECT TRIM( REGEXP_SUBSTR( (SELECT * FROM MRP_NO_STR ) ,'[^,]+', 1, LEVEL ) ) AS MRP_NO FROM DUAL \r\n"
					+ "CONNECT BY REGEXP_SUBSTR( (SELECT * FROM MRP_NO_STR ) , '[^,]+', 1, LEVEL ) IS NOT NULL\r\n"
					+ ") \r\n" + "\r\n" + 
					"SELECT \r\n"
					+ "    ( CASE TO_CHAR(ITEM_CLASSIFICATION) WHEN '원재료' THEN '구매' \r\n"
					+ "            WHEN '반제품' THEN '생산' WHEN '완제품' THEN '생산' \r\n"
					+ "            ELSE TO_CHAR(ITEM_CLASSIFICATION) END ) AS ORDER_OR_PRODUCTION_STATUS, \r\n"
					+ "    ITEM_CODE, TRIM(ITEM_NAME) AS ITEM_NAME, UNIT_OF_MRP AS UNIT_OF_MRP_GATHERING, \r\n"
					+ "    MIN(ORDER_DATE) AS CLAIM_DATE, MIN(REQUIRED_DATE) AS DUE_DATE, \r\n"
					+ "    SUM(REQUIRED_AMOUNT) AS NECESSARY_AMOUNT\r\n" + "FROM ( \r\n" + "    SELECT * FROM MRP \r\n"
					+ "    WHERE MRP_GATHERING_STATUS IS NULL  \r\n"
					+ "    AND MRP_NO IN ( SELECT MRP_NO FROM MRP_NO_LIST )\r\n" + "    )\r\n"
					+ "GROUP BY ITEM_CLASSIFICATION, ITEM_CODE, TRIM(ITEM_NAME), UNIT_OF_MRP \r\n"
					+ "ORDER BY CLAIM_DATE, ORDER_OR_PRODUCTION_STATUS");

			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, mrpNoList);

			rs = pstmt.executeQuery();

			MrpGatheringTO bean = null;

			while (rs.next()) {

				bean = new MrpGatheringTO();

				bean.setOrderOrProductionStatus(rs.getString("ORDER_OR_PRODUCTION_STATUS"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setUnitOfMrpGathering(rs.getString("UNIT_OF_MRP_GATHERING"));
				bean.setClaimDate(rs.getString("CLAIM_DATE"));
				bean.setDueDate(rs.getString("DUE_DATE"));
				bean.setNecessaryAmount(rs.getInt("NECESSARY_AMOUNT"));

				mrpGatheringList.add(bean);

			}

			return mrpGatheringList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	public int selectMrpGatheringCount(String mrpGatheringRegisterDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpGatheringDAOImpl : selectMrpGatheringCount 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * SELECT * FROM MRP_GATHERING WHERE INSTR(MRP_GATHERING_NO, REPLACE( ? , '-' ,
			 * '' ) ) > 0
			 */
			query.append("SELECT * FROM MRP_GATHERING WHERE INSTR(MRP_GATHERING_NO, REPLACE( ? , '-' , '' ) ) > 0");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, mrpGatheringRegisterDate);

			rs = pstmt.executeQuery();

			TreeSet<Integer> intSet = new TreeSet<>();

			while (rs.next()) {
				String mrpGatheringNo = rs.getString("MRP_GATHERING_NO");

				// mrpGathering 일련번호에서 마지막 2자리만 가져오기
				int no = Integer
						.parseInt(mrpGatheringNo.substring(mrpGatheringNo.length() - 2, mrpGatheringNo.length()));

				intSet.add(no);
			}

			if (intSet.isEmpty()) {
				return 1;
			} else {
				return intSet.pollLast() + 1; // 가장 높은 번호 + 1
			}

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void insertMrpGathering(MrpGatheringTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpGatheringDAOImpl : insertMrpGathering 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * Insert into MRP_GATHERING (MRP_GATHERING_NO, ORDER_OR_PRODUCTION_STATUS,
			 * ITEM_CODE, ITEM_NAME, UNIT_OF_MRP_GATHERING, CLAIM_DATE, DUE_DATE,
			 * NECESSARY_AMOUNT ) values ( ?, ?, ?, ?, ?, ?, ?, ?,
			 * ? )
			 */
			query.append("Insert into MRP_GATHERING \r\n"
					+ "(MRP_GATHERING_NO, ORDER_OR_PRODUCTION_STATUS, ITEM_CODE, ITEM_NAME,\r\n"
					+ "UNIT_OF_MRP_GATHERING, CLAIM_DATE, DUE_DATE, NECESSARY_AMOUNT \r\n"
					+ ") values ( ?, ?, ?, ?, ?, ?, ?, ? )");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getMrpGatheringNo());
			pstmt.setString(2, bean.getOrderOrProductionStatus());
			pstmt.setString(3, bean.getItemCode());
			pstmt.setString(4, bean.getItemName());
			pstmt.setString(5, bean.getUnitOfMrpGathering());
			pstmt.setString(6, bean.getClaimDate());
			pstmt.setString(7, bean.getDueDate());
			pstmt.setInt(8, bean.getNecessaryAmount());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	public void updateMrpGathering(MrpGatheringTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpGatheringDAOImpl : updateMrpGathering 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * UPDATE MRP_GATHERING SET ITEM_CODE = ? , ITEM_NAME = ? ,
			 * UNIT_OF_MRP_GATHERING =? , NECESSARY_AMOUNT = ? , DUE_DATE = ? , CLAIM_DATE
			 * =?, , ORDER_OR_PRODUCTION_STATUS = ? WHERE
			 * MRP_GATHERING_NO = ?
			 */
			query.append(
					"UPDATE MRP_GATHERING SET\r\n" + "ITEM_CODE = ? , ITEM_NAME = ? , UNIT_OF_MRP_GATHERING =? , \r\n"
							+ "NECESSARY_AMOUNT = ? , DUE_DATE = ? , CLAIM_DATE = ?, \r\n"
							+ "ORDER_OR_PRODUCTION_STATUS = ? \r\n"
							+ "WHERE MRP_GATHERING_NO = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getOrderOrProductionStatus());
			pstmt.setString(2, bean.getItemCode());
			pstmt.setString(3, bean.getItemName());
			pstmt.setString(4, bean.getUnitOfMrpGathering());
			pstmt.setInt(5, bean.getNecessaryAmount());
			pstmt.setString(6, bean.getDueDate());
			pstmt.setString(7, bean.getClaimDate());
			pstmt.setString(8, bean.getMrpGatheringNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	public void deleteMrpGathering(MrpGatheringTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("MrpGatheringDAOImpl : deleteMrpGathering 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			query.append("DELETE FROM MRP_GATHERING WHERE MRP_GATHERING_NO = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getMrpGatheringNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}
}
