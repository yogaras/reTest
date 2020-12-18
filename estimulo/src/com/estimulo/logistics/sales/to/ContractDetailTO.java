package com.estimulo.logistics.sales.to;

import com.estimulo.base.to.BaseTO;

public class ContractDetailTO extends BaseTO {
	
	private String contractDetailNo;
	private String contractNo;
	private String itemCode;
	private String itemName;	
	private String unitOfContract; 
	private String dueDateOfContract;	
	private String estimateAmount;
	private String stockAmountUse; 
	private String productionRequirement;     
	private String unitPriceOfContract;	
	private String sumPriceOfContract;
	private String processingStatus;
	private String operationCompletedStatus;	
	private String deliveryCompletionStatus;	
	private String description;
	
	public String getContractDetailNo() {
		return contractDetailNo;
	}
	public void setContractDetailNo(String contractDetailNo) {
		this.contractDetailNo = contractDetailNo;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
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
	public String getDueDateOfContract() {
		return dueDateOfContract;
	}
	public void setDueDateOfContract(String dueDateOfContract) {
		this.dueDateOfContract = dueDateOfContract;
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
	public String getUnitPriceOfContract() {
		return unitPriceOfContract;
	}
	public void setUnitPriceOfContract(String unitPriceOfContract) {
		this.unitPriceOfContract = unitPriceOfContract;
	}
	public String getSumPriceOfContract() {
		return sumPriceOfContract;
	}
	public void setSumPriceOfContract(String sumPriceOfContract) {
		this.sumPriceOfContract = sumPriceOfContract;
	}
	public String getProcessingStatus() {
		return processingStatus;
	}
	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}
	public String getOperationCompletedStatus() {
		return operationCompletedStatus;
	}
	public void setOperationCompletedStatus(String operationCompletedStatus) {
		this.operationCompletedStatus = operationCompletedStatus;
	}
	public String getDeliveryCompletionStatus() {
		return deliveryCompletionStatus;
	}
	public void setDeliveryCompletionStatus(String deliveryCompletionStatus) {
		this.deliveryCompletionStatus = deliveryCompletionStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}