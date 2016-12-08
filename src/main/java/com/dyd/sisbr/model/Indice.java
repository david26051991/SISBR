package com.dyd.sisbr.model;

public class Indice {

	private int idIndice;
	private int idCampo;
	private String descripcion;
	private int idDocumento;
	private int frecuencia;
	private int cantDoc;
	
	public int getIdIndice() {
		return idIndice;
	}
	public void setIdIndice(int idIndice) {
		this.idIndice = idIndice;
	}
	public int getIdCampo() {
		return idCampo;
	}
	public void setIdCampo(int idCampo) {
		this.idCampo = idCampo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}
	public int getFrecuencia() {
		return frecuencia;
	}
	public void setFrecuencia(int frecuencia) {
		this.frecuencia = frecuencia;
	}
	public int getCantDoc() {
		return cantDoc;
	}
	public void setCantDoc(int cantDoc) {
		this.cantDoc = cantDoc;
	}
	
}
