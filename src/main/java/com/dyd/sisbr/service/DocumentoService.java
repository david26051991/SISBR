package com.dyd.sisbr.service;

import java.io.File;
import java.util.List;

import com.dyd.sisbr.model.Documento;

public interface DocumentoService {

	public void guardarDocumentos(List<Documento> listaDocumentos);
	public void guardarDocumento(Documento documento);
	public List<Documento> obtenerDocumentos();
	public void actualizarDocumento(Documento documento);
	public List<Documento> selectDocumentos(Documento documento);
	public int obtenerCantidadDocumentos();
	public Documento obtenerDocumentoPorNombre(String nombre);
	public List<Documento> obtenerDocumentosPorClase(int idClase, int anioIni, int anioFin);
	public List<File> obtenerArchivosExportar(List<Integer> listaId);
}
