<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!--     <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SB Admin - Bootstrap Admin Template</title>

	<!-- CSS -->
    <link href="/sisbr/webjars/bootstrap/3.3.7/css/bootstrap.css" rel="stylesheet">
    <link href="/sisbr/css/sb-admin.css" rel="stylesheet">
    <link href="/sisbr/css/estilos.css" rel="stylesheet">
    <link href="/sisbr/css/morris.css" rel="stylesheet">
    <link href="/sisbr/webjars/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
	<link href="/sisbr/webjars/eonasdan-bootstrap-datetimepicker/4.17.37/build/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
	<link href="/sisbr/webjars/datatables/1.10.10/css/jquery.dataTables.min.css" rel="stylesheet">
	<link href="/sisbr/img/favicon.png" rel="shortcut icon">
	
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.html">SBRR</a>
            </div>
            <!-- Top Menu Items -->
            <form id="formInicio" action="/sisbr/logout" method="POST">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>	
			</form>
            <ul class="nav navbar-right top-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> <strong id="username"></strong><b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li>
                            <a onclick="cerrarSession()" ><i class="fa fa-fw fa-power-off"></i> Log Out</a>
                        </li>
                    </ul>
                </li>
            </ul>
            <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav side-nav" id="ulOpciones">
                    <li class="menu active">
              	          <a class="opcion" href="#" data-url="iniciarConsulta"><i class="fa fa-fw fa-search"></i> B&uacute;squeda</a>
                    </li>
                    <li class="menu">
                        <a class="opcion" href="#" data-url="iniciarClasificacion"><i class="fa fa-fw fa-edit"></i> Registro</a>
                    </li>
                    <li class="menu">
                        <a href="javascript:;" data-toggle="collapse" data-target="#demo"><i class="fa fa-fw fa-wrench"></i> Mantenimiento <i class="fa fa-fw fa-caret-down"></i></a>
                        <ul id="demo" class="collapse">
                            <li>
                                <a class="opcion" href="#" data-url="iniciarMantResolucion">Resoluci&oacute;n</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </nav>

        <div id="page-wrapper" style="padding:0px">

            <div class="container-fluid">
<!-- 				<iframe id="iframetab" src="iniciarConsulta" style="border: 0; width: 100%; height: 580px">Load Failed?</iframe> -->
				<div id="cargaHtml" style="border: 0px; width: 100%; min-height: 600px; height: 100%"></div>
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

	<!-- javascript -->
	<script src="/sisbr/webjars/jquery/1.11.2/jquery.min.js"></script>
	<script src="/sisbr/webjars/bootstrap/3.3.7/js/bootstrap.js"></script>
	<script src="/sisbr/js/moment-with-locales.js"></script>
	<script src="/sisbr/webjars/eonasdan-bootstrap-datetimepicker/4.17.37/build/js/bootstrap-datetimepicker.min.js"></script>
	<script src="/sisbr/webjars/datatables/1.10.10/js/jquery.dataTables.min.js"></script>
	<script src="/sisbr/js/bootstrap-filestyle.min.js"></script>
	<script src="/sisbr/webjars/jquery.inputmask/3.1.0/jquery.inputmask.bundle.min.js" type="text/javascript"></script>
	<!-- Morris Charts JavaScript -->
	<script src="/sisbr/js/morris/raphael.min.js"></script>
	<script src="/sisbr/js/morris/morris.min.js"></script>
	 <!-- FLOT JavaScript -->
	 <script src="/sisbr/js/flot/jquery.flot.js"></script>
	 <script src="/sisbr/js/flot/jquery.flot.pie.js"></script>
	 <script src="/sisbr/js/flot/jquery.flot.resize.js"></script>
	 <script src="/sisbr/js/flot/jquery.flot.tooltip.min.js"></script>
	 <script src="/sisbr/js/flot/excanvas.min.js"></script>
	<script src="/sisbr/js/script.js"></script>
</body>

<script type="text/javascript">
	 
	$(document).ready(function(){
		
		$("#username").html("${username}");
		var listaOpciones = $.parseJSON('${listaOpciones}');
		var strHtml = "";
		$.each(listaOpciones, function(i, objOpcion){
			if(objOpcion.listaHijos != null){
				strHtml += ' <li class="menu">'
						+ '<li class="menu">'
	                	+ '<a href="javascript:;" data-toggle="collapse" data-target="#demo"><i class="fa fa-fw fa-wrench"></i> ' + objOpcion.nombre + ' <i class="fa fa-fw fa-caret-down"></i></a>'
	                	+ '<ul id="demo" class="collapse">';
				$.each(objOpcion.listaHijos, function(i, objHijo){
					strHtml += '<li>'
	                    	+ '<a class="opcion" href="#" data-url="' + objHijo.link + '">' + objHijo.nombre + '</a>'
	                		+ '</li>';
				});
	            strHtml += '</ul>'
	            		+ '</li>';
			} else{
				strHtml += ' <li class="menu">'
	    	          + '<a class="opcion" href="#" data-url="' + objOpcion.link + '"><i class="glyphicon glyphicon-triangle-right"></i> '+ objOpcion.nombre + '</a>'
	                  + '</li>';	
			}
		});
		$("#ulOpciones").html(strHtml);
		
		$(".opcion").each(function(){
		    var el = $(this);
		    el.click(function() {
// 		        $("#iframetab").attr("src", el.data('url'));
				$.get(el.data('url'), function(htmlexterno){
					   $("#cargaHtml").html(htmlexterno);
		    	});
		        $(".opcion").removeClass("active");
		        $(this).addClass("active");
		    });
		});
		
		$(".menu").click(function(){
			$(".menu").removeClass("active")
			$(this).addClass("active");
		});
	});	
	
	function cerrarSession(){
		document.getElementById("formInicio").submit();
	}
</script>

</html>