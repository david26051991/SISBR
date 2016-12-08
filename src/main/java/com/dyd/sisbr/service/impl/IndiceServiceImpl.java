package com.dyd.sisbr.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyd.sisbr.dao.CampoDAO;
import com.dyd.sisbr.dao.IndiceDAO;
import com.dyd.sisbr.model.Campo;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.Indice;
import com.dyd.sisbr.service.IndiceService;
import com.dyd.sisbr.util.Utils;

@Service
public class IndiceServiceImpl implements IndiceService {

	private String texto;
	
	@Autowired
	private CampoDAO campoDAO;
	
	@Autowired
	private IndiceDAO indiceDAO;
	
	@Override
	public void guardarListaIndices(List<Indice> listaIndices){
		if(listaIndices != null){
			for(Indice indice: listaIndices){
				indiceDAO.insertIndice(indice);
			}
		}
	}
	
	@Override
	public List<Indice> identificarAtributos(Documento documento) {	
		String titulo = "";
		int anio = 0;
		String fulltexto = Utils.obtenerTextoPDF(documento.getPath());
		fulltexto = Utils.quitarEspacios(fulltexto);
		fulltexto = Utils.quitarTildes(fulltexto);
		fulltexto  = fulltexto.toLowerCase();
		setTexto(fulltexto.toLowerCase());
		List<Campo> listaCampos = campoDAO.selectAllCampos();
		
		List<Indice> listaIndice = new ArrayList<Indice>();
		
		for (Campo campo : listaCampos) {
			List<Indice> listaIndices = null;
			if(campo.getTipo() == Campo.TIPO_CAMPO_EXP_REGULAR){
				listaIndices = obtenerCamposFormato(campo);
				if(campo.getIdCampo() == 1 && titulo.isEmpty()){
					titulo = extraerTitulo(listaIndices);
				} else if(campo.getIdCampo() == 2 && anio == 0){
					anio = extraerAnio(listaIndices);
				}
			}
//			else if(campo.getTipo() == Campo.TIPO_LISTA){
//				listaIndices = obtenerCamposLista(campo);
//			}
			
			if(listaIndices != null){
				for(Indice indice : listaIndices){
					indice.setIdCampo(campo.getIdCampo());
					indice.setIdDocumento(documento.getIdDocumento());
//					indice.setDocumento(documento);
					listaIndice.add(indice);
				}	
				
			}
		}
		
		documento.setTitulo(titulo);
		documento.setAnio(anio);
		System.out.println("Identificando atributos, documento: "+titulo);
		
		return listaIndice;
	}
	
	@Override
	public List<Indice> identificarAtributos(String fulltexto){	
		fulltexto = Utils.quitarEspacios(fulltexto);
		fulltexto = Utils.quitarTildes(fulltexto);
		fulltexto  = fulltexto.toLowerCase();
		setTexto(fulltexto.toLowerCase());
		List<Campo> listaCampos = campoDAO.selectAllCampos();
		
		List<Indice> listaIndices = new ArrayList<Indice>();
		
		for (Campo campo : listaCampos) {
			List<Indice> indices = new ArrayList<Indice>();
			if(campo.getTipo() == Campo.TIPO_CAMPO_EXP_REGULAR){
				indices = obtenerCamposFormato(campo);
			}
//			else if(campo.getTipo() == Campo.TIPO_LISTA){
//				indices = obtenerCamposLista(campo);
//			}
			listaIndices.addAll(indices);
		}
		return listaIndices;
	}
	
	public List<Indice> obtenerCamposFormato(Campo campo) {
		List<Indice> listaIndices = new ArrayList<Indice>();
		Pattern p = Pattern.compile(campo.getFormato());
		Matcher m = p.matcher(getTexto());
		while (m.find()) {
			String descripcion = m.group();
			Indice indice = new Indice();
//			indice.setCampo(campo);
			indice.setDescripcion(descripcion);
//			indice.setInicio(m.start());
//			indice.setFin(m.end());
			listaIndices.add(indice);
//			setTexto(getTexto().replaceAll(campo.getFormato(), " "));
		}
		return listaIndices;
	}

	public List<Indice> obtenerCamposLista(Campo campo) {
		List<Indice> listaIndices = new ArrayList<Indice>();
		List<String> listaDellate = campoDAO.selectDetalleCampos(campo);
		String formato = "\\b(";
		for (String detalle : listaDellate) {
			formato += detalle + "|";
		}
		formato = formato.substring(0, formato.length() - 1) + ")\\b";
		Pattern p = Pattern.compile(formato);
		Matcher m = p.matcher(getTexto());
		while (m.find()) {
			Indice indice = new Indice();
//			indice.setCampo(campo);
			indice.setDescripcion(m.group());
//			indice.setInicio(m.start());
//			indice.setFin(m.end());
			listaIndices.add(indice);
		}
		return listaIndices;
	}
	
	public List<Indice> obtenerIndicesByIdDocumentos(List<Documento> listaDocumentos){
		List<Integer> lista = new ArrayList<>();
		for(Documento doc: listaDocumentos)
			lista.add(doc.getIdDocumento());
		return indiceDAO.selectIndicesByIdDocumento(lista);
	}
	
	public List<Indice> mapearIndicesPorLista(List<Indice> listaIndices, int idDocumento){
		List<Indice> lista = new ArrayList<>();
		for(Indice indice: listaIndices){
			if(indice.getIdDocumento() == idDocumento)
				lista.add(indice);
		}
		return lista;
	}
	
	private String extraerTitulo(List<Indice> listaIndices){
		String titulo = "";
		if(listaIndices!= null && listaIndices.size()>0){
			titulo = "RESOLUCION RECTORAL "+listaIndices.get(0).getDescripcion().toUpperCase();
		}
		return titulo;
	}
	
	private int extraerAnio(List<Indice> listaIndices){
		int anio = 0;
		if(listaIndices!= null && listaIndices.size()>0){
			anio = Integer.parseInt((listaIndices.get(0).getDescripcion().split(" "))[4]);
		}
		return anio;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

}
