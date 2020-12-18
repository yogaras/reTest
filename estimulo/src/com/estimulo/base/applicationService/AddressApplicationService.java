package com.estimulo.base.applicationService;

import java.util.ArrayList;

import com.estimulo.base.to.AddressTO;

public interface AddressApplicationService {
		
	public ArrayList<AddressTO> getAddressList(String sidoName, String searchAddressType, String searchValue, String mainNumber);
	
}
