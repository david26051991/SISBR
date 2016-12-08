package com.dyd.sisbr.model;

import java.io.Serializable;

public class Modelo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int idModelo;
	private byte[] datosModelo;
	private byte[] estructura;
	private String estado;
	
	public int getIdModelo() {
		return idModelo;
	}
	public void setIdModelo(int idModelo) {
		this.idModelo = idModelo;
	}
	public byte[] getDatosModelo() {
		return datosModelo;
	}
	public void setDatosModelo(byte[] datosModelo) {
		this.datosModelo = datosModelo;
	}
	public byte[] getEstructura() {
		return estructura;
	}
	public void setEstructura(byte[] estructura) {
		this.estructura = estructura;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
