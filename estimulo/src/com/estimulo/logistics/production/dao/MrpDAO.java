package com.estimulo.logistics.production.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.estimulo.logistics.production.to.MrpTO;

public interface MrpDAO {

	public ArrayList<MrpTO> selectMrpList(String mrpGatheringStatusCondition) ;
	
	public ArrayList<MrpTO> selectMrpList(String dateSearchCondtion, String startDate, String endDate); 

	public ArrayList<MrpTO> selectMrpListAsMrpGatheringNo(String mrpGatheringNo);
	
	public HashMap<String,Object> openMrp(String mpsNoList);
	
	public int selectMrpCount(String mrpRegisterDate);

	public void insertMrp(MrpTO TO);
	
	public void updateMrp(MrpTO TO);
	
	public void  changeMrpGatheringStatus(String mrpNo, String mrpGatheringNo, String mrpGatheringStatus);
	
	public void deleteMrp(MrpTO TO);
	
}
