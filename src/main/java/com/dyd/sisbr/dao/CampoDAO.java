package com.dyd.sisbr.dao;

import java.util.List;

import com.dyd.sisbr.model.Campo;

public interface CampoDAO {

	public List<Campo> selectAllCampos();
	
	public List<Campo> selectCampos(Campo campo);
	
	public List<String> selectDetalleCampos(Campo campo);
	
}
