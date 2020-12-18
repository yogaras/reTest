package com.estimulo.logistics.sales.applicationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.estimulo.logistics.sales.to.DeliveryInfoTO;


public interface DeliveryApplicationService {

	public ArrayList<DeliveryInfoTO> getDeliveryInfoList();

	public HashMap<String, Object> batchDeliveryListProcess(List<DeliveryInfoTO> deliveryTOList);

	public HashMap<String, Object> deliver(String contractDetailNo);
	
}


