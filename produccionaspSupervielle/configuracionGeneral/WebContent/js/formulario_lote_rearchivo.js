
$(document).ready(function() {
	$("#btnGuardar").click(function(){
		popupOnDiv($('#pop'),'darkLayer');
		document.forms[0].submit();
	});
	$("#btnCancelar").click(function(){
		popupOnDiv($('#pop'),'darkLayer');
		document.location="consultaLotesRearchivo.html";
	});
	$("#btnVolver").click(function(){
		popupOnDiv($('#pop'),'darkLayer');
		document.location="consultaLotesRearchivo.html";
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
	$("#codigoRemito").blur(function(){
		getAjaxRemito('remitoServlet','codigo','codigoRemito',getRemitoCallBack);
	});
	
	$("#buscaEmpresa").click(function(){
		abrirPopupSeleccion("popUpSeleccionEmpresa.html",null,null,"empresasPopupMap", null);
	});
	$("#buscaSucursal").click(function(){
		abrirPopupSeleccion("popUpSeleccionSucursal.html","codigoEmpresa","sucursalesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionCliente.html","codigoEmpresa","clientesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaRemito").click(function(){
		abrirPopupSeleccion("popUpSeleccionRemito.html",'codigoCliente',"remitoPopupMap", $("#mensajeSeleccioneCliente").val());
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
	
	try{
		var iframe = document.getElementById('seccionRearchivos');
		iframe = iframe.contentWindow;
		iframe.limpiarClasificacionDocumental();
	}catch(e){}
}

//ajax
function getRemitoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoRemitoLabel").html(sResponseText);
	}else{
		$("#codigoRemitoLabel").html("");
		$("#codigoRemito").val("");
	}
}

//ajax
function getAjaxRemito(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#codigoCliente").val()+"&codigoEmpresa="+$("#codigoEmpresa").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

