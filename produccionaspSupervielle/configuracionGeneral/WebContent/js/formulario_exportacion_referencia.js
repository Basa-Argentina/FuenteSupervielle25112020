$(document).ready(function() {
	
	$('input[name=filtrarPor]:radio').click(checkFiltrarPor);
	
	$("#codigoEmpresa").blur(function(){
		searchAjax('seleccionEmpresa.html','codigoEmpresa',null,getEmpresaCallBack);
	});
	$("#codigoSucursal").blur(function(){
		searchAjax('seleccionSucursal.html','codigoSucursal','codigoEmpresa',getSucursalCallBack);
	});
	$("#codigoCliente").blur(function(){
		searchAjax('seleccionCliente.html','codigoCliente','codigoEmpresa',getClienteCallBack);
	});
	$("#codigoClasificacionDocumental").blur(function(){
		searchAjax('seleccionClasificacionDocumental.html','codigoClasificacionDocumental','codigoCliente',getClasificacionDocumentalCallBack);
	});
	$("#codigoPersonal").blur(function(){
		searchAjax('seleccionEmpleado.html','codigoPersonal','codigoCliente',getEmpleadoCallBack);
	});
	
	$("#buscaEmpresa").click(function(){
		abrirPopupSeleccion("popUpSeleccionEmpresa.html",null,"empresasPopupMap", null);
	});
	$("#buscaSucursal").click(function(){
		abrirPopupSeleccion("popUpSeleccionSucursal.html","codigoEmpresa","sucursalesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionCliente.html","codigoEmpresa","clientesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaClasificacionDocumental").click(function(){
		abrirPopupSeleccion("popUpSeleccionClasificacionDocumental.html",'codigoCliente',"clasificacionDocumentalPopupMap", $("#mensajeSeleccioneCliente").val());
	});
	$("#buscaPersonal").click(function(){
		abrirPopupSeleccion("popUpSeleccionEmpleadoSolicitante.html",'codigoCliente',"personalPopupMap", $("#mensajeSeleccioneCliente").val());
	});
	$('#volver_atras').click(function(){
		document.location="index.html";;
	});
	
	$("#enviarMail").click(function(){
		if($("#enviarMail").is(":checked")){
			$("#enviarConCopia").show();
			$("#envioConCopiaLabel").show();
		}else{
			$("#enviarConCopia").hide();
			$("#envioConCopiaLabel").hide();
		}
	});
	
	checkFiltrarPor();
	
	$("#codigoEmpresa").trigger('blur');
	$("#codigoClasificacionDocumental").trigger('blur');
	$("#codigoPersonal").trigger('blur');

	$('#btnExportar').click(function(){
		$('#elementosSeleccionadosDer option').attr('selected','selected');
		//setTimeout(function(){$('#btnExportar').attr('disabled','disabled');}, 100);
	});
	
});	

function checkFiltrarPor(){
	var input=$("input[name='filtrarPor']:checked").val(); 
	if(input=="ClasificacionDocumental"){
		$("#buscaClasificacionDocumental").show();
		$("#codigoClasificacionDocumental").show();
		$("#codigoClasificacionDocumentalLabel").show();
		$("#codigoPersonal").hide();
		$("#codigoPersonalLabel").hide();
		$("#buscaPersonal").hide();
		$("#enviarMail").hide();
		$("#envioMailLabel").hide();
	}else{
		$("#buscaClasificacionDocumental").hide();
		$("#codigoClasificacionDocumental").hide();	
		$("#codigoClasificacionDocumentalLabel").hide();
		$("#codigoPersonal").show();
		$("#buscaPersonal").show();
		$("#codigoPersonalLabel").show();
		$("#enviarMail").show();
		$("#envioMailLabel").show();
		if($("#enviarMail").is(":checked")){
			$("#enviarConCopia").show();
			$("#envioConCopiaLabel").show();
		}else{
			$("#enviarConCopia").hide();
			$("#envioConCopiaLabel").hide();
		}
	}
}

function getEmpleadoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoPersonalLabel").html(sResponseText);
	else{
		$("#codigoPersonalLabel").html("");
		$("#codigoPersonal").val("");
	}
}

function getClasificacionDocumentalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoClasificacionDocumentalLabel").html(sResponseText);
	}else{
		$("#codigoClasificacionDocumentalLabel").html("");
		$("#codigoClasificacionDocumental").val("");
	}
}

function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#codigoEmpresaLabel").html("");
		$("#codigoEmpresa").val("");
		$("#codigoSucursal").val("");
		$("#codigoCliente").val("");
	}
	$("#codigoSucursal").trigger('blur');
	$("#codigoCliente").trigger('blur');
}
function getSucursalCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#codigoSucursalLabel").html("");
		$("#codigoSucursal").val("");
	}
}
function getClienteCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#codigoClienteLabel").html("");
		$("#codigoCliente").val("");
	}
}