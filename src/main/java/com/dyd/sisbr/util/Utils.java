package com.dyd.sisbr.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.PalabraClave;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;


public class Utils {

	public static final String SIGNOS_PUNTUACION = "()[ .,:;ø?°!]+\n";
	private static long tiempoInicioLog;
	private static ObjectMapper mapper = new ObjectMapper();
	
	public enum Mes {
    	ENERO, FEBRERO, MARZO, ABRIL, MAYO, JUNIO, JULIO, AGOSTO, SETIEMBRE, SEPTIEMBRE, OCTUBRE, NOVIEMBRE, DICIEMBRE
    }
	
	public static File guardarArchivoPDF(String ruta, File origen){
		try{
			String fechaActual = convertDateToString(new Date(), "YYYYMM");
			File carpeta_destino = new File(ruta + File.separator + fechaActual);
			carpeta_destino.mkdir();
			String path_destino = carpeta_destino.getAbsolutePath() + File.separator + origen.getName();
			PdfReader pdfReader = new PdfReader(origen.getPath());
			PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(path_destino));			
			pdfStamper.close();
			return new File(path_destino);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static File guardarBytesArchivoPDF(String ruta, String nameFile, byte[] bytesFile) {
		try {
			String fechaActual = convertDateToString(new Date(), "YYYYMM");
			File carpeta_destino = new File(ruta + File.separator + fechaActual);
			carpeta_destino.mkdir();
			String path_destino = carpeta_destino.getAbsolutePath() + File.separator + nameFile;
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path_destino));
			stream.write(bytesFile);
			stream.close();
			return new File(path_destino);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String obtenerTextoPDF(String url_PDF) {
		
		String textFromPage = "";
		try {
			PdfReader pr = new PdfReader(url_PDF);
			int numpages = pr.getNumberOfPages();
			for (int i = 1; i <= numpages; i++) {
				textFromPage += PdfTextExtractor.getTextFromPage(pr, i);
			}	
			pr.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en lectura de: "+url_PDF);
		}
		return textFromPage;
	}
	
	@SuppressWarnings("resource")
	public static String obtenerTextoWordDOC(String url){
        String textFromPage = "";
        try{
        	File file = new File(url);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            HWPFDocument document = new HWPFDocument(fis);
			WordExtractor extractor = new WordExtractor(document);
            String[] fileData = extractor.getParagraphText();
            for (int i = 0; i < fileData.length; i++){
                if (fileData[i] != null)
                	textFromPage += fileData[i];
            }
            fis.close();
        } catch (Exception exep){
            exep.printStackTrace();
        }
        return textFromPage;
	}
	
	@SuppressWarnings("resource")
	public static String obtenerTextoWordDOCX(String url){
        String textFromPage = "";
        try{
        	File file = new File(url);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
            	textFromPage += para.getText();
            }
            fis.close();
        } catch (Exception exep){
            exep.printStackTrace();
        }
        return textFromPage;
	}
	
	
	public static String quitarTildes(String texto){
		String original = "·‡‰ÈËÎÌÏÔÛÚˆ˙˘uÒ¡¿ƒ…»ÀÕÃœ”“÷⁄Ÿ‹—Á«";
	 	String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	 	for (int i=0; i<original.length(); i++) {
			texto = texto.replace(original.charAt(i), ascii.charAt(i));
	    }
	 	return texto;
	}
	
	public static String quitarEspacios(String texto){
		if(texto != null){
			texto =  texto.replaceAll(" +", " ").trim();
			return texto.replace("\r\n", " ").replace("\n", " ");
		}
		return texto;
	}
	
	public static String quitarCaracteresEspeciales(String texto){
        Pattern p = Pattern.compile("[^a-zA-Z]");
		Matcher m = p.matcher(texto);
		if(m.find()){
			texto = m.replaceAll(" ");
		}
		return texto;
	}
	
