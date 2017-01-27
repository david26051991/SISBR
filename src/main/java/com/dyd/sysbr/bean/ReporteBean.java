package com.dyd.sysbr.bean;

import java.io.Serializable;
import java.util.List;

public class ReporteBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<String> listaCabeceraFechas;
	private List<DetalleReporteBean> listaDetalleCantidad;
	
	public List<String> getListaCabeceraFechas() {
		return listaCabeceraFechas;
	}
	public void setListaCabeceraFechas(List<String> listaCabeceraFechas) {
		this.listaCabeceraFechas = listaCabeceraFechas;
	}
	public List<DetalleReporteBean> getListaDetalleCantidad() {
		return listaDetalleCantidad;
	}
	public void setListaDetalleCantidad(List<DetalleReporteBean> listaDetalleCantidad) {
		this.listaDetalleCantidad = listaDetalleCantidad;
	}
	
	
	
}
