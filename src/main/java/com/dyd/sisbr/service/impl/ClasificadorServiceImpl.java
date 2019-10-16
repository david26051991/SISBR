package com.dyd.sisbr.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dyd.sisbr.dao.ClaseDAO;
import com.dyd.sisbr.dao.ModeloDAO;
import com.dyd.sisbr.model.Archivo;
import com.dyd.sisbr.model.Clase;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.Indice;
import com.dyd.sisbr.model.Modelo;
import com.dyd.sisbr.model.PalabraClave;
import com.dyd.sisbr.service.ClasificadorService;
import com.dyd.sisbr.service.DocumentoService;
import com.dyd.sisbr.service.IndiceService;
import com.dyd.sisbr.service.PalabraClaveService;
import com.dyd.sisbr.service.PreprocesadorService;
import com.dyd.sisbr.service.ServiceException;
import com.dyd.sisbr.util.Constantes;
import com.dyd.sisbr.util.Utils;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

@Service
public class ClasificadorServiceImpl implements ClasificadorService{

	@Autowired
	private ClaseDAO claseDAO;
	
	@Autowired
	private ModeloDAO modeloDAO;
	
	@Autowired
	private PreprocesadorService preprocesadorService;
	
	@Autowired
	private DocumentoService documentoService;
	
	@Autowired
	private PalabraClaveService palabraClaveService;
		
	@Autowired
	private IndiceService indiceService;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Documento clasificarDocumento(File file) throws Exception {
				
		List<Clase> listaClases = claseDAO.selectAllClases();
		Modelo modelo = modeloDAO.selectModelo();
		
		Instances instancias = (Instances) Utils.deserialize(modelo.getEstructura());
		
		List<PalabraClave> listaTokenNuevoDoc = preprocesadorService.preprocesamiento(file);
		List<PalabraClave> listaTokenUsadosClas = getListaTokenValidosClasifidor(listaTokenNuevoDoc);
		
		Instance nueva_instancia = crearInstancia(listaTokenUsadosClas, instancias, null);
		nueva_instancia.setDataset(instancias);
		
		NaiveBayes cModel = (NaiveBayes) Utils.deserialize(modelo.getDatosModelo());
		cModel.classifyInstance(nueva_instancia);
		
        double[] distribucion = cModel.distributionForInstance(nueva_instancia);
        
        int max = 0;
        for(int k = 0;k<distribucion.length;k++){
        	if(distribucion[k] > distribucion[max]){
        		max = k;
        	}
            System.out.println(k+": "+distribucion[k]);
        }
        
        Clase clase_elegida = listaClases.get(max);
        System.out.println("Documento pertenece a la clase: "+clase_elegida.getNombre());
        
        Documento nuevoDoc = new Documento();
        nuevoDoc.setNombre(file.getName());
        nuevoDoc.setIdClase(clase_elegida.getIdClase());
        nuevoDoc.setClase(clase_elegida);
        nuevoDoc.setListaToken(listaTokenNuevoDoc);
        nuevoDoc.setListaTokenBuscador(listaTokenNuevoDoc);
        
        Documento docByNombre = documentoService.obtenerDocumentoPorNombre(file.getName());
        
        if(docByNombre == null){ //si documento no existe en BD
        	
        	Archivo archivo = new Archivo();
    		archivo.setNombre(file.getName());
    		archivo.setTexto(Utils.obtenerTextoPDF(file.getAbsolutePath()));
    		archivo.setPath(file.getPath());
    		documentoService.guardarArchivo(archivo);
    		
    		nuevoDoc.setIdArchivo(archivo.getIdArchivo());
        	documentoService.guardarDocumento(nuevoDoc);
			for(PalabraClave palabra: nuevoDoc.getListaTokenBuscador()){
				palabra.setIdDocumento(nuevoDoc.getIdDocumento());
			}
			System.out.println("Guardar Palabras Clave, documento: "+nuevoDoc.getNombre()+ ", cantidad: "+nuevoDoc.getListaTokenBuscador().size());
			
			calcularPeso(nuevoDoc);
			actualizarLista(nuevoDoc);
			
			palabraClaveService.guardarPalabrasClave(nuevoDoc.getListaTokenBuscador());
			List<Indice> listaIndice = indiceService.identificarAtributos(nuevoDoc);
				
			if(!listaIndice.isEmpty()){
				indiceService.guardarListaIndices(listaIndice);
			}
			documentoService.actualizarDocumento(nuevoDoc);
        } else {
        	throw new ServiceException("El archivo " + file.getName() + " ya existe");
        }
        return nuevoDoc;
            
	}
	
