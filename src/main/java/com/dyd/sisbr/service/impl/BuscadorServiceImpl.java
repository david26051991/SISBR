package com.dyd.sisbr.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyd.sisbr.dao.ClaseDAO;
import com.dyd.sisbr.model.Clase;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.Indice;
import com.dyd.sisbr.model.PalabraClave;
import com.dyd.sisbr.service.BuscadorService;
import com.dyd.sisbr.service.DocumentoService;
import com.dyd.sisbr.service.IndiceService;
import com.dyd.sisbr.service.PalabraClaveService;
import com.dyd.sisbr.service.PreprocesadorService;
import com.dyd.sisbr.util.Utils;

@Service
public class BuscadorServiceImpl implements BuscadorService{

	@Autowired
	private ClaseDAO claseDAO;
	
	@Autowired
	private IndiceService indiceService;
	
	@Autowired
	private PreprocesadorService preprocesadorService;
	
	@Autowired
	private DocumentoService documentoService;
	
	@Autowired
	private PalabraClaveService palabraClaveService;
	
	@Override
	public List<Clase> obtenerClases(){
		return claseDAO.selectAllClases();
	}
			
	@Override
	public List<Documento> buscarDocumentos(String consulta, int idClase, int anioIni, int anioFin) {
		
		long ini = System.currentTimeMillis();
		List<Documento> listaDocumentos = documentoService.obtenerDocumentosPorClase(idClase, anioIni, anioFin);
		if(listaDocumentos == null || listaDocumentos.isEmpty()){
			return null;
		}
		
		if(!consulta.isEmpty()){
			System.out.println("lista documentos: " + (System.currentTimeMillis() - ini));
			ini = System.currentTimeMillis();
			List<PalabraClave> listaPalabras = palabraClaveService.obtenerPalabrasClavesByClase(idClase, anioIni, anioFin);
			System.out.println("lista palabras: " + (System.currentTimeMillis() - ini));
			
			ini = System.currentTimeMillis();
			List<Indice> listaIndices = indiceService.obtenerIndicesByIdDocumentos(listaDocumentos);
			System.out.println("lista indices: " + (System.currentTimeMillis() - ini));
			
			ini = System.currentTimeMillis();
			//mapear las palabras que corresponden a cada documento
			for(Documento doc: listaDocumentos){
				List<PalabraClave> listaPalabraxDoc = new ArrayList<PalabraClave>();
				for(PalabraClave token : listaPalabras){
					if(token.getIdDocumento() == doc.getIdDocumento()){
						listaPalabraxDoc.add(token);
					}
				}
//				List<Indice> indicesDocumento = indiceService.identificarAtributos(Utils.obtenerTextoPDF(doc.getPath()));
				List<Indice> indicesDocumento = indiceService.mapearIndicesPorLista(listaIndices, doc.getIdDocumento());
//				System.out.println("cantidad de indices: "+ indicesDocumento.size() + ", idDocumento: " + doc.getIdDocumento());
				for(Indice indice: indicesDocumento){
//					String[] palabrasIndice = indice.getDescripcion().split(" ");
//					for(String palabra: palabrasIndice){
//						if(!palabra.equals("de") && !palabra.equals("del")){
//							PalabraClave token = new PalabraClave();
//							token.setRaiz(palabra);
//							token.setTipo(1);
//							listaPalabraxDoc.add(token);	
//						}
//					}
					PalabraClave token = new PalabraClave();
					token.setRaiz(indice.getDescripcion());
					token.setTipo(1);//indica que es de tipo anotador
					listaPalabraxDoc.add(token);
				}			
				doc.setListaToken(listaPalabraxDoc);
			}
			System.out.println("identificacion de anotadores: " + (System.currentTimeMillis() - ini));
			
			ini = System.currentTimeMillis();
			//agregar a la lista los anotadores identificados
			//comentario temporal
//			for(Documento doc: listaDocumentos){
//				for(PalabraClave token : doc.getListaToken()){
//					if(token.getTipo() == 1){
//						token.setFrecuencia(Utils.calcularFrecuenciaPalabra(token.getRaiz(), doc));
//					}
//					token.setCantDoc(Utils.calcularFrecuenciaDocumentos(listaDocumentos, token.getRaiz()));
//				}
//			}
			//fin comentario temporal
			Map<String, Integer> mapTokenUnicos = new HashMap<>();
			for(Documento doc: listaDocumentos){
				for(PalabraClave token : doc.getListaToken()){
					mapTokenUnicos.put(token.getRaiz(), 0);
				}
			}
			
			for(String token : mapTokenUnicos.keySet()){
				int cantDoc = 0;
				for(Documento doc: listaDocumentos){
					for(PalabraClave tokenDoc : doc.getListaToken()){
						if(tokenDoc.getRaiz().equals(token)){
							cantDoc++;
							break;
						}
					}
				}
				mapTokenUnicos.put(token, cantDoc);
			}
			System.out.println("Cantidad de documentos: " + (System.currentTimeMillis() - ini));
			ini = System.currentTimeMillis();
			for(Documento doc: listaDocumentos){
				for(PalabraClave token : doc.getListaToken()){
					if(token.getTipo() == 1){
						token.setFrecuencia(Utils.calcularFrecuenciaPalabra(token.getRaiz(), doc));
					}
					token.setCantDoc(mapTokenUnicos.get(token.getRaiz()));
				}
			}
			
			System.out.println("frecuencia de palabras: " + (System.currentTimeMillis() - ini));
			
			ini = System.currentTimeMillis();
			List<PalabraClave> listaConsulta = preprocesadorService.preprocesarConsulta(consulta);
			System.out.println("preprocesar consulta: " + (System.currentTimeMillis() - ini));
			
			//agregar a la lista los anotadores identificados
			ini = System.currentTimeMillis();
			List<Indice> indicesConsulta = indiceService.identificarAtributos(consulta);
			for(Indice indice: indicesConsulta){
//				String[] palabrasIndice = indice.getDescripcion().split(" ");
//				for(String palabra: palabrasIndice){
//					if(!palabra.equals("de") && !palabra.equals("del")){
//						PalabraClave token = new PalabraClave();
//						token.setRaiz(palabra);
//						listaConsulta.add(token);	
//					}
//				}
				PalabraClave token = new PalabraClave();
				token.setRaiz(indice.getDescripcion());
				listaConsulta.add(token);
			}
			
			System.out.println("identificar atributos de consulta: " + (System.currentTimeMillis() - ini));
			
			Utils.calcularFrecuenciaPalabras(new Documento(listaConsulta));
			
			for(PalabraClave tokenQ: listaConsulta){
				tokenQ.setCantDoc(Utils.calcularFrecuenciaDocumentos(listaDocumentos, tokenQ.getRaiz()));
			}			
			ini = System.currentTimeMillis();
			for(Documento doc: listaDocumentos){
				double gradoSim = similitudConsultaDocumento(listaConsulta, doc, listaDocumentos.size());
				doc.setGradoSimilitud(gradoSim);
			}
			System.out.println("similitud consulta y documentos: " + (System.currentTimeMillis() - ini));
			
			listaDocumentos = eliminarSimilitudCero(listaDocumentos);
			
			return ordenarDocumentosSegunGradoSim(listaDocumentos);
			
		} else{
			return listaDocumentos;
		}
	}
	
