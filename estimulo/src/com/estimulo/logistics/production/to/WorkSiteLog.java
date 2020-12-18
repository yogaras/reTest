package com.estimulo.logistics.production.to;

public class WorkSiteLog {
	private String workOrderNo;
	private String itemCode;
	private String itemName;
	private String reaeson;
	private String workSiteName;
	private String workDate;
	private String productionProcessCode;
	private String productionProcessName;
	
	
	public String getProductionProcessCode() {
		return productionProcessCode;
	}
	public void setProductionProcessCode(String productionProcessCode) {
		this.productionProcessCode = productionProcessCode;
	}
	public String getProductionProcessName() {
		return productionProcessName;
	}
	public void setProductionProcessName(String productionProcessName) {
		this.productionProcessName = productionProcessName;
	}
	public String getReaeson() {
		return reaeson;
	}
	public void setReaeson(String reaeson) {
		this.reaeson = reaeson;
	}
	
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
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
	public String getWorkSiteName() {
		return workSiteName;
	}
	public void setWorkSiteName(String workSiteName) {
		this.workSiteName = workSiteName;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
}
