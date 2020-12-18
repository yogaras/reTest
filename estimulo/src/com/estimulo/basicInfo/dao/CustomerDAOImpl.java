package com.estimulo.basicInfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.basicInfo.to.CustomerTO;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;

public class CustomerDAOImpl implements CustomerDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(CustomerDAOImpl.class);

	// 싱글톤
	private static CustomerDAO instance = new CustomerDAOImpl();

	private CustomerDAOImpl() {
	}

	public static CustomerDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ CustomerDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	public ArrayList<CustomerTO> selectCustomerListByCompany() {

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerDAOImpl : selectCustomerListByCompany 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<CustomerTO> customerList = new ArrayList<CustomerTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			query.append("SELECT * FROM CUSTOMER");
			pstmt = conn.prepareStatement(query.toString());
			rs = pstmt.executeQuery();

			CustomerTO bean = null;
			
			while (rs.next()) {

				bean = new CustomerTO();

				bean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
				bean.setWorkplaceCode(rs.getString("WORKPLACE_CODE"));
				bean.setCustomerName(rs.getString("CUSTOMER_NAME"));
				bean.setCustomerType(rs.getString("CUSTOMER_TYPE"));
				bean.setCustomerCeo(rs.getString("CUSTOMER_CEO"));
				bean.setBusinessLicenseNumber(rs.getString("BUSINESS_LICENSE_NUMBER"));
				bean.setSocialSecurityNumber(rs.getString("SOCIAL_SECURITY_NUMBER"));
				bean.setCustomerBusinessConditions(rs.getString("CUSTOMER_BUSINESS_CONDITIONS"));
				bean.setCustomerBusinessItems(rs.getString("CUSTOMER_BUSINESS_ITEMS"));
				bean.setCustomerZipCode(rs.getString("CUSTOMER_ZIP_CODE"));
				bean.setCustomerBasicAddress(rs.getString("CUSTOMER_BASIC_ADDRESS"));
				bean.setCustomerDetailAddress(rs.getString("CUSTOMER_DETAIL_ADDRESS"));
				bean.setCustomerTelNumber(rs.getString("CUSTOMER_TEL_NUMBER"));
				bean.setCustomerFaxNumber(rs.getString("CUSTOMER_FAX_NUMBER"));
				bean.setCustomerNote(rs.getString("CUSTOMER_NOTE"));

				customerList.add(bean);
			}

			return customerList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public ArrayList<CustomerTO> selectCustomerListByWorkplace(String workplaceCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerDAOImpl : selectCustomerListByWorkplace 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<CustomerTO> customerList = new ArrayList<CustomerTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			query.append("SELECT * FROM CUSTOMER WHERE WORKPLACE_CODE = ?");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, workplaceCode);
			rs = pstmt.executeQuery();

			CustomerTO bean = null;
			
			while (rs.next()) {

				bean = new CustomerTO();

				bean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
				bean.setWorkplaceCode(rs.getString("WORKPLACE_CODE"));
				bean.setCustomerName(rs.getString("CUSTOMER_NAME"));
				bean.setCustomerType(rs.getString("CUSTOMER_TYPE"));
				bean.setCustomerCeo(rs.getString("CUSTOMER_CEO"));
				bean.setBusinessLicenseNumber(rs.getString("BUSINESS_LICENSE_NUMBER"));
				bean.setSocialSecurityNumber(rs.getString("SOCIAL_SECURITY_NUMBER"));
				bean.setCustomerBusinessConditions(rs.getString("CUSTOMER_BUSINESS_CONDITIONS"));
				bean.setCustomerBusinessItems(rs.getString("CUSTOMER_BUSINESS_ITEMS"));
				bean.setCustomerZipCode(rs.getString("CUSTOMER_ZIP_CODE"));
				bean.setCustomerBasicAddress(rs.getString("CUSTOMER_BASIC_ADDRESS"));
				bean.setCustomerDetailAddress(rs.getString("CUSTOMER_DETAIL_ADDRESS"));
				bean.setCustomerTelNumber(rs.getString("CUSTOMER_TEL_NUMBER"));
				bean.setCustomerFaxNumber(rs.getString("CUSTOMER_FAX_NUMBER"));
				bean.setCustomerNote(rs.getString("CUSTOMER_NOTE"));

				customerList.add(bean);
			}

			return customerList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void insertCustomer(CustomerTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerDAOImpl : insertCustomer 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * 
			 * Insert into CUSTOMER ( CUSTOMER_CODE , WORKPLACE_CODE , CUSTOMER_NAME ,
			 * CUSTOMER_TYPE , CUSTOMER_CEO , BUSINESS_LICENSE_NUMBER ,
			 * SOCIAL_SECURITY_NUMBER , CUSTOMER_BUSINESS_CONDITIONS ,
			 * CUSTOMER_BUSINESS_ITEMS , CUSTOMER_ZIP_CODE , CUSTOMER_BASIC_ADDRESS ,
			 * CUSTOMER_DETAIL_ADDRESS , CUSTOMER_TEL_NUMBER , CUSTOMER_FAX_NUMBER ,
			 * CUSTOMER_NOTE ) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,
			 * ? , ? )
			 * 
			 */

			query.append("Insert into CUSTOMER \r\n"
					+ "( CUSTOMER_CODE , WORKPLACE_CODE , CUSTOMER_NAME , CUSTOMER_TYPE , CUSTOMER_CEO , \r\n"
					+ "BUSINESS_LICENSE_NUMBER , SOCIAL_SECURITY_NUMBER , CUSTOMER_BUSINESS_CONDITIONS , \r\n"
					+ "CUSTOMER_BUSINESS_ITEMS , CUSTOMER_ZIP_CODE , CUSTOMER_BASIC_ADDRESS , CUSTOMER_DETAIL_ADDRESS , \r\n"
					+ "CUSTOMER_TEL_NUMBER , CUSTOMER_FAX_NUMBER , CUSTOMER_NOTE ) \r\n"
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getCustomerCode());
			pstmt.setString(2, bean.getWorkplaceCode());
			pstmt.setString(3, bean.getCustomerName());
			pstmt.setString(4, bean.getCustomerType());
			pstmt.setString(5, bean.getCustomerCeo());
			pstmt.setString(6, bean.getBusinessLicenseNumber());
			pstmt.setString(7, bean.getSocialSecurityNumber());
			pstmt.setString(8, bean.getCustomerBusinessConditions());
			pstmt.setString(9, bean.getCustomerBusinessItems());
			pstmt.setString(10, bean.getCustomerZipCode());
			pstmt.setString(11, bean.getCustomerBasicAddress());
			pstmt.setString(12, bean.getCustomerDetailAddress());
			pstmt.setString(13, bean.getCustomerTelNumber());
			pstmt.setString(14, bean.getCustomerFaxNumber());
			pstmt.setString(15, bean.getCustomerNote());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void updateCustomer(CustomerTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerDAOImpl : updateCustomer 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * 
			 * UPDATE CUSTOMER SET WORKPLACE_CODE = ?, CUSTOMER_NAME = ?, CUSTOMER_TYPE = ?,
			 * CUSTOMER_CEO = ?, BUSINESS_LICENSE_NUMBER = ?, SOCIAL_SECURITY_NUMBER = ?,
			 * CUSTOMER_BUSINESS_CONDITIONS = ?, CUSTOMER_BUSINESS_ITEMS = ?,
			 * CUSTOMER_ZIP_CODE = ?, CUSTOMER_BASIC_ADDRESS = ?, CUSTOMER_DETAIL_ADDRESS =
			 * ?, CUSTOMER_TEL_NUMBER = ?, CUSTOMER_FAX_NUMBER = ?, CUSTOMER_NOTE = ? WHERE
			 * CUSTOMER_CODE = ?
			 * 
			 */

			query.append("UPDATE CUSTOMER SET WORKPLACE_CODE = ?, CUSTOMER_NAME = ?, CUSTOMER_TYPE = ?,\r\n"
					+ "	CUSTOMER_CEO = ?, BUSINESS_LICENSE_NUMBER = ?, SOCIAL_SECURITY_NUMBER = ?,\r\n"
					+ "	CUSTOMER_BUSINESS_CONDITIONS = ?, CUSTOMER_BUSINESS_ITEMS = ?,\r\n"
					+ "	CUSTOMER_ZIP_CODE = ?, CUSTOMER_BASIC_ADDRESS = ?, CUSTOMER_DETAIL_ADDRESS = ?, \r\n"
					+ "	CUSTOMER_TEL_NUMBER = ?, CUSTOMER_FAX_NUMBER = ?, CUSTOMER_NOTE = ? \r\n"
					+ "WHERE CUSTOMER_CODE = ?\r\n");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getWorkplaceCode());
			pstmt.setString(2, bean.getCustomerName());
			pstmt.setString(3, bean.getCustomerType());
			pstmt.setString(4, bean.getCustomerCeo());
			pstmt.setString(5, bean.getBusinessLicenseNumber());
			pstmt.setString(6, bean.getSocialSecurityNumber());
			pstmt.setString(7, bean.getCustomerBusinessConditions());
			pstmt.setString(8, bean.getCustomerBusinessItems());
			pstmt.setString(9, bean.getCustomerZipCode());
			pstmt.setString(10, bean.getCustomerBasicAddress());
			pstmt.setString(11, bean.getCustomerDetailAddress());
			pstmt.setString(12, bean.getCustomerTelNumber());
			pstmt.setString(13, bean.getCustomerFaxNumber());
			pstmt.setString(14, bean.getCustomerNote());
			pstmt.setString(15, bean.getCustomerCode());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void deleteCustomer(CustomerTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("CustomerDAOImpl : deleteCustomer 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * 
			 * DELETE FROM CUSTOMER WHERE CUSTOMER_CODE = ?
			 * 
			 */

			query.append("DELETE FROM CUSTOMER WHERE CUSTOMER_CODE = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getCustomerCode());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

}
