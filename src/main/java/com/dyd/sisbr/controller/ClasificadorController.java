		package com.dyd.sisbr.controller;

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.service.ClasificadorService;
import com.dyd.sisbr.util.Utils;


@Controller
public class ClasificadorController {

	@Autowired
	private ClasificadorService clasificadorService;
	
	@RequestMapping("/iniciarClasificacion")
	public String inicio(Model model){
		
		model.addAttribute("listaDocClasificados", Utils.getJson(new ArrayList<Documento>()));
		return "clasificacion";
	}
	
//	@RequestMapping(value = "/clasificarDocumentos", method = RequestMethod.POST)
//	public String clasificarDocumentos(@RequestParam("resoluciones") List<MultipartFile> resoluciones,
//			Model model){
//		
//		List<Documento> listaDocClasificados = new ArrayList<>();
//		try{
//			for(MultipartFile file: resoluciones){
//				File resolucion = Utils.guardarBytesArchivoPDF("D:/REPOSITORIO_RESOLUCIONES", file.getOriginalFilename(), file.getBytes());
//				Documento documento = clasificadorService.clasificarDocumento(resolucion);
//				listaDocClasificados.add(documento);
//			}
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		
//		model.addAttribute("listaDocClasificados", new Gson().toJson(listaDocClasificados));
//		return "clasificacion";o
//	}
	
	@RequestMapping(value = "/clasificarDocumentos", method = RequestMethod.POST)
	public @ResponseBody Documento clasificarDocumentos(
			@RequestParam("file") MultipartFile file,
			Model model){
		
		Documento documento = null;
		try{
			File resolucion = Utils.guardarBytesArchivoPDF("D:/REPOSITORIO_RESOLUCIONES", file.getOriginalFilename(), file.getBytes());
			documento = clasificadorService.clasificarDocumento(resolucion);
		} catch (Exception e){
			e.printStackTrace();
		}
		return documento;
	}
}
