package com.dyd.sisbr.model;

public class Matriz {

	private int idDocumento;
	private int idPalabraClave;
	private int frecuencia;
	private Documento documento;
	private PalabraClave token;
	private double peso;
	
	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public int getIdPalabraClave() {
		return idPalabraClave;
	}

	public void setIdPalabraClave(int idPalabraClave) {
		this.idPalabraClave = idPalabraClave;
	}

	public int getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(int frecuencia) {
		this.frecuencia = frecuencia;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public PalabraClave getToken() {
		return token;
	}

	public void setToken(PalabraClave token) {
		this.token = token;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

}
