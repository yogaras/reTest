package com.estimulo.logistics.sales.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.production.to.SalesPlanInMpsAvailableTO;
import com.estimulo.logistics.sales.to.SalesPlanTO;

public class SalesPlanDAOImpl implements SalesPlanDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(SalesPlanDAOImpl.class);

	// 싱글톤
	private static SalesPlanDAO instance = new SalesPlanDAOImpl();

	private SalesPlanDAOImpl() {
	}

	public static SalesPlanDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ SalesPlanDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	public ArrayList<SalesPlanTO> selectSalesPlanList(String dateSearchCondition, String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanDAOImpl : selectSalesPlanList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<SalesPlanTO> salesPlanTOList = new ArrayList<SalesPlanTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * SELECT * FROM SALES_PLAN WHERE ( CASE ? WHEN 'salesPlanDate' THEN
			 * TO_DATE(SALES_PLAN_DATE, 'YYYY-MM-DD') WHEN 'dueDateOfSales' THEN
			 * TO_DATE(DUE_DATE_OF_SALES, 'YYYY-MM-DD') END ) BETWEEN TO_DATE(?,
			 * 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')
			 */
			query.append("SELECT * FROM SALES_PLAN WHERE ( CASE ? WHEN 'salesPlanDate' THEN\r\n"
					+ "TO_DATE(SALES_PLAN_DATE, 'YYYY-MM-DD') WHEN 'dueDateOfSales' THEN\r\n"
					+ "TO_DATE(DUE_DATE_OF_SALES, 'YYYY-MM-DD') END ) \r\n"
					+ "BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, dateSearchCondition);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);

			rs = pstmt.executeQuery();

			SalesPlanTO bean = null;

			while (rs.next()) {

				bean = new SalesPlanTO();

				bean.setSalesPlanNo(rs.getString("SALES_PLAN_NO"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setSalesPlanDate(rs.getString("SALES_PLAN_DATE"));
				bean.setUnitOfSales(rs.getString("UNIT_OF_SALES"));
				bean.setDueDateOfSales(rs.getString("DUE_DATE_OF_SALES"));
				bean.setSalesAmount(rs.getInt("SALES_AMOUNT"));
				bean.setUnitPriceOfSales(rs.getInt("UNIT_PRICE_OF_SALES"));
				bean.setSumPriceOfSales(rs.getInt("SUM_PRICE_OF_SALES"));
				bean.setMpsApplyStatus(rs.getString("MPS_APPLY_STATUS"));
				bean.setDescription(rs.getString("DESCRIPTION"));

				salesPlanTOList.add(bean);
				
			}

			return salesPlanTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public int selectSalesPlanCount(String salesPlanDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanDAOImpl : selectSalesPlanCount 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * SELECT COUNT(*) FROM SALES_PLAN WHERE TO_DATE(SALES_PLAN_DATE, 'YYYY-MM-DD')
			 * = TO_DATE('2018-07-10', 'YYYY-MM-DD')
			 */
			query.append("SELECT COUNT(*) FROM SALES_PLAN \r\n" + "WHERE TO_DATE(SALES_PLAN_DATE, 'YYYY-MM-DD') =\r\n"
					+ "TO_DATE(?, 'YYYY-MM-DD')");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, salesPlanDate);

			rs = pstmt.executeQuery();

			int i = 0;

			while (rs.next()) {
				i = rs.getInt(1);
			}

			return i + 1;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public ArrayList<SalesPlanInMpsAvailableTO> selectSalesPlanListInMpsAvailable(String searchCondition,
			String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanDAOImpl : selectSalesPlanListInMpsAvailable 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList = new ArrayList<SalesPlanInMpsAvailableTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * SELECT * FROM SALES_PLAN WHERE MPS_APPLY_STATUS IS NULL AND ( CASE ? WHEN
			 * 'salesPlanDate' THEN TO_DATE(SALES_PLAN_DATE, 'YYYY-MM-DD') WHEN
			 * 'dueDateOfSales' THEN TO_DATE(DUE_DATE_OF_SALES, 'YYYY-MM-DD') END ) BETWEEN
			 * TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')
			 */
			query.append("SELECT * FROM SALES_PLAN\r\n" + "WHERE MPS_APPLY_STATUS IS NULL\r\n"
					+ "AND ( CASE ? WHEN 'salesPlanDate' THEN TO_DATE(SALES_PLAN_DATE, 'YYYY-MM-DD') \r\n"
					+ "						WHEN 'dueDateOfSales' THEN TO_DATE(DUE_DATE_OF_SALES, 'YYYY-MM-DD') END ) \r\n"
					+ "BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, searchCondition);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);

			rs = pstmt.executeQuery();

			SalesPlanInMpsAvailableTO bean = null;

			while (rs.next()) {

				bean = new SalesPlanInMpsAvailableTO();

				bean.setSalesPlanNo(rs.getString("SALES_PLAN_NO"));
				// bean.setPlanClassification(rs.getString(""));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setUnitOfSales(rs.getString("UNIT_OF_SALES"));
				bean.setSalesPlanDate(rs.getString("SALES_PLAN_DATE"));
				// bean.setMpsPlanDate(rs.getString(""));
				// bean.setScheduledEndDate(rs.getString(""));
				bean.setDueDateOfSales(rs.getString("DUE_DATE_OF_SALES"));
				bean.setSalesAmount(rs.getString("SALES_AMOUNT"));
				bean.setUnitPriceOfSales(rs.getInt("UNIT_PRICE_OF_SALES"));
				bean.setSumPriceOfSales(rs.getInt("SUM_PRICE_OF_SALES"));
				bean.setMpsApplyStatus(rs.getString("MPS_APPLY_STATUS"));
				bean.setDescription(rs.getString("DESCRIPTION"));

				salesPlanInMpsAvailableList.add(bean);
				
			}

			return salesPlanInMpsAvailableList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void insertSalesPlan(SalesPlanTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanDAOImpl : insertSalesPlan 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * Insert into SALES_PLAN (SALES_PLAN_NO, ITEM_CODE, ITEM_NAME, SALES_PLAN_DATE,
			 * UNIT_OF_SALES, DUE_DATE_OF_SALES, SALES_AMOUNT, UNIT_PRICE_OF_SALES,
			 * SUM_PRICE_OF_SALES, MPS_APPLY_STATUS, DESCRIPTION) values ('SA2018070301',
			 * 'DK-01', '디지털카메라 NO.1', '2018-07-03', 'EA', '2018-07-13', 200, 210000,
			 * 42000000, 'Y', null)
			 * 
			 */
			query.append("Insert into SALES_PLAN \r\n" + "(SALES_PLAN_NO, ITEM_CODE, ITEM_NAME, \r\n"
					+ "SALES_PLAN_DATE, UNIT_OF_SALES, DUE_DATE_OF_SALES, \r\n"
					+ "SALES_AMOUNT, UNIT_PRICE_OF_SALES, SUM_PRICE_OF_SALES, \r\n"
					+ "MPS_APPLY_STATUS, DESCRIPTION) \r\n" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getSalesPlanNo());
			pstmt.setString(2, bean.getItemCode());
			pstmt.setString(3, bean.getItemName());
			pstmt.setString(4, bean.getSalesPlanDate());
			pstmt.setString(5, bean.getUnitOfSales());
			pstmt.setString(6, bean.getDueDateOfSales());
			pstmt.setInt(7, bean.getSalesAmount());
			pstmt.setInt(8, bean.getUnitPriceOfSales());
			pstmt.setInt(9, bean.getSumPriceOfSales());
			pstmt.setString(10, bean.getMpsApplyStatus());
			pstmt.setString(11, bean.getDescription());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void updateSalesPlan(SalesPlanTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanDAOImpl : updateSalesPlan 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * UPDATE SALES_PLAN SET ITEM_CODE = ? , ITEM_NAME = ? , SALES_PLAN_DATE = ? ,
			 * UNIT_OF_SALES = ? , DUE_DATE_OF_SALES = ? , SALES_AMOUNT = ? ,
			 * UNIT_PRICE_OF_SALES = ? , SUM_PRICE_OF_SALES = ? , MPS_APPLY_STATUS = ? ,
			 * DESCRIPTION = ? WHERE SALES_PLAN_NO = ?
			 */
			query.append("UPDATE SALES_PLAN SET \r\n" + "ITEM_CODE = ? , ITEM_NAME = ? , SALES_PLAN_DATE = ? ,\r\n"
					+ "UNIT_OF_SALES = ? , DUE_DATE_OF_SALES = ? ,\r\n"
					+ "SALES_AMOUNT = ? , UNIT_PRICE_OF_SALES = ? ,\r\n"
					+ "SUM_PRICE_OF_SALES = ? , MPS_APPLY_STATUS = ? ,\r\n" + "DESCRIPTION = ?\r\n"
					+ "WHERE SALES_PLAN_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getItemCode());
			pstmt.setString(2, bean.getItemName());
			pstmt.setString(3, bean.getSalesPlanDate());
			pstmt.setString(4, bean.getUnitOfSales());
			pstmt.setString(5, bean.getDueDateOfSales());
			pstmt.setInt(6, bean.getSalesAmount());
			pstmt.setInt(7, bean.getUnitPriceOfSales());
			pstmt.setInt(8, bean.getSumPriceOfSales());
			pstmt.setString(9, bean.getMpsApplyStatus());
			pstmt.setString(10, bean.getDescription());
			pstmt.setString(11, bean.getSalesPlanNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void changeMpsStatusOfSalesPlan(String salesPlanNo, String mpsStatus) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanDAOImpl : changeMpsStatusOfSalesPlan 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * UPDATE SALES_PLAN SET MPS_APPLY_STATUS = ? WHERE SALES_PLAN_NO = ?
			 */
			query.append("UPDATE SALES_PLAN SET MPS_APPLY_STATUS = ? WHERE SALES_PLAN_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, mpsStatus);
			pstmt.setString(2, salesPlanNo);

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void deleteSalesPlan(SalesPlanTO TO) {

		if (logger.isDebugEnabled()) {
			logger.debug("SalesPlanDAOImpl : deleteSalesPlan 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			query.append("DELETE FROM SALES_PLAN WHERE SALES_PLAN_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, TO.getSalesPlanNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

}
