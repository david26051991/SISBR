package com.dyd.sisbr.service;

import com.dyd.sisbr.swing.GeneradorClasificador;

public interface ClasificadorBatchService {

	public void setFrame(GeneradorClasificador frame);
	public void generadorModeloClasificador(String ruta_entrenamiento, String ruta_repositorio);
	
}
