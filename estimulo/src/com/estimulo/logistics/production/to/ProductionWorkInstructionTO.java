package com.estimulo.logistics.production.to;

import com.estimulo.base.to.BaseTO;

public class ProductionWorkInstructionTO extends BaseTO  {
	 private int workInstructionAmount;
	 private String workInstructionNo;
	 private String description;
	 private String itemCode;
	 private String productionStatus;
	 private String instructionDate;
	 private String mrpGatheringNo;
	 private String itemName;
	 private String unitOfWorkInstruction;

	 public int getWorkInstructionAmount() { 
		 return workInstructionAmount;
	 }
	 public void setWorkInstructionAmount(int workInstructionAmount) { 
		 this.workInstructionAmount = workInstructionAmount;
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
	 public String getItemCode() { 
		 return itemCode;
	 }
	 public void setItemCode(String itemCode) { 
		 this.itemCode = itemCode;
	 }
	 public String getProductionStatus() { 
		 return productionStatus;
	 }
	 public void setProductionStatus(String productionStatus) { 
		 this.productionStatus = productionStatus;
	 }
	 public String getInstructionDate() { 
		 return instructionDate;
	 }
	 public void setInstructionDate(String instructionDate) { 
		 this.instructionDate = instructionDate;
	 }
	 public String getMrpGatheringNo() { 
		 return mrpGatheringNo;
	 }
	 public void setMrpGatheringNo(String mrpGatheringNo) { 
		 this.mrpGatheringNo = mrpGatheringNo;
	 }
	 public String getItemName() { 
		 return itemName;
	 }
	 public void setItemName(String itemName) { 
		 this.itemName = itemName;
	 }
	 public String getUnitOfWorkInstruction() { 
		 return unitOfWorkInstruction;
	 }
	 public void setUnitOfWorkInstruction(String unitOfWorkInstruction) { 
		 this.unitOfWorkInstruction = unitOfWorkInstruction;
	 }
}