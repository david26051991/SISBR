package com.dyd.sisbr.model;

import java.io.Serializable;

public class Stopword implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int idstopword;
	private String nombre;
	private String tipo;
	
	public int getIdstopword() {
		return idstopword;
	}
	public void setIdstopword(int idstopword) {
		this.idstopword = idstopword;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
