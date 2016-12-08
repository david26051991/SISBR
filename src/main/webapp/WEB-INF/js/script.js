
$(document).ajaxStop(function(){
    $("#ajax_loader").hide();
 });
$(document).ajaxStart(function(){
    $("#ajax_loader").show();
});

function startAjax(){
	jQuery.event.trigger("ajaxStart");
}

function endAjax(){
	 jQuery.event.trigger("ajaxStop");
}