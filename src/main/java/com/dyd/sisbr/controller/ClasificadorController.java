package com.dyd.sisbr.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dyd.sisbr.model.Clase;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.service.ClasificadorService;
import com.dyd.sisbr.util.Utils;
import com.dyd.sysbr.bean.RespuestaBean;

@Controller
@RequestMapping("/clasificador")
public class ClasificadorController extends ExceptionHandlerController {

	@Autowired
	private ClasificadorService clasificadorService;

	@Value("${path.repository.files}")
	private String pathRepositoryFiles;

	@RequestMapping("/iniciarClas")
	public String inicio(Model model) {

		model.addAttribute("listaDocClasificados", Utils.getJson(new ArrayList<Documento>()));
		return "clasificacion";
	}

	@RequestMapping(value = "/clasificarDocumentos", method = RequestMethod.POST)
	public @ResponseBody RespuestaBean<Documento> clasificarDocumentos(
			MultipartHttpServletRequest mpRequest, Model model) {

		List<Documento> listaDocClasificados = new ArrayList<>();
		Iterator<String> it = mpRequest.getFileNames();
		
		while(it.hasNext()){
			MultipartFile file = mpRequest.getFile(it.next());
			Documento documento = null;
			try {
				File resolucion = Utils.guardarBytesArchivoPDF(pathRepositoryFiles, file.getOriginalFilename(),
						file.getBytes());
				documento = clasificadorService.clasificarDocumento(resolucion);
			} catch (Exception e) {
				e.printStackTrace();
				documento = new Documento();
				Clase clase = new Clase();
				clase.setNombre(e.getMessage());
				documento.setClase(clase);
				documento.setNombre(file.getOriginalFilename());
			}
			listaDocClasificados.add(documento);
		}

		RespuestaBean<Documento> respuesta = new RespuestaBean<>();
		respuesta.setExito(true);
		respuesta.setLista(listaDocClasificados);
		return respuesta;
	}

	// @RequestMapping(value = "/clasificarDocumentos", method =
	// RequestMethod.POST)
	// public @ResponseBody RespuestaBean<Documento>
	// clasificarDocumentos(@RequestParam("file") MultipartFile file,
	// Model model) throws Exception {
	//
	// RespuestaBean<Documento> respuesta = new RespuestaBean<>();
	// Documento documento = new Documento();
	// documento.setNombre(file.getOriginalFilename());
	// try {
	// File resolucion = Utils.guardarBytesArchivoPDF(pathRepositoryFiles,
	// file.getOriginalFilename(),
	// file.getBytes());
	// documento = clasificadorService.clasificarDocumento(resolucion);
	// respuesta.setExito(true);
	// respuesta.setData(documento);
	//
	// } catch (ServiceException e) {
	// e.printStackTrace();
	// respuesta.setExito(false);
	// respuesta.setData(documento);
	// respuesta.setMensaje(e.getMessage());
	// } catch (Exception e) {
	// e.printStackTrace();
	// respuesta.setExito(false);
	// respuesta.setData(documento);
	// respuesta.setMensaje(Constantes.MSJ_ERROR_CLASIFICAR_NUEVO_DOC);
	// }
	// return respuesta;
	// }
}
