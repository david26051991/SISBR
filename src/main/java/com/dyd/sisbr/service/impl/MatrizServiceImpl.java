package com.dyd.sisbr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyd.sisbr.dao.MatrizDAO;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.Matriz;
import com.dyd.sisbr.model.PalabraClave;
import com.dyd.sisbr.service.MatrizService;

@Service
public class MatrizServiceImpl implements MatrizService{

	@Autowired
	private MatrizDAO matrizDAO;
	
	@Override
	public void guardarMatriz(List<Documento> listaDocumentos) {
		if(listaDocumentos != null){
			for(Documento doc : listaDocumentos){
				for(PalabraClave token : doc.getListaToken()){
					Matriz matriz = new Matriz();
					matriz.setIdDocumento(doc.getIdDocumento());
					matriz.setIdPalabraClave(token.getIdPalabraClave());
					matriz.setFrecuencia(token.getFrecuencia());
					matrizDAO.insertMatriz(matriz);
					matriz = null;
				}
			}
		}
	}

	@Override
	public List<PalabraClave> obtenerListaMatrizPorDocumento(int idDocumento) {
		
		List<PalabraClave> listaPalabraClave = null;
		List<Matriz> matriz = matrizDAO.selectPalabraByDocumento(idDocumento);
		
		if(matriz != null){
			listaPalabraClave = new ArrayList<PalabraClave>();
			for(Matriz palabraMatriz : matriz){
				listaPalabraClave.add(palabraMatriz.getToken());
			}
		}
		return listaPalabraClave;
	}

}
