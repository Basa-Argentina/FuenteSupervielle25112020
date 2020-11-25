var menuAbierto=false;

$(document).ready(function() {
	//tabla cliente
	$("#loteReferencia tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#loteReferencia tbody tr").mouseout(function(){
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
      $('#loteReferencia tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
          'consultar': function(t) {
            consultar($(t).find('#hdn_id').val());
          },
 
          'modificar': function(t) {
            modificar($(t).find('#hdn_id').val());
          },
 
          'agregarRango': function(t) {
        	  agregarRango($(t).find('#hdn_id').val());
            },
            
          'eliminar': function(t) {
        	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val());
          },
          'imprimir': function(t) {
              imprimir($(t).find('#hdn_id').val());
          }
 
        },
		onShowMenu: function(e, menu) {
			var cell = e.currentTarget.children[1].children[0].value;
			if(cell==null || cell != 'true'){
				menuAbierto=true;
				$('#agregarRango', menu).remove();
				return menu;
			}else{
				menuAbierto=true;
				return menu;
			}
		},
		onHide: function(){
			menuAbierto=false;
			$("#lotesReferencia tbody tr").removeClass('tr_mouseover');
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
	
	$("#codigoPersonal").blur(function(){
		getAjaxEmpleado('empleadosReturnCodigoDireccionServlet', 'codigo', 'codigoPersonal',getPersonalCallBack);
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
	
	$("#buscaPersonal").click(function(){
		abrirPopupPorCliente("popUpSeleccionEmpleadoSolicitante.html",'codigoCliente',"personalPopupMap", $("#mensajeSeleccioneCliente").val());
  	});
	
	
	
	$("#codigoEmpresa").trigger('blur');
	
	$('#cargaIndividual').click(function(){
		document.location="iniciarFormularioLoteReferencia.html?accion=nueva_carga_individual";
	});
	$('#cargaGrupal').click(function(){
		document.location="iniciarFormularioLoteReferencia.html?accion=nueva_carga_grupal";
	});
	$('#cargaRango').click(function(){
		document.location="iniciarFormularioLoteReferencia.html?accion=nueva_carga_rango";
	});
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
		//Modifica Adrian 20/07/2017 // Oculta  Carga Grupal y por Rango para Supervielle
		var codclient = $("#codigoCliente").val();
		
		if (codclient == "231"){
		
		$("#cargaRango").addClass("hiddenInput");
		$("#cargaIndividual").addClass("hiddenInput");
		
		}
	
}
function consultar(id){
	if(id!=null)
		document.location="iniciarFormularioLoteReferencia.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="iniciarFormularioLoteReferencia.html?accion=MODIFICACION&id="+id;
}
function agregarRango(id){
	if(id!=null)
		document.location="iniciarFormularioLoteReferencia.html?accion=MODIFICACIONRANGO&id="+id;
}

function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarLoteReferencia.html?id="+id;
		    }
		});		
	}
}

function imprimir(id){
	if(id!=null)
		document.location="imprimirLoteReferencia.html?id="+id;
}

function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosLoteReferencia.html";
}
function volver(){
	document.location="menu.html";
}
function abrirPopupPorCliente(url,inputValue,nombreClase, mensaje){
	if(inputValue!=null){
		if($("#"+inputValue).val()==null || $("#"+inputValue).val()==""){
			jAlert(mensaje);
			return;
		}
		url = url+"?clienteCodigoString="+$("#"+inputValue).val();
	}
	
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