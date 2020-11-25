var menuAbierto=false;

$(document).ready(function() {
	//tabla Lectura
	$("#lectura tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#lectura tbody tr").mouseout(function(){
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
	//PopUp
    $('#lectura tbody tr').contextMenu('myMenu1', {

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
			$("#lectura tbody tr").removeClass('tr_mouseover');
		}

    });
    
  //binding OnBlur() para los campos con popup
	$("#clienteCodigo").blur(function(){
		getAjaxCliente('clientesServlet','codigo','clienteCodigo',getClienteCallBack);
	});

	//forzamos un evento de cambio para que se carge por primera vez
	 if($("#accion").val() == 'MODIFICACION'){
		 $("#tipoAgrupador").attr('disabled', 'disabled');
		 $("#btnCliente").attr('disabled', 'disabled');
		 $("#clienteCodigo").attr('readonly', 'readonly');
	 }
	
});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function agregarLecturaDetalle(){
	var accion = document.getElementById("accion");
	document.location="precargaFormularioLectura.html?accion="+accion.value;
}

function consultar(id){
	var accion = document.getElementById("accion");
	if(id!=null)
		document.location="precargaFormularioLectura.html?accion="+accion.value+"&accion=CONSULTA&id="+id;
}

function modificar(id){
	var accion = document.getElementById("accion");
	if(id!=null)
		document.location="precargaFormularioLectura.html?accion="+accion.value+"&accion=MODIFICACION&id="+id;
}

function eliminar(mensaje, id){
	var accion = document.getElementById("accion");
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarLectura.html?id="+id+"&accion="+accion.value;
		    }
		});
	}
}

function changeCodigo(){
	var codigo = document.getElementById("codigo");
	var valor = codigo.value;
	codigo.value = valor.toUpperCase();
}

function volver(){
	document.location="mostrarLectura.html";
}

function volverCancelar(){
	document.location="mostrarLectura.html";
}

function guardarYSalir(){
 	document.forms[0].submit();
}


function pasar(){
	$("#descripcion2").val($("#descripcion").val());
	$("#fecha2").val($("#fecha").val());
	$("#observacion2").val($("#observacion").val());
}

function pregunta(){
    if (confirm('¿Estas seguro de importar este archivo de lecturas?')){
       document.fileUploadForm.submit();
    }
}

function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
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
function getClienteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#clienteCodigoLabel").html(sResponseText);
	else
		$("#clienteCodigo").val("");
}