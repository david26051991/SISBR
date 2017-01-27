package com.dyd.sisbr.swing;

import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import com.dyd.sisbr.service.ClasificadorBatchService;

@Component
public class Main extends Thread {

	@Autowired
	private ClasificadorBatchService clasificadorBatchService;

	private GeneradorClasificador frame;
	
	@Override
	public void run() {
		try {
			String ruta_entrenamiento = this.frame.getTxtRutaResEjemplo().getText();
			String ruta_repositorio = this.frame.getTxtRutaResAlmacen().getText();
			clasificadorBatchService.setFrame(frame);
			clasificadorBatchService.generadorModeloClasificador(ruta_entrenamiento, ruta_repositorio);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.frame, "Ocurrió un error al procesar los archivos.");
		}
	}
	
	
	public void setFormulario(GeneradorClasificador frame) {
		this.frame = frame;
		this.frame.getPbarProceso().setVisible(true);
		this.frame.getBtnGenerar().setEnabled(false);
//		this.frame.getBtnSeleccionarAlmacen().setEnabled(false);
		this.frame.getBtnSeleccionarEjemplos().setEnabled(false);
		this.frame.getTxtRutaResAlmacen().setEditable(false);
		this.frame.getTxtRutaResEjemplo().setEditable(false);
	}

	public String getPathRepositoryFiles() {
		 try {
			Properties pro = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/application.properties"));
			return pro.getProperty("path.repository.files");
		} catch (IOException e) {
			e.printStackTrace();
			return ".......";
		}
	}

}
