package com.dyd.sisbr.model;

import java.io.Serializable;
import java.util.List;

public class Documento implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int idDocumento;
	private int idClase;
	private String nombre;
	private String path;
	private String titulo;
	private int anio;
	private String resumen;
	private double gradoSimilitud;
	private int anioIni;
	private int anioFin;
	
	private Clase clase;
	
	private List<PalabraClave> listaToken;

	public Documento(){}
	
	public Documento(List<PalabraClave> listaToken){
		this.listaToken = listaToken;
	}
	
	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}	

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Clase getClase() {
		return clase;
	}

	public void setClase(Clase clase) {
		this.clase = clase;
	}

	public List<PalabraClave> getListaToken() {
		return listaToken;
	}

	public void setListaToken(List<PalabraClave> listaToken) {
		this.listaToken = listaToken;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public double getGradoSimilitud() {
		return gradoSimilitud;
	}

	public void setGradoSimilitud(double gradoSimilitud) {
		this.gradoSimilitud = gradoSimilitud;
	}

	public int getAnioIni() {
		return anioIni;
	}

	public void setAnioIni(int anioIni) {
		this.anioIni = anioIni;
	}

	public int getAnioFin() {
		return anioFin;
	}

	public void setAnioFin(int anioFin) {
		this.anioFin = anioFin;
	}

}
