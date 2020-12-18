package com.estimulo.basicInfo.dao;

import java.util.ArrayList;

import com.estimulo.basicInfo.to.WorkplaceTO;

public interface WorkplaceDAO {
	
	public ArrayList<WorkplaceTO> selectWorkplaceList(String companyCode);

	public void insertWorkplace(WorkplaceTO TO);
	
	public void updateWorkplace(WorkplaceTO TO);
	
	public void deleteWorkplace(WorkplaceTO TO);
}
