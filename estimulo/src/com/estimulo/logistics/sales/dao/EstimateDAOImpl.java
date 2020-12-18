package com.estimulo.logistics.sales.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.sales.to.EstimateTO;

public class EstimateDAOImpl implements EstimateDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(EstimateDAOImpl.class);

	// 싱글톤
	private static EstimateDAO instance = new EstimateDAOImpl();

	private EstimateDAOImpl() {
	}

	public static EstimateDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ EstimateDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	public ArrayList<EstimateTO> selectEstimateList(String dateSearchCondition, String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateDAOImpl : selectEstimateList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<EstimateTO> estimateTOList = new ArrayList<EstimateTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * SELECT * FROM ESTIMATE WHERE ( CASE ? WHEN 'estimateDate' THEN
			 * TO_DATE(ESTIMATE_DATE, 'YYYY-MM-DD') WHEN 'effectiveDate' THEN
			 * TO_DATE(EFFECTIVE_DATE, 'YYYY-MM-DD') END ) BETWEEN TO_DATE(?,'YYYY-MM-DD')
			 * AND TO_DATE(?,'YYYY-MM-DD')
			 */
			query.append("SELECT * FROM ESTIMATE WHERE ( CASE ? WHEN 'estimateDate' THEN\r\n"
					+ "TO_DATE(ESTIMATE_DATE, 'YYYY-MM-DD') WHEN 'effectiveDate' THEN\r\n"
					+ "TO_DATE(EFFECTIVE_DATE, 'YYYY-MM-DD') END ) \r\n"
					+ "BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, dateSearchCondition);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);

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

				estimateTOList.add(bean);
				
			}

			return estimateTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	@Override
	public EstimateTO selectEstimate(String estimateNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateDAOImpl : EstimateTO 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		EstimateTO bean = new EstimateTO();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM ESTIMATE WHERE ESTIMATE_NO = ?");

			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, estimateNo);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				bean.setContractStatus(rs.getString("CONTRACT_STATUS"));
				bean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
				bean.setDescription(rs.getString("DESCRIPTION"));
				bean.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				bean.setEstimateDate(rs.getString("ESTIMATE_DATE"));
				bean.setEstimateNo(rs.getString("ESTIMATE_NO"));
				bean.setEstimateRequester(rs.getString("ESTIMATE_REQUESTER"));
				bean.setPersonCodeInCharge(rs.getString("PERSON_CODE_IN_CHARGE"));

			}

			return bean;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public int selectEstimateCount(String estimateDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateDAOImpl : selectEstimateCount 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * SELECT COUNT(*) FROM ESTIMATE WHERE ESTIMATE_DATE =
			 * TO_DATE('2018-07-10','YYYY-MM-DD')
			 */
			query.append("SELECT COUNT(*) FROM ESTIMATE WHERE ESTIMATE_DATE =" + "TO_DATE(?,'YYYY-MM-DD')"); //0
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, estimateDate);

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

	public void insertEstimate(EstimateTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateDAOImpl : insertEstimate 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * Insert into ESTIMATE (ESTIMATE_NO,CUSTOMER_CODE, ESTIMATE_DATE,
			 * CONTRACT_STATUS, ESTIMATE_REQUESTER, EFFECTIVE_DATE, PERSON_CODE_IN_CHARGE,
			 * DESCRIPTION) values ('ES2018070101' , 'PTN-01' , '2018-07-01' , 'Y' , '김종한' ,
			 * '2018-08-01' , 'EMP-02' , null)
			 */
			//ESTIMATE테이블에 뷰단 견적과 견적상세 그리고 생성한 견적 일련번호를 인서트
			query.append("Insert into ESTIMATE (ESTIMATE_NO,CUSTOMER_CODE, ESTIMATE_DATE,\r\n"
					+ "CONTRACT_STATUS, ESTIMATE_REQUESTER, EFFECTIVE_DATE, \r\n"
					+ "PERSON_CODE_IN_CHARGE, DESCRIPTION) values \r\n" + "(? , ? , ? , ? , ? , ? , ? , ?)");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getEstimateNo());
			pstmt.setString(2, bean.getCustomerCode());
			pstmt.setString(3, bean.getEstimateDate());
			pstmt.setString(4, bean.getContractStatus());
			pstmt.setString(5, bean.getEstimateRequester());
			pstmt.setString(6, bean.getEffectiveDate());
			pstmt.setString(7, bean.getPersonCodeInCharge());
			pstmt.setString(8, bean.getDescription());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void updateEstimate(EstimateTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateDAOImpl : updateEstimate 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * UPDATE ESTIMATE SET CUSTOMER_CODE = ? , ESTIMATE_DATE = ? , CONTRACT_STATUS =
			 * ? , ESTIMATE_REQUESTER = ? , EFFECTIVE_DATE = ? , PERSON_CODE_IN_CHARGE = ? ,
			 * DESCRIPTION = ? WHERE ESTIMATE_NO = ?
			 */
			query.append("UPDATE ESTIMATE SET CUSTOMER_CODE = ? , ESTIMATE_DATE = ? , \r\n"
					+ "CONTRACT_STATUS = ? , ESTIMATE_REQUESTER = ? , \r\n"
					+ "EFFECTIVE_DATE = ? , PERSON_CODE_IN_CHARGE = ? ,\r\n" + "DESCRIPTION = ? WHERE ESTIMATE_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getCustomerCode());
			pstmt.setString(2, bean.getEstimateDate());
			pstmt.setString(3, bean.getContractStatus());
			pstmt.setString(4, bean.getEstimateRequester());
			pstmt.setString(5, bean.getEffectiveDate());
			pstmt.setString(6, bean.getPersonCodeInCharge());
			pstmt.setString(7, bean.getDescription());
			pstmt.setString(8, bean.getEstimateNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public void changeContractStatusOfEstimate(String estimateNo, String contractStatus) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateDAOImpl : changeContractStatusOfEstimate 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			/*
			 * UPDATE ESTIMATE SET CONTRACT_STATUS = ? WHERE ESTIMATE_NO = ?
			 */
			query.append("UPDATE ESTIMATE SET CONTRACT_STATUS = ? WHERE ESTIMATE_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, contractStatus);
			pstmt.setString(2, estimateNo);

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

}