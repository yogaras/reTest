package com.estimulo.logistics.logisticsInfo.dao;

import java.util.ArrayList;

import com.estimulo.logistics.logisticsInfo.to.WarehouseTO;

public interface WarehouseDAO {
	public ArrayList<WarehouseTO> selectWarehouseList();
}
