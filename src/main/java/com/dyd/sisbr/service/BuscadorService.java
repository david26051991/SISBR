package com.dyd.sisbr.service;

import java.util.List;

import com.dyd.sisbr.model.Clase;
import com.dyd.sisbr.model.Documento;
import com.dyd.sysbr.bean.ReporteBean;

public interface BuscadorService {

	public List<Clase> obtenerClases();
	
	public List<Documento> buscarDocumentos(String consulta, int idClase, int anioIni, int anioFin);
	
	public ReporteBean generarReporte(List<Clase> listaClases, String fechaIni, String fechaFin);
}
