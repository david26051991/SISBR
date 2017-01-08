package com.dyd.sisbr.swing;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.dyd.sisbr.dao.ClaseDAO;
import com.dyd.sisbr.model.Archivo;
import com.dyd.sisbr.model.Clase;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.Indice;
import com.dyd.sisbr.model.PalabraClave;
import com.dyd.sisbr.service.BuscadorService;
import com.dyd.sisbr.service.ClasificadorService;
import com.dyd.sisbr.service.DocumentoService;
import com.dyd.sisbr.service.IndiceService;
import com.dyd.sisbr.service.PalabraClaveService;
import com.dyd.sisbr.service.PreprocesadorService;
import com.dyd.sisbr.util.Utils;

@Component
public class Main extends Thread {

	@Autowired
	private PreprocesadorService preprocesadorService;

	@Autowired
	private DocumentoService documentoService;

	@Autowired
	private IndiceService indiceService;

	@Autowired
	private ClaseDAO claseDAO;

	@Autowired
	private ClasificadorService clasificadorService;

	@Autowired
	private PalabraClaveService palabraClaveService;

	@Autowired
	private BuscadorService buscadorService;

	private GeneradorClasificador frame;

	@Override
	public void run() {

//		long ini = System.currentTimeMillis();
//		String texto = Utils.obtenerTextoPDF("D:/REPOSITORIO_RESOLUCIONES/03952-15t.pdf");
//		long fin = System.currentTimeMillis();
//		System.out.println("lectura de archivo en ruta PDF: " + (fin - ini));
//		
//		ini = System.currentTimeMillis();
//		Archivo arch = documentoService.obtenerArchivo(26);
//		fin = System.currentTimeMillis();
//		System.out.println("lectura de archivo en BD : " + (fin - ini));
//		
//		
//		ini = System.currentTimeMillis();
//		String arch2 = documentoService.obtenerTextArchivo(26);
//		fin = System.currentTimeMillis();
//		System.out.println("lectura del texto en BD: " + (fin - ini));
		
		try {
			double progreso = 0;
//			Properties prop = new Properties();
//			InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.properties");
//			prop.load(input);

			String path = this.frame.getTxtRutaResEjemplo().getText();//prop.getProperty("ruta_entrenar");
			// preprocesadorService.cargarListaStopWords(new
			// File(prop.getProperty("ruta_stopword1")), new
			// File(prop.getProperty("ruta_stopword2")));
			// preprocesadorService.cargarListaStopWords(new
			// File(this.getClass().getClassLoader().getResource("stopword.json").toURI()),
			// new
			// File(this.getClass().getClassLoader().getResource("stopword2.json").toURI()));
			preprocesadorService.limpiarListaUnica();

			List<Clase> listaClases = claseDAO.selectAllClases();
			List<Documento> listaDocumentos = new ArrayList<>();
			double progresoxClase = 20 / listaClases.size();
			for (Clase clase : listaClases) {
				File carpeta = new File(path + "/" + clase.getNombre());
				File[] listaArchivos = carpeta.listFiles();
				if (listaArchivos == null)
					continue;
				for (File file : listaArchivos) {
					// preprocesar documento
					pintarLog("COMIENZA PREPRECESAMIENTO: " + file.getName());
					List<PalabraClave> listaToken = preprocesadorService.preprocesamiento(file);
					if (listaToken != null) {
						// guardar documento
//						File file_repo = Utils.guardarArchivoPDF((String) prop.get("ruta_guardar"), file);
//						Documento doc = new Documento();
//						doc.setIdClase(clase.getIdClase());
//						doc.setClase(clase);
//						doc.setNombre(file_repo.getName());
//						doc.setPath(file_repo.getPath());
//						doc.setListaToken(listaToken);
//						listaDocumentos.add(doc);
						
						Archivo archivo = new Archivo();
						archivo.setDatos(Files.readAllBytes(file.toPath()));
						archivo.setNombre(file.getName());
						archivo.setTexto(Utils.obtenerTextoPDF(file.getAbsolutePath()));
						documentoService.guardarArchivo(archivo);
						
						Documento doc = new Documento();
						doc.setIdClase(clase.getIdClase());
						doc.setClase(clase);
						doc.setNombre(file.getName());
						doc.setIdArchivo(archivo.getIdArchivo());
						doc.setListaToken(listaToken);
						listaDocumentos.add(doc);
					}
				}
				progreso += progresoxClase;
				setProgreso(progreso);
			}

			List<PalabraClave> listaUnica = preprocesadorService.getListaUnicaToken();

			// ************** Prueba borrar luego/

			calcularPeso(listaUnica, listaDocumentos);

			// actualizar listaUnica y vector en documentos

			actualizarLista(listaUnica, listaDocumentos);

			// *************FIN PRUEBA

			clasificadorService.construirClasificador(listaClases, listaDocumentos, listaUnica);
			progreso = 30;
			setProgreso(progreso);

			pintarLog("Cantidad de documentos: " + listaDocumentos.size());
			documentoService.guardarDocumentos(listaDocumentos);
			progreso = 35;
			setProgreso(progreso);
			double progressxDoc = 65.0 / listaDocumentos.size();

			for (Documento documento : listaDocumentos) {
				for (PalabraClave palabra : documento.getListaToken()) {
					palabra.setIdDocumento(documento.getIdDocumento());
				}
				pintarLog("Guardar Palabras Clave, documento: "
						+ documento.getNombre() + ", cantidad: "
						+ documento.getListaToken().size());
				palabraClaveService.guardarPalabrasClave(documento.getListaToken());
				List<Indice> listaIndice = indiceService.identificarAtributos(documento);

				if (!listaIndice.isEmpty()) {
					indiceService.guardarListaIndices(listaIndice);
				}
				documentoService.actualizarDocumento(documento);
				progreso += progressxDoc;
				setProgreso(progreso);
			}
			finalizarProgreso();
			pintarLog("Fin - Generador Modelo Clasificador");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actualizarLista(List<PalabraClave> listaUnica,
			List<Documento> listaDocumentos) {

		for (Documento doc : listaDocumentos) {
			List<PalabraClave> listaReducidaDoc = new ArrayList<PalabraClave>();
			for (PalabraClave token : doc.getListaToken()) {
				if (token.getTfidf() > 1) {
					listaReducidaDoc.add(token);
				}
			}
			doc.getListaToken().clear();
			doc.setListaToken(listaReducidaDoc);
		}

		listaUnica.clear();
		HashSet<String> listUniqTemp = new HashSet<String>();

		for (Documento doc : listaDocumentos) {
			for (PalabraClave token : doc.getListaToken()) {
				listUniqTemp.add(token.getRaiz());
			}
		}
		Iterator<String> it = listUniqTemp.iterator();
		while (it.hasNext()) {
			PalabraClave t = new PalabraClave();
			t.setRaiz(it.next());
			listaUnica.add(t);
		}

	}

	public void calcularPeso(List<PalabraClave> listaUnica,
			List<Documento> listaDocumentos) {

		for (Documento doc : listaDocumentos) {
			Utils.calcularFrecuenciaPalabras(doc);
		}

		Map<String, Integer> mapDocumentosPorPalabra = new HashMap<String, Integer>();
		for (PalabraClave token : listaUnica) {
			mapDocumentosPorPalabra.put(
					token.getRaiz(),
					Utils.calcularFrecuenciaDocumentos(listaDocumentos,
							token.getRaiz()));
		}

		for (Documento doc : listaDocumentos) {
			for (PalabraClave token : doc.getListaToken()) {
				token.setCantDoc(mapDocumentosPorPalabra.get(token.getRaiz()));
				token.setTfidf(Utils.calcularTFIDF(token.getFrecuencia(),
						token.getCantDoc(), listaDocumentos.size()));
			}
		}

		// for(PalabraClave token: listaDocumentos.get(0).getListaToken()){
		// System.out.println("palabra: " + token.getRaiz() + ", frecuencia: "+
		// token.getTfidf());
		// }

	}

	public void clasificar(File file) {
		try {
			// Properties prop = new Properties();
			// InputStream input = new
			// FileInputStream("conf/config.properties");
			// prop.load(input);
			// preprocesadorService.cargarListaStopWords(new
			// File(this.getClass().getClassLoader().getResource("stopword.json").toURI()),
			// new
			// File(this.getClass().getClassLoader().getResource("stopword2.json").toURI()));
			preprocesadorService.limpiarListaUnica();

			Utils.logInicioTiempoDuracion();
			clasificadorService.clasificarDocumento(file);
			Utils.logFinTiempoDuracion("Clasificar nuevo doc");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscar(String consulta) {
		try {
			buscadorService.buscarDocumentos(consulta, 13, 2016, 2016);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFormulario(GeneradorClasificador frame) {
		this.frame = frame;
		this.frame.getPbarProceso().setVisible(true);
		this.frame.getBtnGenerar().setEnabled(false);
		this.frame.getBtnSeleccionarAlmacen().setEnabled(false);
		this.frame.getBtnSeleccionarEjemplos().setEnabled(false);
		this.frame.getTxtRutaResAlmacen().setEditable(false);
		this.frame.getTxtRutaResEjemplo().setEditable(false);
	}

	private void setProgreso(double progreso) {
		if (this.frame != null) {
			this.frame.getPbarProceso().setValue((int) progreso);
		}
	}

	private void finalizarProgreso() {
		if (this.frame != null) {
			setProgreso(100);
			this.frame.getBtnGenerar().setEnabled(true);
			this.frame.getBtnSeleccionarAlmacen().setEnabled(true);
			this.frame.getBtnSeleccionarEjemplos().setEnabled(true);
			this.frame.getTxtRutaResAlmacen().setEditable(true);
			this.frame.getTxtRutaResEjemplo().setEditable(true);
		}
	}

	private void pintarLog(String mensaje) {
		System.out.println(mensaje);
		try {
			if (this.frame != null) {
				this.frame.getTxtLog().setText(
						this.frame.getTxtLog().getText() + "\n" + mensaje);
				this.frame
						.getScrollPane()
						.getVerticalScrollBar()
						.setValue(
								this.frame.getScrollPane()
										.getVerticalScrollBar().getMaximum());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		Main main = ctx.getBean(Main.class);

		// main.clasificar(new
		// File("C:/Users/DAVID LOYOLA/Dropbox/tesis/Tesis 2015/Resoluciones/Bachiller/00224-15t.pdf"));
		main.clasificar(new File(
				"C:/Users/DAVID LOYOLA/Dropbox/tesis/Tesis 2015/Resoluciones/Matricula/00467-15t.pdf"));
		// main.clasificar(new
		// File("C:/Users/DAVID LOYOLA/Dropbox/tesis/Tesis 2015/Resoluciones/Presupuestos/00521-15t.pdf"));
		// main.clasificar(new
		// File("C:/Users/DAVID LOYOLA/Dropbox/tesis/Tesis 2015/Resoluciones/Rectificaciones/00450-15t.pdf"));
		// main.clasificar(new
		// File("C:/Users/DAVID LOYOLA/Dropbox/tesis/Tesis 2015/Resoluciones/Rectificaciones/00450-15t.pdf"));

		// main.clasificar(new
		// File("C:/Users/DAVID LOYOLA/Dropbox/tesis/Tesis 2015/Resoluciones/Encargatura/00536-15t.pdf"));

		Utils.logInicioTiempoDuracion();
		// main.iniciar();
		// String consulta = "22 de enero del 2015";
		// main.buscar(consulta);
		// Utils.guardarArchivoPDF("D:/REPOSITORIO_RESOLUCIONES/", new
		// File("D:/RESOLUCIONES_ENTRENAMIENTO/PRESUPUESTO/00301-15t.pdf"));
		// Utils.guardarArchivoRepositorio("D:/REPOSITORIO_RESOLUCIONES/",new
		// File("D:/RESOLUCIONES_ENTRENAMIENTO/PRESUPUESTO/00285-15t.pdf"));
		Utils.logFinTiempoDuracion("Construir clasificador");

	}

}
