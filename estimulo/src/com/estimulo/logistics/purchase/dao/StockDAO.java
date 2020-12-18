package com.estimulo.logistics.purchase.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.estimulo.logistics.purchase.to.StockLogTO;
import com.estimulo.logistics.purchase.to.StockTO;

public interface StockDAO {
	
	public ArrayList<StockTO> selectStockList();
	
	public ArrayList<StockLogTO> selectStockLogList(String startDate,String endDate);
	
	public HashMap<String,Object> warehousing(String orderNoList);
	
}