	public void calcularPeso(Documento doc){
		
		Utils.calcularFrecuenciaPalabras(doc);
		
		List<String> listaRaiz = new ArrayList<>();
		for(PalabraClave token: doc.getListaToken()){
			listaRaiz.add(token.getRaiz());
		}

		List<PalabraClave> listaPalabrasBD = palabraClaveService.obtenerPalabrasClavesByRaiz(listaRaiz);
		int cantidadDocumentosTotal = documentoService.obtenerCantidadDocumentos();
		
		for(PalabraClave token: doc.getListaToken()){
			for(PalabraClave tokenBD : listaPalabrasBD){
				if(tokenBD.getRaiz().equals(token.getRaiz())){
					token.setCantDoc(tokenBD.getCantDoc() + 1);
					break;
				}
			}
		}		
		for(PalabraClave token: doc.getListaToken()){
			token.setTfidf(Utils.calcularTFIDF(token.getFrecuencia(), token.getCantDoc(), cantidadDocumentosTotal));
		}
		
	}
	
	public void actualizarLista(Documento doc){
		
		List<PalabraClave> listaReducidaDoc = new ArrayList<PalabraClave>();
		for(PalabraClave token: doc.getListaToken()){
			if(token.getTfidf() > 2){
				listaReducidaDoc.add(token);
			}
		}
		doc.getListaToken().clear();
		doc.setListaToken(listaReducidaDoc);
	}
	
	
	public void construirClasificador(List<Clase> listaClases, List<Documento> listaDocumentos,  
			List<PalabraClave> listaUnicasPalabras){
		
		try{
			FastVector fvWekaAttributes = crearAtributos(listaClases, listaUnicasPalabras);
			Instances instancias = new Instances("rel", fvWekaAttributes, listaDocumentos.size());
	       
			instancias.setClassIndex(listaUnicasPalabras.size());
	        	        
	        for(Documento documento : listaDocumentos){
				Instance instancia = crearInstancia(documento.getListaToken(), instancias, documento);
				instancias.add(instancia);
			}
	        System.out.println("Generar modelo Clasificador");
	        NaiveBayes cModel = new NaiveBayes();
			cModel.buildClassifier(instancias);
			
			Modelo modelo = new Modelo();
			modelo.setDatosModelo(Utils.serialize(cModel));
			modelo.setEstructura(Utils.serialize(instancias));
			modelo.setEstado("1"); //activo
			
			System.out.println("Guardar modelo Clasificador");
			modeloDAO.insertModelo(modelo);
			
			System.out.println("Evaluar modelo Clasificador");
			evaluarModelo(cModel, instancias, listaClases);
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void evaluarModelo(NaiveBayes cModel, Instances instancias, List<Clase> listaClases){
		try{
			Evaluation eTest = new Evaluation(instancias);
			eTest.evaluateModel(cModel, instancias);
			 
			 String strSummary = eTest.toSummaryString();
			 System.out.println(strSummary);
			 
			 // Get the confusion matrix
			 double[][] cmMatrix = eTest.confusionMatrix();
			 
			 for(Clase clase : listaClases){
				 System.out.print(clase.getNombre()+"\t");
			 }
			 System.out.println("-----------------------------------------");
			 System.out.println();
			 for(int i = 0; i <cmMatrix.length; i++){
				 for(int j = 0; j <cmMatrix[i].length; j++){
					 System.out.print(cmMatrix[i][j]+"\t");
				 }
				 System.out.println();
			 }
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private FastVector crearAtributos(List<Clase> listaClases, List<PalabraClave> listaUnicaPalabras){
		FastVector fvWekaAttributes = new FastVector(listaUnicaPalabras.size()+1);
		
		for(PalabraClave token : listaUnicaPalabras){
			fvWekaAttributes.addElement(new Attribute(token.getRaiz()));
		}
        FastVector fvClassVal = new FastVector(listaClases.size());
        for(Clase clase : listaClases){
        	fvClassVal.addElement(clase.getNombre());	
        }
        
        Attribute classAttribute = new Attribute("clases", fvClassVal);
        fvWekaAttributes.addElement(classAttribute);
        
        return fvWekaAttributes;
	}
	
	private Instance crearInstancia(List<PalabraClave> listaTokens, Instances instancias, Documento documento){
		Instance instancia = new Instance(instancias.numAttributes());

		@SuppressWarnings("unchecked")
		Enumeration<Attribute>  iterator = instancias.enumerateAttributes();
		
		while(iterator.hasMoreElements()){
			Attribute atributo = iterator.nextElement();
			boolean encontro = false;
			
			for(PalabraClave token_buscar : listaTokens){
				if(token_buscar.getRaiz().equals(atributo.name())){
					encontro = true;
					break;
				}
			}
			if(encontro){
				instancia.setValue(atributo, 1);
			}
			else{
				instancia.setValue(atributo, 0);
			}
		}
		if(documento != null){
			instancia.setValue(instancias.classAttribute() , documento.getClase().getNombre());	
		}
		return instancia;
	}
	
	public List<PalabraClave> getListaTokenValidosClasifidor(List<PalabraClave> listaPalabras){
		List<PalabraClave> listaPalabrasVal = new ArrayList<>();
		for(PalabraClave token: listaPalabras){
//			if(token.getSeccion() == Constantes.SECCION_VISTO || token.getSeccion() == Constantes.SECCION_RESUELVE){
			if(token.getSeccion() == Constantes.SECCION_VISTO){
				listaPalabrasVal.add(token);
			}	
		}
		return listaPalabrasVal;
	}

}
