package com.estimulo.logistics.sales.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import com.estimulo.logistics.sales.to.ContractDetailTO;
import com.estimulo.logistics.sales.to.ContractInfoTO;
import com.estimulo.logistics.sales.to.ContractTO;
import com.estimulo.logistics.sales.to.EstimateTO;

public interface ContractApplicationService {

	public ArrayList<ContractInfoTO> getContractList(String searchCondition, String[] paramArray);

	public ArrayList<ContractDetailTO> getContractDetailList(String estimateNo);
	
	public ArrayList<EstimateTO> getEstimateListInContractAvailable(String startDate, String endDate);

	// ApplicationService 안에서만 호출
	public String getNewContractNo(String contractDate);

	public HashMap<String, Object> addNewContract(String contractDate, String personCodeInCharge, ContractTO workingContractTO);

	public HashMap<String, Object> batchContractDetailListProcess(ArrayList<ContractDetailTO> contractDetailTOList);

	public void changeContractStatusInEstimate(String estimateNo , String contractStatus);
	
	public ArrayList<ContractInfoTO> getDeliverableContractList(String searchCondition, String[] paramArray);

}
