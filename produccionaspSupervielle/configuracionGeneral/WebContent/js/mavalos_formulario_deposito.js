var menuAbierto=false;

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
	
	$("#observacion").val($.trim($('#observacion').val()));

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
	$("#pais").blur(function(){
		getAjaxUbicacion('paisServlet','nom','pais', null,getPaisCallBack);
	});
	$("#provincia").blur(function(){
		getAjaxUbicacion('provinciasPorPaisServlet','nom','provincia','pais',getProvinciaCallBack);
	});
	$("#localidad").blur(function(){
		getAjaxUbicacion('localidadesPorProvinciaServlet','nom','localidad','provincia',getLocalidadCallBack);
	});
	$("#barrio").blur(function(){
		getAjaxUbicacion('barriosPorLocalidadServlet','nom','barrio','localidad',getBarrioCallBack);
	});
	$("#codigoEmpresa").blur(function(){
  		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
  	});
  	$("#codigoSucursal").blur(function(){
  		getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
  	});
  	if($("#codigoSucursal").val() != null){
  		getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
  	}
  	if($("#codigoEmpresa").val() != null){
  		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
  	}
});

function volver(){
	document.location="mostrarDeposito.html";
}
function volverCancelar(){
	document.location="mostrarDeposito.html";
}
function guardarYSalir(){
	$("#idBarrio").val($("#barrioLabel").html());
	//No funciona el parseador
	var subDisponible = document.getElementById("subDisponible");
	var fSB = subDisponible.value.toString().replace (".", "");
	fSB = fSB.replace (",", ".");
	subDisponible.value = fSB;
	var subTotal = document.getElementById("subTotal");
	var fSB = subTotal.value.toString().replace (".", "");
	fSB = fSB.replace (",", ".");
	subTotal.value = fSB;
	//Hacemos el submit
 	document.forms[0].submit();
}


function addOptionToSelect(selectComponent, option){
	if($.browser.msie)
		selectComponent.add(option); //IE
	else
		selectComponent.add(option,null); //Mozilla, Chrome
}


function abrirPopup(claseNom){
	$("#codigoSucursal").val("");
	var labelLista =  document.getElementById("codigoSucursalLabel");
	labelLista.textContent = "";
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

function abrirPopupSucursal(claseNom, mensaje, title){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "precargaFormularioDeposito.html?empresaCodigo="+$("#codigoEmpresa").val();
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
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getSucursalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoSucursalLabel").html("");
	}
}

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoEmpresaLabel").html("");
		$("#codigoSucursalLabel").html("");
		$("#codigoSucursal").val("");
	}
}



function abrirPopupProvincia(claseNom){
	//uso clienteCodigo porque esta en duro y si se cambia dejan de funcionar otras pantallas
	var url = "precargaFormularioDeposito.html?clienteCodigo="+$("#paisLabel").html();
	abrirPopupGeneral(claseNom, url);
}
function abrirPopupLocalidad(claseNom){
	//uso clienteCodigo porque esta en duro y si se cambia dejan de funcionar otras pantallas
	var url = "precargaFormularioDeposito.html?clienteCodigo="+$("#provinciaLabel").html();
	abrirPopupGeneral(claseNom, url);
}
function abrirPopupBarrio(claseNom){
	//uso clienteCodigo porque esta en duro y si se cambia dejan de funcionar otras pantallas
	var url = "precargaFormularioDeposito.html?clienteCodigo="+$("#localidadLabel").html();
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
function getAjaxUbicacion(url, varName, elementId, parentId, callBack){
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