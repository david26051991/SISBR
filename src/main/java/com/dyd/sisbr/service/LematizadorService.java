package com.dyd.sisbr.service;

public interface LematizadorService {

	public String stemm(String word);
	
	public String substr(String word, int start, int length);
	
	public String substr(String word, int i);

}
