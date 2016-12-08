package com.dyd.sisbr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyd.sisbr.dao.CampoDAO;
import com.dyd.sisbr.model.Campo;
import com.dyd.sisbr.service.CampoService;

@Service
public class CampoServiceImpl  implements CampoService{

	@Autowired
	private CampoDAO campoDAO;
	
	@Override
	public List<Campo> obtenerCampos() {
		return campoDAO.selectAllCampos();
	}

}
