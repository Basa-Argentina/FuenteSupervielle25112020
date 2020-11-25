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
	$("#clienteCodigo").blur(function(){
		getAjaxPopUp('clientesServlet','codigo','clienteCodigo',getClienteCallBack);
	});
	$("#listaPrecioCodigo").blur(function(){
		getAjaxPopUp('listasPreciosServlet','codigo','listaPrecioCodigo',getListaCallBack);
	});
	$("#conceptoCodigo").blur(function(){
		getAjaxPopUp('conceptoFacturableServlet','codigo','conceptoCodigo',getConceptoCallBack);
	});
	//Actualización en cascada de pais, provincia, localidad y barrio (AJAX)
	 $("#pais").change(function(){
		 getAjax('provinciasPorPaisServlet','val','pais',getProcinciasCallBack);
	    });
	 $("#provincia").change(function(){
		 getAjax('localidadesPorProvinciaServlet','val','provincia',getLocalidadesCallBack);
	    });
	 $("#localidad").change(function(){
		 getAjax('barriosPorLocalidadServlet','val','localidad',getBarriosCallBack);
	 });
	//forzamos un evento de cambio para que se carge por primera vez
	 if(urlParams["accion"] != 'MODIFICACION' && urlParams["accion"] != 'CONSULTA')
		 $("#pais").change();   
});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarClienteConcepto.html";
}
function volverCancelar(){
	document.location="mostrarClienteConcepto.html";
}
function guardarYSalir(){
 	document.forms[0].submit();
}

function abrirPopup(claseNom){
	$("#listaPrecioCodigo").val("");
	$("#conceptoCodigo").val("");
	var labelLista =  document.getElementById("listaPrecioCodigoLabel");
	labelLista.textContent = "";
	var labelConcepto =  document.getElementById("conceptoCodigoLabel");
	labelConcepto.textContent = "";
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

function abrirPopupListaPrecio(claseNom, mensaje, title){
	if($("#clienteCodigo").val()==null || $("#clienteCodigo").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "precargaFormularioClienteConcepto.html?clienteCodigo="+$("#clienteCodigo").val();
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

function abrirPopupConcepto(claseNom, mensaje, title){
	if($("#listaPrecioCodigo").val()==null || $("#listaPrecioCodigo").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "precargaFormularioClienteConcepto.html?clienteCodigo="+$("#clienteCodigo").val()+"&listaPrecioCodigo="+$("#listaPrecioCodigo").val();
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
function getAjaxPopUp(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getClienteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#clienteCodigoLabel").html(sResponseText);
	else
		$("#idCliente").val("");
}
//ajax
function getListaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#listaPrecioCodigoLabel").html(sResponseText);
	else
		$("#idCliente").val("");
}

//ajax
function getConceptoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#conceptoCodigoLabel").html(sResponseText);
	else
		$("#idCliente").val("");
}

//ajax
function getAjax(url, varName, elementName, callBack){
	var combo = document.getElementById(elementName);
	if(combo == null || combo.length == 0)
		return;
	var url = url+'?'+varName+'='+combo.value;	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getProcinciasCallBack(sResponseText){
	var combo = document.getElementById("provincia");
	combo.options.length = 0;
	if (sResponseText != 'null'){
		var items = sResponseText.split('|');				
		for(i=0;i<items.length;i++){
			var item = items[i].split('-');
			var newOption = new Option(item[0],item[1]);
			addOptionToSelect(combo, newOption);
		}
		$("#provincia").change();
	}			
}

//ajax
function getLocalidadesCallBack(sResponseText){
	var combo = document.getElementById("localidad");
	combo.options.length = 0;
	if (sResponseText != 'null'){
		var items = sResponseText.split('|');				
		for(i=0;i<items.length;i++){
			var item = items[i].split('-');
			var newOption = new Option(item[0],item[1]);
			addOptionToSelect(combo, newOption);
		}
		$("#localidad").change();
	}			
}

//ajax
function getBarriosCallBack(sResponseText){
	var combo = document.getElementById("idBarrio");
	combo.options.length = 0;
	if (sResponseText != 'null'){
		var items = sResponseText.split('|');				
		for(i=0;i<items.length;i++){
			var item = items[i].split('-');
			var newOption = new Option(item[0],item[1]);
			addOptionToSelect(combo, newOption);
		}
	}			
}

function addOptionToSelect(selectComponent, option){
	if($.browser.msie)
		selectComponent.add(option); //IE
	else
		selectComponent.add(option,null); //Mozilla, Chrome
}	