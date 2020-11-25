
$(document).ready(function() {
	
// Modificar la casilla de texto de nombre codigoContenedor para que lea 13 d�gitos en lugar de 12
//	$("#chkLectorContenedor").change(function(){
//		if($('#chkLectorContenedor').is(':checked')) {
//			$('#seccionReferencias').contents().find('#codigoContenedor').attr("maxlength", "13");
//		} else {
//			$('#seccionReferencias').contents().find('#codigoContenedor').attr("maxlength", "12");
//		}
//	});
	

	
	//Funcion para deshablitiar la tecla F5
	document.onkeydown = function(){  
	    if(window.event && window.event.keyCode == 116){ 
	     return false;
	    } 
	};
	
	$("#btnGuardar").click(function(){
		$('html, body').animate({ scrollTop: 100 }, 'slow');
		$("#btnGuardar").attr("disabled","disabled");
		$("#btnGuardarContinuar").attr("disabled","disabled");
		$("#btnCancelar").attr("disabled","disabled");
		var div = $("#pop");
		popupOnDiv(div,'darkLayer');
		document.forms[0].submit();
	});
	$("#btnGuardarContinuar").click(function(){
		$('html, body').animate({ scrollTop: 100 }, 'slow');
		$("#btnGuardarContinuar").attr("disabled","disabled");
		$("#btnGuardar").attr("disabled","disabled");
		$("#btnCancelar").attr("disabled","disabled");
		var div = $("#pop");
		popupOnDiv(div,'darkLayer');
		$('#guardarContinuar').val('true');
		$('#bloqueoClasificacion').val($('#seccionReferencias').contents().find('#bloqueoClasificacionDocumental').val());
		$('#bloqueoTipo').val($('#seccionReferencias').contents().find('#bloqueoTipoElementoContenedor').val());
		$('#bloqueoContenedor').val($('#seccionReferencias').contents().find('#bloqueoContenedor').val());
		$('#bloqueoNumero1').val($('#seccionReferencias').contents().find('#bloqueoNumero1').val());
		$('#bloqueoTexto1').val($('#seccionReferencias').contents().find('#bloqueoTexto1').val());
		$('#bloqueoNumero2').val($('#seccionReferencias').contents().find('#bloqueoNumero2').val());
		$('#bloqueoTexto2').val($('#seccionReferencias').contents().find('#bloqueoTexto2').val());
		$('#incrementoElemento').val($('#seccionReferencias').contents().find('#incrementoElemento').val());
		$('#codigoClasificacionDocumentalPadre').val($('#seccionReferencias').contents().find('#codigoClasificacionDocumental').val());
		$('#codigoTipoPadre').val($('#seccionReferencias').contents().find('#prefijoCodigoTipoElemento').val());
		$('#codigoContenedorPadre').val($('#seccionReferencias').contents().find('#codigoContenedor').val());
		$('#codigoContenedorComparar').val($('#seccionReferencias').contents().find('#codigoContenedorComparar').val());
		$('#codigoElementoPadre').val($('#seccionReferencias').contents().find('#codigoElemento').val());
		$('#numero1Padre').val($('#seccionReferencias').contents().find('#numero1').val());
		$('#texto1Padre').val($('#seccionReferencias').contents().find('#texto1').val());
		$('#numero2Padre').val($('#seccionReferencias').contents().find('#numero2').val());
		$('#texto2Padre').val($('#seccionReferencias').contents().find('#texto2').val());
		
		document.forms[0].submit();
	});
	
	$("#btnCancelar").click(function(){
		document.location="consultaLotesReferencia.html";
	});
	$("#btnVolver").click(function(){
		document.location="consultaLotesReferencia.html";
	});
		
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
	
	//Slide 1
	$('#grupoDiv').attr({'style':'display:block'});
	$('#grupoImg').click(function() {
		$('#grupoDiv').slideToggle('slideUp');
		$('#grupoImgSrcDown').slideToggle('slideUp');
		$('#grupoImgSrc').slideToggle('slideUp');
	});
	
	//Tooltips
	$("img[title]").tooltip();
	
	//binding OnBlur() para los campos con popup	
	$("#codigoEmpresa").blur(function(){
		searchAjax('seleccionEmpresa.html','codigoEmpresa',null,getEmpresaCallBack);
	});
	$("#codigoSucursal").blur(function(){
		searchAjax('seleccionSucursal.html','codigoSucursal','codigoEmpresa',getSucursalCallBack);
	});
	$("#codigoCliente").blur(function(){
		searchAjax('seleccionCliente.html','codigoCliente','codigoEmpresa',getClienteCallBack);
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
	
	$("#codigoEmpresa").trigger('blur');
			
});

//ajax
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
	//Modifica Adrian , Oculta Boton Guardar y continuar
	var codclient1 = $("#codigoCliente").val();
	if (codclient1 == "231"){
	
	$("#btnGuardarContinuar").addClass("hiddenInput");
	
	
	}
	
	try{
		var iframe = document.getElementById('seccionReferencias');
		iframe = iframe.contentWindow;
		if($('#codigoClasificacionDocumentalPadre').val()== null || $('#codigoClasificacionDocumentalPadre').val()== ""
			|| $('#codigoClasificacionDocumentalPadre').val()== undefined)
			iframe.limpiarClasificacionDocumental();
	}catch(e){}
}

