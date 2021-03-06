package com.dyd.sisbr.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.stereotype.Service;

import com.dyd.sisbr.model.PalabraClave;
import com.dyd.sisbr.service.PreprocesadorService;
import com.dyd.sisbr.util.Utils;


@Service
public class PreprocesadorServiceImpl implements PreprocesadorService{

	private HashSet<String> listaUnica;
	
//	public void cargarListaStopWords(File archivo1, File archivo2){
//		listaStopWords = Utils.extraerPalabrasJSON(archivo1);
//		listaStopWordsComp = Utils.extraerPalabrasJSON(archivo2);
//	}
	
	public void limpiarListaUnica(){
		if(listaUnica != null){
			listaUnica.clear();
		}
	}
	
	@Override
	public List<PalabraClave> preprocesamiento(File file){
		
//		System.out.println("COMIENZA PREPRECESAMIENTO: "+file.getName());
		//leer los documentos
		String textFromPage = Utils.obtenerTextoPDF(file.getPath());
		//Identificar secciones del documento
		if(textFromPage == null || textFromPage.isEmpty())
			return null;
		//eliminacion de stop words
		textFromPage = Utils.quitarEspacios(textFromPage);
		textFromPage = Utils.quitarTildes(textFromPage);
		textFromPage = Utils.quitarCaracteresEspeciales(textFromPage);
		textFromPage  = textFromPage.toLowerCase();
		textFromPage = borrarStopWords_compuestos(textFromPage);
		textFromPage = borrarStopWords(textFromPage);
		
		StringTokenizer tokens = new StringTokenizer(textFromPage, Utils.SIGNOS_PUNTUACION);
		
		//identificacion de palabras por secciones
		List<PalabraClave> lista = new ArrayList<PalabraClave>();
		agregarTokenSeccion(lista, tokens, "visto", 1);			//seccion Titulo
		agregarTokenSeccion(lista, tokens, "considerando", 2);	//seccion Visto
		agregarTokenSeccion(lista, tokens, "resuelve", 3);		//seccion Considerando
		agregarTokenSeccion(lista, tokens, "atentamente", 4);	//seccion Se resuelve
		
		//stemming
		lematizarTokens(lista);
		
		//agregar a lista unica
		agregarListaUnica(lista);
		
		//reduccion de dimensionalidad
	    
		//se quitan palabras repetidas
		HashSet<String> listaUniqDoc = new HashSet<String>();
		
		for(PalabraClave token : lista){
			listaUniqDoc.add(token.getRaiz());
		}
		lista.clear();
		Iterator<String> it = listaUniqDoc.iterator();
		while(it.hasNext()){
			PalabraClave t = new PalabraClave();
			t.setRaiz(it.next());
			lista.add(t);
		}
		
//		for(PalabraClave token : lista){
//			System.out.println(token.getRaiz() + " " + token.getSeccion()+",");
//		}
		
		return lista;
	}
	
	@Override
	public List<PalabraClave> preprocesarConsulta(String consulta){
		String textoConsulta = Utils.quitarEspacios(consulta);
		textoConsulta = Utils.quitarTildes(textoConsulta);
		textoConsulta = Utils.quitarCaracteresEspeciales(textoConsulta);
		textoConsulta  = textoConsulta.toLowerCase();
		textoConsulta = borrarStopWords_compuestos(textoConsulta);
		textoConsulta = borrarStopWords(textoConsulta);
		
		StringTokenizer tokens = new StringTokenizer(textoConsulta, Utils.SIGNOS_PUNTUACION);
		
		List<PalabraClave> listaConsulta = new ArrayList<PalabraClave>();
		
		while (tokens.hasMoreTokens()) {
			String palabra = tokens.nextToken().trim();
			if(palabra.length()>2){
				PalabraClave token = new PalabraClave();
				token.setRaiz(palabra);
				listaConsulta.add(token);	
			}		
		}
		
		//stemming
		lematizarTokens(listaConsulta);
		return listaConsulta;
	}
	
	
	private void agregarListaUnica(List<PalabraClave> lista){
		if(listaUnica == null){
			listaUnica = new HashSet<String>();
		}
		for(PalabraClave token: lista){
			listaUnica.add(token.getRaiz());
		}
	}
	
	private void agregarTokenSeccion(List<PalabraClave> lista, StringTokenizer tokens, String seccion, int numSeccion){
				
		while (tokens.hasMoreTokens()) {
			String palabra = tokens.nextToken().trim();
			if(!palabra.equals(seccion)){
				//if(palabra.length()>2 && numSeccion != 3 && numSeccion != 4){
				if(palabra.length()>2){
					PalabraClave token = new PalabraClave();
					token.setRaiz(palabra);
					token.setSeccion(numSeccion);
					lista.add(token);	
				}
			} else{
				break;	
			}			
		}
	}
	
	@Override
	public String borrarStopWords(String texto) {
//		String[] listaStopWords = Utils.extraerPalabrasJSON(this.getClass().getClassLoader().getResourceAsStream("stopword.json"));
		String[] listaStopWords = Utils.extraerPalabrasJSON(new File(getClass().getClassLoader().getResource("stopword.json").getFile()));
		for(String palabra: listaStopWords){
			texto = texto.replaceAll(" "+palabra+" ", " ");				
		}		
		return texto;
	}
	
	@Override
	public String borrarStopWords_compuestos(String texto) {
		String[] listaStopWordsComp = Utils.extraerPalabrasJSON(new File(getClass().getClassLoader().getResource("stopword2.json").getFile()));
		for(String palabra_compuesta: listaStopWordsComp){
			texto = texto.replaceAll(palabra_compuesta, "");
		}		
		return texto;
	}
	
	@Override
	public void lematizarTokens(List<PalabraClave> lista) {
		LematizadorServiceImpl lem = new LematizadorServiceImpl();
		for (PalabraClave token : lista) {
			token.setRaiz(lem.stemm(token.getRaiz()));
		}
	}
	
	public List<PalabraClave> getListaUnicaToken(){
		if(this.listaUnica != null){
			List<PalabraClave> lista = new ArrayList<PalabraClave>();
			Iterator<String> it = this.listaUnica.iterator();
			while(it.hasNext()){
				PalabraClave token = new PalabraClave();
				token.setRaiz(it.next());
				lista.add(token);
			}
			return lista;
		}
		return null;
	}
	
	
}
