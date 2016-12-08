<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Sistema de Búsqueda de Resoluciones Rectorales</title>

        <!-- CSS -->
        <link href="/sisbr/webjars/bootstrap/3.3.7/css/bootstrap.css" rel="stylesheet">
		<link href="/sisbr/webjars/eonasdan-bootstrap-datetimepicker/4.17.37/build/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
		<link href="/sisbr/webjars/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
		<link href="/sisbr/webjars/datatables/1.10.10/css/jquery.dataTables.min.css" rel="stylesheet">
		<link href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500" rel="stylesheet">
		<link href="/sisbr/css/form-elements.css" rel="stylesheet">
		<link href="/sisbr/css/style.css" rel="stylesheet">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

        <!-- Favicon and touch icons -->
        <link rel="shortcut icon" href="/sisbr/img/favicon.png">
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="/sisbr/img/apple-touch-icon-144-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/sisbr/img/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/sisbr/img/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="/sisbr/img/apple-touch-icon-57-precomposed.png">

    </head>

    <body>

        <!-- Top content -->
        <div class="top-content">
        	
            <div class="inner-bg">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-8 col-sm-offset-2 text">
                            <h1><strong>Sistema de B&uacute;squeda de Resoluciones Rectorales</strong></h1>
<!--                             <div class="description"> -->
<!--                             	<p> -->
<!-- 	                            	This is a free responsive login form made with Bootstrap.  -->
<!-- 	                            	Download it on <a href="http://azmind.com"><strong>AZMIND</strong></a>, customize and use it as you like! -->
<!--                             	</p> -->
<!--                             </div> -->
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-6 col-sm-offset-3 form-box">
                        	<div class="form-top">
                        		<div class="form-top-left">
                        			<h3>Iniciar sesi&oacute;n</h3>
                            		<p>Ingrese el usuario y contraseña para acceder:</p>
                        		</div>
                        		<div class="form-top-right">
                        			<i class="fa fa-key"></i>
                        		</div>
                            </div>
                            <div class="form-bottom">
			                    <form role="form" action="login" method="POST" class="login-form"  >
			                    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			                    	<div class="form-group">
			                    		<label class="sr-only" for="form-username">Usuario</label>
			                        	<input type="text" name="username" id="username" placeholder="Usuario..." class="form-username form-control" id="form-username">
			                        </div>
			                        <div class="form-group">
			                        	<label class="sr-only" for="form-password">Contraseña</label>
			                        	<input type="password" name="password" id="password" placeholder="Contraseña..." class="form-password form-control" id="form-password">
			                        </div>
			                        <div class="form-group" id="div-error">
			                        	<div id="msj-error" class="control-label"></div>
			                        </div>
			                        <button class="btn" id="btnLogin">Iniciar</button>
			                    </form>
		                    </div>
                        </div>
                    </div>
                </div>
            </div>
            
        </div>


        <!-- Javascript -->
        <script src="/sisbr/webjars/jquery/1.11.2/jquery.min.js"></script>
		<script src="/sisbr/webjars/bootstrap/3.3.7/js/bootstrap.js"></script>		
		<script src="/sisbr/webjars/moment/2.16.0/moment.js"></script>
		<script src="/sisbr/webjars/eonasdan-bootstrap-datetimepicker/4.17.37/build/js/bootstrap-datetimepicker.min.js"></script>
		<script src="/sisbr/webjars/datatables/1.10.10/js/jquery.dataTables.min.js"></script>
		<script src="/sisbr/webjars/jquery-validation/1.13.1/dist/jquery.validate.js"> </script>
		<script src="/sisbr/webjars/jquery-validation/1.13.1/dist/additional-methods.min.js"> </script>
		<script src="/sisbr/webjars/jquery-validation/1.13.1/src/localization/messages_es.js"> </script>
		<script src="/sisbr/js/jquery.backstretch.min.js"></script>
        <!--[if lt IE 10]>
            <script src="/sisbr/js/placeholder.js"></script>
        <![endif]-->

    </body>
	<script type="text/javascript">
	$(document).ready(function(){
		
		$.backstretch("/sisbr/img/1.jpg");
		
		var msj = '${error}';
		if(msj != ""){
			$('#msj-error').html(msj);
    		$('#div-error').addClass("has-error");
		}
	
	    $('#btnLogin').on('click', function(e) {
	    	e.preventDefault();
	    	if($('#username').val() == "" || $('#password').val() == ""){
	    		$('#msj-error').html("Ingresar usuario y/o contraseña");
	    		$('#div-error').addClass("has-error");
	    		return;
	    	}
	    	$('.login-form').submit();
	    });
	});
	</script>
</html>