<div class="container-fluid" >
	<div  align="left">
		<h3>Clasificador de nuevas resoluciones</h3>
	</div>
	<div class="panel panel-default">
		<div class="panel-body">
			<form id="formClasif" class="form-horizontal" action="clasificador/clasificarDocumentos?${_csrf.parameterName}=${_csrf.token}" method="post" enctype="multipart/form-data" >
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<div class="form-group">
					<div class="col-sm-12">
						Seleccionar ubicación de las nuevas resoluciones a clasificar
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-10">
						<input type="file" id = "resoluciones" name = "file" data-buttonText="Seleccionar Archivos" accept=".pdf" multiple="multiple">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-10">
						<button type="button" class="btn btn-primary" id="btnClasificar">Clasificar</button>
						<button type="button" class="btn btn-default" id="btnLimpiar">Limpiar</button>
					</div>
				</div>
			</form>			
			<div id="barraProgreso" class="progress"  style="display:none;" >
				<div class="progress-bar progress-bar-success progress-bar-striped active" style="width: 0%">
				</div>
			</div>
			<div id="panelResultado" class="panel panel-default" style="margin-top: 40px; display:none;">
				<div class="panel-heading" style="font-size:medium">
					Resultado de la Clasificación
				</div>
				<div class="panel-body" id="panelResultado">
					<table id="example" class="cell-border" style="font-size:13px; width: 100%">
					</table>
				</div>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="idModalMensaje" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
      	<button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="myModalLabel">
            <span id="idTituloDialog"></span>
        </h4>
      </div>
      <div class="modal-body">
         <table id="idModelTbl">
              <tr><td style="width: 60px;"></td>
               <td></td>
               </tr>
         </table>
      </div>
      <div class="modal-footer">
           <div id="idButtonFooter"></div>
        </div>
    </div>
  </div>
</div>

<script src="/sisbr/js/script.js"></script>
<script type="text/javascript">

	var contFiltro = 0;
	var listaDocClasificados = $.parseJSON('${listaDocClasificados}');
	 
	$(document).ready(function(){
		
		$('#example').DataTable( {
	        columns: [
	                  { title: "Resolución"},
	                  { title: "Clase" }
	        ],
	        searching: false,
	        paging: false,
	        info : false
	    } );
				
		$(":file").filestyle({buttonName: "btn-primary"});
		
		$("#btnLimpiar").click(function(){
			limpiar();	
		});
		
		$("#btnClasificar").click(function(){
			startAjax();
			$("#btnClasificar").prop("disabled", true);

			var form = new FormData();
			var files = $('#resoluciones').prop('files');
			for (var i = 0; i < files.length ; i++) {
				form.append(files[i].name, files[i]);
            }
			form.append('${_csrf.parameterName}', '${_csrf.token}');
			$.ajax({
				    url: 'clasificador/clasificarDocumentos?${_csrf.parameterName}=${_csrf.token}',
					data: form,
				    dataType: 'text',
				    method: 'POST',
				    processData: false,
				    contentType: false,
				    success: function(respuesta){
				    	endAjax();
				    	$("#btnClasificar").prop("disabled", false);
				    	respuesta = $.parseJSON(respuesta);
				    	if(respuesta.exito){
				    		pintarResultados(respuesta.lista);
				    	} else{
				    		dialogError(respuesta.mensaje);
				    	}
				    }
			  });
			
// 			var bar = $('.progress-bar');
// 			var cantFiles = $('#resoluciones').prop('files').length;
// 			var aumento = 100/cantFiles;
// 			var porcentaje = 0;
			
// 			$("#barraProgreso").show();
			
// 			var listaDocumentos = [];
			
// 			for(var i = 0; i < cantFiles; i++){
// 				var form = new FormData();
// 				form.append('file', $('#resoluciones').prop('files')[i]);
// 				form.append('${_csrf.parameterName}', '${_csrf.token}');
// 				 $.ajax({
// 					    url: 'clasificador/clasificarDocumentos?${_csrf.parameterName}=${_csrf.token}',
// 						data: form,
// 					    dataType: 'text',
// 					    method: 'POST',
// 					    processData: false,
// 					    contentType: false,
// 					    async: false,
// 					    success: function(respuesta){
// 					    	respuesta = $.parseJSON(respuesta);
// 					    	porcentaje += aumento;
// 					    	bar.width( porcentaje + '%');
// 					    	if(respuesta.exito){
// 					    		listaDocumentos.push(respuesta.data);
// 					    	} else{
// 					    		var doc = {"nombre": respuesta.data.nombre};
// 					    		var clase = {"nombre": respuesta.mensaje};
// 					    		doc["clase"] = clase;
// 					    		listaDocumentos.push(doc);
// 					    	}
// 					    	if(listaDocumentos.length == (cantFiles)){
// 					    		$("#barraProgreso").hide();
// 					      		pintarResultados(listaDocumentos);
// 					      		$("#btnClasificar").prop("disabled", false);
// 					      		endAjax();
// 					    	}
// 					    }
// 					  });
// 			}
		});
		
	});
	
	function pintarResultados(listaDocumentos){
		
		$("#panelResultado").show();		
		$('#example').DataTable().clear();		
		$('.progress-bar').width('0%');
		
		for(var i = 0; i< listaDocumentos.length; i++){
			var documento = listaDocumentos[i];
			$('#example').DataTable().row.add( [
						documento.nombre,
						documento.clase.nombre
			        ] ).draw( false );
		}
	}
	
	function limpiar(){
		$("#panelResultado").show();
		$('.progress-bar').width('0%');
		$('#example').DataTable().clear().draw();
		$('#resoluciones').value = "";
		$("#panelResultado").hide();
		$("#resoluciones").filestyle('clear');
	}
</script>
</html>