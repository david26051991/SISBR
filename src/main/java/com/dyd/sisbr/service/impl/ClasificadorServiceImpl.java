package com.dyd.sisbr.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import com.dyd.sisbr.dao.ClaseDAO;
import com.dyd.sisbr.dao.ModeloDAO;
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
import com.dyd.sisbr.util.Utils;

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
	public Documento clasificarDocumento(File file) {
		
		try{
			List<Clase> listaClases = claseDAO.selectAllClases();
			Modelo modelo = modeloDAO.selectModelo();
			
			Instances instancias = (Instances) Utils.deserialize(modelo.getEstructura());
			
			List<PalabraClave> listaTokenNuevoDoc = preprocesadorService.preprocesamiento(file);
			
			Instance nueva_instancia = crearInstancia(listaTokenNuevoDoc, instancias, null);
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
            nuevoDoc.setPath(file.getPath());
            nuevoDoc.setIdClase(clase_elegida.getIdClase());
            nuevoDoc.setClase(clase_elegida);
            nuevoDoc.setListaToken(listaTokenNuevoDoc);
            
            Documento docByNombre = documentoService.obtenerDocumentoPorNombre(file.getName());
            
            if(docByNombre == null){ //si documento no existe en BD
            	documentoService.guardarDocumento(nuevoDoc);
                
    			for(PalabraClave palabra: nuevoDoc.getListaToken()){
    				palabra.setIdDocumento(nuevoDoc.getIdDocumento());
    			}
    			System.out.println("Guardar Palabras Clave, documento: "+nuevoDoc.getNombre()+ ", cantidad: "+nuevoDoc.getListaToken().size());
    			
    			calcularPeso(nuevoDoc);
    			actualizarLista(nuevoDoc);
    			
    			palabraClaveService.guardarPalabrasClave(nuevoDoc.getListaToken());
    			List<Indice> listaIndice = indiceService.identificarAtributos(nuevoDoc);
    				
    			if(!listaIndice.isEmpty()){
    				indiceService.guardarListaIndices(listaIndice);
    			}
    			documentoService.actualizarDocumento(nuevoDoc);
            }
            				
            return nuevoDoc;
            
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
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
	
	@Override
	public Documento clasificarDocumento2(File file) {
		
		try{
			List<Clase> listaClases = claseDAO.selectAllClases();
			Modelo modelo = modeloDAO.selectModelo();
			
			Instances instancias = (Instances) Utils.deserialize(modelo.getEstructura());
			
			Instance nueva_instancia = crearInstanciaNuevoDoc(instancias, file.getPath());
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
            nuevoDoc.setPath(file.getPath());
            nuevoDoc.setClase(clase_elegida);
            return nuevoDoc;
            
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
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
	
	public void construirClasificador2(List<Clase> listaClases, List<Documento> listaDocumentos){
		
		try{
			Instances instancias = crearAtributos2(listaClases, listaDocumentos.size());
	        	        
	        for(Documento documento : listaDocumentos){
				Instance instancia = crearInstancia2(instancias, documento.getPath(), documento.getClase().getNombre());
//				if(instancia != null){
					instancias.add(instancia);
					//Thread.sleep(100);
//				}
			}
	        	        
	        StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(instancias);
			filter.setLowerCaseTokens(true);
			filter.setStopwords(new File("D:/DAVID/Documentos/workspace/ClasificadorDocumento/resources/stopword.txt"));
			filter.setUseStoplist(true);
			filter.setTFTransform(true);
			filter.setIDFTransform(true);
			filter.setStemmer(new SnowballStemmer("spanish"));
			instancias = Filter.useFilter(instancias, filter);
			
	        NaiveBayes cModel = new NaiveBayes();
			cModel.buildClassifier(instancias);
			
			Modelo modelo = new Modelo();
			modelo.setDatosModelo(Utils.serialize(cModel));
			modelo.setEstructura(Utils.serialize(instancias));
			modelo.setEstado("1"); //activo
			
			modeloDAO.insertModelo(modelo);
			
			evaluarModelo(cModel, instancias, listaClases);
			
			System.out.println("Guardar caracteristicas");
			
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
	
	private Instances crearAtributos2(List<Clase> listaClases, int cantDocumentos){
		
		FastVector fvClassVal = new FastVector(listaClases.size());
		for(Clase clase : listaClases){
			fvClassVal.addElement(clase.getNombre());
		}
		FastVector atts = new FastVector(2);
		//atts.addElement(new Attribute("filename", (FastVector) null));
		atts.addElement(new Attribute("contents", (FastVector) null));
		atts.addElement(new Attribute("clases", fvClassVal));
		
		Instances data = new Instances("rel", atts,cantDocumentos);
		data.setClassIndex(1);	
		return data;
		
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
	
	private Instance crearInstancia2(Instances instancias, String path, String clase){
		
		String textFromPage = Utils.obtenerTextoPDF(path);
		
//		if(textFromPage != null && !textFromPage.isEmpty()){
			Pattern p = Pattern.compile("[^a-zA-ZА-За-з]");
			Matcher m = p.matcher(textFromPage);
			if(m.find()){
				textFromPage = m.replaceAll(" ");
			}
			
			Instance instancia = new Instance(2);
			//instancia.setValue(instancias.attribute(0), documento.getNombre());
			instancia.setValue(instancias.attribute(0), textFromPage);
			instancia.setValue(instancias.attribute(1), clase);
			return instancia;
//		} else{
//			return null;
//		}
	}
	
	private Instance crearInstanciaNuevoDoc(Instances instancias, String path) throws Exception{
		
		FastVector atts = new FastVector(1);
		atts.addElement(new Attribute("contents", (FastVector) null));
		
		Instances data = new Instances("text_files_in_" + "D:///", atts,0);	
        
		String textFromPage = Utils.obtenerTextoPDF(path);
		
		Pattern p = Pattern.compile("[^a-zA-ZА-За-з]");
		Matcher m = p.matcher(textFromPage);
		if(m.find()){
			textFromPage = m.replaceAll(" ");
		}
		
		Instance instancia = new Instance(1);
		instancia.setValue(data.attribute(0), textFromPage);
		
		data.add(instancia);
		        	        
        StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(data);
		filter.setLowerCaseTokens(true);
		filter.setStopwords(new File("D:/DAVID/Documentos/workspace/ClasificadorDocumento/resources/stopword.txt"));
		filter.setUseStoplist(true);
		filter.setTFTransform(true);
		filter.setIDFTransform(true);
		filter.setStemmer(new SnowballStemmer("spanish"));
		data = Filter.useFilter(data, filter);
		
		Instance instanciaNuevoDoc = new Instance(instancias.numAttributes());

		@SuppressWarnings("unchecked")
		Enumeration<Attribute>  iterator = instancias.enumerateAttributes();
		
		while(iterator.hasMoreElements()){
			Attribute atributo = iterator.nextElement();
			boolean encontro = false;
			
			@SuppressWarnings("unchecked")
			Enumeration<Attribute> atrNuevoDoc = data.instance(0).enumerateAttributes();
			while(atrNuevoDoc.hasMoreElements()){
				String nameAtributo = atrNuevoDoc.nextElement().name();
				if(nameAtributo.equals(atributo.name())){
					encontro = true;
					break;
				}
			}
			if(encontro){
				instanciaNuevoDoc.setValue(atributo, 1);
			}
			else{
				instanciaNuevoDoc.setValue(atributo, 0);
			}
		}
		return instanciaNuevoDoc;
	}

}
