package com.dyd.sisbr.dao;

import java.util.List;

import com.dyd.sisbr.model.Stopword;

public interface StopwordDAO {

	public List<Stopword> selectStopword(String tipo);
	
}
