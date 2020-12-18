package com.estimulo.basicInfo.to;

import com.estimulo.base.to.BaseTO;

public class DepartmentTO extends BaseTO  {
	 private String workplaceName;
	 private String deptName;
	 private String deptCode;
	 private String workplaceCode;
	 private String companyCode;
	 private String deptEndDate;
	 private String deptStartDate;

	 public String getWorkplaceName() { 
		 return workplaceName;
	 }
	 public void setWorkplaceName(String workplaceName) { 
		 this.workplaceName = workplaceName;
	 }
	 public String getDeptName() { 
		 return deptName;
	 }
	 public void setDeptName(String deptName) { 
		 this.deptName = deptName;
	 }
	 public String getDeptCode() { 
		 return deptCode;
	 }
	 public void setDeptCode(String deptCode) { 
		 this.deptCode = deptCode;
	 }
	 public String getWorkplaceCode() { 
		 return workplaceCode;
	 }
	 public void setWorkplaceCode(String workplaceCode) { 
		 this.workplaceCode = workplaceCode;
	 }
	 public String getCompanyCode() { 
		 return companyCode;
	 }
	 public void setCompanyCode(String companyCode) { 
		 this.companyCode = companyCode;
	 }
	 public String getDeptEndDate() { 
		 return deptEndDate;
	 }
	 public void setDeptEndDate(String deptEndDate) { 
		 this.deptEndDate = deptEndDate;
	 }
	 public String getDeptStartDate() { 
		 return deptStartDate;
	 }
	 public void setDeptStartDate(String deptStartDate) { 
		 this.deptStartDate = deptStartDate;
	 }
}