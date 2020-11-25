$(document).ready(function() {	
	
	//Tooltips
	$("img[title]").tooltip();
	$("a[title]").tooltip();

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
	 $("#codigoEmpresa").blur(function(){
		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
	 });
	 $("#codigoSucursal").blur(function(){
		getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
	 });
	 
	 $("#pais").change();
});

function volverCancelar(){
	document.location="menu.html";
}
function guardar(){
	document.forms[0].submit();
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
	var combo = document.getElementById("persona.direccion.idBarrio");
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


function abrirPopupEmpresa(claseNom){
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
	var url = "precargaFormularioUserPersonalData.html?accion=NUEVO&empresaCodigo="+$("#codigoEmpresa").val();
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