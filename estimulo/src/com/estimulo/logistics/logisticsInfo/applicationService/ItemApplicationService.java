package com.estimulo.logistics.logisticsInfo.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import com.estimulo.logistics.logisticsInfo.to.ItemInfoTO;
import com.estimulo.logistics.logisticsInfo.to.ItemTO;

public interface ItemApplicationService {

	public ArrayList<ItemInfoTO> getItemInfoList(String searchCondition, String[] paramArray);
	
	public HashMap<String, Object> batchItemListProcess(ArrayList<ItemTO> itemTOList);
		
	public int getStandardUnitPrice(String itemCode);
	
	public int getStandardUnitPriceBox(String itemCode);
}
