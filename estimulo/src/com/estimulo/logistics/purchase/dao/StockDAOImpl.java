package com.estimulo.logistics.purchase.dao;

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
import com.estimulo.logistics.purchase.to.StockLogTO;
import com.estimulo.logistics.purchase.to.StockTO;

import oracle.jdbc.internal.OracleTypes;

public class StockDAOImpl implements StockDAO {

		// SLF4J logger
		private static Logger logger = LoggerFactory.getLogger(StockDAOImpl.class);	
			
		// 싱글톤
		private static StockDAO instance = new StockDAOImpl();

		private StockDAOImpl() {
		}

		public static StockDAO getInstance() {
				
			if (logger.isDebugEnabled()) {
				logger.debug("@ StockDAOImpl 객체접근");
			}
			
			return instance;
		}

		// 참조변수 선언
		private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

		@Override
		public ArrayList<StockTO> selectStockList() {
			if (logger.isDebugEnabled()) {
				logger.debug("StockDAOImpl : selectStockList 시작");
			}

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			ArrayList<StockTO> stockList = new ArrayList<StockTO>();

			try {
				conn = dataSourceTransactionManager.getConnection();

				StringBuffer query = new StringBuffer();

				query.append("SELECT * FROM STOCK order by item_code");
				pstmt = conn.prepareStatement(query.toString());
				rs = pstmt.executeQuery();

				StockTO bean = null;
				
				while (rs.next()) {

					bean = new StockTO();
					bean.setWarehouseCode(rs.getString("WAREHOUSE_CODE"));
					bean.setItemCode(rs.getString("ITEM_CODE"));
					bean.setItemName(rs.getString("ITEM_NAME"));
					bean.setUnitOfStock(rs.getString("UNIT_OF_STOCK"));
					bean.setSafetyAllowanceAmount(rs.getString("SAFETY_ALLOWANCE_AMOUNT"));
					bean.setStockAmount(rs.getString("STOCK_AMOUNT"));
					bean.setOrderAmount(rs.getString("ORDER_AMOUNT"));
					bean.setInputAmount(rs.getString("INPUT_AMOUNT"));
					bean.setDeliveryAmount(rs.getString("DELIVERY_AMOUNT"));
					
					stockList.add(bean);
				}

				return stockList;

			} catch (Exception sqle) {

				throw new DataAccessException(sqle.getMessage());

			} finally {

				dataSourceTransactionManager.close(pstmt, rs);

			}
		}

		@Override
		public ArrayList<StockLogTO> selectStockLogList(String startDate, String endDate) {
			if (logger.isDebugEnabled()) {
				logger.debug("StockDAOImpl : selectStockLogList 시작");
			}

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			ArrayList<StockLogTO> StockLogList = new ArrayList<StockLogTO>();

			try {
				conn = dataSourceTransactionManager.getConnection();

				StringBuffer query = new StringBuffer();
				System.out.println(startDate);
				System.out.println(endDate);
				query.append("select * from stock_log \r\n" + 
						"where to_date(log_date,'YYYY-MM-DD HH24:MI:SS') \r\n" + 
						"between to_date(?,'YYYY-MM-DD') \r\n" + 
						"AND to_date(?,'YYYY-MM-DD') \r\n" + 
						"order by log_date desc");
				pstmt = conn.prepareStatement(query.toString());
				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
				rs = pstmt.executeQuery();

				StockLogTO bean = null;
				
				while (rs.next()) {

					bean = new StockLogTO();
					bean.setCause(rs.getString("CAUSE"));
					bean.setEffect(rs.getString("EFFECT"));
					bean.setLogDate(rs.getString("LOG_DATE"));
					bean.setItemCode(rs.getString("ITEM_CODE"));
					bean.setItemName(rs.getString("ITEM_NAME"));
					bean.setAmount(rs.getString("AMOUNT"));
					bean.setReason(rs.getString("REASON"));					
					
					StockLogList.add(bean);
				}

				return StockLogList;

			} catch (Exception sqle) {

				throw new DataAccessException(sqle.getMessage());

			} finally {

				dataSourceTransactionManager.close(pstmt, rs);

			}
		}

		@Override
		public HashMap<String,Object> warehousing(String orderNoList) {
			
			if (logger.isDebugEnabled()) {
		         logger.debug("StockDAOImpl : warehousing 시작");
		      }
		      
		      Connection conn = null;
		      CallableStatement cs = null;
		      ResultSet rs = null;
              HashMap<String,Object> resultMap = new HashMap<>();
              
		     try {
		         conn = dataSourceTransactionManager.getConnection();
		         
		         StringBuffer query = new StringBuffer();

		         System.out.println("      @ 프로시저 호출시도"); 
		         query.append(" {call P_WAREHOUSING(?,?,?)} ");
		         System.out.println("P_WAREHOUSING"); 
			         
		         
		         cs = conn.prepareCall(query.toString());
		         cs.setString(1,orderNoList);
		         cs.registerOutParameter(2,OracleTypes.NUMBER);
		         cs.registerOutParameter(3,OracleTypes.VARCHAR);
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
}
