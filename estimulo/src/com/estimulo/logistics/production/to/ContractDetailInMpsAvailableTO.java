package com.estimulo.logistics.production.to;

import com.estimulo.base.to.BaseTO;

public class ContractDetailInMpsAvailableTO extends BaseTO {
	
	private String contractNo;
	private String contractType;
	private String contractDate;
	private String customerCode;
	private String contractDetailNo;
	private String itemCode;
	private String itemName;
	private String unitOfContract;
	private String estimateAmount;
	private String stockAmountUse;
	private String productionRequirement;
	private String dueDateOfContract;
	private String description;
	private String planClassification;
	private String mpsPlanDate;
	private String scheduledEndDate;
	
	public String getPlanClassification() {
		return planClassification;
	}
	public void setPlanClassification(String planClassification) {
		this.planClassification = planClassification;
	}
	public String getMpsPlanDate() {
		return mpsPlanDate;
	}
	public void setMpsPlanDate(String mpsPlanDate) {
		this.mpsPlanDate = mpsPlanDate;
	}
	public String getScheduledEndDate() {
		return scheduledEndDate;
	}
	public void setScheduledEndDate(String scheduledEndDate) {
		this.scheduledEndDate = scheduledEndDate;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public String getContractDate() {
		return contractDate;
	}
	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getContractDetailNo() {
		return contractDetailNo;
	}
	public void setContractDetailNo(String contractDetailNo) {
		this.contractDetailNo = contractDetailNo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getUnitOfContract() {
		return unitOfContract;
	}
	public void setUnitOfContract(String unitOfContract) {
		this.unitOfContract = unitOfContract;
	}
	public String getEstimateAmount() {
		return estimateAmount;
	}
	public void setEstimateAmount(String estimateAmount) {
		this.estimateAmount = estimateAmount;
	}
	public String getStockAmountUse() {
		return stockAmountUse;
	}
	public void setStockAmountUse(String stockAmountUse) {
		this.stockAmountUse = stockAmountUse;
	}
	public String getProductionRequirement() {
		return productionRequirement;
	}
	public void setProductionRequirement(String productionRequirement) {
		this.productionRequirement = productionRequirement;
	}
	public String getDueDateOfContract() {
		return dueDateOfContract;
	}
	public void setDueDateOfContract(String dueDateOfContract) {
		this.dueDateOfContract = dueDateOfContract;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}