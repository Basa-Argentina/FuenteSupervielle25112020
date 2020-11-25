var menuAbierto = false;

$(document).ready(
		function() {
						
			// Tooltips
			$("img[title]").tooltip();

			// tabla items
			$("#plantillaFacturacion tbody tr").mouseover(function() {

				if (!menuAbierto)
					$(this).addClass('tr_mouseover');

			});
			$("#plantillaFacturacion tbody tr").mouseout(function() {
				if (!menuAbierto)
					$(this).removeClass('tr_mouseover');
			});

			// PopUp
			$('#plantillaFacturacion tbody tr').contextMenu(
					'myMenu1',
					{

						bindings : {

							'eliminar' : function(t) {

								eliminar($(t).find('#hdn_eliminar').val(), $(t)
										.find('#hdn_id').val());

							}

						},
						onShowMenu : function(e, menu) {
							menuAbierto = true;
							return menu;

						},
						onHide : function() {
							menuAbierto = false;
							$("#requerimiento tbody tr").removeClass(
									'tr_mouseover');
						}

					});

			// Validaciones y botones
			$('.completarZeros').blur(function(e) {
				valorSinZeros = '';
				valorSinZeros = $(this).val();
				zerosIzq = '';
				maximo = $(this).attr('maxlength');
				actual = valorSinZeros.length;
				for (i = 0; i < (maximo - actual); i++) {
					zerosIzq += '0';
				}

				$(this).val(zerosIzq + valorSinZeros);
			});

			// Slide 1
			$('#busquedaDiv').attr({'style' : 'display:block'});
			$('#busquedaImg').click(function() {
				$('#busquedaDiv').slideToggle('slideUp');
				$('#busquedaImgSrcDown').slideToggle('slideUp');
				$('#busquedaImgSrc').slideToggle('slideUp');
			});
				
			//Slide 2
			$('#grupoDiv').attr({'style':'display:block'});
			$('#grupoImg').click(function() {
				$('#grupoDiv').slideToggle('slideUp');
				$('#grupoImgSrcDown').slideToggle('slideUp');
				$('#grupoImgSrc').slideToggle('slideUp');
			});
			
			
			$('#tipoComprobanteId').change(function(){
				$('#codigoSerie').blur();
			});

			$(".integer").numeric(false, function() {
				this.value = "";
				this.focus();
			});

			// binding OnBlur() para los campos con popup
			$("#clienteCodigo").blur(function() {
				getAjax('clientesServlet', 'codigo', 'clienteCodigo',getClienteCallBack);
			});
			if ($("#clienteCodigo").val() != null) {
				getAjax('clientesServlet', 'codigo','clienteCodigo',getClienteCallBack);
			}
			$("#codigoSerie").blur(function() {
				getAjaxCodigoSerie();
			});
			if ($("#codigoSerie").val() != null && $("#codigoSerie").val() != "") {
				getAjaxCodigoSerie();
			}
	    	$("#buscaCliente").click(function(){
	    		abrirPopupSeleccion("popUpSeleccionClienteEmpresaDefecto.html",null,null, null);
	      	});
	    	
	    	$("#buscaListaPrecio").click(function(){
	    		abrirPopupSeleccion("popUpSeleccionListaPrecios.html",null,null, null);
	      	});
	    	if ($("#listaPreciosCodigo").val() != null && $("#listaPreciosCodigo").val() != "") {
	    		getAjaxConParent('listasPreciosServlet','codigo','listaPreciosCodigo','listaPreciosCodigo',getListaPreciosCallBack);
			}
	    	$("#listaPreciosCodigo").blur(function() {
	    		getAjaxConParent('listasPreciosServlet','codigo','listaPreciosCodigo','listaPreciosCodigo',getListaPreciosCallBack);
			});
	    	$("#buscaSerie").click(function(){
	    		error = $('#error').val();
	    		if($('#tipoComprobanteId').val() == null || $('#tipoComprobanteId').val() == "" || $('#tipoComprobanteId').val()=="0"){
	    			jAlert($('#errorSeleccioneTipoComprobante').val(),error);
	    			return;
	    		}
	    		abrirPopupPorSerie("popUpSerie.html",null,'tipoComprobanteId', null);
	      	});
	    	
		});

function abrirPopup(claseNom) {
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.' + claseNom);
	popupOnDiv(div, 'darkLayer');
}

// ajax
function getAjax(url, varName, elementName, callBack) {
	var input = document.getElementById(elementName);
	if (input == null)
		return;
	var url = url + '?' + varName + '=' + input.value + '&clienteId='
			+ $("#clienteId").val();
	var request = new HttpRequest(url, callBack);
	request.send();
}

// ajax
function getClienteCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
		$("#clienteCodigoLabel").html(sResponseText);
	} else {
		$("#clienteCodigo").val("");
		$("#clienteCodigoLabel").html("");
	}
}

//ajax
function getListaPreciosCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
		$("#listaPreciosCodigoLabel").html(sResponseText);
	} else {
		$("#listaPreciosCodigo").val("");
		$("#listaPreciosCodigoLabel").html("");
	}
}

// ajax
function getCodigoSerieCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
			var array = sResponseText.split(";*;");
			$("#codigoSerieLabel").html(array[0]);
		} else {
			$("#codigoSerie").val("");
			$("#codigoSerieLabel").html("");
		}
}

