package com.dyd.sisbr.service;

import java.util.List;

import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.Indice;

public interface IndiceService {

	public void guardarListaIndices(List<Indice> listaIndices);
	
	public List<Indice> identificarAtributos(Documento documento);
	
	public List<Indice> identificarAtributos(String fulltexto);
	
	public List<Indice> obtenerIndicesByIdDocumentos(List<Documento> listaDocumentos);
	
	public List<Indice> mapearIndicesPorLista(List<Indice> listaIndices, int idDocumento);
	
}
