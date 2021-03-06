package com.dyd.sisbr.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyd.sisbr.dao.DocumentoDAO;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.service.DocumentoService;

@Service
public class DocumentoServiceImpl implements DocumentoService{

	@Autowired
	private DocumentoDAO documentoDAO;
	
	@Override
	public void guardarDocumentos(List<Documento> listaDocumentos) {
		if(listaDocumentos != null){
			for(Documento documento : listaDocumentos){
				documentoDAO.insertDocumento(documento);
			}
		}
	}
	
	@Override
	public void guardarDocumento(Documento documento) {
		documentoDAO.insertDocumento(documento);
	}

	@Override
	public List<Documento> obtenerDocumentos() {
		return documentoDAO.selectAllDocumento();
	}
	
	@Override
	public List<Documento> selectDocumentos(Documento documento) {
		return documentoDAO.selectDocumentos(documento);
	}
	
	@Override
	public int obtenerCantidadDocumentos() {
		return documentoDAO.selectCantidadDocumentos();
	}
	
	@Override
	public void actualizarDocumento(Documento documento) {
		documentoDAO.updateDocumento(documento);
	}
	
	@Override
	public Documento obtenerDocumentoPorNombre(String nombre) {
		Documento documento = new Documento();
		documento.setNombre(nombre);
		List<Documento> lista =  documentoDAO.selectDocumentos(documento);
		return lista.isEmpty() ? null : lista.get(0);
	}

	@Override
	public List<Documento> obtenerDocumentosPorClase(int idClase, int anioIni, int anioFin) {
		Documento documento = new Documento();
		documento.setIdClase(idClase);
		documento.setAnioIni(anioIni);
		documento.setAnioFin(anioFin);
		return documentoDAO.selectDocumentos(documento);
	}
	
	@Override
	public List<File> obtenerArchivosExportar(List<Integer> listaId){
		List<File> listaFile = new ArrayList<>();
		for(Integer idDocumento : listaId){
			Documento doc = documentoDAO.selectDocumentoByID(idDocumento);
			listaFile.add(new File(doc.getPath()));
		}
		return listaFile;
	}
}
