package com.estimulo.logistics.production.to;

import java.util.ArrayList;

import com.estimulo.base.to.BaseTO;

public class MrpGatheringTO extends BaseTO {

	private String mrpGatheringNo;
	private String orderOrProductionStatus;
	private String itemCode;
	private String itemName;
	private String unitOfMrpGathering;
	private String claimDate;
	private String dueDate;
	private int necessaryAmount;
	private ArrayList<MrpTO> mrpTOList;

	public String getMrpGatheringNo() {
		return mrpGatheringNo;
	}

	public void setMrpGatheringNo(String mrpGatheringNo) {
		this.mrpGatheringNo = mrpGatheringNo;
	}

	public String getOrderOrProductionStatus() {
		return orderOrProductionStatus;
	}

	public void setOrderOrProductionStatus(String orderOrProductionStatus) {
		this.orderOrProductionStatus = orderOrProductionStatus;
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

	public String getUnitOfMrpGathering() {
		return unitOfMrpGathering;
	}

	public void setUnitOfMrpGathering(String unitOfMrpGathering) {
		this.unitOfMrpGathering = unitOfMrpGathering;
	}

	public String getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(String claimDate) {
		this.claimDate = claimDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public int getNecessaryAmount() {
		return necessaryAmount;
	}

	public void setNecessaryAmount(int necessaryAmount) {
		this.necessaryAmount = necessaryAmount;
	}

	public ArrayList<MrpTO> getMrpTOList() {
		return mrpTOList;
	}

	public void setMrpTOList(ArrayList<MrpTO> mrpTOList) {
		this.mrpTOList = mrpTOList;
	}

}