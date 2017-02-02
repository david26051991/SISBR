<div class="container-fluid" >
	<div  align="left">
		<h3>Buscador de Resoluciones</h3>
	</div>
	<div class="panel panel-default">
		<div class="panel-body">
			<form class="form-horizontal">
				<div class="form-group">
					<div class="col-sm-3">
						<select class="form-control" id="cmbClase"></select>
					</div>
					<label class="control-label col-sm-1" for="txtAnioIni" style="font-weight: normal"> Desde:</label>
					<div class="col-sm-2">
						<div class='input-group date' id='divFechaIni'>
							<input type='text' class="form-control" id="txtAnioIni" />
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
					<label class="control-label col-sm-1" for="txtAnioFin" style="font-weight: normal"> Hasta:</label>
					<div class="col-sm-2">
						<div class='input-group date' id='divFechaFin'>
							<input type='text' class="form-control" id="txtAnioFin" />
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-10">
						<input type="text" class="form-control col-sm-10" id="txtConsulta" placeholder="Ingresar términos a buscar" autofocus>
					</div>
					<div class="col-sm-2">
						<button type="button" class="btn btn-primary" id="btnBuscar">Buscar</button>
					</div>	
				</div>
			</form>
		</div>
		<div class="panel-footer" style="display:none;" id="panelFiltro">
			<div align="right">
				<button type="button" class="btn btn-default" id="btnAgregarFiltro">Agregar</button>
				<button type="button" class="btn btn-primary" id="btnFiltroBuscar">Buscar</button>
			</div>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="panel-body hidden" id="panelResultado">
			Resultados
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
	var listaCampos = $.parseJSON('${listaCampos}');
	var listaClases = $.parseJSON('${listaClases}');
	 
	$(document).ready(function(){
		
		$('#divFechaIni').datetimepicker({
            viewMode: 'years',
            format: 'YYYY'
        });
		
		$('#divFechaIni').data('DateTimePicker').date(moment());
		
		$('#divFechaFin').datetimepicker({
            viewMode: 'years',
            format: 'YYYY'
        });
		
		$('#divFechaFin').data('DateTimePicker').date(moment());
		
		//cargar combo
		$("#cmbClase").append($("<option />").val("0").text("Seleccionar Clase")); 
		$.each(listaClases, function() { 
	    	$("#cmbClase").append($("<option />").val(this.idClase).text(this.nombre)); 
	    }); 
		
		$("#btnFiltroBuscar").click(function(){
			
			var strConsulta = "";
			
			//guardar temp valores ingresados en los filtros
			for(var i= 1; i<=contFiltro; i++){
				var operador = $("#cmbOperacion"+i).val();
				var consulta = $("#txtFiltro"+i).val();
				var campo = $("#cmbCampo"+i).val();
				
				if(operador != undefined && $.trim(consulta) != "" && campo != "0"){
					strConsulta = strConsulta + $("#cmbOperacion"+i+" :selected").text() + "(" + $("#cmbCampo"+i+" :selected").text() + " : " + $.trim(consulta) +")";	
				} else if (operador == undefined && $.trim(consulta) != "" && campo != "0"){
					strConsulta = "(" + $("#cmbCampo"+i+" :selected").text() + " : " + $.trim(consulta) +")";
				}	
			}
			
			$("#txtConsulta").val(strConsulta);
			$("#panelFiltro").hide();
			$("#btnBuscar").show();
			$("#btnBuscar").trigger("click");
		});
		
		$("#txtConsulta").keypress( function(e) {
			 if(e.which === 13){
				 $("#btnBuscar").trigger("click");
			 }
		});
		 
		$("#btnBuscar").click(function(){			
			startAjax();
			$("#btnBuscar").prop("disabled", true);
			 var numAnioIni = $.trim($("#txtAnioIni").val()) != "" ? $.trim($("#txtAnioIni").val()) : "0";
			 var numAnioFin = $.trim($("#txtAnioFin").val()) != "" ? $.trim($("#txtAnioFin").val()) : "0";
			$.ajax({
				method: "POST",
				url: "buscador/buscar",
				data: {
					"${_csrf.parameterName}": "${_csrf.token}",
					strConsulta: $.trim($("#txtConsulta").val()),
					codClase: $("#cmbClase").val(),
					anioIni: numAnioIni,
					anioFin: numAnioFin
				}
			}).done(function(respuesta){
				endAjax();
				$("#btnBuscar").prop("disabled", false);
				if(respuesta.exito){
					pintarListaDocumentos(respuesta.lista);
				} else{
					dialogError(respuesta.mensaje);
				}
			}).error(function(jqxhr, textStatus, errorThrown){
			      alert(textStatus + "\n" + errorThrown);
		    });
		});
	});
	
	function pintarListaDocumentos(lista){
		var strlHtml = "";
		var msjCantidadResultados = "";
		
		if(lista != null && lista != ""){
			if(lista.length == 1){
				msjCantidadResultados = "Se encontró " + lista.length + " resultado.";
			} else{
				msjCantidadResultados = "Se encontraron " + lista.length + " resultados.";
			}
			
			strlHtml += '<div class="row" style="padding-bottom:10px;">';
			strlHtml += '<div class="col-sm-9" style="font-size:10pt;color:silver;padding-top:5px;"><p>' + msjCantidadResultados + '</p></div>';
			strlHtml += '</div>';
			
			for(var i=0; i<lista.length;i++){
				var documento = lista[i];
				strlHtml += '<div class="panel panel-default">'
				     + '<div class="panel-heading"><label>' + documento.titulo + "<font color='#2d7da4'> &mdash; " + getDescClase(documento.idClase) + '</font></label></div>'
				     + '<div class="panel-body">' 
				     + '<div class="col-sm-11">' + documento.resumen + '... </div>'
				     + '</div></div>';
			}
		}else{
			strlHtml += 'No se encontraron resultados';
		}
		
		$("#panelResultado").removeClass("hidden");
		$("#panelResultado").html(strlHtml);		
	}
	
	function getDescClase(idClase){
		var texto = $("#cmbClase option[value='" + idClase + "']").text();
		return texto;
	}
	
</script>