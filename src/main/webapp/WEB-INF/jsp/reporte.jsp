
<div class="container-fluid">
	<div align="left">
		<h3>Reporte de Resoluciones</h3>
	</div>
	<div class="panel panel-default">
		<div class="panel-body">
			<form class="form-horizontal">
				<div class="form-group">
					<label class="control-label col-sm-2" for="cmbListaClases" style="font-weight: normal"> Seleccionar una o varias Clases:</label>
					<div class="col-sm-4">
						<select class="form-control" id="cmbListaClases" size="8" multiple></select>
					</div>
					<div class="col-sm-1" align="center">
						<div class="btn-group-vertical" role="group">
							 <button type="button" class="btn btn-primary" id="btnAddAll">
								<span class="glyphicon glyphicon-forward" aria-hidden="true"></span>
							</button>
							<button type="button" class="btn btn-success" id="btnAdd">
								<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
							</button>
							<button type="button" class="btn btn-success" id="btnSupr">
								<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
							</button>
							<button type="button" class="btn btn-primary" id="btnSuprAll">
								<span class="glyphicon glyphicon-backward" aria-hidden="true"></span>
							</button>
						</div>
					</div>
					<div class="col-sm-4">
						<select class="form-control" id="cmbListaClasesSelect" size="8" multiple></select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2" for="txtAnioIni" style="font-weight: normal"> Desde:</label>
					<div class="col-sm-4">
						<div class='input-group date' id='divFechaIni'>
							<input type='text' class="form-control" id="txtAnioIni" />
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
					<label class="control-label col-sm-1" for="txtAnioFin" style="font-weight: normal"> Hasta:</label>
					<div class="col-sm-4">
						<div class='input-group date' id='divFechaFin'>
							<input type='text' class="form-control" id="txtAnioFin" />
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-11" align="right">
						<button type="button" class="btn btn-primary" id="btnBuscar" style="margin-right:10px">Buscar</button>
						<button type="button" class="btn btn-default" id="btnLimpiar">Limpiar</button>
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
	
	<div class="panel panel-default hidden" id="panelResultado">
		<ul class="nav nav-tabs">
			<li role="presentation" class="active"> <a data-toggle="tab" href="#report1"><i class="glyphicon glyphicon-list-alt"></i> Tabla</a></li>
			<li role="presentation"><a data-toggle="tab" href="#report2"><i class="fa fa-area-chart"></i> Gr&aacute;fico Lineas</a></li>
			<li role="presentation"><a data-toggle="tab" href="#report3"><i class="fa fa-pie-chart"></i> Gr&aacute;fico Porcentajes</a></li>
		</ul>
		<div class="tab-content">
			<div id="report1" class="tab-pane fade in active">
				<div class="row">
                    <div class="col-lg-12">
                        <div class="panel panel-success">
                            <div class="panel-body">
                                <div class="table-responsive">
									<table id="tblResultado" class="display"  cellspacing="0" width="100%" style="font-size: 13px;">
										<thead class="bg-primary">
										</thead>
									</table>
								</div>
                            </div>
                        </div>
                    </div>
                </div>
			</div>
			<div id="report2" class="tab-pane fade">
				<div class="row">
                    <div class="col-lg-12">
                        <div class="panel panel-success">
                            <div class="panel-body">
<!--                                 <div id="morris-area-chart" style="height: 250px;"></div> -->
                                <div id="morris-area-chart"></div>
                            </div>
                        </div>
                    </div>
                </div>
			</div>
			<div id="report3" class="tab-pane fade">
				<div class="row">
                    <div class="col-lg-12">
                        <div class="panel panel-success">
                             <div class="flot-chart">
                                 <div class="flot-chart-content" id="flot-pie-chart"></div>
                             </div>
                        </div>
                    </div>
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
	
