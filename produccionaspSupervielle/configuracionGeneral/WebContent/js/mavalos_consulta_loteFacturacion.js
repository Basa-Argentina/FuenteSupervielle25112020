var menuAbierto=false;

$(document).ready(function() {
	//tabla user
	$("#loteFacturacion tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#loteFacturacion tbody tr").mouseout(function(){
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
	
	$('#botonPopupEmpresa').click(function(){
  	  abrirPopupEmpresa();
    });
    
    $('#botonPopupSucursales').click(function(){
  	  abrirPopupSucursal();
    });
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
		
	//PopUp
    $('#loteFacturacion tbody tr').contextMenu('myMenu1',
		{
			bindings : {

				'consultar' : function(t) {
					consultar($(t).find('#hdn_id').val());
				},

				'modificar' : function(t) {
					modificar($(t).find('#hdn_id').val());

				},

				'eliminar' : function(t) {

					eliminar($(t).find('#hdn_eliminar').val(), $(t).find('#hdn_id').val());

				},

				'facturar' : function(t) {
					facturar($('#mensajeFacturar').val(), $(t).find('#hdn_id').val());
				}

			},
			onShowMenu : function(e, menu) {
				var cell = e.currentTarget.children[2].firstChild.nodeValue;
				if(cell == 'Facturado'){
					menuAbierto=true;
					$('#eliminar', menu).remove();
					$('#modificar', menu).remove();
					$('#facturar', menu).remove();
					return menu;
				}else{
					menuAbierto=true;
					return menu;
				}

			},
			onHide : function() {
				menuAbierto = false;
				$("#loteFacturacion tbody tr").removeClass('tr_mouseover');
			}

		}); 
    //Inicio menu
  	$("ul.topnav li").hover(
  		function() {
  			$(this).addClass("hover");
  			$(this).find("ul.subnav").slideDown('fast').show(); //Drop down the subnav on click  
  		}, 
  		function(){  
  			$(this).removeClass("hover");
  			$(this).find("ul.subnav").slideUp('fast'); //When the mouse hovers out of the subnav, move it back up  
  		}
      );
  	
  //Fin menu
  	//binding OnBlur() para los campos con popup	
	$("#codigoEmpresa").blur(function(){
		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
	});
	$("#codigoSucursal").blur(function(){
		getAjaxConParent('sucursalesPorCodigoEmpresaServlet','codigo','codigoSucursal','codigoEmpresa',getSucursalCallBack);
	});
	if($("#codigoSucursal").val() != null){
		getAjaxConParent('sucursalesPorCodigoEmpresaServlet','codigo','codigoSucursal','codigoEmpresa',getSucursalCallBack);
	}
	if($("#codigoEmpresa").val() != null){
		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
	}
  		
	$('.completarZeros').blur(function(e){
		valorSinZeros='';
		valorSinZeros=$(this).val();
		zerosIzq='';
		if(valorSinZeros!=null && valorSinZeros!=''){
			maximo=$(this).attr('maxlength');
			actual=valorSinZeros.length;
			for(i=0;i<(maximo-actual);i++){
				zerosIzq+='0';
			}
		}
		$(this).val(zerosIzq+valorSinZeros);		
	});
	
	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	
	$('#agregar').click(function(){
	    	document.location='iniciarPrecargaFormularioLoteFacturacion.html';
	});
	
});

function buscarFiltro(){
	document.forms[0].submit();
}

function consultar(id){
	if(id!=null)
		document.location="iniciarPrecargaFormularioLoteFacturacion.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="iniciarPrecargaFormularioLoteFacturacion.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarLoteFacturacion.html?id="+id;
		    }
		});
	}
}

function facturar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Facturar',function(r) {
		    if(r){
		    	document.location="facturarLoteFacturacion.html?id="+id;
		    }
		});
	}
};

function volver(){
	document.location="menu.html";
}

function abrirPopupEmpresa(){
	
	var url = "popUpSeleccionEmpresa.html";
	
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

function abrirPopupSucursal(){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert($('#errorSeleccioneEmpresa').val());
		return;
	}
	var url = "popUpSeleccionSucursal.html?codigo="+$("#codigoEmpresa").val();
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
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getAjaxConParent(url, varName, elementName, parentId,callBack){

		var input = document.getElementById(elementName);
		if(input == null)
			return;
		var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val()+'&empresaId='+$("#"+parentId+"").val()+'&'+parentId+'='+$("#"+parentId+"").val();	
		var request = new HttpRequest(url, callBack);
		request.send();
	
}

//ajax
function getSucursalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoSucursalLabel").html("");
		$("#codigoDepositoLabel").html("");
		$("#codigoDeposito").val("");
		$("#codigoSucursal").val("");
	}
}

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoEmpresaLabel").html(sResponseText);
		$("#codigoSucursal").blur();
	}
	else{
		$("#idCliente").val("");
		$("#codigoEmpresaLabel").html("");
		$("#codigoDepositoLabel").html("");
		$("#codigoSucursalLabel").html("");
		$("#codigoDeposito").val("");
		$("#codigoSucursal").val("");
	}
}