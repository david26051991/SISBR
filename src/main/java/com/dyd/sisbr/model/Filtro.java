package com.dyd.sisbr.model;

import java.io.Serializable;

public class Filtro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int campo;
	private String valor;
	private int condicion;
	public int getCampo() {
		return campo;
	}
	public void setCampo(int campo) {
		this.campo = campo;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public int getCondicion() {
		return condicion;
	}
	public void setCondicion(int condicion) {
		this.condicion = condicion;
	}
	
	
}
