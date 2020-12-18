package com.estimulo.basicInfo.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import com.estimulo.basicInfo.to.WorkplaceTO;

public interface WorkplaceApplicationService {
	
	public ArrayList<WorkplaceTO> getWorkplaceList(String companyCode);
	
	public String getNewWorkplaceCode(String companyCode);
	
	public HashMap<String, Object> batchWorkplaceListProcess(ArrayList<WorkplaceTO> workplaceList);
	
}
