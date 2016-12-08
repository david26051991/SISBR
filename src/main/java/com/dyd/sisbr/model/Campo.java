package com.dyd.sisbr.model;

import java.io.Serializable;

public class Campo implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private int idCampo;
	private String nombre;
	private int tipo;
	private String formato;
	
	
	public static final int TIPO_CAMPO_EXP_REGULAR = 1;

	public int getIdCampo() {
		return idCampo;
	}

	public void setIdCampo(int idCampo) {
		this.idCampo = idCampo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

}
