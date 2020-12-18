package com.estimulo.basicInfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.basicInfo.to.DepartmentTO;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;

public class DepartmentDAOImpl implements DepartmentDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(DepartmentDAOImpl.class);

	// 싱글톤
	private static DepartmentDAO instance = new DepartmentDAOImpl();

	private DepartmentDAOImpl() {
	}

	public static DepartmentDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ DepartmentDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();
	

	public ArrayList<DepartmentTO> selectDepartmentListByCompany(String companyCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentDAOImpl : selectDepartmentListByCompany 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<DepartmentTO> departmentList = new ArrayList<DepartmentTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM DEPARTMENT WHERE COMPANY_CODE = ?");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, companyCode);
			
			rs = pstmt.executeQuery();

			DepartmentTO bean = null;
			
			while (rs.next()) {
				
				bean = new DepartmentTO();
				
				bean.setWorkplaceCode(rs.getString("WORKPLACE_CODE"));
				bean.setDeptCode(rs.getString("DEPT_CODE"));
				bean.setDeptName(rs.getString("DEPT_NAME"));
				bean.setWorkplaceName(rs.getString("WORKPLACE_NAME"));
				bean.setCompanyCode(companyCode);
				bean.setDeptStartDate(rs.getString("DEPT_START_DATE"));
				bean.setDeptEndDate(rs.getString("DEPT_END_DATE"));
				
				departmentList.add(bean);

			}

			return departmentList;
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
			
		} finally {
			
			dataSourceTransactionManager.close(pstmt, rs);
		
		}
	}
	
	
	public ArrayList<DepartmentTO> selectDepartmentListByWorkplace(String workplaceCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentDAOImpl : selectDepartmentList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<DepartmentTO> departmentList = new ArrayList<DepartmentTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM DEPARTMENT WHERE WORKPLACE_CODE = ?");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, workplaceCode);
			
			rs = pstmt.executeQuery();

			DepartmentTO bean = null;
			
			while (rs.next()) {
				
				bean = new DepartmentTO();
				
				bean.setWorkplaceCode(workplaceCode);
				bean.setDeptCode(rs.getString("DEPT_CODE"));
				bean.setDeptName(rs.getString("DEPT_NAME"));
				bean.setWorkplaceName(rs.getString("WORKPLACE_NAME"));
				bean.setCompanyCode(rs.getString("COMPANY_CODE"));
				bean.setDeptStartDate(rs.getString("DEPT_START_DATE"));
				bean.setDeptEndDate(rs.getString("DEPT_END_DATE"));
				
				departmentList.add(bean);
				
			}

			return departmentList;
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
			
		} finally {
			
			dataSourceTransactionManager.close(pstmt, rs);
		
		}
	}
	
	public void insertDepartment(DepartmentTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentDAOImpl : insertDepartment 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer();
			
/*

Insert into DEPARTMENT 
( WORKPLACE_CODE , DEPT_CODE , DEPT_NAME , 
WORKPLACE_NAME , COMPANY_CODE , COMPANY_NAME , 
DEPT_START_DATE , DEPT_END_DATE ) 
values ( ? , ? , ? , ? , ? , ? , ? )
			
 */

			query.append(
					"Insert into DEPARTMENT \r\n" + 
					"( WORKPLACE_CODE , DEPT_CODE , DEPT_NAME , \r\n" + 
					"WORKPLACE_NAME , COMPANY_CODE , \r\n" + 
					"DEPT_START_DATE , DEPT_END_DATE ) \r\n" + 
					"values ( ? , ? , ? , ? , ? , ? , ? )");
			
			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, bean.getWorkplaceCode() );
			pstmt.setString(2, bean.getDeptCode() );
			pstmt.setString(3, bean.getDeptName() );
			pstmt.setString(4, bean.getWorkplaceName() );
			pstmt.setString(5, bean.getCompanyCode() );
			pstmt.setString(6, bean.getDeptStartDate() );
			pstmt.setString(7, bean.getDeptEndDate() );

			rs = pstmt.executeQuery();			

		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
			
		} finally {
			
			dataSourceTransactionManager.close(pstmt, rs);
		
		}
	}
	
	
	public void updateDepartment(DepartmentTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentDAOImpl : updateDepartment 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer();
			
/*

UPDATE DEPARTMENT SET 
DEPT_NAME = ? ,  WORKPLACE_NAME = ? , COMPANY_CODE = ? , 
DEPT_START_DATE = ? , DEPT_END_DATE = ? 
WHERE WORKPLACE_CODE = ? AND DEPT_CODE = ? 
			
 */

			query.append(
					"UPDATE DEPARTMENT SET \r\n" + 
					"DEPT_NAME = ? ,  WORKPLACE_NAME = ? , COMPANY_CODE = ? , \r\n" + 
					" DEPT_START_DATE = ? , DEPT_END_DATE = ? \r\n" + 
					"WHERE WORKPLACE_CODE = ? AND DEPT_CODE = ? ");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getDeptName() );
			pstmt.setString(2, bean.getWorkplaceName() );
			pstmt.setString(3, bean.getCompanyCode() );
			pstmt.setString(4, bean.getDeptStartDate() );
			pstmt.setString(5, bean.getDeptEndDate() );
			pstmt.setString(6, bean.getWorkplaceCode() );
			pstmt.setString(7, bean.getDeptCode() );
			
			rs = pstmt.executeQuery();			

		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
			
		} finally {
			
			dataSourceTransactionManager.close(pstmt, rs);
		
		}
	}
	

	public void deleteDepartment(DepartmentTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("DepartmentDAOImpl : deleteDepartment 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer();
			
/*

DELETE FROM DEPARTMENT 
WHERE WORKPLACE_CODE = ? AND DEPT_CODE = ? 
			
 */

			query.append(
					"DELETE FROM DEPARTMENT \r\n" + 
					"WHERE WORKPLACE_CODE = ? AND DEPT_CODE = ?");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getWorkplaceCode() );
			pstmt.setString(2, bean.getDeptCode() );
			
			rs = pstmt.executeQuery();			

		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
			
		} finally {
			
			dataSourceTransactionManager.close(pstmt, rs);
		
		}
	}
	
}
