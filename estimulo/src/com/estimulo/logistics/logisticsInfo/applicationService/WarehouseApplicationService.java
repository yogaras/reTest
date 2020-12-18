package com.estimulo.logistics.logisticsInfo.applicationService;

import java.util.ArrayList;

import com.estimulo.logistics.logisticsInfo.to.WarehouseTO;

public interface WarehouseApplicationService {
	
	public ArrayList<WarehouseTO> getWarehouseInfoList();

	public void modifyWarehouseInfo(WarehouseTO warehouseTO);

	public String findLastWarehouseCode();
}
