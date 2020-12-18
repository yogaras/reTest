package com.estimulo.logistics.production.to;

import com.estimulo.base.to.BaseTO;

public class ProductionResultManagementTO extends BaseTO {
	 private String productionResultNo;
	 private String workInstructionNo;
	 private String description;
	 private String productionDate;
	 private String itemCode;
	 private String unitOfProductionResult;
	 private String productionAmount;
	 private String itemName;

	 public String getProductionResultNo() { 
		 return productionResultNo;
	 }
	 public void setProductionResultNo(String productionResultNo) { 
		 this.productionResultNo = productionResultNo;
	 }
	 public String getWorkInstructionNo() { 
		 return workInstructionNo;
	 }
	 public void setWorkInstructionNo(String workInstructionNo) { 
		 this.workInstructionNo = workInstructionNo;
	 }
	 public String getDescription() { 
		 return description;
	 }
	 public void setDescription(String description) { 
		 this.description = description;
	 }
	 public String getProductionDate() { 
		 return productionDate;
	 }
	 public void setProductionDate(String productionDate) { 
		 this.productionDate = productionDate;
	 }
	 public String getItemCode() { 
		 return itemCode;
	 }
	 public void setItemCode(String itemCode) { 
		 this.itemCode = itemCode;
	 }
	 public String getUnitOfProductionResult() { 
		 return unitOfProductionResult;
	 }
	 public void setUnitOfProductionResult(String unitOfProductionResult) { 
		 this.unitOfProductionResult = unitOfProductionResult;
	 }
	 public String getProductionAmount() { 
		 return productionAmount;
	 }
	 public void setProductionAmount(String productionAmount) { 
		 this.productionAmount = productionAmount;
	 }
	 public String getItemName() { 
		 return itemName;
	 }
	 public void setItemName(String itemName) { 
		 this.itemName = itemName;
	 }
}