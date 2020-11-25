var menuAbierto=false;

$(document).ready(function() {
	//tabla user
	$("#direccion tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#direccion tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
		
	//PopUp
      $('#direccion tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
          'consultar': function(t) {
            consultar($(t).find('#hdn_id').val());
          },
 
          'modificar': function(t) {
            modificar($(t).find('#hdn_id').val());
 
          },
 
          'eliminar': function(t) {
 
        	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val());
 
          }
 
        },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#direccion tbody tr").removeClass('tr_mouseover');
		}
 
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
	
	 $("#pais").change();
});

function agregarDireccion(){
	document.location="precargaFormularioDireccion.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioDireccion.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioDireccion.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarDireccion.html?id="+id;
		    }
		});		
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosDireccion.html";
}

function volver(){
	document.location="menu.html";
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
