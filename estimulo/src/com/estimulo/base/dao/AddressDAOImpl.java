package com.estimulo.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.base.to.AddressTO;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;

public class AddressDAOImpl implements AddressDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(AddressDAOImpl.class);

	// 싱글톤
	private static AddressDAO instance = new AddressDAOImpl();

	private AddressDAOImpl() {
	}

	public static AddressDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ AddressDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	@Override
	public String selectSidoCode(String sidoName) {

		if (logger.isDebugEnabled()) {
			logger.debug("AddressDAOImpl : selectSidoCode 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM ADDR_SIDO WHERE SIDO_NAME = ? ");

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, sidoName);

			rs = pstmt.executeQuery();

			String sidoCode = null;

			while (rs.next()) {

				sidoCode = rs.getString("SIDO_CODE");

			}

			return sidoCode;

		} catch (SQLException e) {

			throw new DataAccessException(e.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

	@Override
	public ArrayList<AddressTO> selectRoadNameAddressList(String sidoCode, String searchValue, String buildingMainNumber) {

		if (logger.isDebugEnabled()) {
			logger.debug("AddressDAOImpl : selectRoadNameAddressList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<AddressTO> addressList = new ArrayList<AddressTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*
			 * 
WITH BASIC_ADDRESS_DATA AS
( SELECT * FROM ADDRESS_GSND  // GSND => 여기를 sidoCode.toUpperCase() 로 바꿔야 함!!
WHERE ( 
    ROAD_NAME = REPLACE( ? , ' ' ,'') 
    AND BUILDING_MAIN_NUMBER = ?   // buildingMainNumber 가 null 이 아닌 경우만 쿼리문에 포함됨
    ) OR SIGUNGU_BUILDING_NAME = REPLACE( ? , ' ' ,'') ) ,
    
GET_ADDRESS AS 
( SELECT ZIP_CODE, 
    SIDO || ' ' || SIGUNGU || ' ' || 
    ( CASE WHEN TOWN IS NULL THEN '' ELSE TOWN || ' ' END ) ||
    ROAD_NAME || ' ' || BUILDING_MAIN_NUMBER ||
    ( CASE WHEN BUILDING_SUB_NUMBER = 0 THEN ''
            ELSE '-' || BUILDING_SUB_NUMBER END ) || 
    ( CASE WHEN DONG_NAME IS NULL AND SIGUNGU_BUILDING_NAME IS NULL THEN ''
            ELSE ' ('  END ) || DONG_NAME ||
    ( CASE WHEN DONG_NAME IS NULL OR SIGUNGU_BUILDING_NAME IS NULL THEN ''
            ELSE ', ' END ) ||
    SIGUNGU_BUILDING_NAME ||
    ( CASE WHEN DONG_NAME IS NULL AND SIGUNGU_BUILDING_NAME IS NULL THEN ''
            ELSE ')'  END ) AS ROAD_NAME_ADDRESS ,
    SIDO || ' ' || SIGUNGU || ' ' || 
    ( CASE WHEN TOWN IS NULL THEN '' ELSE TOWN || ' ' END ) ||
    ( CASE WHEN DONG_NAME IS NULL THEN '' ELSE DONG_NAME || ' '  END ) ||
    ( CASE WHEN LI IS NULL THEN '' ELSE LI || ' '  END ) || JIBUN_MAIN_ADDRESS || 
    ( CASE WHEN JIBUN_SUB_ADDRESS = '0' THEN '' 
            ELSE '-' || JIBUN_SUB_ADDRESS END ) AS JIBUN_ADDRESS
FROM BASIC_ADDRESS_DATA
ORDER BY BUILDING_MAIN_NUMBER, BUILDING_SUB_NUMBER ) , 

GET_ADDRESS_AND_ROWNUM AS
( SELECT ROWNUM AS CNT, ZIP_CODE, ROAD_NAME_ADDRESS, JIBUN_ADDRESS
    FROM GET_ADDRESS ) ,

GET_DUPLICATION AS
( SELECT LPAD( T1.CNT, 4, '0') || '-' || T2.RN AS ADDRESS_NO, T1.CNT, 
    ( CASE WHEN RN = 1 THEN '도로명' 
            WHEN RN = 2 THEN '지번' END ) AS ADDRESS_TYPE,
    T1.ZIP_CODE, T1.ROAD_NAME_ADDRESS, T1.JIBUN_ADDRESS
    FROM GET_ADDRESS_AND_ROWNUM T1,  
    ( SELECT ROWNUM AS RN FROM USER_TABLES WHERE ROWNUM < 3 ) T2
    ORDER BY ADDRESS_NO )
    
SELECT ADDRESS_NO, CNT, ZIP_CODE, 
    ( CASE ADDRESS_TYPE WHEN '도로명' THEN ROAD_NAME_ADDRESS 
                               WHEN '지번' THEN JIBUN_ADDRESS END ) AS ADDRESS,
    ADDRESS_TYPE
FROM GET_DUPLICATION T1 
ORDER BY ADDRESS_NO

			 * 
			 */

			query.append(
					"WITH BASIC_ADDRESS_DATA AS\r\n" + 
					"( SELECT * FROM ADDRESS_" + sidoCode.toUpperCase() + "\r\n" +
					"WHERE ( \r\n" + 
					"    ROAD_NAME = REPLACE( ? , ' ' ,'') ");

			if ( ! buildingMainNumber.equals("") ) { // buildingMainNumber 가 입력되었을 경우 조건문에 추가

				query.append("    AND BUILDING_MAIN_NUMBER = ? ");

			}

			query.append("    ) OR SIGUNGU_BUILDING_NAME = REPLACE( ? , ' ' ,'') ) , \r\n");

			query.append(

					"GET_ADDRESS AS \r\n" + 
					"( SELECT ZIP_CODE, \r\n" + 
					"    SIDO || ' ' || SIGUNGU || ' ' || \r\n" + 
					"    ( CASE WHEN TOWN IS NULL THEN '' ELSE TOWN || ' ' END ) ||\r\n" + 
					"    ROAD_NAME || ' ' || BUILDING_MAIN_NUMBER ||\r\n" + 
					"    ( CASE WHEN BUILDING_SUB_NUMBER = 0 THEN ''\r\n" + 
					"            ELSE '-' || BUILDING_SUB_NUMBER END ) || \r\n" + 
					"    ( CASE WHEN DONG_NAME IS NULL AND SIGUNGU_BUILDING_NAME IS NULL THEN ''\r\n" + 
					"            ELSE ' ('  END ) || DONG_NAME ||\r\n" + 
					"    ( CASE WHEN DONG_NAME IS NULL OR SIGUNGU_BUILDING_NAME IS NULL THEN ''\r\n" + 
					"            ELSE ', ' END ) ||\r\n" + 
					"    SIGUNGU_BUILDING_NAME ||\r\n" + 
					"    ( CASE WHEN DONG_NAME IS NULL AND SIGUNGU_BUILDING_NAME IS NULL THEN ''\r\n" + 
					"            ELSE ')'  END ) AS ROAD_NAME_ADDRESS ,\r\n" + 
					"    SIDO || ' ' || SIGUNGU || ' ' || \r\n" + 
					"    ( CASE WHEN TOWN IS NULL THEN '' ELSE TOWN || ' ' END ) ||\r\n" + 
					"    ( CASE WHEN DONG_NAME IS NULL THEN '' ELSE DONG_NAME || ' '  END ) ||\r\n" + 
					"    ( CASE WHEN LI IS NULL THEN '' ELSE LI || ' '  END ) || JIBUN_MAIN_ADDRESS || \r\n" + 
					"    ( CASE WHEN JIBUN_SUB_ADDRESS = '0' THEN '' \r\n" + 
					"            ELSE '-' || JIBUN_SUB_ADDRESS END ) AS JIBUN_ADDRESS\r\n" + 
					"FROM BASIC_ADDRESS_DATA\r\n" + 
					"ORDER BY BUILDING_MAIN_NUMBER, BUILDING_SUB_NUMBER ) , \r\n" + 
					"\r\n" + 
					"GET_ADDRESS_AND_ROWNUM AS\r\n" + 
					"( SELECT ROWNUM AS CNT, ZIP_CODE, ROAD_NAME_ADDRESS, JIBUN_ADDRESS\r\n" + 
					"    FROM GET_ADDRESS ) ,\r\n" + 
					"\r\n" + 
					"GET_DUPLICATION AS\r\n" + 
					"( SELECT LPAD( T1.CNT, 4, '0') || '-' || T2.RN AS ADDRESS_NO, T1.CNT, \r\n" + 
					"    ( CASE WHEN RN = 1 THEN '도로명' \r\n" + 
					"            WHEN RN = 2 THEN '지번' END ) AS ADDRESS_TYPE,\r\n" + 
					"    T1.ZIP_CODE, T1.ROAD_NAME_ADDRESS, T1.JIBUN_ADDRESS\r\n" + 
					"    FROM GET_ADDRESS_AND_ROWNUM T1,  \r\n" + 
					"    ( SELECT ROWNUM AS RN FROM USER_TABLES WHERE ROWNUM < 3 ) T2\r\n" + 
					"    ORDER BY ADDRESS_NO )\r\n" + 
					"    \r\n" + 
					"SELECT ADDRESS_NO, CNT, ZIP_CODE, \r\n" + 
					"    ( CASE ADDRESS_TYPE WHEN '도로명' THEN ROAD_NAME_ADDRESS \r\n" + 
					"                               WHEN '지번' THEN JIBUN_ADDRESS END ) AS ADDRESS,\r\n" + 
					"    ADDRESS_TYPE\r\n" + 
					"FROM GET_DUPLICATION T1 \r\n" + 
					"ORDER BY ADDRESS_NO"

			);

			pstmt = conn.prepareStatement(query.toString());

			if ( ! buildingMainNumber.equals("") ) { // buildingMainNumber 가 입력되었을 경우 ' 검색값 , 건물번호, 검색값 ' 으로 ? 변수 처리

				pstmt.setString(1, searchValue);
				pstmt.setString(2, buildingMainNumber);
				pstmt.setString(3, searchValue);

			} else { // buildingMainNumber 가 입력되지 않았을 경우 ' 검색값 , 검색값 ' 으로 ? 변수 처리

				pstmt.setString(1, searchValue);
				pstmt.setString(2, searchValue);
			}

			rs = pstmt.executeQuery();

			AddressTO bean = null;

			while (rs.next()) {

				bean = new AddressTO();

				bean.setAddressNo(rs.getString("ADDRESS_NO"));
				bean.setCnt(rs.getInt("CNT"));
				bean.setZipCode(rs.getString("ZIP_CODE"));
				bean.setAddress(rs.getString("ADDRESS"));
				bean.setAddressType(rs.getString("ADDRESS_TYPE"));

				addressList.add(bean);

			}

			return addressList;

		} catch (SQLException e) {

			throw new DataAccessException(e.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);
		}

	}

	@Override
	public ArrayList<AddressTO> selectJibunAddressList(String sidoCode, String searchValue, String jibunMainAddress) {

		if (logger.isDebugEnabled()) {
			logger.debug("AddressDAOImpl : selectJibunAddressList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<AddressTO> addressList = new ArrayList<AddressTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			/*

WITH BASIC_ADDRESS_DATA AS
( SELECT * FROM ADDRESS_GSND    // GSND => 여기를 sidoCode.toUpperCase() 로 바꿔야 함!!
WHERE (
    DONG_NAME = REPLACE( ? , ' ' ,'') 
    -- AND JIBUN_MAIN_ADDRESS  = ?    // jibunMainAddress 가 null 이 아닌 경우에만 쿼리문에 포함
    ) OR TOWN = REPLACE( ? , ' ' ,'') 
    OR LI = REPLACE( ? , ' ' ,'') 
    OR SIGUNGU_BUILDING_NAME = REPLACE( ? , ' ' ,'') ) ,

GET_ADDRESS AS 
( SELECT ZIP_CODE, 
    SIDO || ' ' || SIGUNGU || ' ' || 
    ( CASE WHEN TOWN IS NULL THEN '' ELSE TOWN || ' ' END ) ||
    ROAD_NAME || ' ' || BUILDING_MAIN_NUMBER ||
    ( CASE WHEN BUILDING_SUB_NUMBER = 0 THEN ''
            ELSE '-' || BUILDING_SUB_NUMBER END ) || 
    ( CASE WHEN DONG_NAME IS NULL AND SIGUNGU_BUILDING_NAME IS NULL THEN ''
            ELSE ' ('  END ) || DONG_NAME ||
    ( CASE WHEN DONG_NAME IS NULL OR SIGUNGU_BUILDING_NAME IS NULL THEN ''
            ELSE ', ' END ) ||
    SIGUNGU_BUILDING_NAME ||
    ( CASE WHEN DONG_NAME IS NULL AND SIGUNGU_BUILDING_NAME IS NULL THEN ''
            ELSE ')'  END ) AS ROAD_NAME_ADDRESS ,
    SIDO || ' ' || SIGUNGU || ' ' || 
    ( CASE WHEN TOWN IS NULL THEN '' ELSE TOWN || ' ' END ) ||
    ( CASE WHEN DONG_NAME IS NULL THEN '' ELSE DONG_NAME || ' '  END ) ||
    ( CASE WHEN LI IS NULL THEN '' ELSE LI || ' '  END ) || JIBUN_MAIN_ADDRESS || 
    ( CASE WHEN JIBUN_SUB_ADDRESS = '0' THEN '' 
            ELSE '-' || JIBUN_SUB_ADDRESS END ) AS JIBUN_ADDRESS
FROM BASIC_ADDRESS_DATA
ORDER BY BUILDING_MAIN_NUMBER, BUILDING_SUB_NUMBER ) , 

GET_ADDRESS_AND_ROWNUM AS
( SELECT ROWNUM AS CNT, ZIP_CODE, ROAD_NAME_ADDRESS, JIBUN_ADDRESS
    FROM GET_ADDRESS ) ,

GET_DUPLICATION AS
( SELECT LPAD( T1.CNT, 4, '0') || '-' || T2.RN AS ADDRESS_NO, T1.CNT, 
    ( CASE WHEN RN = 1 THEN '도로명' 
            WHEN RN = 2 THEN '지번' END ) AS ADDRESS_TYPE,
    T1.ZIP_CODE, T1.ROAD_NAME_ADDRESS, T1.JIBUN_ADDRESS
    FROM GET_ADDRESS_AND_ROWNUM T1,  
    ( SELECT ROWNUM AS RN FROM USER_TABLES WHERE ROWNUM < 3 ) T2
    ORDER BY ADDRESS_NO )
    
SELECT ADDRESS_NO, CNT, ZIP_CODE, 
    ( CASE ADDRESS_TYPE WHEN '도로명' THEN ROAD_NAME_ADDRESS 
                               WHEN '지번' THEN JIBUN_ADDRESS END ) AS ADDRESS,
    ADDRESS_TYPE
FROM GET_DUPLICATION T1 
ORDER BY ADDRESS_NO

			 * 
			 */

			query.append(
					"WITH BASIC_ADDRESS_DATA AS\r\n" + 
					"( SELECT * FROM ADDRESS_" + sidoCode.toUpperCase() + "\r\n" + 
					"WHERE (\r\n" + 
					"    DONG_NAME = REPLACE( ? , ' ' ,'') ");

			if ( ! jibunMainAddress.equals("") ) { // jibunMainAddress 가 입력되었을 경우 조건문에 추가

				query.append("AND JIBUN_MAIN_ADDRESS  = ? ");

			}

			query.append(") OR TOWN = REPLACE( ? , ' ' ,'') \r\n" + 
					"    OR LI = REPLACE( ? , ' ' ,'') \r\n" + 
					"    OR SIGUNGU_BUILDING_NAME = REPLACE( ? , ' ' ,'') ) ,");

			query.append(

					"GET_ADDRESS AS \r\n" + 
					"( SELECT ZIP_CODE, \r\n" + 
					"    SIDO || ' ' || SIGUNGU || ' ' || \r\n" + 
					"    ( CASE WHEN TOWN IS NULL THEN '' ELSE TOWN || ' ' END ) ||\r\n" + 
					"    ROAD_NAME || ' ' || BUILDING_MAIN_NUMBER ||\r\n" + 
					"    ( CASE WHEN BUILDING_SUB_NUMBER = 0 THEN ''\r\n" + 
					"            ELSE '-' || BUILDING_SUB_NUMBER END ) || \r\n" + 
					"    ( CASE WHEN DONG_NAME IS NULL AND SIGUNGU_BUILDING_NAME IS NULL THEN ''\r\n" + 
					"            ELSE ' ('  END ) || DONG_NAME ||\r\n" + 
					"    ( CASE WHEN DONG_NAME IS NULL OR SIGUNGU_BUILDING_NAME IS NULL THEN ''\r\n" + 
					"            ELSE ', ' END ) ||\r\n" + 
					"    SIGUNGU_BUILDING_NAME ||\r\n" + 
					"    ( CASE WHEN DONG_NAME IS NULL AND SIGUNGU_BUILDING_NAME IS NULL THEN ''\r\n" + 
					"            ELSE ')'  END ) AS ROAD_NAME_ADDRESS ,\r\n" + 
					"    SIDO || ' ' || SIGUNGU || ' ' || \r\n" + 
					"    ( CASE WHEN TOWN IS NULL THEN '' ELSE TOWN || ' ' END ) ||\r\n" + 
					"    ( CASE WHEN DONG_NAME IS NULL THEN '' ELSE DONG_NAME || ' '  END ) ||\r\n" + 
					"    ( CASE WHEN LI IS NULL THEN '' ELSE LI || ' '  END ) || JIBUN_MAIN_ADDRESS || \r\n" + 
					"    ( CASE WHEN JIBUN_SUB_ADDRESS = '0' THEN '' \r\n" + 
					"            ELSE '-' || JIBUN_SUB_ADDRESS END ) AS JIBUN_ADDRESS\r\n" + 
					"FROM BASIC_ADDRESS_DATA\r\n" + 
					"ORDER BY BUILDING_MAIN_NUMBER, BUILDING_SUB_NUMBER ) , \r\n" + 
					"\r\n" + 
					"GET_ADDRESS_AND_ROWNUM AS\r\n" + 
					"( SELECT ROWNUM AS CNT, ZIP_CODE, ROAD_NAME_ADDRESS, JIBUN_ADDRESS\r\n" + 
					"    FROM GET_ADDRESS ) ,\r\n" + 
					"\r\n" + 
					"GET_DUPLICATION AS\r\n" + 
					"( SELECT LPAD( T1.CNT, 4, '0') || '-' || T2.RN AS ADDRESS_NO, T1.CNT, \r\n" + 
					"    ( CASE WHEN RN = 1 THEN '도로명' \r\n" + 
					"            WHEN RN = 2 THEN '지번' END ) AS ADDRESS_TYPE,\r\n" + 
					"    T1.ZIP_CODE, T1.ROAD_NAME_ADDRESS, T1.JIBUN_ADDRESS\r\n" + 
					"    FROM GET_ADDRESS_AND_ROWNUM T1,  \r\n" + 
					"    ( SELECT ROWNUM AS RN FROM USER_TABLES WHERE ROWNUM < 3 ) T2\r\n" + 
					"    ORDER BY ADDRESS_NO )\r\n" + 
					"    \r\n" + 
					"SELECT ADDRESS_NO, CNT, ZIP_CODE, \r\n" + 
					"    ( CASE ADDRESS_TYPE WHEN '도로명' THEN ROAD_NAME_ADDRESS \r\n" + 
					"                               WHEN '지번' THEN JIBUN_ADDRESS END ) AS ADDRESS,\r\n" + 
					"    ADDRESS_TYPE\r\n" + 
					"FROM GET_DUPLICATION T1 \r\n" + 
					"ORDER BY ADDRESS_NO"

			);

			pstmt = conn.prepareStatement(query.toString());

			if ( ! jibunMainAddress.equals("") ) { // jibunMainAddress 가 입력되었을 경우 ' 검색값 , 건물번호, 검색값 ' 으로 ? 변수 처리

				pstmt.setString(1, searchValue);
				pstmt.setString(2, jibunMainAddress);
				pstmt.setString(3, searchValue);
				pstmt.setString(4, searchValue);
				pstmt.setString(5, searchValue);

			} else { // jibunMainAddress 가 입력되지 않았을 경우 ' 검색값 , 검색값 ' 으로 ? 변수 처리

				pstmt.setString(1, searchValue);
				pstmt.setString(2, searchValue);
				pstmt.setString(3, searchValue);
				pstmt.setString(4, searchValue);

			}
			rs = pstmt.executeQuery();

			AddressTO bean = null;

			while (rs.next()) {

				bean = new AddressTO();

				bean.setAddressNo(rs.getString("ADDRESS_NO"));
				bean.setCnt(rs.getInt("CNT"));
				bean.setZipCode(rs.getString("ZIP_CODE"));
				bean.setAddress(rs.getString("ADDRESS"));
				bean.setAddressType(rs.getString("ADDRESS_TYPE"));

				addressList.add(bean);

			}

			return addressList;

		} catch (SQLException e) {

			throw new DataAccessException(e.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);
		}

	}

}
