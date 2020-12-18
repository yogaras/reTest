package com.estimulo.hr.dao;

import java.util.ArrayList;

import com.estimulo.hr.to.EmpInfoTO;

public interface EmpSearchingDAO {

	public ArrayList<EmpInfoTO> selectAllEmpList(String searchCondition, String[] paramArray);

	public ArrayList<EmpInfoTO> getTotalEmpInfo(String companyCode, String workplaceCode, String userId);
	
	
}
