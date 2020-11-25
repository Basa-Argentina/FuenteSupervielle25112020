var menuAbierto=false;

$(document).ready(function() {
	//tabla cuentaCorriente
	$("#factura tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#factura tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	//PopUp
    $('#factura tbody tr').contextMenu('myMenu1', {

      bindings: {

        'consultar': function(t) {
          consultar($(t).find('#hdn_id').val(),$(t).find('#hdn_tipo').val());
        },

        'eliminar': function(t) {
      	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val(),$(t).find('#hdn_tipo').val());
        }

      },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#factura tbody tr").removeClass('tr_mouseover');
		}

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
	
	
	
    //binding OnBlur() para los campos con popup	
	$("#codigoEmpresa").blur(function(){
		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
	});
	
	$("#codigoCliente").blur(function(){
		searchAjax('seleccionCliente.html','codigoCliente','codigoEmpresa',getClienteCallBack);
	});
	
	$("#buscaEmpresa").click(function(){
		abrirPopupSeleccion("popUpSeleccionEmpresa.html",null,null,"empresasPopupMap", null);
	});
	
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionCliente.html","codigoEmpresa","clientesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	
	$("#codigoEmpresa").trigger('blur');
	$("#codigoCliente").trigger('blur');	

});


function agregar(){
	document.location="inicioFormularioCuentaCorriente.html";
}
function consultar(id, tipo){
	if(id!=null && tipo!='X')
		parent.$.fancybox({
			'width'				: '60%',
			'height'			: '60%',
			'autoScale'			: true,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
			onCleanup : function(){  
				return document.location = 'mostrarCuentaCorriente.html';
				
				
			},
			'href'				: 'mostrarFacturaDocto.html?id='
				+id
		});
}


function eliminar(mensaje, id, tipo){
	if(id!=null && id!=undefined && mensaje!=undefined && tipo=='X'){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarCuentaCorriente.html?id="+id;
		    }
		});		
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosCliente.html";
}
function volver(){
	document.location="menu.html";
}
//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&cuentaCorrienteId='+$("#cuentaCorrienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
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

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoEmpresaLabel").html("");
	}
}

//ajax
function getClienteCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#codigoClienteLabel").html("");
		$("#codigoCliente").val("");
	}
}
