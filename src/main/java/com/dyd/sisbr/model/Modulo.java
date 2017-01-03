package com.dyd.sisbr.model;

import java.util.List;

public class Modulo{

	private Integer idModulo;
	private String nombre;
	private String link;
	private Integer idPadre;
	private String estado;
	private List<Modulo> listaHijos;
	
	public Integer getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Integer idModulo) {
		this.idModulo = idModulo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Integer getIdPadre() {
		return idPadre;
	}
	public void setIdPadre(Integer idPadre) {
		this.idPadre = idPadre;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public List<Modulo> getListaHijos() {
		return listaHijos;
	}
	public void setListaHijos(List<Modulo> listaHijos) {
		this.listaHijos = listaHijos;
	}
	
}
