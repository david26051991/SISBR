package com.dyd.sisbr.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dyd.sisbr.model.Archivo;
import com.dyd.sisbr.model.Campo;
import com.dyd.sisbr.model.Clase;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.Indice;
import com.dyd.sisbr.model.PalabraClave;
import com.dyd.sisbr.service.BuscadorService;
import com.dyd.sisbr.service.CampoService;
import com.dyd.sisbr.service.DocumentoService;
import com.dyd.sisbr.service.IndiceService;
import com.dyd.sisbr.service.PreprocesadorService;
import com.dyd.sisbr.util.Utils;
import com.dyd.sysbr.bean.RespuestaBean;

@Controller
@RequestMapping("/buscador")
public class BuscadorController extends ExceptionHandlerController {

	@Autowired
	private CampoService campoService;

	@Autowired
	private BuscadorService buscadorService;

	@Autowired
	private DocumentoService documentoService;

	@Autowired
	private PreprocesadorService preprocesadorService;

	@Autowired
	private IndiceService indiceService;

	@RequestMapping("/iniciarConsulta")
	public String inicio(Model model) {

		List<Campo> listaCampos = campoService.obtenerCampos();
		List<Clase> listaClases = buscadorService.obtenerClases();

		model.addAttribute("listaCampos", Utils.getJson(listaCampos));
		model.addAttribute("listaClases", Utils.getJson(listaClases));

		return "busqueda";
	}

	@RequestMapping(value = "/buscar", produces = "application/json; charset=utf-8")
	public @ResponseBody RespuestaBean<Documento> buscar(HttpServletRequest request,
			@RequestParam("strConsulta") String strConsulta, @RequestParam("codClase") String codClase,
			@RequestParam("anioIni") String anioIni, @RequestParam("anioFin") String anioFin) {

		long ini = System.currentTimeMillis();
		List<Documento> lista = buscadorService.buscarDocumentos(strConsulta.toLowerCase(), Integer.parseInt(codClase),
				Integer.parseInt(anioIni), Integer.parseInt(anioFin));
		System.out.println("Tiempo total de busqueda: " + (System.currentTimeMillis() - ini));

		List<Integer> listaIDArch = new ArrayList<>();
		if (lista != null) {
			if (!strConsulta.isEmpty()) {
				List<PalabraClave> listaTokenConsulta = preprocesadorService.preprocesarConsulta(strConsulta);

				// agregar a la lista los anotadores identificados
				List<Indice> indicesConsulta = indiceService.identificarAtributos(strConsulta);
				for (Indice indice : indicesConsulta) {
					PalabraClave token = new PalabraClave();
					token.setRaiz(indice.getDescripcion());
					listaTokenConsulta.add(token);
				}

				for (Documento documento : lista) {
					String resumen = "";
					// String texto =
					// Utils.obtenerTextoPDF(documento.getPath());
					String texto = documentoService.obtenerTextArchivo(documento.getIdArchivo());
					StringTokenizer tokensLinea = new StringTokenizer(texto, "\n");
					while (tokensLinea.hasMoreTokens()) {
						String linea = tokensLinea.nextToken();
						linea = Utils.quitarTildes(linea);
						linea = linea.toLowerCase();
						boolean existe = false;
						for (PalabraClave tokenCon : listaTokenConsulta) {
							if (linea.indexOf(tokenCon.getRaiz()) != -1) {
								existe = true;
								linea = linea.replaceAll(tokenCon.getRaiz(), "<b>" + tokenCon.getRaiz() + "</b>");
							}
						}
						if (existe)
							resumen += "<br>" + linea;
					}
					if (!resumen.isEmpty())
						documento.setResumen(resumen.substring(4));
					else
						documento.setResumen("");
					listaIDArch.add(documento.getIdArchivo());
				}
			} else {
				for (Documento documento : lista) {
					String texto = documentoService.obtenerTextArchivo(documento.getIdArchivo());
					int posIni = texto.indexOf("Visto");
					documento.setResumen(texto.substring(posIni, posIni + 100));
					System.out.println(documento.getTitulo());
					listaIDArch.add(documento.getIdArchivo());
				}
			}
		}
		request.getSession().setAttribute("listaIDArch", listaIDArch);
		RespuestaBean<Documento> respuesta = new RespuestaBean<Documento>();
		respuesta.setExito(true);
		respuesta.setLista(lista);
		return respuesta;
	}

	@RequestMapping("/verPDF/{idArchivo}")
	public void verPDF(HttpServletResponse response, @PathVariable("idArchivo") String idArchivo) {

		Archivo archivo = documentoService.obtenerArchivo(Utils.parseInt(idArchivo));
		File pdfFile = new File(archivo.getPath());
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "inline; filename=" + "rec1.pdf");
		try {
			FileInputStream fileInputStream = new FileInputStream(pdfFile);
			OutputStream responseOutputStream = response.getOutputStream();
			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/exportar")
	public void exportar(HttpServletRequest request, HttpServletResponse response) {
		byte[] buffer = new byte[1024];
		try {
			@SuppressWarnings("unchecked")
			List<Integer> listaIDArch = (List<Integer>) request.getSession().getAttribute("listaIDArch");
			ServletOutputStream op = response.getOutputStream();
			List<File> listaFile = documentoService.obtenerArchivosExportar(listaIDArch);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(baos);

			for (File file : listaFile) {
				ZipEntry ze = new ZipEntry(file.getName());
				zos.putNextEntry(ze);
				FileInputStream in = new FileInputStream(file);
				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				in.close();
			}
			zos.closeEntry();
			// remember close it
			zos.close();
			response.setContentType("application/zip");
			response.addHeader("Content-Disposition", "attachment; filename=" + "ResultadoResoluciones.zip");
			response.setContentLength(baos.size());
			op.write(baos.toByteArray());
			op.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
