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
	//Actualización en cascada de pais, provincia, localidad y barrio del contacto(AJAX)
	 $("#paisContacto").change(function(){
		 getAjax('provinciasPorPaisServlet','val','paisContacto',getProcinciasContactoCallBack);
	 });
	 $("#provinciaContacto").change(function(){
		 getAjax('localidadesPorProvinciaServlet','val','provinciaContacto',getLocalidadesContactoCallBack);
	 });
	 $("#localidadContacto").change(function(){
		 getAjax('barriosPorLocalidadServlet','val','localidadContacto',getBarriosContactoCallBack);
	 });
	//forzamos un evento de cambio para que se carge por primera vez
	 if(urlParams["accion"] != 'MODIFICACION' && urlParams["accion"] != 'CONSULTA'){
		 $("#pais").change();   
		 $("#paisContacto").change();   
	 }	 
});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarCliente.html";
}
function volverCancelar(){
	document.location="mostrarCliente.html";
}
function guardarYSalir(){
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

//ajax
function getProcinciasContactoCallBack(sResponseText){
	var combo = document.getElementById("provinciaContacto");
	combo.options.length = 0;
	if (sResponseText != 'null'){
		var items = sResponseText.split('|');				
		for(i=0;i<items.length;i++){
			var item = items[i].split('-');
			var newOption = new Option(item[0],item[1]);
			addOptionToSelect(combo, newOption);
		}
		$("#provinciaContacto").change();
	}			
}

//ajax
function getLocalidadesContactoCallBack(sResponseText){
	var combo = document.getElementById("localidadContacto");
	combo.options.length = 0;
	if (sResponseText != 'null'){
		var items = sResponseText.split('|');				
		for(i=0;i<items.length;i++){
			var item = items[i].split('-');
			var newOption = new Option(item[0],item[1]);
			addOptionToSelect(combo, newOption);
		}
		$("#localidadContacto").change();
	}			
}

//ajax
function getBarriosContactoCallBack(sResponseText){
	var combo = document.getElementById("contacto.direccion.idBarrio");
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