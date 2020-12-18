package com.estimulo.authorityManager.dao;

import java.util.HashMap;

import com.estimulo.authorityManager.to.UserMenuTO;

public interface UserMenuDAO {

	public HashMap<String, UserMenuTO> selectUserMenuCodeList(String workplaceCode, String deptCode, String positionCode);

	
}
