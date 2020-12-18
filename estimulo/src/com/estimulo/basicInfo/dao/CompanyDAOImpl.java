package com.estimulo.basicInfo.dao;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.basicInfo.to.CompanyTO;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;

public class CompanyDAOImpl implements CompanyDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(CompanyDAOImpl.class);

	// 싱글톤
	private static CompanyDAO instance = new CompanyDAOImpl();

	private CompanyDAOImpl() {
	}

	public static CompanyDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ CompanyDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	public ArrayList<CompanyTO> selectCompanyList() {

		if (logger.isDebugEnabled()) {
			logger.debug("CompanyDAOImpl : selectCompanyList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<CompanyTO> companyTOList = new ArrayList<CompanyTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM COMPANY ORDER BY COMPANY_CODE");
			pstmt = conn.prepareStatement(query.toString());
			rs = pstmt.executeQuery();

			CompanyTO bean = null;

			while (rs.next()) {

				bean = new CompanyTO();

				bean.setCompanyTelNumber(rs.getString("COMPANY_TEL_NUMBER"));
				bean.setCompanyDivision(rs.getString("COMPANY_DIVISION"));
				bean.setCompanyBasicAddress(rs.getString("COMPANY_BASIC_ADDRESS"));
				bean.setCompanyOpenDate(rs.getString("COMPANY_OPEN_DATE"));
				bean.setCompanyBusinessItems(rs.getString("COMPANY_BUSINESS_ITEMS"));
				bean.setBusinessLicenseNumber(rs.getString("BUSINESS_LICENSE_NUMBER"));
				bean.setCompanyName(rs.getString("COMPANY_NAME"));
				bean.setCompanyDetailAddress(rs.getString("COMPANY_DETAIL_ADDRESS"));
				bean.setCompanyFaxNumber(rs.getString("COMPANY_FAX_NUMBER"));
				bean.setCompanyCeoName(rs.getString("COMPANY_CEO_NAME"));
				bean.setCompanyEstablishmentDate(rs.getString("COMPANY_ESTABLISHMENT_DATE"));
				bean.setCompanyCode(rs.getString("COMPANY_CODE"));
				bean.setHomepage(rs.getString("HOMEPAGE"));
				bean.setCorporationLicenseNumber(rs.getString("CORPORATION_LICENSE_NUMBER"));
				bean.setCompanyBusinessConditions(rs.getString("COMPANY_BUSINESS_CONDITIONS"));
				bean.setCompanyZipCode(rs.getString("COMPANY_ZIP_CODE"));

				companyTOList.add(bean);
			}

			return companyTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void insertCompany(CompanyTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("CompanyDAOImpl : insertCompany 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * Insert into COMPANY (COMPANY_CODE , COMPANY_NAME , COMPANY_DIVISION ,
			 * BUSINESS_LICENSE_NUMBER , CORPORATION_LICENSE_NUMBER , COMPANY_CEO_NAME ,
			 * COMPANY_BUSINESS_CONDITIONS , COMPANY_BUSINESS_ITEMS , COMPANY_ZIP_CODE ,
			 * COMPANY_BASIC_ADDRESS , COMPANY_DETAIL_ADDRESS , COMPANY_TEL_NUMBER ,
			 * COMPANY_FAX_NUMBER , COMPANY_ESTABLISHMENT_DATE , COMPANY_OPEN_DATE ,
			 * HOMEPAGE) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?
			 * , ? )
			 */

			query.append("Insert into COMPANY \r\n"
					+ "(COMPANY_CODE , COMPANY_NAME , COMPANY_DIVISION , BUSINESS_LICENSE_NUMBER , \r\n"
					+ "CORPORATION_LICENSE_NUMBER , COMPANY_CEO_NAME , COMPANY_BUSINESS_CONDITIONS , \r\n"
					+ "COMPANY_BUSINESS_ITEMS , COMPANY_ZIP_CODE , COMPANY_BASIC_ADDRESS , COMPANY_DETAIL_ADDRESS , \r\n"
					+ "COMPANY_TEL_NUMBER , COMPANY_FAX_NUMBER , COMPANY_ESTABLISHMENT_DATE , \r\n"
					+ "COMPANY_OPEN_DATE , HOMEPAGE) \r\n"
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getCompanyCode());
			pstmt.setString(2, bean.getCompanyName());
			pstmt.setString(3, bean.getCompanyDivision());
			pstmt.setString(4, bean.getBusinessLicenseNumber());
			pstmt.setString(5, bean.getCorporationLicenseNumber());
			pstmt.setString(6, bean.getCompanyCeoName());
			pstmt.setString(7, bean.getCompanyBusinessConditions());
			pstmt.setString(8, bean.getCompanyBusinessItems());
			pstmt.setString(9, bean.getCompanyZipCode());
			pstmt.setString(10, bean.getCompanyBasicAddress());
			pstmt.setString(11, bean.getCompanyDetailAddress());
			pstmt.setString(12, bean.getCompanyTelNumber());
			pstmt.setString(13, bean.getCompanyFaxNumber());
			pstmt.setString(14, bean.getCompanyEstablishmentDate());
			pstmt.setString(15, bean.getCompanyOpenDate());
			pstmt.setString(16, bean.getHomepage());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void updateCompany(CompanyTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("CompanyDAOImpl : updateCompany 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * UPDATE COMPANY SET COMPANY_NAME = ? , COMPANY_DIVISION = ? ,
			 * BUSINESS_LICENSE_NUMBER = ? , CORPORATION_LICENSE_NUMBER = ? ,
			 * COMPANY_CEO_NAME = ? , COMPANY_BUSINESS_CONDITIONS = ? ,
			 * COMPANY_BUSINESS_ITEMS = ? , COMPANY_ZIP_CODE = ? , COMPANY_BASIC_ADDRESS = ?
			 * , COMPANY_DETAIL_ADDRESS = ? , COMPANY_TEL_NUMBER = ? , COMPANY_FAX_NUMBER =
			 * ? , COMPANY_ESTABLISHMENT_DATE = ? , COMPANY_OPEN_DATE = ? , HOMEPAGE = ?
			 * WHERE COMPANY_CODE = ?
			 */

			query.append("UPDATE COMPANY SET \r\n"
					+ "COMPANY_NAME = ? , COMPANY_DIVISION = ? , BUSINESS_LICENSE_NUMBER = ? ,  \r\n"
					+ "CORPORATION_LICENSE_NUMBER = ? , COMPANY_CEO_NAME = ? , COMPANY_BUSINESS_CONDITIONS = ? , \r\n"
					+ "COMPANY_BUSINESS_ITEMS = ? , COMPANY_ZIP_CODE = ? , COMPANY_BASIC_ADDRESS = ? , \r\n"
					+ "COMPANY_DETAIL_ADDRESS = ? , COMPANY_TEL_NUMBER = ? , COMPANY_FAX_NUMBER = ? , \r\n"
					+ "COMPANY_ESTABLISHMENT_DATE = ? , COMPANY_OPEN_DATE = ? , HOMEPAGE = ? \r\n"
					+ "WHERE COMPANY_CODE = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getCompanyName());
			pstmt.setString(2, bean.getCompanyDivision());
			pstmt.setString(3, bean.getBusinessLicenseNumber());
			pstmt.setString(4, bean.getCorporationLicenseNumber());
			pstmt.setString(5, bean.getCompanyCeoName());
			pstmt.setString(6, bean.getCompanyBusinessConditions());
			pstmt.setString(7, bean.getCompanyBusinessItems());
			pstmt.setString(8, bean.getCompanyZipCode());
			pstmt.setString(9, bean.getCompanyBasicAddress());
			pstmt.setString(10, bean.getCompanyDetailAddress());
			pstmt.setString(11, bean.getCompanyTelNumber());
			pstmt.setString(12, bean.getCompanyFaxNumber());
			pstmt.setString(13, bean.getCompanyEstablishmentDate());
			pstmt.setString(14, bean.getCompanyOpenDate());
			pstmt.setString(15, bean.getHomepage());
			pstmt.setString(16, bean.getCompanyCode());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void deleteCompany(CompanyTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("CompanyDAOImpl : deleteCompany 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * DELETE FROM COMPANY WHERE COMPANY_CODE = ?
			 */

			query.append("DELETE FROM COMPANY\r\n" + "WHERE COMPANY_CODE = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getCompanyCode());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

}
