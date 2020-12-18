package com.estimulo.logistics.purchase.dao;

import java.util.ArrayList;

import com.estimulo.logistics.purchase.to.BomDeployTO;
import com.estimulo.logistics.purchase.to.BomInfoTO;
import com.estimulo.logistics.purchase.to.BomTO;

public interface BomDAO {
	
	
	public ArrayList<BomDeployTO> selectBomDeployList(String deployCondition, String itemCode,String itemClassificationCondition);
	
	public ArrayList<BomInfoTO> selectBomInfoList(String parentItemCode);
	
	public ArrayList<BomInfoTO> selectAllItemWithBomRegisterAvailable();
	
	public void insertBom(BomTO TO);
	
	public void updateBom(BomTO TO);
	
	public void deleteBom(BomTO TO);
	
}