<script type="text/javascript">

	var contFiltro = 0;
	var listaClases = $.parseJSON('${listaClases}');
	var respuestaReport = null;
	
	$(document).ready(function(){
		
		initDate([$('#divFechaIni'), $('#divFechaFin')], "YYYY-MM");

		$('#divFechaIni').data('DateTimePicker').date(moment());
		
		$('#divFechaFin').data('DateTimePicker').date(moment());
		
		//cargar combo
		$.each(listaClases, function() { 
	    	$("#cmbListaClases").append($("<option />").val(this.idClase).text(this.nombre)); 
	    }); 
		 
		$("#btnAddAll").click(function(){
			$('#cmbListaClases option').each(function(){
			  	$("#cmbListaClasesSelect").append($("<option />").val(this.value).text(this.text)); 
			  	$("#cmbListaClases option[value='" + this.value + "']").remove();
			});
		});
		
		$("#btnAdd").click(function(){
			$('#cmbListaClases option:selected').each(function(){
			  	$("#cmbListaClasesSelect").append($("<option />").val(this.value).text(this.text)); 
			  	$("#cmbListaClases option[value='" + this.value + "']").remove();
			});
		});
		
		$("#btnSupr").click(function(){
			$('#cmbListaClasesSelect option:selected').each(function(){
			  	$("#cmbListaClases").append($("<option />").val(this.value).text(this.text)); 
			  	$("#cmbListaClasesSelect option[value='" + this.value + "']").remove();
			});
		});
		
		$("#btnSuprAll").click(function(){
			$('#cmbListaClasesSelect option').each(function(){
			  	$("#cmbListaClases").append($("<option />").val(this.value).text(this.text)); 
			  	$("#cmbListaClasesSelect option[value='" + this.value + "']").remove();
			});
		});
		
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			e.preventDefault();
			var target = $(e.target).attr("href") // activated tab
			if(target == "#report2"){
				pintarReporteGraficoArea(respuestaReport);
			}
		});
		
		$("#btnBuscar").click(function(){
			var listClasesSel = [];
			$('#cmbListaClasesSelect option').each(function(){
			  	listClasesSel.push({"idClase": this.value, "nombre": this.text});
			});
			if(listClasesSel.length == 0){
				dialogError("Debe seleccionar por lo menos una clase");
				return;
			}
			if($("#txtAnioIni").val() == "" || $("#txtAnioFin").val() == ""){
				dialogError("Debe seleccionar un rango de fechas");
				return;
			}
			
			if(moment($("#txtAnioIni").val(), "YYYY-MM") > moment($("#txtAnioFin").val(), "YYYY-MM")){
				dialogError("El periodo inicio debe ser menor o igual al periodo final");
				return;
			}
			var fechaIni = $.trim($("#txtAnioIni").val());
			var fechaFin = $.trim($("#txtAnioFin").val());
			
			$("#btnBuscar").prop("disabled", true);
			startAjax();
			$.ajax({
				method: "POST",
				url: "reporte/buscar",
				data:{
					"${_csrf.parameterName}": "${_csrf.token}",
					listClases: JSON.stringify(listClasesSel),
					fechaIni: fechaIni,
					fechaFin: fechaFin
				}
			}).done(function(respuesta){
				endAjax();
				respuestaReport = respuesta.data
				if(respuesta.exito){
					$("#panelResultado").removeClass("hidden");
					pintarReporteTabla(respuesta.data);
					pintarReporteGraficoArea(respuesta.data);
					pintarReporteGraficoPie(respuesta.data);
				} else{
					dialogError(respuesta.mensaje);
				}
				$("#btnBuscar").prop("disabled", false);
		            
			}).error(function(jqxhr, textStatus, errorThrown){
			      alert(textStatus + "\n" + errorThrown);
		    });
		});
	});
	
	function pintarReporteTabla(reporte){
		
		var listaColumnas = [];
		listaColumnas.push({ "title": "CLASE", "data": "clase", sClass: 'text-left'});
		$.each(reporte.listaCabeceraFechas, function(i, item){
			var column = { "title": item, "data": item, sClass: 'text-center'};
			listaColumnas.push(column);
		});
		var columnTotal = { "title": "Total",  
							"data" : function ( row, type, val, meta ) {
										var suma = 0;
										$.each(reporte.listaCabeceraFechas, function(i, item){
											suma += row[item];
										});
										return suma;
									},						
							"sClass": 'text-center th-total'};
		listaColumnas.push(columnTotal);
		
		var listaData = [];
		$.each(reporte.listaDetalleCantidad, function(i, item){
			var fila = item.mapCantResoluciones;
			fila["clase"] = item.nombreClase;
			listaData.push(fila);
		});
		
		var strFootTabla = "<tfoot><tr><th>Total:</th>";
		for(var i = 0; i < listaColumnas.length - 1; i++){
			strFootTabla += "<th></th>";
		}
		strFootTabla += "</tr></tfoot>";
		
		if ($.fn.DataTable.isDataTable("#tblResultado")){
			$("#tblResultado").DataTable().clear();
			$("#tblResultado").DataTable().destroy();
		}
		$('#tblResultado').empty();
		$('#tblResultado').html('<table id="tblResultado" class="display"  cellspacing="0" width="100%" style="font-size: 13px;">'+
								'<thead class="bg-primary"></thead>' + strFootTabla +
								'</table>');
		
		$("#tblResultado").DataTable({
			data: listaData,
			columns: listaColumnas,
			footerCallback: function ( row, data, start, end, display ) {
	            var api = this.api(), data;
	            // Remove the formatting to get integer data for summation
	            var intVal = function ( i ) {
	                return typeof i === 'string' ?
	                    i.replace(/[\$,]/g, '')*1 :
	                    typeof i === 'number' ?
	                        i : 0;
	            };
	            $.each(listaColumnas, function(i, item){
	            	if(i > 0){
	            		total = api.column(i).data().reduce(function (a, b){return intVal(a) + intVal(b);}, 0);
			            // Update footer
			            $( api.column(i).footer() ).html(total);
	            	}
	            });
	        },
			searching: false,
			bLengthChange: false,
			paginate: false,
			ordering: false,
			info: false
	    });
	}
	
	function pintarReporteGraficoArea(reporte){
		var arrayClases = [];
		var arrayData = [];
		$.each(reporte.listaDetalleCantidad, function(i, item){
			arrayClases.push(item.nombreClase);
		});
		
		$.each(reporte.listaCabeceraFechas, function(i, fecha){
			var data = {'periodo': fecha};
			$.each(reporte.listaDetalleCantidad, function(i, item){
				data[item.nombreClase] = item.mapCantResoluciones[fecha];
			});			
			arrayData.push(data);
		});
		
		$("#morris-area-chart").html("");
		
		new Morris.Area({
		  element: 'morris-area-chart',
		  data: arrayData,
		  xkey: 'periodo',
		  ykeys: arrayClases,
		  labels: arrayClases
		});
		
	}
	
	function pintarReporteGraficoPie(reporte){
		
		var arrayData = [];
		$.each(reporte.listaDetalleCantidad, function(i, item){
			var sum = 0;
			$.each(reporte.listaCabeceraFechas, function(i, fecha){
				sum += item.mapCantResoluciones[fecha];
			});
			var data = {label: item.nombreClase, data: sum};
			arrayData.push(data);
		});
		
		var plotObj = $.plot($("#flot-pie-chart"), arrayData, {
	        series: {
	            pie: {
	                show: true
	            }
	        },
	        grid: {
	            hoverable: true
	        },
	        tooltip: true,
	        tooltipOpts: {
	            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
	            shifts: {
	                x: 20,
	                y: 0
	            },
	            defaultTheme: false
	        }
	    });
	}
	
</script>
