package com.estimulo.logistics.purchase.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import com.estimulo.logistics.purchase.to.StockLogTO;
import com.estimulo.logistics.purchase.to.StockTO;

public interface StockApplicationService {
	
	public ArrayList<StockTO> getStockList();
	
	public ArrayList<StockLogTO> getStockLogList(String startDate,String endDate);
	
	public HashMap<String,Object> warehousing(ArrayList<String> orderNoArr);
	
}
