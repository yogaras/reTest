package com.estimulo.base.dao;

import java.util.ArrayList;

import com.estimulo.base.to.CodeTO;

public interface CodeDAO {

	public ArrayList<CodeTO> selectCodeList();

	public void insertCode(CodeTO codeTO);

	public void updateCode(CodeTO codeTO);

	public void deleteCode(CodeTO codeTO);

}
