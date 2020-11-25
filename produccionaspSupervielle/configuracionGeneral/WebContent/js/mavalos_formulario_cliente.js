//nombres de componentes dependientes del pais
	var paisDep = new Array();
	paisDep[0] = "provincia";
	paisDep[1] = "localidad";
	paisDep[2] = "barrio";

$(document).ready(function() {		
	var urlParams = {};
	(function () {
	    var e,
	        a = /\+/g,  // Regex for replacing addition symbol with a space
	        r = /([^&=]+)=?([^&]*)/g,
	        d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
	        q = window.location.search.substring(1);

	    while (e = r.exec(q))
	       urlParams[d(e[1])] = d(e[2]);
	})();

	$("#observaciones").val($.trim($('#observaciones').val()));
	
	$("#notasFacturacion").val($.trim($('#notasFacturacion').val()));
	
	//Tooltips
	$("img[title]").tooltip();
	
	//Slide 1
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	//Slide 1
	$('#grupoDiv').attr({'style':'display:block'});
	$('#grupoImg').click(function() {
		$('#grupoDiv').slideToggle('slideUp');
		$('#grupoImgSrcDown').slideToggle('slideUp');
		$('#grupoImgSrc').slideToggle('slideUp');
	});

	//binding OnBlur() para los campos con popup
	$("#codigoEmpresa").blur(function(){
		getAjaxCliente('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
  	});
	$("#pais").blur(function(){
		getAjax('paisServlet','nom','pais', null,getPaisCallBack);
	});
	$("#provincia").blur(function(){
		getAjax('provinciasPorPaisServlet','nom','provincia','pais',getProvinciaCallBack);
	});
	$("#localidad").blur(function(){
		getAjax('localidadesPorProvinciaServlet','nom','localidad','provincia',getLocalidadCallBack);
	});
	$("#barrio").blur(function(){
		getAjax('barriosPorLocalidadServlet','nom','barrio','localidad',getBarrioCallBack);
	});	
	$("#tipoPersona").change(function(){
		seleccionarCombos();
	});	
	
	$("#mes").chromatable({
		width: "150px",
		height: "150px",
		scrolling: "yes"
	});
});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarCliente.html";
}
function volverCancelar(){
	document.location="mostrarCliente.html";
}
function guardarYSalir(){
	seleccionarTodos("listasSeleccionadas");
	var listaDef=$('#listasSeleccionadasDef option:selected').val();
	$('#listaDefecto').val(listaDef);
	var valor = document.getElementById("numeroDoc");
	var tipo = document.getElementById("idTipoDocSel").options[document.getElementById("idTipoDocSel").selectedIndex];
	$("#idBarrio").val($("#barrioLabel").html());
	if(tipo.text == "CUIL" ||  tipo.text == "CUIT"){
		if(validarCuit(valor.value)){
			document.forms[0].submit();
		} 
	}else{
		document.forms[0].submit();
	}
}

function abrirPopupProvincia(claseNom){
	//uso clienteCodigo porque esta en duro y si se cambia dejan de funcionar otras pantallas
	var url = "precargaFormularioCliente.html?clienteCodigo="+$("#paisLabel").html();
	abrirPopupGeneral(claseNom, url);
}
function abrirPopupLocalidad(claseNom){
	//uso clienteCodigo porque esta en duro y si se cambia dejan de funcionar otras pantallas
	var url = "precargaFormularioCliente.html?clienteCodigo="+$("#provinciaLabel").html();
	abrirPopupGeneral(claseNom, url);
}
function abrirPopupBarrio(claseNom){
	//uso clienteCodigo porque esta en duro y si se cambia dejan de funcionar otras pantallas
	var url = "precargaFormularioCliente.html?clienteCodigo="+$("#localidadLabel").html();
	abrirPopupGeneral(claseNom, url);
}
function abrirPopupGeneral(claseNom, url){
	jQuery.ajax({
        url: url,
        success: function(data){
           filteredResponse =  $(data).find(this.selector);
           if(filteredResponse.size() == 1){
                $(this).html(filteredResponse);                            
           }else{
                $(this).html(data);
           }
           $(this).displayTagAjax();
        } ,
        data: ({"time":new Date().getTime()}),
        context: $(".displayTagDiv."+ claseNom)
    });
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}


