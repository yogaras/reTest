package com.estimulo.logistics.purchase.to;

public class OrderDialogTempTO {
	
	private String mrpGatheringNo;
	private String itemCode;
	private String itemName;
	private String unitOfMrp;
	private String requiredAmount;
	private String stockAmount;
	private String calculatedRequiredAmount;
	private String standardUnitPrice;
	private String sumPrice;

	public String getMrpGatheringNo() {
		return mrpGatheringNo;
	}
	public void setMrpGatheringNo(String mrpGatheringNo) {
		this.mrpGatheringNo = mrpGatheringNo;
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
	public String getUnitOfMrp() {
		return unitOfMrp;
	}
	public void setUnitOfMrp(String unitOfMrp) {
		this.unitOfMrp = unitOfMrp;
	}
	public String getRequiredAmount() {
		return requiredAmount;
	}
	public void setRequiredAmount(String requiredAmount) {
		this.requiredAmount = requiredAmount;
	}
	public String getStockAmount() {
		return stockAmount;
	}
	public void setStockAmount(String stockAmount) {
		this.stockAmount = stockAmount;
	}
	public String getCalculatedRequiredAmount() {
		return calculatedRequiredAmount;
	}
	public void setCalculatedRequiredAmount(String calculatedRequiredAmount) {
		this.calculatedRequiredAmount = calculatedRequiredAmount;
	}
	public String getStandardUnitPrice() {
		return standardUnitPrice;
	}
	public void setStandardUnitPrice(String standardUnitPrice) {
		this.standardUnitPrice = standardUnitPrice;
	}
	public String getSumPrice() {
		return sumPrice;
	}
	public void setSumPrice(String sumPrice) {
		this.sumPrice = sumPrice;
	}
	
}
