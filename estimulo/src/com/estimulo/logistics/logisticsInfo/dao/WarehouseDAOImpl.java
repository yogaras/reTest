package com.estimulo.logistics.logisticsInfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.logisticsInfo.to.WarehouseTO;

public class WarehouseDAOImpl implements WarehouseDAO{

	// SLF4J logger
		private static Logger logger = LoggerFactory.getLogger(WarehouseDAOImpl.class);

		// 싱글톤
		private static WarehouseDAO instance = new WarehouseDAOImpl();

		private WarehouseDAOImpl() {
		}

		public static WarehouseDAO getInstance() {

			if (logger.isDebugEnabled()) {
				logger.debug("@ CustomerDAOImpl 객체접근");
			}

			return instance;
		}

		// 참조변수 선언
		private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
				.getInstance();

		public ArrayList<WarehouseTO> selectWarehouseList() {

			if (logger.isDebugEnabled()) {
				logger.debug("CustomerDAOImpl : selectCustomerListByCompany 시작");
			}

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			ArrayList<WarehouseTO> warehouseList = new ArrayList<WarehouseTO>();

			try {
				conn = dataSourceTransactionManager.getConnection();

				StringBuffer query = new StringBuffer();

				query.append("SELECT * FROM WAREHOUSE");
				pstmt = conn.prepareStatement(query.toString());
				rs = pstmt.executeQuery();

				WarehouseTO bean = null;
				
				while (rs.next()) {

					bean = new WarehouseTO();
					bean.setWarehouseCode(rs.getString("WAREHOUSE_CODE"));
					bean.setWarehouseName(rs.getString("WAREHOUSE_NAME"));
					bean.setWarehouseUseOrNot(rs.getString("WAREHOUSE_USE_OR_NOT"));
					bean.setDescription(rs.getString("DESCRIPTION"));

					warehouseList.add(bean);
				}

				return warehouseList;

			} catch (Exception sqle) {

				throw new DataAccessException(sqle.getMessage());

			} finally {

				dataSourceTransactionManager.close(pstmt, rs);

			}
		}

}
