package com.dyd.sisbr.model;

import java.io.Serializable;

public class PalabraClave implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int idPalabraClave;
	private int idDocumento;
	private String raiz;
	private int frecuencia;
	private int cantDoc;
	private double tfidf;
	private int seccion;
	private int tipo;
	
	public int getIdPalabraClave() {
		return idPalabraClave;
	}
	public void setIdPalabraClave(int idPalabraClave) {
		this.idPalabraClave = idPalabraClave;
	}
	public int getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getRaiz() {
		return raiz;
	}
	public void setRaiz(String raiz) {
		this.raiz = raiz;
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
	public double getTfidf() {
		return tfidf;
	}
	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}
	public int getSeccion() {
		return seccion;
	}
	public void setSeccion(int seccion) {
		this.seccion = seccion;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
}
