package com.estimulo.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.to.CodeTO;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;

public class CodeDAOImpl implements CodeDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(CodeDAOImpl.class);

	// 싱글톤
	private static CodeDAO instance = new CodeDAOImpl();

	private CodeDAOImpl() {
	}

	public static CodeDAO getInstance() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("@ CodeDAOImpl 객체접근");
		}
		
		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	@Override
	public ArrayList<CodeTO> selectCodeList() {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeDAOImpl : selectCodeList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<CodeTO> codeList = new ArrayList<CodeTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM CODE ORDER BY DIVISION_CODE_NO");

			pstmt = conn.prepareStatement(query.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {

				CodeTO bean = new CodeTO();

				bean.setDivisionCodeNo(rs.getString("DIVISION_CODE_NO"));
				bean.setCodeType(rs.getString("CODE_TYPE"));
				bean.setDivisionCodeName(rs.getString("DIVISION_CODE_NAME"));
				bean.setCodeChangeAvailable(rs.getString("CODE_CHANGE_AVAILABLE"));
				bean.setDescription(rs.getString("DESCRIPTION"));

				codeList.add(bean);
			}

			return codeList;

		} catch (SQLException e) {

			throw new DataAccessException(e.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

	@Override
	public void insertCode(CodeTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeDAOImpl : insertCode 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
Insert into CODE ( DIVISION_CODE_NO , CODE_TYPE , DIVISION_CODE_NAME ,
CODE_CHANGE_AVAILABLE , DESCRIPTION ) values ( ? , ? , ? , ? , ? )
*/
			query.append("Insert into CODE \r\n"
					+ "( DIVISION_CODE_NO , CODE_TYPE , DIVISION_CODE_NAME , CODE_CHANGE_AVAILABLE , DESCRIPTION ) \r\n"
					+ "values ( ? , ? , ? , ? , ? )");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getDivisionCodeNo());
			pstmt.setString(2, bean.getCodeType());
			pstmt.setString(3, bean.getDivisionCodeName());
			pstmt.setString(4, bean.getCodeChangeAvailable());
			pstmt.setString(5, bean.getDescription());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	@Override
	public void updateCode(CodeTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeDAOImpl : updateCode 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
UPDATE CODE SET CODE_TYPE = ? , DIVISION_CODE_NAME = ? ,
CODE_CHANGE_AVAILABLE = ? , DESCRIPTION = ? WHERE DIVISION_CODE_NO = ?
*/
			query.append("UPDATE CODE SET \r\n"
					+ "CODE_TYPE = ? , DIVISION_CODE_NAME = ? , CODE_CHANGE_AVAILABLE = ? , DESCRIPTION = ? \r\n"
					+ "WHERE DIVISION_CODE_NO = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getCodeType());
			pstmt.setString(2, bean.getDivisionCodeName());
			pstmt.setString(3, bean.getCodeChangeAvailable());
			pstmt.setString(4, bean.getDescription());
			pstmt.setString(5, bean.getDivisionCodeNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	@Override
	public void deleteCode(CodeTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeDAOImpl : deleteCode 시작");
		}
	
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
DELETE FROM CODE WHERE DIVISION_CODE_NO = ?
*/
			query.append("DELETE FROM CODE WHERE DIVISION_CODE_NO = ?");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getDivisionCodeNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

}
