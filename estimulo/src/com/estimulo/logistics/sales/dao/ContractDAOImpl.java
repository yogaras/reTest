package com.estimulo.logistics.sales.dao;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.sales.to.ContractInfoTO;
import com.estimulo.logistics.sales.to.ContractTO;
import com.estimulo.logistics.sales.to.EstimateTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ContractDAOImpl implements ContractDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(ContractDAOImpl.class);

	// 싱글톤
	private static ContractDAO instance = new ContractDAOImpl();

	private ContractDAOImpl() {
	}

	public static ContractDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ ContractDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	public ArrayList<EstimateTO> selectEstimateListInContractAvailable(String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDAOImpl : selectEstimateListInContractAvailable 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<EstimateTO> EstimateListInContractAvailable = new ArrayList<EstimateTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();

			/*EFFECTIVE_DATE = 유효일자, ESTIMATE_DATE = 견적일자
			 * SELECT * FROM ESTIMATE WHERE CONTRACT_STATUS IS NULL AND
			 * TO_DATE(EFFECTIVE_DATE,'YYYY-MM-DD') >= SYSDATE AND TO_DATE(ESTIMATE_DATE,
			 * 'YYYY-MM-DD') BETWEEN TO_DATE('20180701','YYYY-MM-DD') AND
			 * TO_DATE('20180731','YYYY-MM-DD')
			 */

			query.append("SELECT * FROM ESTIMATE \r\n" + "WHERE CONTRACT_STATUS IS NULL \r\n"
		               + "AND TO_DATE(EFFECTIVE_DATE,'YYYY-MM-DD') >= TO_DATE(SYSDATE,'YYYY-MM-DD') \r\n"
		               + "AND TO_DATE(ESTIMATE_DATE, 'YYYY-MM-DD') \r\n"
		               + "BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);

			rs = pstmt.executeQuery();

			EstimateTO bean = null;

			while (rs.next()) {

				bean = new EstimateTO();

				bean.setContractStatus(rs.getString("CONTRACT_STATUS"));
				bean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
				bean.setDescription(rs.getString("DESCRIPTION"));
				bean.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				bean.setEstimateDate(rs.getString("ESTIMATE_DATE"));
				bean.setEstimateNo(rs.getString("ESTIMATE_NO"));
				bean.setEstimateRequester(rs.getString("ESTIMATE_REQUESTER"));
				bean.setPersonCodeInCharge(rs.getString("PERSON_CODE_IN_CHARGE"));

				EstimateListInContractAvailable.add(bean);
			}

			return EstimateListInContractAvailable;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public ArrayList<ContractInfoTO> selectContractListByDate(String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDAOImpl : selectContractListByDate 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<ContractInfoTO> contractInfoTOList = new ArrayList<ContractInfoTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			  WITH CODE_DETAIL_LIST AS ( SELECT DETAIL_CODE, DETAIL_CODE_NAME FROM
			  CODE_DETAIL ) ,
			  
			  CONTRACT_LIST AS ( SELECT * FROM CONTRACT WHERE CONTRACT_DATE BETWEEN
			  TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD') ),
			  
			  ESTIMATE_LIST AS ( SELECT * FROM ESTIMATE )
			  
			  SELECT T1.CONTRACT_NO, T1.ESTIMATE_NO, T1.CONTRACT_TYPE, T2.DETAIL_CODE_NAME
			  AS CONTRACT_TYPE_NAME, T1.CUSTOMER_CODE, T3.DETAIL_CODE_NAME AS
			  CUSTOMER_NAME, E.ESTIMATE_DATE, T1.CONTRACT_DATE, T1.CONTRACT_REQUESTER,
			  T1.PERSON_CODE_IN_CHARGE, T4.DETAIL_CODE_NAME AS EMP_NAME_IN_CHARGE,
			  T1.DESCRIPTION FROM CONTRACT_LIST T1 , CODE_DETAIL_LIST T2 , CODE_DETAIL_LIST
			  T3, CODE_DETAIL_LIST T4, ESTIMATE_LIST E WHERE T1.CONTRACT_TYPE =
			  T2.DETAIL_CODE AND T1.CUSTOMER_CODE = T3.DETAIL_CODE AND
			  T1.PERSON_CODE_IN_CHARGE = T4.DETAIL_CODE AND T1.ESTIMATE_NO = E.ESTIMATE_NO
			 */
			query.append(" WITH \r\n" + 
					"              CODE_DETAIL_LIST AS ( SELECT DETAIL_CODE, DETAIL_CODE_NAME FROM\r\n" + 
					"			  CODE_DETAIL ) ,\r\n" + 
					"			  \r\n" + 
					"			  CONTRACT_LIST AS ( SELECT * FROM CONTRACT WHERE CONTRACT_DATE BETWEEN\r\n" + 
					"			  TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD') ),\r\n" + 
					"			  \r\n" + 
					"			  ESTIMATE_LIST AS ( SELECT * FROM ESTIMATE )\r\n" + 
					"			  \r\n" + 
					"			  SELECT T1.CONTRACT_NO, T1.ESTIMATE_NO, T1.CONTRACT_TYPE, T2.DETAIL_CODE_NAME\r\n" + 
					"			  AS CONTRACT_TYPE_NAME, T1.CUSTOMER_CODE, T3.DETAIL_CODE_NAME AS\r\n" + 
					"			  CUSTOMER_NAME, E.ESTIMATE_DATE, T1.CONTRACT_DATE, T1.CONTRACT_REQUESTER,\r\n" + 
					"			  T1.PERSON_CODE_IN_CHARGE, T4.DETAIL_CODE_NAME AS EMP_NAME_IN_CHARGE,\r\n" + 
					"			  T1.DESCRIPTION, T1.DELIVERY_COMPLETION_STATUS FROM CONTRACT_LIST T1 , CODE_DETAIL_LIST T2 , CODE_DETAIL_LIST\r\n" + 
					"			  T3, CODE_DETAIL_LIST T4, ESTIMATE_LIST E WHERE T1.CONTRACT_TYPE =\r\n" + 
					"			  T2.DETAIL_CODE AND T1.CUSTOMER_CODE = T3.DETAIL_CODE AND\r\n" + 
					"			  T1.PERSON_CODE_IN_CHARGE = T4.DETAIL_CODE AND T1.ESTIMATE_NO = E.ESTIMATE_NO");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);

			rs = pstmt.executeQuery();

			ContractInfoTO bean = null;

			while (rs.next()) {

				bean = new ContractInfoTO();

				bean.setContractNo(rs.getString("CONTRACT_NO"));
				bean.setEstimateNo(rs.getString("ESTIMATE_NO"));
				bean.setContractType(rs.getString("CONTRACT_TYPE"));
				bean.setContractTypeName(rs.getString("CONTRACT_TYPE_NAME"));
				bean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
				bean.setCustomerName(rs.getString("CUSTOMER_NAME"));
				bean.setEstimateDate(rs.getString("ESTIMATE_DATE"));
				bean.setContractDate(rs.getString("CONTRACT_DATE"));
				bean.setContractRequester(rs.getString("CONTRACT_REQUESTER"));
				bean.setPersonCodeInCharge(rs.getString("PERSON_CODE_IN_CHARGE"));
				bean.setEmpNameInCharge(rs.getString("EMP_NAME_IN_CHARGE"));
				bean.setDescription(rs.getString("DESCRIPTION"));
				bean.setDeliveryCompletionStatus(rs.getString("DELIVERY_COMPLETION_STATUS"));
				
				contractInfoTOList.add(bean);

			}
			System.out.println("contractDAOImpl selectContractByDate End");
			return contractInfoTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public ArrayList<ContractInfoTO> selectContractListByCustomer(String customerCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDAOImpl : selectContractListByCustomer 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<ContractInfoTO> contractInfoTOList = new ArrayList<ContractInfoTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * WITH CODE_DETAIL_LIST AS ( SELECT DETAIL_CODE, DETAIL_CODE_NAME FROM
			 * CODE_DETAIL ) ,
			 * 
			 * CONTRACT_LIST AS ( SELECT * FROM CONTRACT WHERE CUSTOMER_CODE = ? ) ,
			 * 
			 * ESTIMATE_LIST AS ( SELECT * FROM ESTIMATE )
			 * 
			 * SELECT T1.CONTRACT_NO, T1.ESTIMATE_NO, T1.CONTRACT_TYPE, T2.DETAIL_CODE_NAME
			 * AS CONTRACT_TYPE_NAME, T1.CUSTOMER_CODE, T3.DETAIL_CODE_NAME AS
			 * CUSTOMER_NAME, E.ESTIMATE_DATE, T1.CONTRACT_DATE, T1.CONTRACT_REQUESTER,
			 * T1.PERSON_CODE_IN_CHARGE, T4.DETAIL_CODE_NAME AS EMP_NAME_IN_CHARGE,
			 * T1.DESCRIPTION FROM CONTRACT_LIST T1 , CODE_DETAIL_LIST T2 , CODE_DETAIL_LIST
			 * T3, CODE_DETAIL_LIST T4, ESTIMATE_LIST E WHERE T1.CONTRACT_TYPE =
			 * T2.DETAIL_CODE AND T1.CUSTOMER_CODE = T3.DETAIL_CODE AND
			 * T1.PERSON_CODE_IN_CHARGE = T4.DETAIL_CODE AND T1.ESTIMATE_NO = E.ESTIMATE_NO
			 * 
			 */
			query.append("WITH CODE_DETAIL_LIST AS\r\n"
					+ "( SELECT DETAIL_CODE, DETAIL_CODE_NAME FROM CODE_DETAIL ) ,\r\n" + "\r\n"
					+ "CONTRACT_LIST AS \r\n" + "( SELECT * FROM CONTRACT WHERE CUSTOMER_CODE = ? ) ,\r\n" + "\r\n"
					+ "ESTIMATE_LIST AS\r\n" + "( SELECT * FROM ESTIMATE )\r\n" + "\r\n"
					+ "SELECT T1.CONTRACT_NO, T1.ESTIMATE_NO, \r\n"
					+ "	T1.CONTRACT_TYPE, T2.DETAIL_CODE_NAME AS CONTRACT_TYPE_NAME,\r\n"
					+ "    T1.CUSTOMER_CODE, T3.DETAIL_CODE_NAME AS CUSTOMER_NAME,\r\n"
					+ "    E.ESTIMATE_DATE, T1.CONTRACT_DATE, T1.CONTRACT_REQUESTER, \r\n"
					+ "    T1.PERSON_CODE_IN_CHARGE, T4.DETAIL_CODE_NAME AS EMP_NAME_IN_CHARGE,\r\n"
					+ "    T1.DESCRIPTION\r\n" + "FROM CONTRACT_LIST T1 , \r\n"
					+ "	CODE_DETAIL_LIST T2 , CODE_DETAIL_LIST T3, CODE_DETAIL_LIST T4, \r\n" + "	ESTIMATE_LIST E\r\n"
					+ "WHERE T1.CONTRACT_TYPE = T2.DETAIL_CODE\r\n" + "	AND T1.CUSTOMER_CODE = T3.DETAIL_CODE\r\n"
					+ "	AND T1.PERSON_CODE_IN_CHARGE = T4.DETAIL_CODE\r\n" + "	AND T1.ESTIMATE_NO = E.ESTIMATE_NO");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, customerCode);

			rs = pstmt.executeQuery();

			ContractInfoTO bean = null;

			while (rs.next()) {

				bean = new ContractInfoTO();

				bean.setContractNo(rs.getString("CONTRACT_NO"));
				bean.setEstimateNo(rs.getString("ESTIMATE_NO"));
				bean.setContractType(rs.getString("CONTRACT_TYPE"));
				bean.setContractTypeName(rs.getString("CONTRACT_TYPE_NAME"));
				bean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
				bean.setCustomerName(rs.getString("CUSTOMER_NAME"));
				bean.setEstimateDate(rs.getString("ESTIMATE_DATE"));
				bean.setContractDate(rs.getString("CONTRACT_DATE"));
				bean.setContractRequester(rs.getString("CONTRACT_REQUESTER"));
				bean.setPersonCodeInCharge(rs.getString("PERSON_CODE_IN_CHARGE"));
				bean.setEmpNameInCharge(rs.getString("EMP_NAME_IN_CHARGE"));
				bean.setDescription(rs.getString("DESCRIPTION"));

				contractInfoTOList.add(bean);
				
			}

			return contractInfoTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public ArrayList<ContractInfoTO> selectDeliverableContractListByDate(String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDAOImpl : selectDeliverableContractListByDate 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<ContractInfoTO> contractInfoTOList = new ArrayList<ContractInfoTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			  
			  WITH CODE_DETAIL_LIST AS ( SELECT DETAIL_CODE, DETAIL_CODE_NAME FROM
			  CODE_DETAIL ) ,
			  
			  CONTRACT_LIST AS ( SELECT * FROM CONTRACT WHERE CONTRACT_DATE BETWEEN
			  TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD') ),
			  
			  ESTIMATE_LIST AS ( SELECT * FROM ESTIMATE )
			  
			  SELECT 
              T1.CONTRACT_NO, 
              T1.ESTIMATE_NO, 
              T1.CONTRACT_TYPE, 
              T2.DETAIL_CODE_NAME AS CONTRACT_TYPE_NAME, 
              T1.CUSTOMER_CODE,
              T3.DETAIL_CODE_NAME AS CUSTOMER_NAME, 
              E.ESTIMATE_DATE, 
              T1.CONTRACT_DATE, 
              T1.CONTRACT_REQUESTER,
			  T1.PERSON_CODE_IN_CHARGE, 
              T4.DETAIL_CODE_NAME AS EMP_NAME_IN_CHARGE,
			  T1.DESCRIPTION,
              T1.DELIVERY_COMPLETION_STATUS
              FROM CONTRACT_LIST T1 , CODE_DETAIL_LIST T2 , CODE_DETAIL_LIST
			  T3, CODE_DETAIL_LIST T4, ESTIMATE_LIST E 
              WHERE T1.CONTRACT_TYPE = T2.DETAIL_CODE 
              AND T1.CUSTOMER_CODE = T3.DETAIL_CODE 
              AND T1.PERSON_CODE_IN_CHARGE = T4.DETAIL_CODE 
              AND T1.ESTIMATE_NO = E.ESTIMATE_NO
              AND T1.DELIVERY_COMPLETION_STATUS IS NULL
			 
			 */
			query.append("\r\n" + 
					"WITH CODE_DETAIL_LIST AS ( SELECT DETAIL_CODE, DETAIL_CODE_NAME FROM\r\n" + 
					"CODE_DETAIL ) ,\r\n" + 
					"\r\n" + 
					"CONTRACT_LIST AS ( SELECT * FROM CONTRACT WHERE CONTRACT_DATE BETWEEN\r\n" + 
					"TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD') ),\r\n" + 
					"\r\n" + 
					"ESTIMATE_LIST AS ( SELECT * FROM ESTIMATE )\r\n" + 
					"\r\n" + 
					"SELECT \r\n" + 
					"T1.CONTRACT_NO, \r\n" + 
					"T1.ESTIMATE_NO, \r\n" + 
					"T1.CONTRACT_TYPE, \r\n" + 
					"T2.DETAIL_CODE_NAME AS CONTRACT_TYPE_NAME, \r\n" + 
					"T1.CUSTOMER_CODE,\r\n" + 
					"T3.DETAIL_CODE_NAME AS CUSTOMER_NAME, \r\n" + 
					"E.ESTIMATE_DATE, \r\n" + 
					"T1.CONTRACT_DATE, \r\n" + 
					"T1.CONTRACT_REQUESTER,\r\n" + 
					"T1.PERSON_CODE_IN_CHARGE, \r\n" + 
					"T4.DETAIL_CODE_NAME AS EMP_NAME_IN_CHARGE,\r\n" + 
					"T1.DESCRIPTION,\r\n" + 
					"T1.DELIVERY_COMPLETION_STATUS\r\n" + 
					"FROM CONTRACT_LIST T1 , CODE_DETAIL_LIST T2 , CODE_DETAIL_LIST\r\n" + 
					"T3, CODE_DETAIL_LIST T4, ESTIMATE_LIST E \r\n" + 
					"WHERE T1.CONTRACT_TYPE = T2.DETAIL_CODE \r\n" + 
					"AND T1.CUSTOMER_CODE = T3.DETAIL_CODE \r\n" + 
					"AND T1.PERSON_CODE_IN_CHARGE = T4.DETAIL_CODE \r\n" + 
					"AND T1.ESTIMATE_NO = E.ESTIMATE_NO\r\n" + 
					"AND T1.DELIVERY_COMPLETION_STATUS IS NULL");
			
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);

			rs = pstmt.executeQuery();

			ContractInfoTO bean = null;

			while (rs.next()) {

				bean = new ContractInfoTO();

				bean.setContractNo(rs.getString("CONTRACT_NO"));
				bean.setEstimateNo(rs.getString("ESTIMATE_NO"));
				bean.setContractType(rs.getString("CONTRACT_TYPE"));
				bean.setContractTypeName(rs.getString("CONTRACT_TYPE_NAME"));
				bean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
				bean.setCustomerName(rs.getString("CUSTOMER_NAME"));
				bean.setEstimateDate(rs.getString("ESTIMATE_DATE"));
				bean.setContractDate(rs.getString("CONTRACT_DATE"));
				bean.setContractRequester(rs.getString("CONTRACT_REQUESTER"));
				bean.setPersonCodeInCharge(rs.getString("PERSON_CODE_IN_CHARGE"));
				bean.setEmpNameInCharge(rs.getString("EMP_NAME_IN_CHARGE"));
				bean.setDescription(rs.getString("DESCRIPTION"));
				bean.setDeliveryCompletionStatus(rs.getString("DELIVERY_COMPLETION_STATUS"));
				
				contractInfoTOList.add(bean);

			}
			return contractInfoTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public ArrayList<ContractInfoTO> selectDeliverableContractListByCustomer(String customerCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDAOImpl : selectDeliverableContractListByCustomer 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<ContractInfoTO> contractInfoTOList = new ArrayList<ContractInfoTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();  
			

/*
WITH CODE_DETAIL_LIST AS 
( SELECT DETAIL_CODE, DETAIL_CODE_NAME FROM CODE_DETAIL ) ,
			  
CONTRACT_LIST AS 
( SELECT * FROM CONTRACT WHERE CUSTOMER_CODE = 'PTN-01' ) ,
			  
ESTIMATE_LIST AS 
( SELECT * FROM ESTIMATE )
			  
SELECT 
T1.CONTRACT_NO, 
T1.ESTIMATE_NO, 
T1.CONTRACT_TYPE, 
T2.DETAIL_CODE_NAME AS CONTRACT_TYPE_NAME,
T1.CUSTOMER_CODE, 
T3.DETAIL_CODE_NAME AS CUSTOMER_NAME,
E.ESTIMATE_DATE,
T1.CONTRACT_DATE,
T1.CONTRACT_REQUESTER,
T1.PERSON_CODE_IN_CHARGE,
T4.DETAIL_CODE_NAME AS EMP_NAME_IN_CHARGE,
T1.DESCRIPTION,
T1.DELIVERY_COMPLETION_STATUS
FROM 
CONTRACT_LIST T1 , 
CODE_DETAIL_LIST T2 ,
CODE_DETAIL_LIST T3, 
CODE_DETAIL_LIST T4, 
ESTIMATE_LIST E 
WHERE 
T1.CONTRACT_TYPE = T2.DETAIL_CODE 
AND T1.CUSTOMER_CODE = T3.DETAIL_CODE 
AND T1.PERSON_CODE_IN_CHARGE = T4.DETAIL_CODE 
AND T1.ESTIMATE_NO = E.ESTIMATE_NO
AND T1.DELIVERY_COMPLETION_STATUS IS NULL*/
                
			
			query.append("\r\n" + 
					"\r\n" + 
					"WITH CODE_DETAIL_LIST AS \r\n" + 
					"( SELECT DETAIL_CODE, DETAIL_CODE_NAME FROM CODE_DETAIL ) ,\r\n" + 
					"			  \r\n" + 
					"CONTRACT_LIST AS \r\n" + 
					"( SELECT * FROM CONTRACT WHERE CUSTOMER_CODE = ? ) ,\r\n" + 
					"			  \r\n" + 
					"ESTIMATE_LIST AS \r\n" + 
					"( SELECT * FROM ESTIMATE )\r\n" + 
					"			  \r\n" + 
					"SELECT \r\n" + 
					"T1.CONTRACT_NO, \r\n" + 
					"T1.ESTIMATE_NO, \r\n" + 
					"T1.CONTRACT_TYPE, \r\n" + 
					"T2.DETAIL_CODE_NAME AS CONTRACT_TYPE_NAME,\r\n" + 
					"T1.CUSTOMER_CODE, \r\n" + 
					"T3.DETAIL_CODE_NAME AS CUSTOMER_NAME,\r\n" + 
					"E.ESTIMATE_DATE,\r\n" + 
					"T1.CONTRACT_DATE,\r\n" + 
					"T1.CONTRACT_REQUESTER,\r\n" + 
					"T1.PERSON_CODE_IN_CHARGE,\r\n" + 
					"T4.DETAIL_CODE_NAME AS EMP_NAME_IN_CHARGE,\r\n" + 
					"T1.DESCRIPTION,\r\n" + 
					"T1.DELIVERY_COMPLETION_STATUS\r\n" + 
					"FROM \r\n" + 
					"CONTRACT_LIST T1 , \r\n" + 
					"CODE_DETAIL_LIST T2 ,\r\n" + 
					"CODE_DETAIL_LIST T3, \r\n" + 
					"CODE_DETAIL_LIST T4, \r\n" + 
					"ESTIMATE_LIST E \r\n" + 
					"WHERE \r\n" + 
					"T1.CONTRACT_TYPE = T2.DETAIL_CODE \r\n" + 
					"AND T1.CUSTOMER_CODE = T3.DETAIL_CODE \r\n" + 
					"AND T1.PERSON_CODE_IN_CHARGE = T4.DETAIL_CODE \r\n" + 
					"AND T1.ESTIMATE_NO = E.ESTIMATE_NO\r\n" + 
					"AND T1.DELIVERY_COMPLETION_STATUS IS NULL\r\n" + 
					"                ");
			
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, customerCode);

			rs = pstmt.executeQuery();

			ContractInfoTO bean = null;

			while (rs.next()) {

				bean = new ContractInfoTO();

				bean.setContractNo(rs.getString("CONTRACT_NO"));
				bean.setEstimateNo(rs.getString("ESTIMATE_NO"));
				bean.setContractType(rs.getString("CONTRACT_TYPE"));
				bean.setContractTypeName(rs.getString("CONTRACT_TYPE_NAME"));
				bean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
				bean.setCustomerName(rs.getString("CUSTOMER_NAME"));
				bean.setEstimateDate(rs.getString("ESTIMATE_DATE"));
				bean.setContractDate(rs.getString("CONTRACT_DATE"));
				bean.setContractRequester(rs.getString("CONTRACT_REQUESTER"));
				bean.setPersonCodeInCharge(rs.getString("PERSON_CODE_IN_CHARGE"));
				bean.setEmpNameInCharge(rs.getString("EMP_NAME_IN_CHARGE"));
				bean.setDescription(rs.getString("DESCRIPTION"));
				bean.setDeliveryCompletionStatus(rs.getString("DELIVERY_COMPLETION_STATUS"));

				contractInfoTOList.add(bean);
				
			}

			return contractInfoTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}
	
	@Override
	public int selectContractCount(String contractDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDAOImpl : selectContractCount 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * SELECT COUNT(*) FROM CONTRACT WHERE CONTRACT_DATE =
			 * TO_DATE('2018-07-10','YYYY-MM-DD')
			 */

			query.append("SELECT COUNT(*) FROM CONTRACT WHERE CONTRACT_DATE = TO_DATE(?,'YYYY-MM-DD')");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, contractDate);

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

	@Override
	public void insertContract(ContractTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDAOImpl : insertContract 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * Insert into CONTRACT (CONTRACT_NO, ESTIMATE_NO, CONTRACT_TYPE, CUSTOMER_CODE,
			 * CONTRACT_DATE, CONTRACT_REQUESTER, PERSON_CODE_IN_CHARGE, DESCRIPTION) values
			 * ('CO2018070301','ES2018070301','견적','PTN-01','2018-07-03','김종한','EMP-02',
			 * null)
			 */
			query.append("Insert into CONTRACT (CONTRACT_NO, ESTIMATE_NO, \r\n"
					+ "CONTRACT_TYPE, CUSTOMER_CODE, CONTRACT_DATE, \r\n"
					+ "CONTRACT_REQUESTER, PERSON_CODE_IN_CHARGE, DESCRIPTION) \r\n"
					+ "values (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getContractNo());
			pstmt.setString(2, bean.getEstimateNo());
			pstmt.setString(3, bean.getContractType());
			pstmt.setString(4, bean.getCustomerCode());
			pstmt.setString(5, bean.getContractDate());
			pstmt.setString(6, bean.getContractRequester());
			pstmt.setString(7, bean.getPersonCodeInCharge());
			pstmt.setString(8, bean.getDescription());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	@Override
	public void updateContract(ContractTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDAOImpl : updateContract 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * UPDATE CONTRACT SET ESTIMATE_NO = ? , CONTRACT_TYPE = ? , CUSTOMER_CODE = ? ,
			 * CONTRACT_DATE = ? , CONTRACT_REQUESTER = ? , PERSON_CODE_IN_CHARGE = ? ,
			 * DESCRIPTION = ? WHERE CONTRACT_NO = ?
			 */
			query.append("UPDATE CONTRACT SET \r\n" + "ESTIMATE_NO = ? , CONTRACT_TYPE = ? , CUSTOMER_CODE = ? ,\r\n"
					+ "CONTRACT_DATE = ? , CONTRACT_REQUESTER = ? , \r\n"
					+ "PERSON_CODE_IN_CHARGE = ? , DESCRIPTION = ? \r\n" + "WHERE CONTRACT_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getEstimateNo());
			pstmt.setString(2, bean.getContractType());
			pstmt.setString(3, bean.getCustomerCode());
			pstmt.setString(4, bean.getContractDate());
			pstmt.setString(5, bean.getContractRequester());
			pstmt.setString(6, bean.getPersonCodeInCharge());
			pstmt.setString(7, bean.getDescription());
			pstmt.setString(8, bean.getContractNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	@Override
	public void deleteContract(ContractTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDAOImpl : deleteContract 시작");
		}

	}

	

}
