package com.estimulo.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.hr.to.EmployeeDetailTO;

public class EmployeeDetailDAOImpl implements EmployeeDetailDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(EmployeeDetailDAOImpl.class);

	// 싱글톤
	private static EmployeeDetailDAO instance = new EmployeeDetailDAOImpl();

	private EmployeeDetailDAOImpl() {
	}

	public static EmployeeDetailDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ EmployeeDetailDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	public ArrayList<EmployeeDetailTO> selectEmployeeDetailList(String companyCode, String empCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmployeeDetailDAOImpl : selectEmployeeDetailList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<EmployeeDetailTO> employeeDetailList = new ArrayList<EmployeeDetailTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			/*
			 * SELECT * FROM EMPLOYEE_DETAIL WHERE COMPANY_CODE = ? AND EMP_CODE =?
			 */

			query.append("SELECT * FROM EMPLOYEE_DETAIL WHERE COMPANY_CODE = ?  AND EMP_CODE =?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, companyCode);
			pstmt.setString(2, empCode);

			rs = pstmt.executeQuery();

			EmployeeDetailTO TO = null;

			while (rs.next()) {
				TO = new EmployeeDetailTO();

				TO.setCompanyCode(rs.getString("COMPANY_CODE"));
				TO.setEmpCode(rs.getString("EMP_CODE"));
				TO.setSeq(rs.getInt("SEQ"));
				TO.setUpdateHistory(rs.getString("UPDATE_HISTORY"));
				TO.setUpdateDate(rs.getString("UPDATE_DATE"));
				TO.setWorkplaceCode(rs.getString("WORKPLACE_CODE"));
				TO.setDeptCode(rs.getString("DEPT_CODE"));
				TO.setPositionCode(rs.getString("POSITION_CODE"));
				TO.setUserId(rs.getString("USER_ID"));
				TO.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				TO.setEmail(rs.getString("EMAIL"));
				TO.setZipCode(rs.getString("ZIP_CODE"));
				TO.setBasicAddress(rs.getString("BASIC_ADDRESS"));
				TO.setDetailAddress(rs.getString("DETAIL_ADDRESS"));
				TO.setLevelOfEducation(rs.getString("LEVEL_OF_EDUCATION"));
				TO.setImage(rs.getString("IMAGE"));

				employeeDetailList.add(TO);
			}

			return employeeDetailList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public ArrayList<EmployeeDetailTO> selectUserIdList(String companyCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmployeeDetailDAOImpl : selectUserIdList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<EmployeeDetailTO> employeeDetailList = new ArrayList<EmployeeDetailTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			/*
			 * 
			 * WITH DETAIL_INFO AS ( ( SELECT EMP_CODE, USER_ID, SEQ FROM EMPLOYEE_DETAIL
			 * WHERE COMPANY_CODE = ? ) ),
			 * 
			 * MAX_SEQ AS ( SELECT EMP_CODE, MAX(SEQ) AS SEQ FROM DETAIL_INFO GROUP BY
			 * EMP_CODE ),
			 * 
			 * ALL_USER_ID AS ( SELECT EMP_CODE, USER_ID FROM DETAIL_INFO WHERE ( EMP_CODE,
			 * SEQ ) IN ( SELECT EMP_CODE, SEQ FROM MAX_SEQ ) )
			 * 
			 * SELECT EMP_CODE, USER_ID FROM ALL_USER_ID WHERE USER_ID IS NOT NULL ORDER BY
			 * EMP_CODE
			 * 
			 */

			query.append("WITH DETAIL_INFO AS\r\n"
					+ "( ( SELECT EMP_CODE, USER_ID, SEQ FROM EMPLOYEE_DETAIL WHERE COMPANY_CODE = ? ) ),\r\n" + "\r\n"
					+ "MAX_SEQ AS\r\n" + "( SELECT EMP_CODE, MAX(SEQ) AS SEQ \r\n" + "FROM DETAIL_INFO\r\n"
					+ "GROUP BY EMP_CODE ),\r\n" + "\r\n" + "ALL_USER_ID AS\r\n" + "( SELECT EMP_CODE, USER_ID \r\n"
					+ "FROM DETAIL_INFO\r\n" + "WHERE ( EMP_CODE, SEQ ) IN ( SELECT EMP_CODE, SEQ FROM MAX_SEQ ) )\r\n"
					+ "\r\n" + "SELECT EMP_CODE, USER_ID FROM ALL_USER_ID\r\n" + "WHERE USER_ID IS NOT NULL\r\n"
					+ "ORDER BY EMP_CODE");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, companyCode);

			rs = pstmt.executeQuery();

			EmployeeDetailTO TO = null;

			while (rs.next()) {
				TO = new EmployeeDetailTO();

				TO.setEmpCode(rs.getString("EMP_CODE"));
				TO.setUserId(rs.getString("USER_ID"));

				employeeDetailList.add(TO);
			}

			return employeeDetailList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	public void insertEmployeeDetail(EmployeeDetailTO TO) {

		if (logger.isDebugEnabled()) {
			logger.debug("EmployeeDetailDAOImpl : insertEmployeeDetail 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			/*
			 * Insert into EMPLOYEE_DETAIL (COMPANY_CODE , EMP_CODE , SEQ , UPDATE_HISTORY ,
			 * UPDATE_DATE , WORKPLACE_CODE , DEPT_CODE , POSITION_CODE , USER_ID ,
			 * PHONE_NUMBER , EMAIL , ZIP_CODE , BASIC_ADDRESS , DETAIL_ADDRESS ,
			 * LEVEL_OF_EDUCATION,IMAGE) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?
			 * , ? , ? , ? , ? , ? )
			 */

			query.append("Insert into EMPLOYEE_DETAIL \r\n" + "(COMPANY_CODE , EMP_CODE , SEQ , \r\n"
					+ "UPDATE_HISTORY , UPDATE_DATE , WORKPLACE_CODE , \r\n"
					+ "DEPT_CODE , POSITION_CODE , USER_ID , \r\n" + "PHONE_NUMBER , EMAIL , ZIP_CODE , \r\n"
					+ "BASIC_ADDRESS , DETAIL_ADDRESS , \r\n" + "LEVEL_OF_EDUCATION,IMAGE) \r\n"
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, TO.getCompanyCode());
			pstmt.setString(2, TO.getEmpCode());
			pstmt.setInt(3, TO.getSeq());
			pstmt.setString(4, TO.getUpdateHistory());
			pstmt.setString(5, TO.getUpdateDate());
			pstmt.setString(6, TO.getWorkplaceCode());
			pstmt.setString(7, TO.getDeptCode());
			pstmt.setString(8, TO.getPositionCode());
			pstmt.setString(9, TO.getUserId());
			pstmt.setString(10, TO.getPhoneNumber());
			pstmt.setString(11, TO.getEmail());
			pstmt.setString(12, TO.getZipCode());
			pstmt.setString(13, TO.getBasicAddress());
			pstmt.setString(14, TO.getDetailAddress());
			pstmt.setString(15, TO.getLevelOfEducation());
			pstmt.setString(16, TO.getImage());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

}
