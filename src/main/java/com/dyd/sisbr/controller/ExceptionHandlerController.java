package com.dyd.sisbr.controller;

import java.lang.reflect.InvocationTargetException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dyd.sisbr.service.ServiceException;
import com.dyd.sysbr.bean.RespuestaBean;

public class ExceptionHandlerController {

   @ExceptionHandler(value = Exception.class)
   public @ResponseBody RespuestaBean<String> handle(Exception exception) {
	  String mensajeError = null;
      try {
         if (exception instanceof InvocationTargetException) {
            InvocationTargetException invocationTargetException = (InvocationTargetException) exception;
            exception = (Exception) invocationTargetException.getTargetException();
         }
         if (exception instanceof ServiceException) {
            mensajeError = exception.getMessage();
         } else {
            exception.printStackTrace();
            mensajeError = "Error no Controlado";
         }
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      RespuestaBean<String> respuesta = new RespuestaBean<>();
      respuesta.setExito(false);
      respuesta.setMensaje(mensajeError);
      return respuesta;
   }
}
