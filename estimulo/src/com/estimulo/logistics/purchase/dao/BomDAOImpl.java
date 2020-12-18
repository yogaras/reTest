package com.estimulo.logistics.purchase.dao;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.purchase.to.BomDeployTO;
import com.estimulo.logistics.purchase.to.BomInfoTO;
import com.estimulo.logistics.purchase.to.BomTO;

public class BomDAOImpl implements BomDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(BomDAOImpl.class);	
	
	// 싱글톤
	private static BomDAO instance = new BomDAOImpl();

	private BomDAOImpl() {
	}

	public static BomDAO getInstance() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("@ BomDAOImpl 객체접근");
		}
		
		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	
	
	public ArrayList<BomDeployTO> selectBomDeployList(String deployCondition, String itemCode, String itemClassificationCondition) {

		if (logger.isDebugEnabled()) {
			logger.debug("BomDAOImpl : selectBomDeployList 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<BomDeployTO> bomDeployList = new ArrayList<BomDeployTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
SELECT T1.BOM_NO, T1.BOM_LEVEL, T1.PARENT_ITEM_CODE, T1.ITEM_CODE, T2.ITEM_NAME, T2.UNIT_OF_STOCK, 
    T1.NET_AMOUNT, TO_NUMBER(NVL(LOSS_RATE,0)) AS LOSS_RATE,
    TO_CHAR(T1.NET_AMOUNT*(1 + TO_NUMBER(NVL(LOSS_RATE,0)) )) AS NECESSARY_AMOUNT, T2.LEAD_TIME, T1.IS_LEAF, T1.DESCRIPTION
FROM
( SELECT CONNECT_BY_ROOT ITEM_CODE || ' -' || TO_CHAR(ROWNUM, '00') AS BOM_NO, ( LEVEL-1 ) AS BOM_LEVEL, PARENT_ITEM_CODE, 
    LPAD(' ' , 8 * (LEVEL-1) ) || ITEM_CODE AS ITEM_CODE, 
    ( CASE CONNECT_BY_ISLEAF WHEN 0 THEN 'false' WHEN 1 THEN 'true' END ) AS IS_LEAF, NET_AMOUNT, DESCRIPTION
FROM BOM
START WITH ITEM_CODE = ?   --- 정전개시 AND PARENT_ITEM_CODE = 'NULL' , 역전개시 AND PARENT_ITEM_CODE != 'NULL'  이 더 붙음
CONNECT BY PARENT_ITEM_CODE = PRIOR ITEM_CODE      -- 역전개는 CONNECT BY PRIOR PARENT_ITEM_CODE = ITEM_CODE
ORDER SIBLINGS BY NO ) T1,
( SELECT ITEM_CODE, ITEM_NAME, ITEM_CLASSIFICATION, UNIT_OF_STOCK, LOSS_RATE, LEAD_TIME FROM ITEM ) T2
WHERE TRIM(T1.ITEM_CODE) = T2.ITEM_CODE
ORDER BY BOM_NO
*/
			query.append(
					"SELECT T1.BOM_NO, T1.BOM_LEVEL, T1.PARENT_ITEM_CODE, T1.ITEM_CODE, T2.ITEM_NAME, T2.UNIT_OF_STOCK, \r\n" + 
					"    T1.NET_AMOUNT, TO_NUMBER(NVL(LOSS_RATE,0)) AS LOSS_RATE,\r\n" + 
					"    TO_CHAR(T1.NET_AMOUNT*(1 + TO_NUMBER(NVL(LOSS_RATE,0)) )) AS NECESSARY_AMOUNT, T2.LEAD_TIME, T1.IS_LEAF, T1.DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"( SELECT CONNECT_BY_ROOT ITEM_CODE || ' -' || TO_CHAR(ROWNUM, '00') AS BOM_NO, ( LEVEL ) AS BOM_LEVEL, PARENT_ITEM_CODE, \r\n" + 
					"    ITEM_CODE AS ITEM_CODE, \r\n" + 
					"    ( CASE CONNECT_BY_ISLEAF WHEN 0 THEN 'false' WHEN 1 THEN 'true' END ) AS IS_LEAF, NET_AMOUNT, DESCRIPTION\r\n" + 
					"FROM BOM\r\n" + 
					"START WITH ITEM_CODE = ? ");
			
			 if( deployCondition.equals("forward") ) {
		            if(itemClassificationCondition.equals("IT-CI")){
		               query.append("AND PARENT_ITEM_CODE = 'NULL' CONNECT BY PARENT_ITEM_CODE = PRIOR ITEM_CODE ");
		            }else if(itemClassificationCondition.equals("IT-SI")){
		               if(itemCode.equals("DK-AP01")) {
		                  query.append("AND PARENT_ITEM_CODE = 'DK-01' CONNECT BY PARENT_ITEM_CODE = PRIOR ITEM_CODE ");  // 정전개
		               }else {
		                  query.append("AND PARENT_ITEM_CODE = 'DK-02' CONNECT BY PARENT_ITEM_CODE = PRIOR ITEM_CODE ");  // 정전개
		               }   
		            }
			 } else if( deployCondition.equals("reverse") ) {
				query.append("AND PARENT_ITEM_CODE != 'NULL' CONNECT BY PRIOR PARENT_ITEM_CODE = ITEM_CODE ");  // 역전개
			}
			
			query.append(
					"ORDER SIBLINGS BY NO ) T1,\r\n" + 
					"( SELECT ITEM_CODE, ITEM_NAME, ITEM_CLASSIFICATION, UNIT_OF_STOCK, LOSS_RATE, LEAD_TIME FROM ITEM ) T2\r\n" + 
					"WHERE TRIM(T1.ITEM_CODE) = T2.ITEM_CODE\r\n" + 
					"ORDER BY BOM_NO");
			
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, itemCode);

			rs = pstmt.executeQuery();

			
			BomDeployTO bean = null; 
			
			while (rs.next()) {
				
				bean = new BomDeployTO();
				
				bean.setBomNo(rs.getString("BOM_NO"));
				bean.setBomLevel(rs.getInt("BOM_LEVEL"));
				bean.setParentItemCode(rs.getString("PARENT_ITEM_CODE"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setUnitOfStock(rs.getString("UNIT_OF_STOCK"));
				bean.setNetAmount(rs.getInt("NET_AMOUNT"));
				bean.setLossRate(rs.getString("LOSS_RATE"));
				bean.setNecessaryAmount(rs.getString("NECESSARY_AMOUNT"));
				bean.setLeadTime(rs.getString("LEAD_TIME"));
				bean.setIsLeaf(rs.getString("IS_LEAF"));				
				bean.setDescription(rs.getString("DESCRIPTION"));
				
				bomDeployList.add(bean);
				
			}

			return bomDeployList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}
	
	

	public ArrayList<BomInfoTO> selectBomInfoList(String itemCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("BomDAOImpl : selectBomInfoList 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<BomInfoTO> bomInfoList = new ArrayList<BomInfoTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
SELECT B.BOM_ITEM_CODE , B.NO, B.PARENT_ITEM_CODE, B.ITEM_CODE, I.ITEM_NAME, I.ITEM_CLASSIFICATION, 
    C.DETAIL_CODE_NAME AS ITEM_CLASSIFICATION_NAME, B.NET_AMOUNT, B.DESCRIPTION
FROM
    ( SELECT * FROM BOM B WHERE ( ITEM_CODE = ? AND PARENT_ITEM_CODE = 'NULL' ) OR PARENT_ITEM_CODE = ? ) B, 
    ITEM I, CODE_DETAIL C
WHERE B.ITEM_CODE = I.ITEM_CODE 
    AND I.ITEM_CLASSIFICATION = C.DETAIL_CODE
ORDER BY NO
*/
			query.append(
					"SELECT B.PARENT_ITEM_CODE, B.ITEM_CODE, B.NO, I.ITEM_NAME, I.ITEM_CLASSIFICATION, \r\n" + 
					"    C.DETAIL_CODE_NAME AS ITEM_CLASSIFICATION_NAME, B.NET_AMOUNT, B.DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    ( SELECT * FROM BOM B WHERE ( ITEM_CODE = ? AND PARENT_ITEM_CODE = 'NULL' ) OR PARENT_ITEM_CODE = ? ) B, \r\n" + 
					"    ITEM I, CODE_DETAIL C\r\n" + 
					"WHERE B.ITEM_CODE = I.ITEM_CODE \r\n" + 
					"    AND I.ITEM_CLASSIFICATION = C.DETAIL_CODE\r\n" + 
					"ORDER BY NO");
			
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, itemCode);
			pstmt.setString(2, itemCode);

			rs = pstmt.executeQuery();

			BomInfoTO bean = null; 
			
			while (rs.next()) {
				
				bean = new BomInfoTO();

				bean.setNo(rs.getInt("NO"));
				bean.setParentItemCode(rs.getString("PARENT_ITEM_CODE"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				bean.setItemClassification(rs.getString("ITEM_CLASSIFICATION"));
				bean.setItemClassificationName(rs.getString("ITEM_CLASSIFICATION_NAME"));
				bean.setNetAmount(rs.getInt("NET_AMOUNT"));
				bean.setDescription(rs.getString("DESCRIPTION"));
				
				bomInfoList.add(bean);
			
			}

			return bomInfoList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}
	
	
	
	public ArrayList<BomInfoTO> selectAllItemWithBomRegisterAvailable() {

		if (logger.isDebugEnabled()) {
			logger.debug("BomDAOImpl : selectAllItemWithBomRegisterAvailable 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<BomInfoTO> bomInfoList = new ArrayList<BomInfoTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
WITH ALL_REGISTERED_BOM AS
	( SELECT ITEM_CODE 
		FROM ( SELECT * FROM BOM WHERE PARENT_ITEM_CODE = 'NULL' )
		GROUP BY ITEM_CODE ) ,

ALL_ITEM_AS_PRODUCT AS
	( SELECT ITEM_CLASSIFICATION, ITEM_CODE, ITEM_NAME 
		FROM ITEM WHERE ITEM_CLASSIFICATION IN ( 'IT-CI', 'IT-SI' ) )

SELECT T2.DETAIL_CODE_NAME AS ITEM_CLASSIFICATION_NAME, 
	T1.ITEM_CODE, T1.ITEM_NAME 
FROM ALL_ITEM_AS_PRODUCT T1, CODE_DETAIL T2
WHERE T1.ITEM_CLASSIFICATION = T2.DETAIL_CODE 
AND T1.ITEM_CODE NOT IN 
	( SELECT ITEM_CODE FROM ALL_REGISTERED_BOM )
*/
			query.append(
					"WITH ALL_REGISTERED_BOM AS\r\n" + 
					"	( SELECT ITEM_CODE \r\n" + 
					"		FROM ( SELECT * FROM BOM WHERE PARENT_ITEM_CODE = 'NULL' )\r\n" + 
					"		GROUP BY ITEM_CODE ) ,\r\n" + 
					"\r\n" + 
					"ALL_ITEM_AS_PRODUCT AS\r\n" + 
					"	( SELECT ITEM_CLASSIFICATION, ITEM_CODE, ITEM_NAME \r\n" + 
					"		FROM ITEM WHERE ITEM_CLASSIFICATION IN ( 'IT-CI', 'IT-SI' ) )\r\n" + 
					"\r\n" + 
					"SELECT T2.DETAIL_CODE_NAME AS ITEM_CLASSIFICATION_NAME, \r\n" + 
					"	T1.ITEM_CODE, T1.ITEM_NAME \r\n" + 
					"FROM ALL_ITEM_AS_PRODUCT T1, CODE_DETAIL T2\r\n" + 
					"WHERE T1.ITEM_CLASSIFICATION = T2.DETAIL_CODE \r\n" + 
					"AND T1.ITEM_CODE NOT IN \r\n" + 
					"	( SELECT ITEM_CODE FROM ALL_REGISTERED_BOM )");
			
			pstmt = conn.prepareStatement(query.toString());

			rs = pstmt.executeQuery();

			BomInfoTO bean = null; 
			
			while (rs.next()) {
				
				bean = new BomInfoTO();
				
				bean.setItemClassificationName(rs.getString("ITEM_CLASSIFICATION_NAME"));
				bean.setItemCode(rs.getString("ITEM_CODE"));
				bean.setItemName(rs.getString("ITEM_NAME"));
				
				bomInfoList.add(bean);
			}

			return bomInfoList;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
	}
	
	

	public void insertBom(BomTO bean) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("BomDAOImpl : insertBom 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
Insert into BOM 
( ITEM_CODE, PARENT_ITEM_CODE, NO, NET_AMOUNT, DESCRIPTION ) 
values (?,?,?,?,?,?)
*/
			query.append(
					"Insert into BOM \r\n" + 
					"( ITEM_CODE, PARENT_ITEM_CODE, NO, NET_AMOUNT, DESCRIPTION ) \r\n" +  
					"values (?, ?, ?, ?, ?)");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getItemCode());
			pstmt.setString(2, bean.getParentItemCode());
			pstmt.setInt(3, bean.getNo());
			pstmt.setInt(4, bean.getNetAmount());
			pstmt.setString(5, bean.getDescription());
			
			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
		
	}
	

	public void updateBom(BomTO bean) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("BomDAOImpl : updateBom 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
/*
UPDATE BOM SET 
NO = ? , NET_AMOUNT = ? , DESCRIPTION = ?
WHERE PARENT_ITEM_CODE = ? AND ITEM_CODE = ?  
*/
			query.append(
					"UPDATE BOM SET \r\n" + 
					"NO = ? , NET_AMOUNT = ? , DESCRIPTION = ?\r\n" + 
					"WHERE PARENT_ITEM_CODE = ? AND ITEM_CODE = ?  ");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setInt(1, bean.getNo());
			pstmt.setInt(2, bean.getNetAmount());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getParentItemCode());
			pstmt.setString(5, bean.getItemCode());
			
			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
		
	}
	
	
	public void deleteBom(BomTO bean) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("BomDAOImpl : deleteBom 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			
			query.append(
					"DELETE FROM BOM WHERE PARENT_ITEM_CODE = ? AND ITEM_CODE = ?");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bean.getParentItemCode()  );
			pstmt.setString(2, bean.getItemCode());
			
			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}
		
	}
	
}