	private List<Documento> eliminarSimilitudCero(List<Documento> listaDocumentos){
		List<Documento> listaFiltro = new ArrayList<>();
		for(Documento doc : listaDocumentos){
			if(doc.getGradoSimilitud() > 0.0){
				listaFiltro.add(doc);
			}
		}
		listaDocumentos.clear();
		return listaFiltro;
	}

	private List<Documento> ordenarDocumentosSegunGradoSim(List<Documento> listaDocumentos){
		
		Documento[] listaOrdenada = new Documento[listaDocumentos.size()];
		
		for(int i = 0; i < listaDocumentos.size(); i++){
			Documento doc2 = new Documento();
			doc2.setIdDocumento(listaDocumentos.get(i).getIdDocumento());
			doc2.setIdClase(listaDocumentos.get(i).getIdClase());
			doc2.setPath(listaDocumentos.get(i).getPath());
			doc2.setTitulo(listaDocumentos.get(i).getTitulo());
			doc2.setNombre(listaDocumentos.get(i).getNombre());
			doc2.setGradoSimilitud(listaDocumentos.get(i).getGradoSimilitud());
			doc2.setIdArchivo(listaDocumentos.get(i).getIdArchivo());
			listaOrdenada[i] = doc2;
		}
		if(listaOrdenada.length > 0){
			quicksort(listaOrdenada, 0, listaOrdenada.length-1);			
			return new ArrayList<>(Arrays.asList(listaOrdenada));
		} else {
			return new ArrayList<>();
		}
		
	}
	
