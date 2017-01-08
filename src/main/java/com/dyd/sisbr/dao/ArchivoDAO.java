package com.dyd.sisbr.dao;

import com.dyd.sisbr.model.Archivo;

public interface ArchivoDAO {

	public void insertArchivo(Archivo archivo);
	
	public Archivo selectArchivo(int idArchivo);
	
	public Archivo selectTextoArchivo(int idArchivo);
}