//ajax 
function getAjax(url, varName, elementId, parentId, callBack){
	var input = document.getElementById(elementId);
	var parentValueId = "";
	if(parentId != null)
		parentValueId = '&val='+$("#"+parentId+"Label").html();
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+parentValueId;	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getAjaxCliente(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getPaisCallBack(sResponseText){	
	var componentId = "pais";
	setResponce(sResponseText, componentId);
	for(i=0;i<paisDep.length;i++){
		$("#"+paisDep[i]).val("");
		$("#"+paisDep[i]+"Label").html("");
	}	
}
//ajax
function getProvinciaCallBack(sResponseText){	
	var componentId = "provincia";
	setResponce(sResponseText, componentId);
	for(i=1;i<paisDep.length;i++){
		$("#"+paisDep[i]).val("");
		$("#"+paisDep[i]+"Label").html("");
	}	
}
//ajax
function getLocalidadCallBack(sResponseText){	
	var componentId = "localidad";
	setResponce(sResponseText, componentId);
	for(i=2;i<paisDep.length;i++){
		$("#"+paisDep[i]).val("");
		$("#"+paisDep[i]+"Label").html("");
	}	
}
//ajax
function getBarrioCallBack(sResponseText){	
	var componentId = "barrio";
	setResponce(sResponseText, componentId);	
}
//ajax
function setResponce(sResponseText, componentId){
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split("-");
		$("#"+componentId).val(array[0]);		
		$("#"+componentId+"Label").html(array[1]);		
	}else{
		$("#"+componentId).val("");
		$("#"+componentId+"Label").html("");
	}	
}
//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#codigoEmpresaLabel").html("");
	}
}
function addOptionToSelect(selectComponent, option){
	if($.browser.msie)
		selectComponent.add(option); //IE
	else
		selectComponent.add(option,null); //Mozilla, Chrome
}	

function seleccionarCombos(){
	var selIndex = document.forms[0].tipoPersona.selectedIndex;
	comboValue = document.forms[0].tipoPersona.options[selIndex].value;
	
	
	var seleccionTipoDocumento = 0;
	var seleccionIVA = 0;
	if(comboValue != null && comboValue == 'Juridica'){
		seleccionTipoDocumento = 1; //CUIT
		seleccionIVA = 1; //Resp Inscripto
		$("#nombre").removeAttr("disabled");
		$("#apellido").removeAttr("disabled");
		$("#razonSocial").removeAttr("disabled");
		$("#nombre").attr('disabled', true);
		$("#apellido").attr('disabled', true);
		$("#nombre").removeClass('requerido');
		$("#apellido").removeClass('requerido');
		$("#razonSocial").addClass('requerido');
	}
	if(comboValue != null && comboValue == 'Fisica'){
		seleccionTipoDocumento = 8; //DNI
		seleccionIVA = 5; //Consumidor Final
		$("#nombre").removeAttr("disabled");
		$("#apellido").removeAttr("disabled");
		$("#razonSocial").removeAttr("disabled");
		$("#razonSocial").attr('disabled', true);
		$("#nombre").addClass('requerido');
		$("#apellido").addClass('requerido');
		$("#razonSocial").removeClass('requerido');
	}
	if($("#accion").val()=="NUEVO"){
		var comboTipoDocumento = document.forms[0].idTipoDocSel;
		var cantidadTipoDocumento = comboTipoDocumento.length;
		for (i = 0; i < cantidadTipoDocumento; i++) {
			if (comboTipoDocumento[i].value == seleccionTipoDocumento) {
				comboTipoDocumento[i].selected = true;
		    }   
		}
	}
	
	var comboIVA = document.forms[0].idAfipCondIva;
	var cantidadIVA = comboIVA.length;
	for (i = 0; i < cantidadIVA; i++) {
		if (comboIVA[i].value == seleccionIVA) {
			comboIVA[i].selected = true;
	    }   
	}
}


function cambioSeleccion() {
	   selIndex = document.forms[0].seleccion.selectedIndex;
	   comboValue = document.forms[0].seleccion.options[selIndex].value;
	   if(comboValue == 'group'){
		   document.forms[0].bookingGroupNumber.value='';
		   $(".integer").removeNumeric();
	   }
	   else{
		   document.forms[0].bookingGroupNumber.value='';
			$(".integer").numeric(false, function() { 
				document.forms[0].bookingGroupNumber.focus(); });
		   
	   }
	}