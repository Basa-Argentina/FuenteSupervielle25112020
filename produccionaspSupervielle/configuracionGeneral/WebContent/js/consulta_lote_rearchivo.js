var menuAbierto=false;

$(document).ready(function() {
	//tabla cliente
	$("#loteRearchivo tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#loteRearchivo tbody tr").mouseout(function(){
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
	
		
	//PopUp
      $('#loteRearchivo tbody tr').contextMenu('myMenu1', {
 
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
			$("#lotesRearchivo tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
      
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
	//binding OnBlur() para los campos con popup	
	$("#codigoClasificacionDocumental").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		searchAjax('seleccionClasificacionDocumental.html?nodo=I','codigoClasificacionDocumental','codigoCliente',getClasificacionDocumentalCallBack);
	});
	
	$("#codigoContenedor").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		searchAjax('seleccionContenedor.html','codigoContenedor','codigoCliente',getContenedorCallBack);
	});
	
	$("#buscaClasificacionDocumental").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		abrirPopupSeleccion("popUpSeleccionClasificacionDocumental.html",'codigoCliente',"clasificacionDocumentalPopupMap", $("#mensajeSeleccioneCliente").val());
	});
	$("#buscaContenedor").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		abrirPopupSeleccion("popUpSeleccionContenedor.html",'codigoCliente',"contenedoresPopupMap", $("#mensajeSeleccioneCliente").val());
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
	
	$("#codigoEmpresa").trigger('blur');
	
	
	$("#codigoClasificacionDocumental").trigger('blur');
	
	$("#codigoContenedor").trigger('blur');
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
}
//ajax
function getClasificacionDocumentalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoClasificacionDocumentalLabel").html(sResponseText);
	}else{
		$("#codigoClasificacionDocumentalLabel").html("");
		$("#codigoClasificacionDocumental").val("");
	}
}
//ajax
function getContenedorCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoContenedorLabel").html(sResponseText);
	else{
		$("#codigoContenedorLabel").html("");
		$("#codigoContenedor").val("");
	}
}

function agregar(){
	document.location="precargaFormularioLoteRearchivo.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioLoteRearchivo.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioLoteRearchivo.html?accion=MODIFICACION&id="+id;
}

function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarLoteRearchivo.html?id="+id;
		    }
		});		
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosLoteRearchivo.html";
}
function volver(){
	document.location="menu.html";
}