package com.dyd.sisbr.dao;

import java.util.List;
import com.dyd.sisbr.model.Indice;

public interface IndiceDAO {

	public void insertIndice(Indice indice);
	
	public List<Indice> selectIndicesByIdDocumento(List<Integer> listaIdDoc);
}
