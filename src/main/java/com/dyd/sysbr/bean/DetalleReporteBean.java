package com.dyd.sysbr.bean;

import java.util.Map;

public class DetalleReporteBean {

	private String nombreClase;
	private Map<String,Integer> mapCantResoluciones;
	
	public String getNombreClase() {
		return nombreClase;
	}
	public void setNombreClase(String nombreClase) {
		this.nombreClase = nombreClase;
	}
	public Map<String, Integer> getMapCantResoluciones() {
		return mapCantResoluciones;
	}
	public void setMapCantResoluciones(Map<String, Integer> mapCantResoluciones) {
		this.mapCantResoluciones = mapCantResoluciones;
	}
}
