package com.dyd.sisbr.dao;

import java.util.List;

import com.dyd.sisbr.model.Documento;

public interface DocumentoDAO {

	public void insertDocumento(Documento documento);
	
	public List<Documento> selectAllDocumento();
	
	public void updateDocumento(Documento documento);
	
	public List<Documento> selectDocumentos(Documento documento);
	
	public int selectCantidadDocumentos();
	
	public Documento selectDocumentoByID(int idDocumento);

}
