package com.estimulo.authorityManager.dao;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.authorityManager.to.UserMenuTO;
import com.estimulo.common.db.DataSourceTransactionManager;
import com.estimulo.common.exception.DataAccessException;

public class UserMenuDAOImpl implements UserMenuDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(UserMenuDAOImpl.class);

	// 싱글톤
	private static UserMenuDAO instance = new UserMenuDAOImpl();

	private UserMenuDAOImpl() {
	}

	public static UserMenuDAO getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ UserMenuDAOImpl 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager
			.getInstance();

	public HashMap<String, UserMenuTO> selectUserMenuCodeList(String workplaceCode, String deptCode,
			String positionCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("UserMenuDAOImpl : selectUserMenuCodeList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		HashMap<String, UserMenuTO> userMenuTOMap = new HashMap<>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();

			query.append("SELECT * FROM\r\n"
					+ "	( SELECT ROWNUM AS NO, LEVEL AS MENU_LEVEL, CONNECT_BY_ISLEAF LEAF, \r\n"
					+ "    MENU_ORDER, MENU_NAME, URL, IS_ACCESS_DENIED\r\n" + "	FROM \r\n" + "	( \r\n"
					+ "        SELECT T1.MENU_CODE, URL, MENU_NAME, MENU_ORDER, \r\n"
					+ "                	IS_ACCESS_DENIED, IS_ACCESS_DENIED_NOT_PRINTED, PARENT_MENU_CODE\r\n"
					+ "        FROM MENU T1, MENU_AVAILABLE_BY_POSITION T2\r\n"
					+ "        WHERE T1.MENU_CODE = T2.MENU_CODE\r\n" + "        AND T1.WORKPLACE_CODE = ?\r\n"
					+ "        AND DEPT_CODE = ?\r\n" + "        AND POSITION_CODE = ? \r\n"
					+ "        AND IS_ACCESS_DENIED_NOT_PRINTED IS NULL\r\n" + "    ) \r\n"
					+ "	START WITH PARENT_MENU_CODE IS NULL \r\n"
					+ "	CONNECT BY PRIOR MENU_CODE = PARENT_MENU_CODE\r\n" + "	ORDER SIBLINGS BY MENU_ORDER )\r\n"
					+ "	WHERE ( MENU_LEVEL = 3 ) OR ( MENU_LEVEL = 2 AND LEAF = 0 ) OR \r\n"
					+ "        ( MENU_LEVEL = 2 AND LEAF = 1 AND URL IS NOT NULL )\r\n"
					+ "        OR ( MENU_LEVEL = 1 AND LEAF = 0 ) OR MENU_ORDER =0\r\n" + "    ORDER BY NO");
			
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, workplaceCode);
			pstmt.setString(2, deptCode);
			pstmt.setString(3, positionCode);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				UserMenuTO bean = new UserMenuTO();
				

				bean.setNo(rs.getInt("NO"));
				bean.setMenuLevel(rs.getInt("MENU_LEVEL"));
				bean.setLeaf(rs.getInt("LEAF"));
				bean.setMenuOrder(rs.getInt("MENU_ORDER"));
				bean.setMenuName(rs.getString("MENU_NAME"));
				bean.setUrl(rs.getString("URL"));
				bean.setIsAccessDenied(rs.getString("IS_ACCESS_DENIED"));

				userMenuTOMap.put(rs.getInt("NO") + "", bean);
			}

			return userMenuTOMap;

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

}