//ajax
function getAjaxConParent(url, varName, elementName, parentId,callBack){
	if($("#"+parentId+"").val()!=null && $("#"+parentId+"").val()!=""){
		var input = document.getElementById(elementName);
		if(input == null)
			return;
		var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val()+'&sucursalId='+$("#"+parentId+"").val()+'&clienteEmpId='+$("#"+parentId+"").val()+'&'+parentId+'='+$("#"+parentId+"").val()+'&tipoSerie=R'+'&habilitado=true';	
		var request = new HttpRequest(url, callBack);
		request.send();
	}
}

// ajax
function setResponce(sResponseText, componentId) {
	if (sResponseText != 'null' && sResponseText != "") {
		$("#" + componentId + "Label").html(sResponseText);
	} else {
		$("#" + componentId).val("");
		$("#" + componentId + "Label").html("");
	}
}

function agregarCeros(numero, maximo) {
	valorSinZeros = '';
	valorSinZeros = numero + '';
	zerosIzq = '';
	actual = valorSinZeros.length;
	for (i = 0; i < (maximo - actual); i++) {
		zerosIzq += '0';
	}
	return zerosIzq + valorSinZeros + '';
}

function sumarDias(fechaEntrada, dias) {
	// pasaremos la fecha a formato mm/dd/yyyy
	f = fechaEntrada.split('/');
	f = f[1] + '/' + f[0] + '/' + f[2];
	// 
	hoy = new Date(f);
	hoy.setTime(hoy.getTime() + dias * 24 * 60 * 60 * 1000);
	mes = hoy.getMonth() + 1;
	dia = hoy.getDate();
	fecha = agregarCeros(dia, 2) + '/' + agregarCeros(mes, 2) + '/'
			+ hoy.getFullYear();
	return fecha;
}

// Metodo que se ejecuta al terminar de cargar todos los componentes de la
// pagina
function volver() {
	document.location = "mostrarPlantillaFacturacion.html";
}
function volverCancelar() {
	document.location = "mostrarPlantillaFacturacion.html";
}
function guardarYSalir() {
	$("#buscarElemento").val(false);
	$("#buscarElementoSinReferencia").val(false);
	$("#eliminarElemento").val(false);
	if($("#idDireccionDefectoAnterior").val() != $("#codigoDireccion").val()) {
		mensaje = $("#cambioDireccionDefectoMensaje").val();
		jConfirm(mensaje, 'Confirmar Cambiar',function(r) {
		    if(r){
		    	$("#cambioDireccionDefecto").val(true);
		    }
		    document.forms[0].submit();
		});
	}
	else{
		document.forms[0].submit();
	}
	
}

function eliminar(mensaje, id) {
	if (id != null && id != undefined && mensaje != undefined) {
		jConfirm(mensaje, 'Confirmar Eliminar', function(r) {
			if (r) {
				$("#buscarElemento").val(false);
				$("#buscarElementoSinReferencia").val(false);
				$("#eliminarElemento").val(true);
				$("#eliminarElementoId").val(id);
				document.forms[0].submit();
			}
		});
	}
}

function abrirPopupPorCliente(url,inputValue,nombreClase, mensaje){
	if(inputValue!=null){
		if($("#"+inputValue).val()==null || $("#"+inputValue).val()==""){
			jAlert(mensaje);
			return;
		}
		url = url+"?clienteCodigoString="+$("#"+inputValue).val();
	}
	if(nombreClase){
		if(url.indexOf("?") != -1)
			url = url+'&codigoEmpresa='+$("#"+nombreClase).val();
		else
			url = url+'?codigoEmpresa='+$("#"+nombreClase).val();
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

function abrirPopupPorSerie(url,inputValue,nombreClase, mensaje){
	if(inputValue!=null){
		if($("#"+inputValue).val()==null || $("#"+inputValue).val()==""){
			jAlert(mensaje);
			return;
		}
		if(url.indexOf("?") != -1)
			url = url+'&codigo='+$("#"+inputValue).val();
		else
			url = url+'?codigo='+$("#"+inputValue).val();
		
	}
	if(nombreClase){
		if(url.indexOf("?") != -1)
			url = url+'&idAfipTipoComprobante='+$("#"+nombreClase).val();
		else
			url = url+'?idAfipTipoComprobante='+$("#"+nombreClase).val();
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

function getAjaxCodigoSerie(){
	codigoEmpresa =  $("#codigoEmpresa").val();
	idAfipTipoComprobante = $('#tipoComprobanteId').val();
	codigoClienteEmp = $('#codigoCliente').val();
	codigoSerie = $('#codigoSerie').val();
	error = $('#error').val();
	
	if(codigoEmpresa ==null || codigoEmpresa == ""){
		jAlert($('#errorSeleccioneEmpresa').val(),error);
		return false;
	}
	if(idAfipTipoComprobante == null || idAfipTipoComprobante == "" || idAfipTipoComprobante=="0"){
		jAlert($('#errorSeleccioneTipoComprobante').val(),error);
		return;
	}

	var url = "serieServletParaFactura2";
	jQuery.ajax({
        url: url,
        success: function(data){
           getCodigoSerieCallBack(data);
           popupOffDiv($('#pop'),'darkLayer');            
        },
        type: "GET",
        timeout:120000,
        data: "&codigoEmpresa=" + codigoEmpresa
			+"&idAfipTipoComprobante=" + idAfipTipoComprobante
			+"&codigoCliente=" + codigoClienteEmp
			+"&codigo=" + codigoSerie,
        context: $(".selectorDiv"),
        dataType: "html",
        beforeSend:function(){
        	popupOnDiv($('#pop'),'');
        },
        error:function(){
        	jAlert("Ha ocurrido un error...");
        	popupOffDiv($('#pop'),'');
        }
    });
}