	public static File guardarArchivoRepositorio(String ruta, File origen){
		try{
			String path = ruta+origen.getName();
			FileInputStream in = new FileInputStream(origen); 
			FileOutputStream out = new FileOutputStream(new File(path)); 
			byte[] b = new byte[255]; 

			while (in.read(b)!=-1) 
			out.write(b); 

			in.close(); 
			out.close(); 
			return new File(path);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static File guardarArchivoRepositorio2(String ruta, File origen){
		try{
			String path = ruta+origen.getName();
			FileInputStream in = new FileInputStream(origen); 
			FileOutputStream out = new FileOutputStream(new File(path)); 
			byte[] b = new byte[1024]; 

			while (in.read(b)!=-1) 
			out.write(b); 

			in.close(); 
			out.close(); 
			return new File(path);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static HashSet<String> extraerPalabras(File file){
		HashSet<String> lista = new HashSet<String>();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null) {
				if (!linea.trim().isEmpty())
					lista.add(linea);
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public static String[] extraerPalabrasJSONss(File file){
		String[] lista = {};
		try{
			lista = mapper.readValue(file, String[].class);
		} catch(Exception e){
			e.printStackTrace();
		}
		return lista;
	}
	
	public static byte[] serialize(Object obj){
		try{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
	        ObjectOutputStream o = new ObjectOutputStream(b);
	        o.writeObject(obj);
	        return b.toByteArray();
		} catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
    }

    public static Object deserialize(byte[] bytes){
    	
    	try{
    		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
            ObjectInputStream o = new ObjectInputStream(b);
            return o.readObject();
		} catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
    }
    
    public static String getJson(Object object){
    	String json = null;
    	try{
    		json = mapper.writeValueAsString(object);
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    	return json;
    }
    
    public static <T> List<T> jsonToList(String jsonString, Class<?> clazz) {
        List<T> result = null;
        try {
           JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
           result = mapper.readValue(jsonString, type);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return result;
     }
    
    /**
	 * contar la frecuencia de una palabra en el mismo documento
	 * @param documento
	 */
	public static int calcularFrecuenciaPalabra(String palabra, Documento documento){
		int count = 0;
		for(PalabraClave token: documento.getListaToken()){		
			if(palabra.equals(token.getRaiz())){
				count++;
			}
		}
		return count;
	}
	
    /**
	 * contar la frecuencia de cada palabra en el mismo documento
	 * @param documento
	 */
	public static void calcularFrecuenciaPalabras(Documento documento){
		for(PalabraClave token: documento.getListaToken()){			
			int count = 0;
			for(PalabraClave tokenBusq: documento.getListaToken()){
				if(tokenBusq.getRaiz().equals(token.getRaiz())){
					count++;
				}
			}
			token.setFrecuencia(count);
		}
	}
	
	/**
	 * contar la cantidad de documentos que aparece una palabra
	 * @param listaDocumentos
	 * @param listaUnicaPalabras
	 */
	public static int calcularFrecuenciaDocumentos(List<Documento> listaDocumentos, String palabra){
		int count = 0;
		for(Documento doc: listaDocumentos){
			for(PalabraClave token: doc.getListaToken()){
				if(token.getRaiz().equals(palabra)){
					count++;
					break;
				}
			}
		}
		return count;
	}
	
	/**
	 * calcular el peso TF-IDF
	 * @param fp: frecuencia de la palabra
	 * @param fd: cantidad de documentos en que aparece la palabra
	 * @param N: cantidad de documentos
	 * @return
	 */
	public static double calcularTFIDF(double fp, double fd, double N){
		double pesoTfIdf = 0.0;
		if(fd > 0.0){
			pesoTfIdf = fp*Math.log(N/fd);
		}
		return pesoTfIdf;
	}
	
	/**
	 * calcular el grado de similitud usando la distancia del ·ngulo coseno
	 * @param vectorQ: vector de la consulta
	 * @param vectorD: vector del documento
	 * @param N: tamaÒo del vector
	 * @return
	 */
	public static double similitudCoseno(Double[] vectorQ, Double[] vectorD, int N){
		
		double numerador = 0.0;		
		for(int i = 0; i < N; i++){
			numerador += vectorQ[i]*vectorD[i];
		}
		
//		double sumaCuadradosQ = 0.0;
//		for(int i = 0; i < N; i++){
//			sumaCuadradosQ += vectorQ[i]*vectorQ[i];
//		}
//		
//		double sumaCuadradosD = 0.0;
//		for(int i = 0; i < N; i++){
//			sumaCuadradosD += vectorD[i]*vectorD[i];
//		}
		
		double denominador = 1;//Math.pow(sumaCuadradosQ*sumaCuadradosD, 0.5);
		
		return denominador == 0.0 || Double.isNaN(denominador) || Double.isInfinite(denominador) ? 0.0 : numerador/denominador;
	}
    
    public static void logInicioTiempoDuracion(){
    	tiempoInicioLog = System.currentTimeMillis();
    }
    
    public static void logFinTiempoDuracion(String mensaje){
    	System.out.println(mensaje + ": " + (System.currentTimeMillis() - tiempoInicioLog) + " milisegundos");
    }
    
    public static Date parsearFecha(String fechaString, String formato) {
        Date resultDate = null;
        try {
           SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
           if (fechaString != null && !fechaString.isEmpty()) {
              resultDate = formatoFecha.parse(fechaString);
           }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return resultDate;
    }
    
    public static String convertDateToString(Date hora, String format) {
        try {
           SimpleDateFormat dt = new SimpleDateFormat(format);
           return dt.format(hora);

        } catch (Exception e) {
           return null;
        }
     }
    
    public static Integer parseInt(Object objeto) {
        try {
           return Integer.parseInt(objeto.toString());
        } catch (NumberFormatException e) {
           e.printStackTrace();
           return null;
        }
     }
    
    public static int getNumeroMes(String strMes){
    	int numMes = 0;
    	switch(Mes.valueOf(strMes)){
    		case ENERO		: numMes = 1;break;
    		case FEBRERO	: numMes = 2;break;
    		case MARZO		: numMes = 3;break;
    		case ABRIL		: numMes = 4;break;
    		case MAYO		: numMes = 5;break;
    		case JUNIO		: numMes = 6;break;
    		case JULIO		: numMes = 7;break;
    		case AGOSTO		: numMes = 8;break;
    		case SETIEMBRE	: numMes = 9;break;
    		case SEPTIEMBRE	: numMes = 9;break;
    		case OCTUBRE	: numMes = 10;break;
    		case NOVIEMBRE	: numMes = 11;break;
    		case DICIEMBRE	: numMes = 12;break;
    	};
    	return numMes;
    }
    
    public static String getExtensionFile(String path){
    	String ext = "";
    	try{
    		ext = path.split("\\.")[path.split("\\.").length -1];
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    	return ext;
    }
    
}