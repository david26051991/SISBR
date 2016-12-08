package com.dyd.sisbr.model;

import java.io.Serializable;

public class Clase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int idClase;
	private String nombre;

	public int getIdClase() {
		return idClase;
	}

	public void setIdClase(int idClase) {
		this.idClase = idClase;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
