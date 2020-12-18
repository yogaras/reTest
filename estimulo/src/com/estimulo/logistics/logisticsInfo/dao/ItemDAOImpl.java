package com.estimulo.logistics.logisticsInfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.logisticsInfo.to.ItemInfoTO;
import com.estimulo.logistics.logisticsInfo.to.ItemTO;

public class ItemDAOImpl implements ItemDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(ItemDAOImpl.class);

	// 싱글톤
	private static ItemDAO instance = new ItemDAOImpl();

	private ItemDAOImpl() {
	}

	public static ItemDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ ItemDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	public ArrayList<ItemInfoTO> selectAllItemList() {

		if (logger.isDebugEnabled()) {
			logger.debug("ItemDAOImpl : selectAllItemList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<ItemInfoTO> itemInfoList = new ArrayList<ItemInfoTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * SELECT I.ITEM_CODE, I.ITEM_NAME, I.ITEM_GROUP_CODE, I.ITEM_CLASSIFICATION,
			 * I.UNIT_OF_STOCK, I.LOSS_RATE, I.LEAD_TIME, I.STANDARD_UNIT_PRICE,
			 * I.DESCRIPTION, C.CODE_USE_CHECK FROM ITEM I, CODE_DETAIL C WHERE I.ITEM_CODE
			 * = C.DETAIL_CODE (+)
			 */
			query.append("SELECT I.ITEM_CODE, I.ITEM_NAME, I.ITEM_GROUP_CODE, I.ITEM_CLASSIFICATION, \r\n"
					+ "    I.UNIT_OF_STOCK, I.LOSS_RATE, I.LEAD_TIME, I.STANDARD_UNIT_PRICE, I.DESCRIPTION, \r\n"
					+ "    C.CODE_USE_CHECK\r\n" + "FROM ITEM I, CODE_DETAIL C\r\n"
					+ "WHERE I.ITEM_CODE = C.DETAIL_CODE (+)");

			pstmt = conn.prepareStatement(query.toString());

			rs = pstmt.executeQuery();

			while (rs.next()) {

				ItemInfoTO TO = new ItemInfoTO();

				TO.setItemCode(rs.getString("ITEM_CODE"));
				TO.setItemName(rs.getString("ITEM_NAME"));
				TO.setItemGroupCode(rs.getString("ITEM_GROUP_CODE"));
				TO.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
				TO.setUnitOfStock(rs.getString("UNIT_OF_STOCK"));
				TO.setLossRate(rs.getString("LOSS_RATE"));
				TO.setLeadTime(rs.getString("LEAD_TIME"));
				TO.setStandardUnitPrice(rs.getInt("STANDARD_UNIT_PRICE"));
				TO.setCodeUseCheck(rs.getString("CODE_USE_CHECK"));
				TO.setDescription(rs.getString("DESCRIPTION"));

				itemInfoList.add(TO);
			}

			return itemInfoList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public ArrayList<ItemInfoTO> selectItemList(String searchCondition, String paramArray[]) {

		if (logger.isDebugEnabled()) {
			logger.debug("ItemDAOImpl : selectItemList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<ItemInfoTO> itemInfoList = new ArrayList<ItemInfoTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			/*
			 * WITH ITEM_WITH_CODE_USE_CHECK AS ( SELECT I.ITEM_CODE, I.ITEM_NAME,
			 * I.ITEM_GROUP_CODE, I.ITEM_CLASSIFICATION, I.UNIT_OF_STOCK, I.LOSS_RATE,
			 * I.LEAD_TIME, I.STANDARD_UNIT_PRICE, I.DESCRIPTION, C.CODE_USE_CHECK FROM ITEM
			 * I, CODE_DETAIL C WHERE I.ITEM_CODE = C.DETAIL_CODE (+) )
			 * 
			 * SELECT * FROM ITEM_WITH_CODE_USE_CHECK
			 */

			query.append("WITH ITEM_WITH_CODE_USE_CHECK AS\r\n"
					+ "( SELECT I.ITEM_CODE, I.ITEM_NAME, I.ITEM_GROUP_CODE, I.ITEM_CLASSIFICATION, \r\n"
					+ "    I.UNIT_OF_STOCK, I.LOSS_RATE, I.LEAD_TIME, I.STANDARD_UNIT_PRICE, I.DESCRIPTION, \r\n"
					+ "    C.CODE_USE_CHECK\r\n" + "FROM ITEM I, CODE_DETAIL C\r\n"
					+ "WHERE I.ITEM_CODE = C.DETAIL_CODE (+) )\r\n" + "\r\n"
					+ "SELECT * FROM ITEM_WITH_CODE_USE_CHECK ");

			switch (searchCondition) {

			case "ITEM_CLASSIFICATION":

				query.append("WHERE ITEM_CLASSIFICATION = ?");
				pstmt = conn.prepareStatement(query.toString());
				pstmt.setString(1, paramArray[0]);

				break;

			case "ITEM_GROUP_CODE":

				query.append("WHERE ITEM_GROUP_CODE = ?");
				pstmt = conn.prepareStatement(query.toString());
				pstmt.setString(1, paramArray[0]);

				break;

			case "STANDARD_UNIT_PRICE":

				query.append("WHERE STANDARD_UNIT_PRICE BETWEEN TO_NUMBER( ? ) AND TO_NUMBER ( ? )");
				pstmt = conn.prepareStatement(query.toString());
				pstmt.setString(1, paramArray[0]);
				pstmt.setString(2, paramArray[1]);

				break;

			}

			rs = pstmt.executeQuery();

			while (rs.next()) {

				ItemInfoTO TO = new ItemInfoTO();

				TO.setItemCode(rs.getString("ITEM_CODE"));
				TO.setItemName(rs.getString("ITEM_NAME"));
				TO.setItemGroupCode(rs.getString("ITEM_GROUP_CODE"));
				TO.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
				TO.setUnitOfStock(rs.getString("UNIT_OF_STOCK"));
				TO.setLossRate(rs.getString("LOSS_RATE"));
				TO.setLeadTime(rs.getString("LEAD_TIME"));
				TO.setStandardUnitPrice(rs.getInt("STANDARD_UNIT_PRICE"));
				TO.setCodeUseCheck(rs.getString("CODE_USE_CHECK"));
				TO.setDescription(rs.getString("DESCRIPTION"));

				itemInfoList.add(TO);
			}

			return itemInfoList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void insertItem(ItemTO TO) {

		if (logger.isDebugEnabled()) {
			logger.debug("ItemDAOImpl : insertItem 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * Insert into ITEM
			 * (ITEM_CODE,ITEM_NAME,ITEM_GROUP_CODE,ITEM_CLASSIFICATION,UNIT_OF_STOCK,
			 * LOSS_RATE,LEAD_TIME,STANDARD_UNIT_PRICE,DESCRIPTION) values
			 * ('1111','1111','1111','IT-CI',null,null,null,null,null)
			 */
			query.append("Insert into ITEM \r\n" + "( ITEM_CODE, ITEM_NAME, ITEM_GROUP_CODE, "
					+ "ITEM_CLASSIFICATION, UNIT_OF_STOCK, LOSS_RATE, "
					+ "LEAD_TIME, STANDARD_UNIT_PRICE, DESCRIPTION) \r\n"
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, TO.getItemCode());
			pstmt.setString(2, TO.getItemName());
			pstmt.setString(3, TO.getItemGroupCode());
			pstmt.setString(4, TO.getItemClassification());
			pstmt.setString(5, TO.getUnitOfStock());
			pstmt.setString(6, TO.getLossRate());
			pstmt.setString(7, TO.getLeadTime());
			pstmt.setInt(8, TO.getStandardUnitPrice());
			pstmt.setString(9, TO.getDescription());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void updateItem(ItemTO TO) {

		if (logger.isDebugEnabled()) {
			logger.debug("ItemDAOImpl : updateItem 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * UPDATE ITEM SET ITEM_NAME = ? , ITEM_GROUP_CODE = ? , ITEM_CLASSIFICATION = ?
			 * , UNIT_OF_STOCK = ? , LOSS_RATE = ? , LEAD_TIME = ? , STANDARD_UNIT_PRICE = ?
			 * , DESCRIPTION = ? WHERE ITEM_CODE = ?
			 */
			query.append("UPDATE ITEM SET\r\n" + "ITEM_NAME = ? , ITEM_GROUP_CODE = ? , ITEM_CLASSIFICATION = ? , \r\n"
					+ "UNIT_OF_STOCK = ? , LOSS_RATE = ? , LEAD_TIME = ? , \r\n"
					+ "STANDARD_UNIT_PRICE = ? , DESCRIPTION = ? \r\n" + "WHERE ITEM_CODE = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, TO.getItemName());
			pstmt.setString(2, TO.getItemGroupCode());
			pstmt.setString(3, TO.getItemClassification());
			pstmt.setString(4, TO.getUnitOfStock());
			pstmt.setString(5, TO.getLossRate());
			pstmt.setString(6, TO.getLeadTime());
			pstmt.setInt(7, TO.getStandardUnitPrice());
			pstmt.setString(8, TO.getDescription());
			pstmt.setString(9, TO.getItemCode());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void deleteItem(ItemTO TO) {

		if (logger.isDebugEnabled()) {
			logger.debug("ItemDAOImpl : deleteItem 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * DELETE FROM ITEM WHERE ITEM_CODE = ?
			 */
			query.append("DELETE FROM ITEM WHERE ITEM_CODE = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, TO.getItemCode());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	@Override
	public int getStandardUnitPrice(String itemCode) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("ItemDAOImpl : getStandardUnitPrice 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int price = 0;
		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			
			query.append("select standard_unit_price\r\n" + 
					"from item\r\n" + 
					"where item_code = ?");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, itemCode);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				price = rs.getInt("STANDARD_UNIT_PRICE");
				
			}
			
			return price;
			
		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}
	
	@Override
	public int getStandardUnitPriceBox(String itemCode) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("ItemDAOImpl : getStandardUnitPrice 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int price = 0;
		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			
			query.append("select standard_unit_price\r\n" + 
					"from item\r\n" + 
					"where item_code = ?");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, itemCode);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				price = rs.getInt("STANDARD_UNIT_PRICE");
				
			}
			
			return price;
			
		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

}