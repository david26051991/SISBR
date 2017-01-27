package com.dyd.sisbr.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dyd.sisbr.model.Clase;
import com.dyd.sisbr.service.BuscadorService;
import com.dyd.sisbr.util.Utils;
import com.dyd.sysbr.bean.ReporteBean;
import com.dyd.sysbr.bean.RespuestaBean;

@Controller
@RequestMapping("/reporte")
public class ReporteController extends ExceptionHandlerController{

	@Autowired
	private BuscadorService buscadorService;
	
	@RequestMapping("/iniciarReporte")
	public String iniciarReporte(Model model){
		List<Clase> listaClases = buscadorService.obtenerClases();
		model.addAttribute("listaClases", Utils.getJson(listaClases));
		return "reporte";
	}
	
	@RequestMapping(value = "/buscar", method = RequestMethod.POST)
	public @ResponseBody RespuestaBean<ReporteBean> buscar(HttpServletRequest request,
			@RequestParam("listClases") String listClases,
			@RequestParam("fechaIni") String fechaIni,
			@RequestParam("fechaFin") String fechaFin){
		
		List<Clase> listaClases = Utils.jsonToList(request.getParameter("listClases"), Clase.class);
		
		ReporteBean reporte = buscadorService.generarReporte(listaClases, fechaIni, fechaFin);
		
		RespuestaBean<ReporteBean> respuesta = new RespuestaBean<ReporteBean>();
		respuesta.setExito(true);
		respuesta.setData(reporte);
		
		return respuesta;
	}
}
