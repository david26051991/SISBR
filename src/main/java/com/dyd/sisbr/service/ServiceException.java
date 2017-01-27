package com.dyd.sisbr.service;

public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ServiceException(String msj){
		super(msj);
	}
}
