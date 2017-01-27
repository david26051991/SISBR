package com.dyd.sisbr.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyd.sisbr.dao.ArchivoDAO;
import com.dyd.sisbr.dao.DocumentoDAO;
import com.dyd.sisbr.model.Archivo;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.service.DocumentoService;
import com.dyd.sisbr.util.Utils;

@Service
public class DocumentoServiceImpl implements DocumentoService{

	@Autowired
	private DocumentoDAO documentoDAO;
	
	@Autowired
	private ArchivoDAO archivoDAO;
	
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
		return documentoDAO.selectCantidadDocumentosTotal();
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
	public Map<String, Integer> obtenerCantidadDocumentos(int idClase, int anioIni, int anioFin,
			int mesIni, int mesFin) {
		Documento documento = new Documento();
		documento.setIdClase(idClase);
		documento.setAnioIni(anioIni);
		documento.setAnioFin(anioFin);
		documento.setMesIni(mesIni);
		documento.setMesFin(mesFin);
		List<Map<String, String>> listMap = documentoDAO.selectCantidadDocumentos(documento);
		
		Map<String, Integer> mapFechas = new HashMap<String, Integer>();
		for(Map<String, String> map : listMap){
			mapFechas.put(map.get("fecha"), Utils.parseInt(map.get("cantidad")));
		}
		return mapFechas;
	}
	
	@Override
	public List<File> obtenerArchivosExportar(List<Integer> listaId){
		List<File> listaFile = new ArrayList<>();
		for(Integer idArchivo : listaId){
			Archivo arch = archivoDAO.selectArchivo(idArchivo);
			listaFile.add(new File(arch.getPath()));
		}
		return listaFile;
	}

	@Override
	public void guardarArchivo(Archivo archivo) {
		archivoDAO.insertArchivo(archivo);
	}

	@Override
	public Archivo obtenerArchivo(int idArchivo) {
		return archivoDAO.selectArchivo(idArchivo);
	}
	
	@Override
	public String obtenerTextArchivo(int idArchivo) {
		Archivo archivo = archivoDAO.selectTextoArchivo(idArchivo);
		return archivo != null ? archivo.getTexto() : null;
	}

	@Override
	public Documento obtenerDocumento(int idDocumento) {
		return documentoDAO.selectDocumentoByID(idDocumento);
	}
	
	
}
