
package com.dyd.sisbr.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dyd.sisbr.dao.ClaseDAO;
import com.dyd.sisbr.model.Archivo;
import com.dyd.sisbr.model.Clase;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.Indice;
import com.dyd.sisbr.model.PalabraClave;
import com.dyd.sisbr.service.ClasificadorBatchService;
import com.dyd.sisbr.service.ClasificadorService;
import com.dyd.sisbr.service.DocumentoService;
import com.dyd.sisbr.service.IndiceService;
import com.dyd.sisbr.service.PalabraClaveService;
import com.dyd.sisbr.service.PreprocesadorService;
import com.dyd.sisbr.swing.GeneradorClasificador;
import com.dyd.sisbr.util.Utils;

@Service
public class ClasificadorBatchServiceImpl implements ClasificadorBatchService{

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
	
	private GeneradorClasificador frame;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void generadorModeloClasificador(String ruta_entrenamiento, String ruta_repositorio) {
		double progreso = 0;
		preprocesadorService.limpiarListaUnica();

		List<Clase> listaClases = claseDAO.selectAllClases();
		List<Documento> listaDocumentos = new ArrayList<>();
		double progresoxClase = 20 / listaClases.size();
		for (Clase clase : listaClases) {
			File carpeta = new File(ruta_entrenamiento + File.separator + clase.getNombre());
			File[] listaArchivos = carpeta.listFiles();
			if (listaArchivos == null)
				continue;
			for (File file : listaArchivos) {
				// preprocesar documento
				pintarLog("COMIENZA PREPROCESAMIENTO: " + file.getName());
				List<PalabraClave> listaToken = preprocesadorService.preprocesamiento(file);
				List<PalabraClave> listaTokenClasificador = clasificadorService.getListaTokenValidosClasifidor(listaToken);
				if (listaToken != null) {
					// guardar documento
					File file_repo = Utils.guardarArchivoPDF(ruta_repositorio, file);
					
					Archivo archivo = new Archivo();
//					archivo.setDatos(Files.readAllBytes(file.toPath()));
					archivo.setNombre(file.getName());
					archivo.setTexto(Utils.obtenerTextoPDF(file.getAbsolutePath()));
					archivo.setPath(file_repo.getPath());
					documentoService.guardarArchivo(archivo);
					
					Documento doc = new Documento();
					doc.setIdClase(clase.getIdClase());
					doc.setClase(clase);
					doc.setNombre(file.getName());
					doc.setIdArchivo(archivo.getIdArchivo());
					doc.setListaToken(listaTokenClasificador);
					doc.setListaTokenBuscador(listaToken);
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
			for (PalabraClave palabra : documento.getListaTokenBuscador()) {
				palabra.setIdDocumento(documento.getIdDocumento());
			}
			pintarLog("Guardar Palabras Clave, documento: "
					+ documento.getNombre() + ", cantidad: "
					+ documento.getListaTokenBuscador().size());
			palabraClaveService.guardarPalabrasClave(documento.getListaTokenBuscador());
			List<Indice> listaIndice = indiceService.identificarAtributos(documento);

			if (!listaIndice.isEmpty()) {
				indiceService.guardarListaIndices(listaIndice);
			}
			documentoService.actualizarDocumento(documento);
			progreso += progressxDoc;
			setProgreso(progreso);
		}
		finalizarProgreso();
		this.frame.bloquearForm();
		JOptionPane.showMessageDialog(this.frame, "Se generó el Modelo Clasificador de Resoluciones. Además se registró las resoluciones en el sistema.");
		pintarLog("Fin - Generador Modelo Clasificador de Resoluciones");
	}

	
	private void actualizarLista(List<PalabraClave> listaUnica,
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

	private void calcularPeso(List<PalabraClave> listaUnica,
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
//			this.frame.getBtnSeleccionarAlmacen().setEnabled(true);
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


	public void setFrame(GeneradorClasificador frame) {
		this.frame = frame;
	}
	
	
}
