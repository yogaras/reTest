package com.estimulo.logistics.sales.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.production.to.ContractDetailInMpsAvailableTO;
import com.estimulo.logistics.sales.to.ContractDetailTO;

import oracle.jdbc.internal.OracleTypes;

public class ContractDetailDAOImpl implements ContractDetailDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(ContractDetailDAOImpl.class);

	// 싱글톤
	private static ContractDetailDAO instance = new ContractDetailDAOImpl();

	private ContractDetailDAOImpl() {
	}

	public static ContractDetailDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ ContractDetailDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	
	@Override
	public HashMap<String,Object> insertContractDetail(String estimateNo, String contractNo) {
		
			if (logger.isDebugEnabled()) {
				logger.debug("ContractDAOImpl : insertContractDetail 시작");
			}
			Connection conn = null;
			CallableStatement cs = null;
			ResultSet rs = null;
            HashMap<String,Object> resultMap = new HashMap<>();
				
			try {
				conn = dataSourceTransactionManager.getConnection();

				StringBuffer query = new StringBuffer();

				System.out.println("      @ 프로시저 호출시도");
				query.append(" {call P_INSERT_NEW_CONTRACT_DETAIL(?,?,?,?)} ");

				cs = conn.prepareCall(query.toString());
				cs.setString(1, estimateNo);
				cs.setString(2, contractNo);
				cs.registerOutParameter(3, OracleTypes.NUMBER);
				cs.registerOutParameter(4, OracleTypes.VARCHAR);
				cs.executeUpdate();
				
				int errorCode = cs.getInt(3);
				String errorMsg = cs.getString(4);
				
				System.out.println("      @ 프로시저 호출완료");

				resultMap.put("errorCode",errorCode); //성공시 0
				resultMap.put("errorMsg",errorMsg); //## MPS등록완료 ##
				
				return resultMap;
				
			} catch (Exception sqle) {

				throw new DataAccessException(sqle.getMessage());

			} finally {

				dataSourceTransactionManager.close(cs, rs);

			}
	}
	
	@Override
	public ArrayList<ContractDetailTO> selectContractDetailList(String contractNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDetailDAOImpl : selectContractDetailList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<ContractDetailTO> contractDetailTOList = new ArrayList<ContractDetailTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM CONTRACT_DETAIL WHERE CONTRACT_NO =?");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, contractNo);

			rs = pstmt.executeQuery();

			ContractDetailTO bean = null;

			while (rs.next()) {

				bean = new ContractDetailTO();

				bean.setContractDetailNo(rs.getString("CONTRACT_DETAIL_NO"));
				bean.setContractNo(rs.getString("CONTRACT_NO"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setUnitOfContract(rs.getString("UNIT_OF_CONTRACT"));
				bean.setDueDateOfContract(rs.getString("DUE_DATE_OF_CONTRACT"));
				bean.setEstimateAmount(rs.getString("ESTIMATE_AMOUNT"));
				bean.setStockAmountUse(rs.getString("STOCK_AMOUNT_USE"));
				bean.setProductionRequirement(rs.getString("PRODUCTION_REQUIREMENT"));
				bean.setUnitPriceOfContract(rs.getString("UNIT_PRICE_OF_CONTRACT"));
				bean.setSumPriceOfContract(rs.getString("SUM_PRICE_OF_CONTRACT"));
				bean.setProcessingStatus(rs.getString("PROCESSING_STATUS"));
				bean.setOperationCompletedStatus(rs.getString("OPERATION_COMPLETED_STATUS"));
				bean.setDeliveryCompletionStatus(rs.getString("DELIVERY_COMPLETION_STATUS"));
				bean.setDescription(rs.getString("DESCRIPTION"));

				contractDetailTOList.add(bean);

			}

			return contractDetailTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	@Override
	public ArrayList<ContractDetailTO> selectDeliverableContractDetailList(String contractNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDetailDAOImpl : selectDeliverableContractDetailList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<ContractDetailTO> contractDetailTOList = new ArrayList<ContractDetailTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM CONTRACT_DETAIL WHERE CONTRACT_NO = ?");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, contractNo);

			rs = pstmt.executeQuery();

			ContractDetailTO bean = null;

			while (rs.next()) {

				bean = new ContractDetailTO();

				bean.setContractDetailNo(rs.getString("CONTRACT_DETAIL_NO"));
				bean.setContractNo(rs.getString("CONTRACT_NO"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setUnitOfContract(rs.getString("UNIT_OF_CONTRACT"));
				bean.setDueDateOfContract(rs.getString("DUE_DATE_OF_CONTRACT"));
				bean.setEstimateAmount(rs.getString("ESTIMATE_AMOUNT"));
				bean.setStockAmountUse(rs.getString("STOCK_AMOUNT_USE"));
				bean.setProductionRequirement(rs.getString("PRODUCTION_REQUIREMENT"));
				bean.setUnitPriceOfContract(rs.getString("UNIT_PRICE_OF_CONTRACT"));
				bean.setSumPriceOfContract(rs.getString("SUM_PRICE_OF_CONTRACT"));
				bean.setProcessingStatus(rs.getString("PROCESSING_STATUS"));
				bean.setOperationCompletedStatus(rs.getString("OPERATION_COMPLETED_STATUS"));
				bean.setDeliveryCompletionStatus(rs.getString("DELIVERY_COMPLETION_STATUS"));
				bean.setDescription(rs.getString("DESCRIPTION"));

				contractDetailTOList.add(bean);

			}

			return contractDetailTOList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
		
	}
	
	@Override
	public int selectContractDetailCount(String contractNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDetailDAOImpl : selectContractDetailCount 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * SELECT * FROM ESTIMATE_DETAIL WHERE ESTIMATE_NO = ?
			 */
			query.append("SELECT * FROM CONTRACT_DETAIL WHERE CONTRACT_NO = ?");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, contractNo);

			rs = pstmt.executeQuery();

			TreeSet<Integer> intSet = new TreeSet<>();

			while (rs.next()) {
				String contractDetailNo = rs.getString("CONTRACT_DETAIL_NO");
				int no = Integer.parseInt(contractDetailNo.split("-")[1]);

				intSet.add(no);
			}

			if (intSet.isEmpty()) {
				return 1;
			} else {
				return intSet.pollLast() + 1;
			}

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	public ArrayList<ContractDetailInMpsAvailableTO> selectContractDetailListInMpsAvailable(String searchCondition,
			String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDetailDAOImpl : selectContractDetailListInMpsAvailable 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList = new ArrayList<ContractDetailInMpsAvailableTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			query.append("select\r\n" + 
					"C.CONTRACT_NO,\r\n" + 
					"C.CONTRACT_TYPE,\r\n" + 
					"C.CONTRACT_DATE,\r\n" + 
					"C.CUSTOMER_CODE,\r\n" + 
					"CD.CONTRACT_DETAIL_NO,\r\n" + 
					"CD.ITEM_CODE,\r\n" + 
					"CD.ITEM_NAME,\r\n" + 
					"CD.UNIT_OF_CONTRACT,\r\n" + 
					"CD.ESTIMATE_AMOUNT,\r\n" + 
					"CD.STOCK_AMOUNT_USE,\r\n" + 
					"CD.PRODUCTION_REQUIREMENT,\r\n" + 
					"CD.DUE_DATE_OF_CONTRACT,\r\n" + 
					"CD.DESCRIPTION\r\n" + 
					"from \r\n" + 
					"contract_detail CD,CONTRACT C \r\n" + 
					"WHERE C.CONTRACT_NO = cd.contract_no\r\n" + 
					"AND PROCESSING_STATUS IS NULL\r\n" + 
					"AND operation_completed_status IS NULL "// IS NULL 을 검사하는 이유는 즉시납품이면 둘다 Y 이고 아니면 MPS 부터 단계를 거쳐가야하기 때문이다.
					);
			
			if(searchCondition.equals("contractDate")) { //수주일자를 선택한 경우
				query.append("AND TO_DATE(C.CONTRACT_DATE,'YYYY-MM-DD') BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
			}
			else if(searchCondition.equals("dueDateOfContract")) { //납기일을 선택한 경우
				query.append("AND TO_DATE(CD.DUE_DATE_OF_CONTRACT,'YYYY-MM-DD') BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
			} 
			
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1,startDate);
			pstmt.setString(2,endDate);
			
			rs = pstmt.executeQuery();

			ContractDetailInMpsAvailableTO bean = null;
			while (rs.next()) {

				bean = new ContractDetailInMpsAvailableTO();
				System.out.println("success");
				bean.setContractNo(rs.getString("CONTRACT_NO"));
				bean.setContractType(rs.getString("CONTRACT_TYPE"));
				bean.setContractDate(rs.getString("CONTRACT_DATE"));
				bean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
				bean.setContractDetailNo(rs.getString("CONTRACT_DETAIL_NO"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				System.out.println("success");
				bean.setItemName(rs.getString("ITEM_NAME"));
				System.out.println("success in");
				bean.setUnitOfContract(rs.getString("UNIT_OF_CONTRACT"));
				System.out.println("success uoc");
				bean.setEstimateAmount(rs.getString("ESTIMATE_AMOUNT"));
				System.out.println("success ea");
				bean.setStockAmountUse(rs.getString("STOCK_AMOUNT_USE"));
				System.out.println("success sau");
				bean.setProductionRequirement(rs.getString("PRODUCTION_REQUIREMENT"));
				System.out.println("success pr");
				bean.setDueDateOfContract(rs.getString("DUE_DATE_OF_CONTRACT"));
				System.out.println("success ddoc");
				bean.setDescription(rs.getString("DESCRIPTION"));
				System.out.println("success d");
				contractDetailInMpsAvailableList.add(bean);

			}

			return contractDetailInMpsAvailableList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}

	/*@Override
	public void insertContractDetail(ContractDetailTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDetailDAOImpl : insertContractDetail 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			
			 * Insert into CONTRACT_DETAIL (CONTRACT_DETAIL_NO, CONTRACT_NO, ITEM_CODE,
			 * ITEM_NAME, UNIT_OF_CONTRACT, DUE_DATE_OF_CONTRACT, CONTRACT_AMOUNT,
			 * UNIT_PRICE_OF_CONTRACT, SUM_PRICE_OF_CONTRACT, MPS_APPLY_STATUS,
			 * DELIVERY_STATUS,DESCRIPTION) values
			 * ('CO2018070301-01','CO2018070301','DK-01','디지털카메라
			 * NO.1','EA','2018-07-13',200,210000,42000000,'Y','Y',null)
			 
			query.append("Insert into CONTRACT_DETAIL (CONTRACT_DETAIL_NO, CONTRACT_NO, ITEM_CODE,\r\n"
					+ "ITEM_NAME, UNIT_OF_CONTRACT, DUE_DATE_OF_CONTRACT, CONTRACT_AMOUNT,\r\n"
					+ "UNIT_PRICE_OF_CONTRACT, SUM_PRICE_OF_CONTRACT, MPS_APPLY_STATUS,\r\n"
					+ "DELIVERY_STATUS,DESCRIPTION) values\r\n" + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getContractDetailNo());
			pstmt.setString(2, bean.getContractNo());
			pstmt.setString(3, bean.getItemCode());
			pstmt.setString(4, bean.getItemName());
			pstmt.setString(5, bean.getUnitOfContract());
			pstmt.setString(6, bean.getDueDateOfContract());
			pstmt.setInt(7, bean.getContractAmount());
			pstmt.setInt(8, bean.getUnitPriceOfContract());
			pstmt.setInt(9, bean.getSumPriceOfContract());
			pstmt.setString(10, bean.getMpsApplyStatus());
			pstmt.setString(11, bean.getDeliveryStatus());
			pstmt.setString(12, bean.getDescription());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}*/

	/*@Override
	public void updateContractDetail(ContractDetailTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDetailDAOImpl : updateContractDetail 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			
			 * UPDATE CONTRACT_DETAIL SET CONTRACT_NO = ?, ITEM_CODE = ?, ITEM_NAME = ?,
			 * UNIT_OF_CONTRACT = ?, DUE_DATE_OF_CONTRACT = ?, CONTRACT_AMOUNT = ?,
			 * UNIT_PRICE_OF_CONTRACT = ?, SUM_PRICE_OF_CONTRACT = ?, MPS_APPLY_STATUS = ?,
			 * DELIVERY_STATUS = ?, DESCRIPTION = ? WHERE CONTRACT_DETAIL_NO = ?
			 
			query.append("UPDATE CONTRACT_DETAIL SET  \r\n" + "CONTRACT_NO = ?, ITEM_CODE = ?, ITEM_NAME = ?, \r\n"
					+ "UNIT_OF_CONTRACT = ?, DUE_DATE_OF_CONTRACT = ?, \r\n"
					+ "CONTRACT_AMOUNT = ?, UNIT_PRICE_OF_CONTRACT = ?, \r\n"
					+ "SUM_PRICE_OF_CONTRACT = ?, MPS_APPLY_STATUS = ?, \r\n"
					+ "DELIVERY_STATUS = ?, DESCRIPTION = ? \r\n" + "WHERE CONTRACT_DETAIL_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getContractNo());
			pstmt.setString(2, bean.getItemCode());
			pstmt.setString(3, bean.getItemName());
			pstmt.setString(4, bean.getUnitOfContract());
			pstmt.setString(5, bean.getDueDateOfContract());
			pstmt.setInt(6, bean.getContractAmount());
			pstmt.setInt(7, bean.getUnitPriceOfContract());
			pstmt.setInt(8, bean.getSumPriceOfContract());
			pstmt.setString(9, bean.getMpsApplyStatus());
			pstmt.setString(10, bean.getDeliveryStatus());
			pstmt.setString(11, bean.getDescription());
			pstmt.setString(12, bean.getContractDetailNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}*/

	public void changeMpsStatusOfContractDetail(String contractDetailNo, String mpsStatus) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDetailDAOImpl : changeMpsStatusOfContractDetail 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * UPDATE CONTRACT_DETAIL SET MPS_APPLY_STATUS = ? WHERE CONTRACT_DETAIL_NO = ?
			 */
			query.append("UPDATE CONTRACT_DETAIL SET PROCESSING_STATUS = ?\r\n" + "WHERE CONTRACT_DETAIL_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, mpsStatus);
			pstmt.setString(2, contractDetailNo);

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	@Override
	public void deleteContractDetail(ContractDetailTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("ContractDetailDAOImpl : deleteContractDetail 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			/*
			 * DELETE FROM CONTRACT_DETAIL WHERE CONTRACT_DETAIL_NO = ?
			 */
			query.append("DELETE FROM CONTRACT_DETAIL WHERE CONTRACT_DETAIL_NO = ?");
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getContractDetailNo());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}

	

}
