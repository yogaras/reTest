package com.estimulo.logistics.purchase.to;

public class StockTO {
	
	private String warehouseCode;
	private String itemCode;
	private String itemName;
	private String unitOfStock;
	private String safetyAllowanceAmount;
	private String stockAmount;
	private String orderAmount;
	private String inputAmount;
	private String deliveryAmount;
	
	public String getDeliveryAmount() {
		return deliveryAmount;
	}
	public void setDeliveryAmount(String deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
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
	public String getUnitOfStock() {
		return unitOfStock;
	}
	public void setUnitOfStock(String unitOfStock) {
		this.unitOfStock = unitOfStock;
	}
	public String getSafetyAllowanceAmount() {
		return safetyAllowanceAmount;
	}
	public void setSafetyAllowanceAmount(String safetyAllowanceAmount) {
		this.safetyAllowanceAmount = safetyAllowanceAmount;
	}
	public String getStockAmount() {
		return stockAmount;
	}
	public void setStockAmount(String stockAmount) {
		this.stockAmount = stockAmount;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getInputAmount() {
		return inputAmount;
	}
	public void setInputAmount(String inputAmount) {
		this.inputAmount = inputAmount;
	}
	
}
