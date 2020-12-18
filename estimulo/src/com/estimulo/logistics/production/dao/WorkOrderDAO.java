package com.estimulo.logistics.production.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.estimulo.logistics.production.to.ProductionPerformanceInfoTO;
import com.estimulo.logistics.production.to.WorkOrderInfoTO;

public interface WorkOrderDAO {

	public HashMap<String,Object> getWorkOrderableMrpList();
	
	public HashMap<String,Object> getWorkOrderSimulationList(String mrpNo);	
	
	public HashMap<String,Object> workOrder(String workPlaceCode,String productionProcess);
	
	public ArrayList<WorkOrderInfoTO> selectWorkOrderInfoList();
	
	public HashMap<String,Object> workOrderCompletion(String workOrderNo,String actualCompletionAmount);
	
	public ArrayList<ProductionPerformanceInfoTO> selectProductionPerformanceInfoList();
	
	public HashMap<String,Object> selectWorkSiteSituation(String workSiteCourse,String workOrderNo,String itemClassIfication);
	
	public void updateWorkCompletionStatus(String workOrderNo,String itemCode,String itemCodeList);
	
	public HashMap<String,Object> workSiteLogList(String workSiteLogDate);
	
}
