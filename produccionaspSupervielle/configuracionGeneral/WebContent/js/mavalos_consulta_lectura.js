var menuAbierto=false;

$(document).ready(function() {
	//tabla transporte
	$("#lectura tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#lectura tbody tr").mouseout(function(){
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
	$('#busquedaDiv > table').addClass('seccion');
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});	
	
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
  	$("#codigoSerie").blur(function(){
  		getAjax('serieServlet','codigo','codigoSerie',getSerieCallBack);
  	});
  	
  	
  	$("#codigoTipoElemento").blur(function(){
		getAjax('tipoElementoDescripcionServlet','codigo','codigoTipoElemento',getTipoElementoCallBack);
	});	
  	
  	if($("#codigoTipoElemento").val() != null){
		getAjax('tipoElementoDescripcionServlet','codigo','codigoTipoElemento',getTipoElementoCallBack);
	}
	
	$('#pop').hide();
  	
  	 $('#botonPopupCliente').click(function(){
   	  abrirPopupCliente()();
     });
  	 
  	$("#codigoCliente").blur(function(){
		getAjax('clientesServlet','codigo','codigoCliente',getClienteCallBack);
	});
  	
  	$("#codigoCliente").trigger("blur");
  	
	$('#botonPopupTiposElementos').click(function(){
	 abrirPopupTiposElementos();
	});
	
});

function agregar(){
	document.location="precargaFormularioLectura.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioLectura.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioLectura.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarLectura.html?id="+id;
		    }
		});		
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosLectura.html";
}
function volver(){
	document.location="menu.html";
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
function getSerieCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSerieLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoSerieLabel").html("");
	}
}

function abrirPopupCliente(){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert($('#errorSeleccioneEmpresa').val());
		return;
	}
	var url = "popUpSeleccionCliente.html?codigo="+$("#codigoEmpresa").val();
	jQuery.ajax({
        url: url,
        success: function(data){
           $(this).html(data);
           $(".displayTagDiv").displayTagAjax();
           popupOnDiv($(this).find('.darkMiddleClass'),'darkLayer');
        },
        data: ({"time":new Date().getTime()}),
        context: $(".selectorDiv")
    });
}

function abrirPopupTiposElementos(){
	var url = "popUpSeleccionTiposElementos.html";
	jQuery.ajax({
        url: url,
        success: function(data){
           $(this).html(data);
           $(".displayTagDiv").displayTagAjax();
           popupOnDiv($(this).find('.darkMiddleClass'),'darkLayer');
        },
        data: ({"time":new Date().getTime()}),
        context: $(".selectorDiv")
    });
}

//ajax
function getClienteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoClienteLabel").html("");
	}
}

function getTipoElementoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoTipoElementoLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoTipoElementoLabel").html("");
	}
}