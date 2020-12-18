package com.estimulo.logistics.production.dao;

import java.util.ArrayList;

import com.estimulo.logistics.production.to.MpsTO;

public interface MpsDAO {

	public ArrayList<MpsTO> selectMpsList(String startDate, String endDate, String includeMrpApply);
	
	public int selectMpsCount(String mpsPlanDate);
	
	public void insertMps(MpsTO TO);
	
	public void updateMps(MpsTO TO);
	
	public void changeMrpApplyStatus(String mpsNo, String mrpStatus);
	
	public void deleteMps(MpsTO TO);
	
}
