package com.dyd.sisbr.model;

public class Archivo {

	private int idArchivo;
	private byte[] datos;
	private String nombre;
	private String texto;
	private String path;
	
	public int getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(int idArchivo) {
		this.idArchivo = idArchivo;
	}
	public byte[] getDatos() {
		return datos;
	}
	public void setDatos(byte[] datos) {
		this.datos = datos;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
