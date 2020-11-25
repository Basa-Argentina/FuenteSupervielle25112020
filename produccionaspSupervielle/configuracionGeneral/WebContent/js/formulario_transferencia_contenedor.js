$(document).ready(function() {
	$("#btnGuardar").click(function(){
		$("#btnGuardar").attr("disabled","disabled");
		$("#btnCancelar").attr("disabled","disabled");
		document.forms[0].submit();
	});
	
	$("#btnCancelar").click(function(){
		document.location="index.html";
	});
	
	//binding OnBlur() para los campos con popup		
	$("#codigoContenedorOrigen").blur(function(){
		searchAjax("seleccionContenedor.html?limitarCliente=true",'codigoContenedorOrigen','codigoCliente',getContenedorOrigenCallBack);
	});
	
	$("#codigoContenedorDestino").blur(function(){
		searchAjax("seleccionContenedor.html?limitarCliente=false",'codigoContenedorDestino','codigoCliente',getContenedorDestinoCallBack);
	});
	
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
	if($("#codigoCliente").val()!=""){
		$("#codigoContenedorOrigen").trigger('blur');
		$("#codigoContenedorDestino").trigger('blur');
	}
	
	$("#buscaContenedorOrigen").click(function(){
		$("#contenedor").val("origen");
		$("#triggerBlurAfterSelectItemInPopUp").val("true");
		abrirPopupSeleccion("popUpSeleccionContenedor.html?limitarCliente=true",'codigoCliente',null, $("#mensajeSeleccioneCliente").val());
	});
	
	$("#buscaContenedorDestino").click(function(){
		$("#contenedor").val("destino");
		$("#triggerBlurAfterSelectItemInPopUp").val("true");
		abrirPopupSeleccion("popUpSeleccionContenedor.html?limitarCliente=false",'codigoCliente',null, $("#mensajeSeleccioneCliente").val());
	});
	//binding OnBlur() para el campo codigoContenedor, para cuando se cierra el popup de búsqueda de origen o destino	
	$("#codigoContenedor").blur(function(){
		if($("#contenedor").val()=="origen"){
			$("#codigoContenedorOrigen").val($("#codigoContenedor").val());
			$("#codigoContenedorOrigen").trigger("blur");
		}else{
			$("#codigoContenedorDestino").val($("#codigoContenedor").val());
			$("#codigoContenedorDestino").trigger("blur");
		}
	});
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
	
	try{
		var iframe = document.getElementById('seccionReferencias');
		iframe = iframe.contentWindow;
		iframe.limpiarClasificacionDocumental();
	}catch(e){}
}
function getContenedorOrigenCallBack(sResponseText){
	$("#triggerBlurAfterSelectItemInPopUp").val("false");
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoContenedorOrigenLabel").html(sResponseText);
		searchAjax("validarReferenciaContenedor.html",'codigoContenedorOrigen',null,validarReferenciasContenedorOrigenCallBack);
	}else{
		$("#codigoContenedorOrigenLabel").html("");
		$("#codigoContenedorOrigen").val("");
		
	}
}
function getContenedorDestinoCallBack(sResponseText){
	$("#triggerBlurAfterSelectItemInPopUp").val("false");
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoContenedorDestinoLabel").html(sResponseText);
		searchAjax("validarReferenciaContenedor.html",'codigoContenedorDestino',null,validarReferenciasContenedorDestinoCallBack);
	}else{
		$("#codigoContenedorDestinoLabel").html("");
		$("#codigoContenedorDestino").val("");
	}
}
function validarReferenciasContenedorOrigenCallBack(sResponseText){
	if(sResponseText==''){
		jAlert('El contenedor origen no tiene referencias cargadas');
	}
}
function validarReferenciasContenedorDestinoCallBack(sResponseText){
	if(sResponseText!=''){
		jAlert('El contenedor destino ya tiene referencias cargadas');
	}
}
