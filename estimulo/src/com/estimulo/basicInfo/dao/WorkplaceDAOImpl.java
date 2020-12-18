package com.estimulo.basicInfo.dao;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.basicInfo.to.WorkplaceTO;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;

public class WorkplaceDAOImpl implements WorkplaceDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(WorkplaceDAOImpl.class);

	// 싱글톤
	private static WorkplaceDAO instance = new WorkplaceDAOImpl();

	private WorkplaceDAOImpl() {
	}

	public static WorkplaceDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ WorkplaceDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	public ArrayList<WorkplaceTO> selectWorkplaceList(String companyCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceDAOImpl : selectWorkplaceList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<WorkplaceTO> workplaceTOList = new ArrayList<WorkplaceTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM WORKPLACE WHERE COMPANY_CODE = ?");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, companyCode);
			rs = pstmt.executeQuery();

			WorkplaceTO bean = null;
			
			while (rs.next()) {

				bean = new WorkplaceTO();

				bean.setWorkplaceCeoName(rs.getString("WORKPLACE_CEO_NAME"));
				bean.setIsMainOffice(rs.getString("IS_MAIN_OFFICE"));
				bean.setWorkplaceDetailAddress(rs.getString("WORKPLACE_DETAIL_ADDRESS"));
				bean.setWorkplaceBusinessConditions(rs.getString("WORKPLACE_BUSINESS_CONDITIONS"));
				bean.setWorkplaceBusinessItems(rs.getString("WORKPLACE_BUSINESS_ITEMS"));
				bean.setWorkplaceFaxNumber(rs.getString("WORKPLACE_FAX_NUMBER"));
				bean.setWorkplaceEstablishDate(rs.getString("WORKPLACE_ESTABLISH_DATE"));
				bean.setBusinessLicenseNumber(rs.getString("BUSINESS_LICENSE_NUMBER"));
				bean.setWorkplaceTelNumber(rs.getString("WORKPLACE_TEL_NUMBER"));
				bean.setWorkplaceName(rs.getString("WORKPLACE_NAME"));
				bean.setWorkplaceBasicAddress(rs.getString("WORKPLACE_BASIC_ADDRESS"));
				bean.setWorkplaceCloseDate(rs.getString("WORKPLACE_CLOSE_DATE"));
				bean.setWorkplaceCode(rs.getString("WORKPLACE_CODE"));
				bean.setCompanyCode(rs.getString("COMPANY_CODE"));
				bean.setWorkplaceOpenDate(rs.getString("WORKPLACE_OPEN_DATE"));
				bean.setCorporationLicenseNumber(rs.getString("CORPORATION_LICENSE_NUMBER"));
				bean.setWorkplaceZipCode(rs.getString("WORKPLACE_ZIP_CODE"));

				workplaceTOList.add(bean);
				
			}

			return workplaceTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void insertWorkplace(WorkplaceTO bean)  {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceDAOImpl : insertWorkplace 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * 
			 * Insert into WORKPLACE ( WORKPLACE_CODE , COMPANY_CODE , WORKPLACE_NAME ,
			 * BUSINESS_LICENSE_NUMBER , CORPORATION_LICENSE_NUMBER , WORKPLACE_CEO_NAME ,
			 * WORKPLACE_BUSINESS_CONDITIONS , WORKPLACE_BUSINESS_ITEMS , WORKPLACE_ZIP_CODE
			 * , WORKPLACE_BASIC_ADDRESS , WORKPLACE_DETAIL_ADDRESS , WORKPLACE_TEL_NUMBER ,
			 * WORKPLACE_FAX_NUMBER , WORKPLACE_ESTABLISH_DATE , WORKPLACE_OPEN_DATE ,
			 * WORKPLACE_CLOSE_DATE , IS_MAIN_OFFICE) values ( ? , ? , ? , ? , ? , ? , ? , ?
			 * , ? , ? , ? , ? , ? , ? , ? , ? , ? )
			 * 
			 */

			query.append("Insert into WORKPLACE \r\n"
					+ "( WORKPLACE_CODE , COMPANY_CODE , WORKPLACE_NAME , BUSINESS_LICENSE_NUMBER , \r\n"
					+ "CORPORATION_LICENSE_NUMBER , WORKPLACE_CEO_NAME , WORKPLACE_BUSINESS_CONDITIONS , \r\n"
					+ "WORKPLACE_BUSINESS_ITEMS , WORKPLACE_ZIP_CODE , WORKPLACE_BASIC_ADDRESS , \r\n"
					+ "WORKPLACE_DETAIL_ADDRESS , WORKPLACE_TEL_NUMBER , WORKPLACE_FAX_NUMBER , \r\n"
					+ "WORKPLACE_ESTABLISH_DATE , WORKPLACE_OPEN_DATE , WORKPLACE_CLOSE_DATE , IS_MAIN_OFFICE) \r\n"
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getWorkplaceCode());
			pstmt.setString(2, bean.getCompanyCode());
			pstmt.setString(3, bean.getWorkplaceName());
			pstmt.setString(4, bean.getBusinessLicenseNumber());
			pstmt.setString(5, bean.getCorporationLicenseNumber());
			pstmt.setString(6, bean.getWorkplaceCeoName());
			pstmt.setString(7, bean.getWorkplaceBusinessConditions());
			pstmt.setString(8, bean.getWorkplaceBusinessItems());
			pstmt.setString(9, bean.getWorkplaceZipCode());
			pstmt.setString(10, bean.getWorkplaceBasicAddress());
			pstmt.setString(11, bean.getWorkplaceDetailAddress());
			pstmt.setString(12, bean.getWorkplaceTelNumber());
			pstmt.setString(13, bean.getWorkplaceFaxNumber());
			pstmt.setString(14, bean.getWorkplaceEstablishDate());
			pstmt.setString(15, bean.getWorkplaceOpenDate());
			pstmt.setString(16, bean.getWorkplaceCloseDate());
			pstmt.setString(17, bean.getIsMainOffice());

			rs = pstmt.executeQuery();
			
		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void updateWorkplace(WorkplaceTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceDAOImpl : updateWorkplace 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * 
			 * UPDATE WORKPLACE SET COMPANY_CODE = ? , WORKPLACE_NAME = ? ,
			 * BUSINESS_LICENSE_NUMBER = ? , CORPORATION_LICENSE_NUMBER = ? ,
			 * WORKPLACE_CEO_NAME = ? , WORKPLACE_BUSINESS_CONDITIONS = ? ,
			 * WORKPLACE_BUSINESS_ITEMS = ? , WORKPLACE_ZIP_CODE = ? ,
			 * WORKPLACE_BASIC_ADDRESS = ? , WORKPLACE_DETAIL_ADDRESS = ? ,
			 * WORKPLACE_TEL_NUMBER = ? , WORKPLACE_FAX_NUMBER = ? ,
			 * WORKPLACE_ESTABLISH_DATE = ? , WORKPLACE_OPEN_DATE = ? , WORKPLACE_CLOSE_DATE
			 * = ? , IS_MAIN_OFFICE = ? WHERE WORKPLACE_CODE = ?
			 * 
			 */

			query.append("UPDATE WORKPLACE SET \r\n"
					+ "COMPANY_CODE = ? , WORKPLACE_NAME = ? , BUSINESS_LICENSE_NUMBER = ? ,  \r\n"
					+ "CORPORATION_LICENSE_NUMBER = ? , WORKPLACE_CEO_NAME = ? , WORKPLACE_BUSINESS_CONDITIONS = ? ,  \r\n"
					+ "WORKPLACE_BUSINESS_ITEMS = ? , WORKPLACE_ZIP_CODE = ? , WORKPLACE_BASIC_ADDRESS = ? , \r\n"
					+ "WORKPLACE_DETAIL_ADDRESS = ? , WORKPLACE_TEL_NUMBER = ? , WORKPLACE_FAX_NUMBER = ? , \r\n"
					+ "WORKPLACE_ESTABLISH_DATE = ? , WORKPLACE_OPEN_DATE = ? , WORKPLACE_CLOSE_DATE = ? , IS_MAIN_OFFICE = ?\r\n"
					+ "WHERE WORKPLACE_CODE = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getCompanyCode());
			pstmt.setString(2, bean.getWorkplaceName());
			pstmt.setString(3, bean.getBusinessLicenseNumber());
			pstmt.setString(4, bean.getCorporationLicenseNumber());
			pstmt.setString(5, bean.getWorkplaceCeoName());
			pstmt.setString(6, bean.getWorkplaceBusinessConditions());
			pstmt.setString(7, bean.getWorkplaceBusinessItems());
			pstmt.setString(8, bean.getWorkplaceZipCode());
			pstmt.setString(9, bean.getWorkplaceBasicAddress());
			pstmt.setString(10, bean.getWorkplaceDetailAddress());
			pstmt.setString(11, bean.getWorkplaceTelNumber());
			pstmt.setString(12, bean.getWorkplaceFaxNumber());
			pstmt.setString(13, bean.getWorkplaceEstablishDate());
			pstmt.setString(14, bean.getWorkplaceOpenDate());
			pstmt.setString(15, bean.getWorkplaceCloseDate());
			pstmt.setString(16, bean.getIsMainOffice());
			pstmt.setString(17, bean.getWorkplaceCode());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void deleteWorkplace(WorkplaceTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("WorkplaceDAOImpl : deleteWorkplace 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * 
			 * DELETE FROM WORKPLACE WHERE WORKPLACE_CODE = ?
			 * 
			 */

			query.append("DELETE FROM WORKPLACE WHERE WORKPLACE_CODE = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getWorkplaceCode());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

}