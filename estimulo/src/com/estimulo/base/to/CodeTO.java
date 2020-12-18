package com.estimulo.base.to;

import java.util.ArrayList;

public class CodeTO extends BaseTO {
	
	private String divisionCodeNo;
	private String codeType;
	private String divisionCodeName;
	private String codeChangeAvailable;
	private String description;
	private ArrayList<CodeDetailTO> codeDetailTOList;
	
	public String getDivisionCodeNo() {
		return divisionCodeNo;
	}
	public void setDivisionCodeNo(String divisionCodeNo) {
		this.divisionCodeNo = divisionCodeNo;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getDivisionCodeName() {
		return divisionCodeName;
	}
	public void setDivisionCodeName(String divisionCodeName) {
		this.divisionCodeName = divisionCodeName;
	}
	public String getCodeChangeAvailable() {
		return codeChangeAvailable;
	}
	public void setCodeChangeAvailable(String codeChangeAvailable) {
		this.codeChangeAvailable = codeChangeAvailable;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<CodeDetailTO> getCodeDetailTOList() {
		return codeDetailTOList;
	}
	public void setCodeDetailTOList(ArrayList<CodeDetailTO> codeDetailTOList) {
		this.codeDetailTOList = codeDetailTOList;
	}
	
	
}