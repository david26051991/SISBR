package com.dyd.sisbr.dao;

import java.util.List;
import java.util.Map;

import com.dyd.sisbr.model.Documento;

public interface DocumentoDAO {

	public void insertDocumento(Documento documento);
	
	public List<Documento> selectAllDocumento();
	
	public void updateDocumento(Documento documento);
	
	public List<Documento> selectDocumentos(Documento documento);
	
	public int selectCantidadDocumentosTotal();
	
	public List<Map<String, String>> selectCantidadDocumentos(Documento documento);
	
	public Documento selectDocumentoByID(int idDocumento);

}
