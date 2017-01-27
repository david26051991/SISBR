
function initDate(lista, formato){
		//Iniciar Calendario
	  	$('.date').datetimepicker({
			format: formato,
			useCurrent: false,
			showClose:true
        });
 		$.each(lista, function(i, item) {
 			lista[i].inputmask("date",{ "placeholder": formato});
 		});
}

function dialogConfirm(text, funcBtnAceptar, funcBtnCancelar) {
	
	var params = {
		id : "idModalMensaje",
		title : "Confirmaci\u00f3n",
		icon : "<i style='color: #337ab7;' class='fa fa-question-circle fa-4x'></i>",
		textContent : text,
		buttons : "<button id='idAceptarConfig' type='button' class='btn btn-default'>Aceptar</button>"
				+ "<button id='idCancelarConfig' type='button' class='btn btn-default' >Cancelar</button>"
	};

	dialogCustom(params);
	if (funcBtnAceptar != undefined) {
		$("#idAceptarConfig").on("click", function() {
			$('#' + params.id).modal('hide');
			funcBtnAceptar();
		});

	}

	$('#idCancelarConfig').on('click', function(e) {
		$('#' + params.id).modal('hide');
		if (funcBtnCancelar != undefined) {
			funcBtnCancelar();
		}
	});

}

function dialogAlert(text,funcBtnAceptar) {

	var params = {
		id : "idModalMensaje",
		title : "Alerta",
		icon : "<i style='color: #337ab7;' class='fa fa-exclamation-triangle fa-4x'></i>",
		textContent : text,
		buttons : "<button id='idAceptarAlert' type='button' class='btn btn-default'>Aceptar</button>"
	};

	dialogCustom(params);
	$('#idAceptarAlert').on('click', function(e) {
		$('#' + params.id).modal('hide');
		if (funcBtnAceptar != undefined) {
			funcBtnAceptar();
		}
	});
}

function dialogInfo(text,funcBtnAceptar) {

	var params = {
		id : "idModalMensaje",
		title : "Mensaje",
		icon : "<i style='color: #337ab7;' class='fa fa-info-circle fa-4x'></i>",
		textContent : text,
		buttons : "<button id='idAceptarInfo' type='button' class='btn btn-default'>Aceptar</button>"
	};

	dialogCustom(params);
	$('#idAceptarInfo').on('click', function(e) {
		$('#' + params.id).modal('hide');
		if (funcBtnAceptar != undefined) {
			funcBtnAceptar();
		}
	});
}

function dialogError(text,funcBtnAceptar) {

	var params = {
		id : "idModalMensaje",
		title : "Error",
		icon : "<i style='color: #337ab7;' class='fa fa-times-circle fa-4x'></i>",
		textContent : text,
		buttons : "<button id='idAceptarError' type='button' class='btn btn-default'>Aceptar</button>"
	};

	dialogCustom(params);
	$('#idAceptarError').on('click', function(e) {
		$('#' + params.id).modal('hide');
		if (funcBtnAceptar != undefined) {
			funcBtnAceptar();
		}
	});
}

function dialogCustom(dialog) {

	$('#' + dialog.id).modal({
		keyboard : false
	});
	$('#' + dialog.id).modal('show');
	$("#idTituloDialog").text(dialog.title);
	$("#idModelTbl tr td:eq(0)").html(dialog.icon);
	$("#idModelTbl tr td:eq(1)").html(dialog.textContent);
	$("#idButtonFooter").html(dialog.buttons);
}

$(document).ajaxStop(function(){
    $("#ajax_loader").hide();
 });
$(document).ajaxStart(function(){
    $("#ajax_loader").show();
});

//function startAjax(){
//	jQuery.event.trigger("ajaxStart");
//}
//
//function endAjax(){
//	 jQuery.event.trigger("ajaxStop");
//}

//GESTION DE LOADERS
function startAjax() {
	showLoader.show();
}

function endAjax() {
	showLoader.hide();
}

//FUNCION PARA CARGAR EL LOADER
var showLoader = (function($) {
	var $dialog = $('<div class="modal fade" id="idModalLoading" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true">'
					+ '<div class="modal-dialog modal-loader" style="width: 62px !important;" >'
					+ '<div class="modal-content">'
					+ '<img src="/sisbr/img/wait-big.gif" style="margin-left:5px">'
					+ '</div></div></div>');
	return {
		show : function() {
			$dialog.modal();
		},
		hide : function() {
			
			$dialog.modal('hide');
		}

	};
})(jQuery);