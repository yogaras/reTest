package com.estimulo.logistics.sales.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.estimulo.logistics.sales.to.ContractDetailTO;
import com.estimulo.logistics.sales.to.ContractInfoTO;
import com.estimulo.logistics.sales.to.ContractTO;
import com.estimulo.logistics.sales.to.DeliveryInfoTO;
import com.estimulo.logistics.sales.to.EstimateDetailTO;
import com.estimulo.logistics.sales.to.EstimateTO;
import com.estimulo.logistics.sales.to.SalesPlanTO;

public interface SalesServiceFacade {

	
	// EstimateApplicationServiceImpl
	public ArrayList<EstimateTO> getEstimateList(String dateSearchCondition, String startDate, String endDate);

	public ArrayList<EstimateDetailTO> getEstimateDetailList(String estimateNo);
	
	public HashMap<String, Object> addNewEstimate(String estimateDate, EstimateTO newEstimateTO);

	public HashMap<String, Object> batchEstimateDetailListProcess(ArrayList<EstimateDetailTO> estimateDetailTOList);	
	
	
	// ContractApplicationServiceImpl
	public ArrayList<ContractInfoTO> getContractList(String searchCondition, String[] paramArray);

	public ArrayList<ContractInfoTO> getDeliverableContractList(String searchCondition, String[] paramArray);
	
	public ArrayList<ContractDetailTO> getContractDetailList(String estimateNo);
	
	public ArrayList<EstimateTO> getEstimateListInContractAvailable(String startDate, String endDate);

	public HashMap<String, Object> addNewContract(String contractDate, String personCodeInCharge, ContractTO workingContractTO);

	public HashMap<String, Object> batchContractDetailListProcess(ArrayList<ContractDetailTO> contractDetailTOList);
	
	public void changeContractStatusInEstimate(String estimateNo , String contractStatus);
	
	
	// SalesPlanApplicationServiceImpl
	public ArrayList<SalesPlanTO> getSalesPlanList(String dateSearchCondition, String startDate, String endDate);
	
	public HashMap<String, Object> batchSalesPlanListProcess(ArrayList<SalesPlanTO> salesPlanTOList);

	public HashMap<String, Object> batchDeliveryListProcess(List<DeliveryInfoTO> deliveryTOList);

	public HashMap<String, Object> deliver(String contractDetailNo);
	
	public ArrayList<DeliveryInfoTO> getDeliveryInfoList();
	
}
