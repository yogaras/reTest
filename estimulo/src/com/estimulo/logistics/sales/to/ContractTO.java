package com.estimulo.logistics.sales.to;

import java.util.ArrayList;

import com.estimulo.base.to.BaseTO;

public class ContractTO extends BaseTO {
	private String contractType;
	private String estimateNo;
	private String contractDate;
	private String description;
	private String contractRequester;
	private String customerCode;
	private String personCodeInCharge;
	private String contractNo;
	private ArrayList<ContractDetailTO> contractDetailTOList;

	 public String getContractType() { 
		 return contractType;
	 }
	 public void setContractType(String contractType) { 
		 this.contractType = contractType;
	 }
	 public String getEstimateNo() { 
		 return estimateNo;
	 }
	 public void setEstimateNo(String estimateNo) { 
		 this.estimateNo = estimateNo;
	 }
	 public String getContractDate() { 
		 return contractDate;
	 }
	 public void setContractDate(String contractDate) { 
		 this.contractDate = contractDate;
	 }
	 public String getDescription() { 
		 return description;
	 }
	 public void setDescription(String description) { 
		 this.description = description;
	 }
	 public String getContractRequester() { 
		 return contractRequester;
	 }
	 public void setContractRequester(String contractRequester) { 
		 this.contractRequester = contractRequester;
	 }
	 public String getCustomerCode() { 
		 return customerCode;
	 }
	 public void setCustomerCode(String customerCode) { 
		 this.customerCode = customerCode;
	 }
	 public String getPersonCodeInCharge() { 
		 return personCodeInCharge;
	 }
	 public void setPersonCodeInCharge(String personCodeInCharge) { 
		 this.personCodeInCharge = personCodeInCharge;
	 }
	 public String getContractNo() { 
		 return contractNo;
	 }
	 public void setContractNo(String contractNo) { 
		 this.contractNo = contractNo;
	 }

		public ArrayList<ContractDetailTO> getContractDetailTOList() {
			return contractDetailTOList;
		}
		public void setContractDetailTOList(ArrayList<ContractDetailTO> contractDetailTOList) {
			this.contractDetailTOList = contractDetailTOList;
		}
	 
}