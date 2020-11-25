var menuAbierto=false;

$(document).ready(function() {
	//tabla remito
	$("#remito tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#remito tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	//Checkbox
	$("input[name=checktodos]").change(function(){
		$('input[type=checkbox]').each( function() {			
			if($("input[name=checktodos]:checked").length == 1){
				this.checked = true;
			} else {
				this.checked = false;
			}
		});
	});
	
	$('#imprimirRemitos').click(function(){
		
		 	var allVals = [];
		    $("input[class='checklote']:checked").each(function() {
		    	allVals.push($(this).val());
		    });
		    if(allVals.length == 0)
		    {
		    	jAlert('Debe seleccionar al menos un remito para imprimir.');
				return;
			}
		    else
			{
		    	window.open('imprimirRemito.html?&seleccionados='+allVals,'');
		    }
	});
	
	$('#cambiarEstado').click(function(){
		
	 	var allVals = [];
	    $("input[class='checklote']:checked").each(function() {
	    	allVals.push($(this).val());
	    });
	    if($('#estadoRemito').val()==""){
	    	jAlert('Debe seleccionar el estado.');
			return;
	    }
	    if(allVals.length == 0)
	    {
	    	jAlert('Debe seleccionar al menos un remito para cambiar el estado.');
			return;
		}
	    else
		{
	    	$('#selectedSel').val(allVals);
	    	document.forms[1].submit();		
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
	
		
	//PopUp
      $('#remito tbody tr').contextMenu('myMenu1', {
 
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
			$("#remito tbody tr").removeClass('tr_mouseover');
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
    		getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
    	});
    	$("#codigoDepositoOrigen").blur(function(){
    		getAjax('depositosServlet','codigo','codigoDepositoOrigen',getDepositoOrigenCallBack);
    	});
    	$("#codigoCliente").blur(function(){
    		getAjax('clientesServlet','codigo','codigoCliente',getClienteCallBack);
    	});
    	$("#codigoSerie").blur(function(){
    		getAjax('serieServletPorCodigo','codigo','codigoSerie',getSerieCallBack);
    	});
    	$("#codigoTransporte").blur(function(){
    		getAjax('transportesServlet','codigo','codigoTransporte',getTransporteCallBack);
    	});
    	if($("#codigoDepositoOrigen").val() != null){
    		getAjax('depositosServlet','codigo','codigoDepositoOrigen',getDepositoOrigenCallBack);
    	}
    	if($("#codigoSucursal").val() != null){
    		getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
    	}
    	if($("#codigoEmpresa").val() != null){
    		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
    	}
    	if($("#codigoCliente").val() != null){
    		getAjax('clientesServlet','codigo','codigoCliente',getClienteCallBack);
    	}
    	if($("#codigoSerie").val() != null){
    		getAjax('serieServletPorCodigo','codigo','codigoSerie',getSerieCallBack);
    	}
    	if($("#codigoTransporte").val() != null){
    		getAjax('transportesServlet','codigo','codigoTransporte',getTransporteCallBack);
    	}
});

function agregar(){
	document.location="iniciarPrecargaFormularioRemito.html";
}
function consultar(id){
	if(id!=null)
		document.location="iniciarPrecargaFormularioRemito.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="iniciarPrecargaFormularioRemito.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarRemito.html?id="+id;
		    }
		});
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosRemito.html";
}
function volver(){
	document.location="menu.html";
}

function abrirPopup(claseNom){
	$("#codigoSucursal").val("");
	$("#codigoDeposito").val("");
	var labelLista =  document.getElementById("codigoSucursalLabel");
	labelLista.textContent = "";
	var labelConcepto =  document.getElementById("codigoDepositoLabel");
	labelConcepto.textContent = "";
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

function abrirPopupDepositoOrigen(claseNom, mensaje){
	if($("#codigoSucursal").val()==null || $("#codigoSucursal").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioRemito.html?empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursal").val();
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

function abrirPopupSucursal(claseNom, mensaje){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "mostrarRemito.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupDepositoOrigen(claseNom, mensaje){
	if($("#codigoSucursal").val()==null || $("#codigoSucursal").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "mostrarRemito.html?empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursal").val();
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

function abrirPopupCliente(claseNom, mensaje){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "mostrarRemito.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupSerie(claseNom, mensaje){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "mostrarRemito.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupTransporte(claseNom, mensaje){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "mostrarRemito.html?empresaCodigo="+$("#codigoEmpresa").val();
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
function getDepositoOrigenCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDepositoOrigenLabel").html(sResponseText);	
	else{
		$("#idCliente").val("");
		$("#codigoDepositoOrigenLabel").html("");
	}
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
	}
}

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoEmpresaLabel").html("");
		$("#codigoDepositoLabel").html("");
		$("#codigoSucursalLabel").html("");
		$("#codigoSerieLabel").html("");
		$("#codigoTransporteLabel").html("");
		$("#codigoDeposito").val("");
		$("#codigoSucursal").val("");
		$("#codigoSerie").val("");
		$("#codigoTransporte").val("");
	}
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

//ajax
function getSerieCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSerieLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoSerieLabel").html("");
	}
}

//ajax
function getTransporteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoTransporteLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoTransporteLabel").html("");
	}
}

function updateTextArea() {         
   
 }
$(function() {
  $('#c_b input').click(updateTextArea);
  updateTextArea();
});
