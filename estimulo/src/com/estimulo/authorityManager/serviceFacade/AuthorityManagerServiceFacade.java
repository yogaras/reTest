package com.estimulo.authorityManager.serviceFacade;

import javax.servlet.ServletContext;

import com.estimulo.authorityManager.exception.IdNotFoundException;
import com.estimulo.authorityManager.exception.PwMissMatchException;
import com.estimulo.authorityManager.exception.PwNotFoundException;
import com.estimulo.hr.to.EmpInfoTO;

public interface AuthorityManagerServiceFacade {

	public EmpInfoTO accessToAuthority(String companyCode, String workplaceCode, String inputId, String inputPassWord)
			throws IdNotFoundException, PwMissMatchException, PwNotFoundException;

	public String getUserMenuCode(String workplaceCode, String deptCode, String positionCode,
			ServletContext application);

}
