package com.dyd.sisbr.service;

import java.util.List;

import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.PalabraClave;

public interface MatrizService {

	public void guardarMatriz(List<Documento> listaDocumentos);
	
	public List<PalabraClave> obtenerListaMatrizPorDocumento(int idDocumento);
}
