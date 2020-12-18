package com.estimulo.logistics.sales.dao;

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
import com.estimulo.logistics.sales.to.DeliveryInfoTO;

import oracle.jdbc.internal.OracleTypes;

public class DeliveryDAOImpl implements DeliveryDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(DeliveryDAOImpl.class);

	// 싱글톤
	private static DeliveryDAO instance = new DeliveryDAOImpl();

	private DeliveryDAOImpl() {
	}

	public static DeliveryDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ DeliveryResultDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

	@Override
	public ArrayList<DeliveryInfoTO> selectDeliveryInfoList() {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryResultDAOImpl : selectDeliveryInfoList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<DeliveryInfoTO> deliveryInfoList = new ArrayList<DeliveryInfoTO>();

		try {

			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			query.append("select * from\r\n" + 
					"DELIVERY_INFO\r\n" + 
					"order by delivery_date desc");

			pstmt = conn.prepareStatement(query.toString());

			rs = pstmt.executeQuery();

			DeliveryInfoTO bean = null;

			while (rs.next()) {

				bean = new DeliveryInfoTO();

				bean.setDeliveryNo(rs.getString("DELIVERY_NO"));
				bean.setEstimateNo(rs.getString("ESTIMATE_NO"));
				bean.setContractNo(rs.getString("CONTRACT_NO"));
				bean.setContractDetailNo(rs.getString("CONTRACT_DETAIL_NO"));
				bean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
				bean.setPersonCodeInCharge(rs.getString("PERSON_CODE_IN_CHARGE"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setUnitOfDelivery(rs.getString("UNIT_OF_DELIVERY"));
				bean.setDeliveryAmount(rs.getString("DELIVERY_AMOUNT"));
				bean.setUnitPrice(rs.getString("UNIT_PRICE"));
				bean.setSumPrice(rs.getString("SUM_PRICE"));
				bean.setDeliverydate(rs.getString("DELIVERY_DATE"));
				bean.setDeliveryPlaceName(rs.getString("DELIVERY_PLACE_NAME"));
				
				deliveryInfoList.add(bean);

			}

			return deliveryInfoList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	
	
	public void insertDeliveryResult(DeliveryInfoTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryResultDAOImpl : insertDeliveryResult 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			
			query.append("insert into delivery_info values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getDeliveryNo());
			pstmt.setString(2, bean.getEstimateNo());
			pstmt.setString(3, bean.getContractNo());
			pstmt.setString(4, bean.getContractDetailNo());
			pstmt.setString(5, bean.getCustomerCode());
			pstmt.setString(6, bean.getPersonCodeInCharge());
			pstmt.setString(7, bean.getItemCode());
			pstmt.setString(8, bean.getItemName());
			pstmt.setString(9, bean.getUnitOfDelivery());
			pstmt.setString(10, bean.getDeliveryAmount());
			pstmt.setString(11, bean.getUnitPrice());
			pstmt.setString(12, bean.getSumPrice());
			pstmt.setString(13, bean.getDeliverydate());
			pstmt.setString(14, bean.getDeliveryPlaceName());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	@Override
	public HashMap<String,Object> deliver(String contractDetailNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryDAOImpl : deliver 시작");
		}

		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		HashMap<String,Object> resultMap = new HashMap<>();
		
		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			System.out.println("      @ 프로시저 호출시도");
			query.append(" {call P_DELIVER(?,?,?)} ");

			cs = conn.prepareCall(query.toString());
			cs.setString(1, contractDetailNo);
			cs.registerOutParameter(2, OracleTypes.NUMBER);
			cs.registerOutParameter(3, OracleTypes.VARCHAR);
			cs.executeUpdate();
			System.out.println("      @ 프로시저 호출완료");
			
            int errorCode = cs.getInt(2);
            String errorMsg = cs.getString(3);

        	resultMap.put("errorCode",errorCode);
        	resultMap.put("errorMsg",errorMsg);
            
			return resultMap;
			
		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(cs, rs);

		}

	}
	
	public void updateDeliveryResult(DeliveryInfoTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryResultDAOImpl : updateDeliveryResult 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			
			
			query.append("update delivery_info set "
					+ "DELIVERY_NO = ?,"
					+ "ESTIMATE_NO = ?,"
					+ "CONTRACT_NO = ?,"
					+ "CONTRACT_DETAIL_NO = ?,"
					+ "CUSTOMER_CODE = ?,"
					+ "PERSON_CODE_IN_CHARGE = ?,"
					+ "ITEM_CODE = ?,"
					+ "ITEM_NAME = ?,"
					+ "UNIT_OF_DELIVERY = ?,"
					+ "DELIVERY_AMOUNT = ?,"
					+ "UNIT_PRICE = ?,"
					+ "SUM_PRICE = ?,"
					+ "DELIVERY_DATE = ?,"
					+ "DELIVERY_PLACE_NAME = ?"
					+ "WHERE "
					+ "DELIVERY_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getDeliveryNo());
			pstmt.setString(2, bean.getEstimateNo());
			pstmt.setString(3, bean.getContractNo());
			pstmt.setString(4, bean.getContractDetailNo());
			pstmt.setString(5, bean.getCustomerCode());
			pstmt.setString(6, bean.getPersonCodeInCharge());
			pstmt.setString(7, bean.getItemCode());
			pstmt.setString(8, bean.getItemName());
			pstmt.setString(9, bean.getUnitOfDelivery());
			pstmt.setString(10, bean.getDeliveryAmount());
			pstmt.setString(11, bean.getUnitPrice());
			pstmt.setString(12, bean.getSumPrice());
			pstmt.setString(13, bean.getDeliverydate());
			pstmt.setString(14, bean.getDeliveryPlaceName());
			pstmt.setString(15, bean.getDeliveryNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	public void deleteDeliveryResult(DeliveryInfoTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("DeliveryResultDAOImpl : deleteDeliveryResult 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			query.append("DELETE FROM DELIVERY_INFO WHERE DELIVERY_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getDeliveryNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

}
