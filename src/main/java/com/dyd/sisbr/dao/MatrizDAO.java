package com.dyd.sisbr.dao;

import java.util.List;

import com.dyd.sisbr.model.Matriz;

public interface MatrizDAO {

	public void insertMatriz(Matriz matriz);
	
	public List<Matriz> selectPalabraByDocumento(int idDocumento);
}