	public static void quicksort(Documento[] A, int izq, int der) {

		Documento pivote = A[izq]; // tomamos primer elemento como pivote
		int i = izq; // i realiza la búsqueda de izquierda a derecha
		int j = der; // j realiza la búsqueda de derecha a izquierda
		Documento aux;

		while (i < j) { // mientras no se crucen las búsquedas
			while (A[i].getGradoSimilitud() >= pivote.getGradoSimilitud()
					&& i < j)
				i++; // busca elemento mayor que pivote
			while (A[j].getGradoSimilitud() < pivote.getGradoSimilitud())
				j--; // busca elemento menor que pivote
			if (i < j) { // si no se han cruzado
				aux = A[i]; // los intercambia
				A[i] = A[j];
				A[j] = aux;
			}
		}
		A[izq] = A[j]; // se coloca el pivote en su lugar de forma que tendremos
		A[j] = pivote; // los menores a su izquierda y los mayores a su derecha
		if (izq < j - 1)
			quicksort(A, izq, j - 1); // ordenamos subarray izquierdo
		if (j + 1 < der)
			quicksort(A, j + 1, der); // ordenamos subarray derecho
	}
	
	private double similitudConsultaDocumento(List<PalabraClave> consulta, Documento documento, int cantDoc){
		
		HashSet<String> listaUnica = new HashSet<>();
		HashMap<String, Double> mapConsulta = new HashMap<>();
		HashMap<String, Double> mapDocumento = new HashMap<>();
				
		for(PalabraClave tokenQ: consulta){
			listaUnica.add(tokenQ.getRaiz());
			if(cantDoc != 1 && tokenQ.getCantDoc() != cantDoc)
				mapConsulta.put(tokenQ.getRaiz(), Utils.calcularTFIDF(tokenQ.getFrecuencia(), tokenQ.getCantDoc(), cantDoc));
			else if(cantDoc != 1 && tokenQ.getCantDoc() == cantDoc)
				mapConsulta.put(tokenQ.getRaiz(), 1.0);
			else 
				mapConsulta.put(tokenQ.getRaiz(), 1.0);
		}
		
		for(PalabraClave tokenD: documento.getListaToken()){
			listaUnica.add(tokenD.getRaiz());
			if(cantDoc != 1 && tokenD.getCantDoc() != cantDoc)
				mapDocumento.put(tokenD.getRaiz(), Utils.calcularTFIDF(tokenD.getFrecuencia(), tokenD.getCantDoc(), cantDoc));
			else if(cantDoc != 1 && tokenD.getCantDoc() == cantDoc)
				mapDocumento.put(tokenD.getRaiz(), 1.0);
			else
				mapDocumento.put(tokenD.getRaiz(), 1.0);
		}
		
		Double[] vectorConsulta = new Double[listaUnica.size()];
		Double[] vectorDocumento = new Double[listaUnica.size()];
		
		//crear vector Consulta
		Iterator<String> iteratorQ = listaUnica.iterator();
		int ind = 0;
		while(iteratorQ.hasNext()){
			String tokenQ = iteratorQ.next();
			vectorConsulta[ind] = mapConsulta.get(tokenQ) != null ? mapConsulta.get(tokenQ) : 0.0;
			ind++;
		}
		
		//crear vector Documento
		Iterator<String> iteratorD = listaUnica.iterator();
		ind = 0;
		while(iteratorD.hasNext()){
			String tokenD = iteratorD.next();
			vectorDocumento[ind] = mapDocumento.get(tokenD) != null ? mapDocumento.get(tokenD) : 0.0;
			ind++;
		}
		
		double gradoSimilitud = Utils.similitudCoseno(vectorConsulta, vectorDocumento, listaUnica.size());
		
//		System.out.println("doc: "+documento.getNombre()+ ", grado: "+ new BigDecimal(gradoSimilitud));
		
		return gradoSimilitud;
	}
}
