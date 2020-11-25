var menuAbierto=false;

$(document).ready(function() {
	//tabla Cai
	$("#cai tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#cai tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
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
	//PopUp
    $('#cai tbody tr').contextMenu('myMenu1', {

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
			$("#cai tbody tr").removeClass('tr_mouseover');
		}

    });
	
	$("#tipoSerie").change(function(){
		 getAjax('tipoComprobantePorTipoSerieServlet','val','tipoSerie',getTipoComprobanteCallBack);
		 checkAll('tipoSerie');
		 checkSelect('tipoSerie');
    });
	
	$("#codigo").keyup(function(){
		changeCodigo();
	    });
	//Actualización en cascada de sucursal(AJAX)
	 $("#idEmpresa").change(function(){
		 getAjax('sucursalPorEmpresaServlet','val','idEmpresa',getSucursalesCallBack);
	    });
	
	 //forzamos un evento de cambio para que se carge por primera vez
	 if(urlParams["accion"] != 'MODIFICACION' && urlParams["accion"] != 'CONSULTA'){
		 $("#idEmpresa").change();		
	 }
	 $("#tipoSerie").change();
});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function agregarCai(){
	var accion = document.getElementById("accion");
	document.location="precargaFormularioCai.html?accionSerie="+accion.value;
}

function consultar(id){
	var accion = document.getElementById("accion");
	if(id!=null)
		document.location="precargaFormularioCai.html?accionSerie="+accion.value+"&accion=CONSULTA&id="+id;
}

function modificar(id){
	var accion = document.getElementById("accion");
	if(id!=null)
		document.location="precargaFormularioCai.html?accionSerie="+accion.value+"&accion=MODIFICACION&id="+id;
}

function eliminar(mensaje, id){
	var accion = document.getElementById("accion");
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarCai.html?id="+id+"&accion="+accion.value;
		    }
		});
	}
}

function checkAll(elementName){
	var combo = document.getElementById(elementName);
	var inputs = document.getElementsByTagName("input");
	for (i = 0; i < inputs.length; i++){
		if (inputs[i].type == "checkbox") {
			if(inputs[i].id != 'habilitado'){				
				if(combo.value != 'F'){					
					inputs[i].disabled = true;
					inputs[i].checked = false;
				}
				else
					inputs[i].disabled = false;
			}
		}
		
	}
}

function checkSelect(elementName){
	var combo = document.getElementById(elementName);
	var select = document.getElementById('condIvaClientes');
				if(combo.value != 'F')
				{	
					if(combo.value != 'NC')
					{
						if(combo.value != 'ND')
							{
								select.disabled = true;
							}
						else
							{
								select.disabled = false;
							}
					}
					else
						{
							select.disabled = false;
						}
				}
				else
				{
					select.disabled = false;
				}

}

function changeCodigo(){
	var codigo = document.getElementById("codigo");
	var valor = codigo.value;
	codigo.value = valor.toUpperCase();
}

function volver(){
	document.location="mostrarSerie.html";
}
function volverCancelar(){
	document.location="mostrarSerie.html";
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
function getTipoComprobanteCallBack(sResponseText){
	var combo = document.getElementById("idAfipTipoComprobante");
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
function getSucursalesCallBack(sResponseText){
	var combo = document.getElementById("idSucursal");
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