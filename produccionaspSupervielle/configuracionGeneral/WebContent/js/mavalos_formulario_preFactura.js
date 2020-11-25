var menuAbierto=false;

$(document).ready(function(){
		
	//fancybox
	$('#fancyboxAgregarDetalle').fancybox({
		'padding'			: 10,
		'titleShow'			: false,
		'autoDimensions'	: false,
		'width'         	: 730,
		'scrolling'			: 'auto',
		'height'        	: 450,
		'overlayColor'		: '#CCCCCC',
		'overlayOpacity'	: 0.7,
		'showCloseButton'	: false,
		'autoScale'			: false,
		'transitionIn'		: 'elastic',
		'transitionOut'		: 'elastic',
		'type'				: 'iframe',
		'onClosed'			: function(){ $('#botonAgregar').removeAttr('disabled'); }
	});
	
	
	$("#notasFacturacion").val($.trim($('#notasFacturacion').val()));
	
	$('#notasFacturacion').keydown(function() {
	    var longitud = $(this).val().length;
	    var resto = 200 - longitud;
	    if(resto <= 0){
	        $('#notasFacturacion').attr("maxlength", 200);
	    }
	});
	
	
	//tabla Detalles
	$("#detalle tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#detalle tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});	
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
	//PopUp click derecho
    $('#detalle tbody tr').contextMenu('myMenu1', {

      bindings: {

        'consultar': function(t) {
          consultar($(t).find('#hdn_orden').val());
        },

        'modificar': function(t) {
          modificar($(t).find('#hdn_orden').val());

        },

        'eliminar': function(t) {

      	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_orden').val());

        },
        
        'habilitar': function(t) {

         habilitar($(t).find('#hdn_habilitar').val(),$(t).find('#hdn_orden').val());

        }
        
      },
		onShowMenu: function(e, menu) {
			var cell = e.currentTarget.children[3].firstChild.nodeValue;
			if(cell == 'Anulado'){
				menuAbierto=true;
				$('#eliminar', menu).remove();
				$('#habilitar', menu).add();
				return menu;
			}else{
				menuAbierto=true;
				$('#habilitar', menu).remove();
				return menu;
			}

		},
		onHide: function(){
			menuAbierto=false;
			$("#detalle tbody tr").removeClass('tr_mouseover');
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
	
	//botones
	$('#botonCancelar').click(function(){
		window.location='precargaFormularioLoteFacturacion.html?accion='+$('#accionLote').val();
	});
	
	$('#botonGuardar').click(function(){
		codigoEmpresa =  $("#codigoEmpresa").val();
		codigoSucursal = $('#codigoSucursal').val();
		idAfipTipoComprobante = $('#idAfipTipoComprobante').val();
		codigoClienteEmp = $('#codigoCliente').val();
		error = $('#error').val();
		if(codigoEmpresa ==null || codigoEmpresa == ""){
			jAlert($('#errorSeleccioneEmpresa').val(),error);
			return false;
		}
		if(codigoSucursal == null || codigoSucursal == ""){
			jAlert($('#errorSeleccioneSucursal').val(),error);
			return false;
		}
		if(codigoClienteEmp == null || codigoClienteEmp == ""){
			jAlert($('#errorSeleccioneCliente').val(),error);
			return false;
		}
		if(idAfipTipoComprobante == null || idAfipTipoComprobante == "" || idAfipTipoComprobante=="0"){
			jAlert($('#errorSeleccioneTipoComprobante').val(),error);
			return;
		}
		document.forms[0].submit();		
	});
	
	$('#botonAgregar').click(function(){
		codigoEmpresa =  $("#codigoEmpresa").val();
		codigoSucursal = $('#codigoSucursal').val();
		idAfipTipoComprobante = $('#idAfipTipoComprobante').val();
		codigoClienteEmp = $('#codigoCliente').val();
		error = $('#error').val();
		if(codigoEmpresa ==null || codigoEmpresa == ""){
			jAlert($('#errorSeleccioneEmpresa').val(),error);
			return false;
		}
		if(codigoSucursal == null || codigoSucursal == ""){
			jAlert($('#errorSeleccioneSucursal').val(),error);
			return false;
		}
		if(codigoClienteEmp == null || codigoClienteEmp == ""){
			jAlert($('#errorSeleccioneCliente').val(),error);
			return false;
		}
		if(idAfipTipoComprobante == null || idAfipTipoComprobante == "" || idAfipTipoComprobante=="0"){
			jAlert($('#errorSeleccioneTipoComprobante').val(),error);
			return;
		}
		actualizarDetalle('NUEVO');
	});
	
	$('#botonAgregar').removeAttr('disabled');
	
	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	//Validaciones sobre formato de codigos
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
	
	//llamadas a los servlets
	$('#codigoCliente').blur(function(){
		getAjaxCodigoClienteEmp();
	});
	
	//cargando descripcion de cliente y serie
	
	codigoClienteIni = $('#codigoCliente').val();
	
	if(codigoClienteIni !=null && codigoClienteIni != ""){
		getAjaxCodigoClienteEmpSoloLabel();
	}
	
	
	headerNoModificable = $('#headerPreFacturaNoModificable').val();
	
	if(headerNoModificable=='FALSE' || headerNoModificable == 'false'){
		$('#idAfipTipoComprobante').click(function(){
			codigoClienteEmp = $('#codigoCliente').val();
			descripcionClienteEmp = $('#codigoClienteLabel').html();
			if(codigoClienteEmp == null || codigoClienteEmp == "" || descripcionClienteEmp == null || descripcionClienteEmp == ""){
				jAlert($('#errorSeleccioneCliente').val(),error);
				$('#codigoSerie').val("");
				$("#codigoSerieLabel").html("");
				$('#prefijoSerie').html("XXXX");
				$('#numeroComprobanteStr').val("0");
				$('#idAfipTipoComprobante').val("0");
				return false;
			}
		});
		
		
		//seleccionando valor del comboBox
		$('#idAfipTipoComprobante').val($('#idAfipTipoComprobanteSelected').val());
	}else{
		esconderOpcionesSelect();
	}
	
	$('#idAfipTipoComprobante').change(function(){
		$('#codigoSerie').val("");
		$("#codigoSerieLabel").html("");
		$('#prefijoSerie').html("XXXX");
		$('#numeroComprobanteStr').val("0");
	});
	
	$('#codigoSerie').blur(function(){
		getAjaxCodigoSerie();
	});
	codigoSerieIni = $('#codigoSerie').val();
	if(codigoSerieIni != null && codigoSerieIni != ""){
		getAjaxCodigoSerie();
	}	
	
});

function actualizarDetalle(accionPreFacturaDetalle){
	accionPreFactura = $('#accionPreFactura').val();
	if(accionPreFacturaDetalle=='NUEVO' || accionPreFacturaDetalle=='MODIFICAR' || accionPreFacturaDetalle=='CONSULTAR' || accionPreFacturaDetalle=='ELIMINAR'){
		accionPreFacturaDetalle = accionPreFacturaDetalle;
	}else{
		accionPreFacturaDetalle = 'NUEVO';
	}
	codigoEmpresa = $('#codigoEmpresa').val();
	codigoSucursal = $('#codigoSucursal').val();
	codigoCliente = $('#codigoCliente').val();
	idAfipTipoComprobante = $('#idAfipTipoComprobante').val();
	codigoSerie = $('#codigoSerie').val();
	fechaStr = $('#fechaStr').val();
	prefijoSerie = $('#prefijoSerie').val();
	numeroComprobanteStr = $('#numeroComprobanteStr').val();
	
	url='agregarPreFacturaDetalle.html'
		+"?accionPreFactura="+accionPreFactura
		+"&accionPreFacturaDetalle="+accionPreFacturaDetalle
		+"&codigoEmpresa="+codigoEmpresa
		+"&codigoSucursal="+codigoSucursal
		+"&codigoCliente="+	codigoCliente
		+"&idAfipTipoComprobante="+idAfipTipoComprobante
		+"&codigoSerie="+codigoSerie
		+"&fechaStr="+fechaStr
		+"&prefijoSerie="+prefijoSerie
		+"&numeroComprobanteStr="+numeroComprobanteStr;
	window.location=url;
}

function volver(){
	document.location="precargaFormularioLoteFacturacion.html?accion="+$('#accionLote').val();
}

//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteAspId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

//
function getAjaxCodigoClienteEmpSoloLabel(){
	var input = document.getElementById('codigoCliente');
	var codigoEmpresa = $('#codigoEmpresa').val();
	if(input == null || codigoEmpresa == null)
		return;
	
	var url = 'clientesCodigoYCodigoCondicionAfipServlet';
	
	jQuery.ajax({
        url: url,
        success: function(data){
           getCodigoClienteEmpCallBackSoloLabel(data);
        },
        type: "GET",
        dataType: "html",
        timeout:120000,
        data: 'codigo='+input.value+'&clienteId='+$("#clienteAspId").val()+'&codigoEmpresa='+codigoEmpresa+'&habilitado=true&modo=F',
        context: $(".selectorDiv"),
        error:function(){
        	jAlert("Ha ocurrido un error...");
        }
    });
}

//llamadas a servlets
function getAjaxCodigoClienteEmp(){
	var input = document.getElementById('codigoCliente');
	var codigoEmpresa = $('#codigoEmpresa').val();
	if(input == null || codigoEmpresa == null)
		return;
	
	var url = 'clientesCodigoYCodigoCondicionAfipServlet';
	
	jQuery.ajax({
        url: url,
        success: function(data){
           getCodigoClienteEmpCallBack(data);
           popupOffDiv($('#pop'),'darkLayer');            
        },
        type: "GET",
        dataType: "html",
        timeout:120000,
        data: 'codigo='+input.value+'&clienteId='+$("#clienteAspId").val()+'&codigoEmpresa='+codigoEmpresa+'&habilitado=true&modo=F',
        context: $(".selectorDiv"),
        beforeSend:function(){
        	popupOnDiv($('#pop'),'darkLayer');
        },
        error:function(){
        	jAlert("Ha ocurrido un error...");
        	popupOffDiv($('#pop'),'darkLayer');
        }
    });
}

function getAjaxCodigoSerie(){
	codigoEmpresa =  $("#codigoEmpresa").val();
	codigoSucursal = $('#codigoSucursal').val();
	idAfipTipoComprobante = $('#idAfipTipoComprobante').val();
	codigoClienteEmp = $('#codigoCliente').val();
	codigoSerie = $('#codigoSerie').val();
	fecha = $('#fechaStr').val();
	error = $('#error').val();
	if(codigoEmpresa ==null || codigoEmpresa == ""){
		jAlert($('#errorSeleccioneEmpresa').val(),error);
		return false;
	}
	if(codigoSucursal == null || codigoSucursal == ""){
		jAlert($('#errorSeleccioneSucursal').val(),error);
		return false;
	}
	if(codigoClienteEmp == null || codigoClienteEmp == ""){
		jAlert($('#errorSeleccioneCliente').val(),error);
		return false;
	}
	if(idAfipTipoComprobante == null || idAfipTipoComprobante == "" || idAfipTipoComprobante=="0"){
		jAlert($('#errorSeleccioneTipoComprobante').val(),error);
		return;
	}
	if(fecha == null || fecha == "" || fecha=="0"){
		jAlert($('#errorSeleccioneFecha').val(),error);
		return;
	}

	var url = "serieServletParaFactura";
	jQuery.ajax({
        url: url,
        success: function(data){
           getCodigoSerieCallBack(data);
           popupOffDiv($('#pop'),'darkLayer');            
        },
        type: "GET",
        timeout:120000,
        data: "&codigoEmpresa=" + codigoEmpresa
			+"&codigoSucursal=" + codigoSucursal
			+"&idAfipTipoComprobante=" + idAfipTipoComprobante
			+"&codigoCliente=" + codigoClienteEmp
			+"&codigo=" + codigoSerie
			+"&fecha=" + fecha,
        context: $(".selectorDiv"),
        dataType: "html",
        beforeSend:function(){
        	popupOnDiv($('#pop'),'darkLayer');
        },
        error:function(){
        	jAlert("Ha ocurrido un error...");
        	popupOffDiv($('#pop'),'darkLayer');
        }
    });
}

function abrirPopup(claseNom){
	headerNoModificable = $('#headerPreFacturaNoModificable').val();
	if(headerNoModificable=='FALSE' || headerNoModificable == 'false'){
		popupClaseNombre = claseNom;
		$(".displayTagDiv").displayTagAjax();
		var div = $('.'+claseNom);
		popupOnDiv(div,'darkLayer');
	}
}

function abrirPopupSucursal(){
	headerNoModificable = $('#headerPreFacturaNoModificable').val();
	if(headerNoModificable=='FALSE' || headerNoModificable == 'false'){
		if($("#codigoEmpresa").val()==""){
			var tittle = $('#tituloError').val();
			var message =$('#codigoEmpresaRequerido').val(); 
			jAlert(message,tittle);
			return false;
		}
		codigoEmpresa =  $("#codigoEmpresa").val();
		
		var url = "mostrarListaComprobantesPreFactura.html?"
					+ "&codigoEmpresa=" + codigoEmpresa;
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
	        context: $(".displayTagDiv."+ "sucursalesPopupMap")
	    });
		popupClaseNombre = 'sucursalesPopupMap';
		$(".displayTagDiv").displayTagAjax();
		var div = $('.'+'sucursalesPopupMap');
		popupOnDiv(div,'darkLayer');
	}
}

function abrirPopupClienteEmp(){
	headerNoModificable = $('#headerPreFacturaNoModificable').val();
	if(headerNoModificable=='FALSE' || headerNoModificable == 'false'){
		if($("#codigoEmpresa").val()==""){
			var tittle = $('#tituloError').val();
			var message =$('#codigoEmpresaRequerido').val(); 
			jAlert(message,tittle);
			return false;
		}
		codigoEmpresa =  $("#codigoEmpresa").val();
		var url = "popUpSeleccionCliente.html?"
			+ "&codigo=" + codigoEmpresa;
		
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
}

function abrirPopupSeries(){
	headerNoModificable = $('#headerPreFacturaNoModificable').val();
	if(headerNoModificable=='FALSE' || headerNoModificable == 'false'){
		codigoEmpresa =  $("#codigoEmpresa").val();
		codigoSucursal = $('#codigoSucursal').val();
		idAfipTipoComprobante = $('#idAfipTipoComprobante').val();
		codigoClienteEmp = $('#codigoCliente').val();
		fecha = $('#fechaStr').val();
		error = $('#error').val();
		if(codigoEmpresa ==null || codigoEmpresa == ""){
			jAlert($('#errorSeleccioneEmpresa').val(),error);
			return false;
		}
		if(codigoSucursal == null || codigoSucursal == ""){
			jAlert($('#errorSeleccioneSucursal').val(),error);
			return false;
		}
		if(codigoClienteEmp == null || codigoClienteEmp == ""){
			jAlert($('#errorSeleccioneCliente').val(),error);
			return false;
		}
		if(idAfipTipoComprobante == null || idAfipTipoComprobante == "" || idAfipTipoComprobante=="0"){
			jAlert($('#errorSeleccioneTipoComprobante').val(),error);
			return;
		}
		if(fecha == null || fecha == "" || fecha=="0"){
			jAlert($('#errorSeleccioneFecha').val(),error);
			return;
		}
		var url = "popUpSeleccionSeriePorClienteYTipoComprobante.html?"
					+ "&codigoEmpresa=" + codigoEmpresa
					+"&codigoSucursal=" + codigoSucursal
					+"&idAfipTipoComprobante=" + idAfipTipoComprobante
					+"&codigoCliente=" + codigoClienteEmp
					+"&fecha=" + fecha;
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
}

function getCodigoClienteEmpCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != ""){
		respuesta = sResponseText.split(";*;");
		if(respuesta[0]!=null && respuesta[0]!=""){
			$("#codigoClienteLabel").html(respuesta[0]);
			$('#clienteEmpCodigoCondIva').val(respuesta[1]);
			$('#idAfipTipoComprobante').html(respuesta[2]);
		}else{			
			$("#codigoCliente").val("");
			$("#codigoClienteLabel").html("");	
			$("#codigoSerie").val("");
			$("#codigoSerieLabel").html("");
			$('#prefijoSerie').html("XXXX");
			$('#numeroComprobanteStr').val("0");
			$('#idAfipTipoComprobante').val("0");
		}
	}else{			
		$("#codigoCliente").val("");
		$("#codigoClienteLabel").html("");	
		$("#codigoSerie").val("");
		$("#codigoSerieLabel").html("");
		$('#prefijoSerie').html("XXXX");
		$('#numeroComprobanteStr').val("0");
		$('#idAfipTipoComprobante').val("0");
	}
}

function getCodigoClienteEmpCallBackSoloLabel(sResponseText){
	if (sResponseText != 'null' && sResponseText != ""){
		respuesta = sResponseText.split(";*;");
		if(respuesta[0]!=null && respuesta[0]!=""){
			$("#codigoClienteLabel").html(respuesta[0]);
			$('#clienteEmpCodigoCondIva').val(respuesta[1]);
		}else{			
			$("#codigoCliente").val("");
			$("#codigoClienteLabel").html("");
			$("#codigoSerie").val("");
			$("#codigoSerieLabel").html("");
			$('#prefijoSerie').html("XXXX");
			$('#numeroComprobanteStr').val("0");
			$('#idAfipTipoComprobante').val("0");
		}
	}else{			
		$("#codigoCliente").val("");
		$("#codigoClienteLabel").html("");
		$("#codigoSerie").val("");
		$("#codigoSerieLabel").html("");
		$('#prefijoSerie').html("XXXX");
		$('#numeroComprobanteStr').val("0");
		$('#idAfipTipoComprobante').val("0");
	}
}

function getCodigoSerieCallBack(sResponseText){
	if (sResponseText == 'null' || sResponseText == ""){
		$("#codigoSerieLabel").html("");
		$("#codigoSerie").val("");
		$('#prefijoSerie').html("XXXX");
		if($('#accionPreFactura').val()=='NUEVO' || $('#accionPreFactura').val()==""
			|| $('#accionPreFactura').val()==null){
			$('#numeroComprobanteStr').val("0");
		}
	}else{
		respuesta = sResponseText.split(";*;");
		$('#codigoSerieLabel').html(respuesta[0]);
		$('#prefijoSerie').html(respuesta[1]);
		if($('#accionPreFactura').val()=='NUEVO' || $('#accionPreFactura').val()==""
			|| $('#accionPreFactura').val()==null){
			$('#numeroComprobanteStr').val(respuesta[2]);
		}
	}
}

function esconderOpcionesSelect(){
	idSelected = $('#idAfipTipoComprobanteSelected').val();
	$('#idAfipTipoComprobante').children('option').each(function(){
		optionActual = $(this);
		if(optionActual.val()!=idSelected){
			optionActual.addClass('hidden');
		}
	});
	$('#idAfipTipoComprobante').val(idSelected);
}

function eliminar(mensaje, idEliminar){
	if(idEliminar!=null && idEliminar!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Anular',function(r) {
		    if(r){
		    	document.location="eliminarDetallePreFactura.html?ordenDetalle="+idEliminar+"&accionPreFactura="+$('#accionPreFactura').val();
		    }
		});
	}
}
function habilitar(mensaje, idEliminar){
	if(idEliminar!=null && idEliminar!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Habilitar',function(r) {
		    if(r){
		    	document.location="habilitarDetallePreFactura.html?ordenDetalle="+idEliminar+"&accionPreFactura="+$('#accionPreFactura').val();
		    }
		});
	}
}
function consultar(idEliminar){
	if(idEliminar!=null)
		document.location="iniciarPrecargaFormularioPreFacturaDetalle.html?accionPreFactura="+$('#accionPreFactura').val()
			+"&ordenDetalle="+idEliminar
			+"&accionPreFacturaDetalle=CONSULTAR"
			+"&codigoClienteEmp="+$('#codigoCliente').val()
			+"&fechaStr="+$('#fechaStr').val()
			+"&codigoEmpresa="+$('#codigoEmpresa').val();
}
function modificar(idEliminar){
	if(idEliminar!=null)
		document.location="iniciarPrecargaFormularioPreFacturaDetalle.html?accionPreFactura="+$('#accionPreFactura').val()
			+"&ordenDetalle="+idEliminar
			+"&accionPreFacturaDetalle=MODIFICACION"
			+"&codigoClienteEmp="+$('#codigoCliente').val()
			+"&fechaStr="+$('#fechaStr').val()
			+"&codigoEmpresa="+$('#codigoEmpresa').val();
}


