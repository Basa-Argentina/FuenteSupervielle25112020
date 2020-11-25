var menuAbierto = false;

$(document).ready(
		function() {

			$("#observaciones").val($.trim($('#observaciones').val()));
			
			$('#observaciones').keydown(function() {
				
			    var longitud = $(this).val().length;
			    var resto = 254 - longitud;
			    if(resto <= 0){
			        $('#observaciones').attr("maxlength", 255);
			    }
			   
			});
			
			
			// Tooltips
			$("img[title]").tooltip();

			// tabla items
			$("#requerimientoElemento tbody tr").mouseover(function() {

				if (!menuAbierto)
					$(this).addClass('tr_mouseover');

			});
			$("#requerimientoElemento tbody tr").mouseout(function() {
				if (!menuAbierto)
					$(this).removeClass('tr_mouseover');
			});

			// PopUp
			$('#requerimientoElemento tbody tr').contextMenu(
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
			$('#busquedaDiv').attr({
				'style' : 'display:block'
			});
			$('#busquedaImg').click(function() {
				$('#busquedaDiv').slideToggle('slideUp');
				$('#busquedaImgSrcDown').slideToggle('slideUp');
				$('#busquedaImgSrc').slideToggle('slideUp');
			});

			$(".integer").numeric(false, function() {
				this.value = "";
				this.focus();
			});

			// binding OnBlur() para los campos con popup
			$("#clienteCodigo").blur(
					function() {
						getAjax('clientesServlet', 'codigo', 'clienteCodigo',
								getClienteCallBack);
					});
			if ($("#clienteCodigo").val() != null) {
				getAjax('clientesServlet', 'codigo', 'clienteCodigo',
						getClienteCallBackMod);
			}
			
			$("#codigoDireccion").blur(
					function() {
						getAjax('direccionesEntregaServlet', 'codigo',
								'codigoDireccion', getDireccionCallBack);
					});
			if ($("#codigoDireccion").val() != null) {
				getAjax('direccionesEntregaServlet', 'codigo', 'codigoDireccion',
						getDireccionCallBack);
			}
			$("#codigoSerie").blur(
					function() {
						getAjax('serieServletPorCodigoReturnPrefijo', 'codigo',
								'codigoSerie', getCodigoSerieCallBack);
					});
			if ($("#codigoSerie").val() != null) {
				if ($("#accion").val() == 'NUEVO') {
				getAjax('serieServletPorCodigoReturnPrefijo', 'codigo', 'codigoSerie',
						getCodigoSerieCallBack);
				}
			}
			$("#tipoRequerimientoCod").blur(
					function() {
						getAjax('tipoRequerimientoServletReturnMultiple',
								'codigo', 'tipoRequerimientoCod',
								getTipoRequerimientoCallBack);
					});
			$("#codigoPersonal").blur(
					function() {
						getAjaxEmpleado('empleadosReturnCodigoDireccionServlet', 'codigo', 'codigoPersonal',
								getPersonalCallBack);
					});
			$("#codigoPersonalAutorizante").blur(
					function() {
						getAjaxEmpleado('empleadosReturnCodigoDireccionServlet', 'codigo',
								'codigoPersonalAutorizante',
								getPersonalAutorizanteCallBack);
					});
			$("#codigoDeposito").blur(function(){
	    		getAjax('depositosServlet','codigo','codigoDeposito',getDepositoCallBack);
	    	});
			if ($("#codigoDeposito").val() != null) {
	    		getAjax('depositosServlet','codigo','codigoDeposito',getDepositoCallBack);
	    	};
	    	$("#listaPrecioCodigo").blur(function(){
	    		getAjaxListaPrecio('listasPreciosServlet','codigo','listaPrecioCodigo',getListaCallBack);
	    	});
	    	
	    	$("#buscaTipoRequerimiento").click(function(){
	    		abrirPopupSeleccion("popUpSeleccionTipoRequerimiento.html",null,null, null);
	      	});
	    	
	    	$("#buscaCliente").click(function(){
	    		abrirPopupSeleccion("popUpSeleccionClienteEmpresaDefecto.html",null,null, null);
	      	});
	    	
	    	$("#buscaSerie").click(function(){
	    		abrirPopupSeleccion("popUpSeleccionSerie.html",null,null, null);
	      	});
	    	
	    	$("#buscaPersonal").click(function(){
	    		if($("#bandera").val()=="true"){
	    			abrirPopupPorClienteTodos("popUpSeleccionEmpleadoSolicitante.html","personalPopupMap", $("#mensajeSeleccioneCliente").val());
	    		}else{
	    			abrirPopupPorCliente("popUpSeleccionEmpleadoSolicitante.html",'clienteCodigo',"personalPopupMap", $("#mensajeSeleccioneCliente").val());
	    		}
	    		
	      	});
	    	$("#buscaPersonalAutorizante").click(function(){
	    		if($("#bandera").val()=="true"){
	    			abrirPopupPorCliente("popUpSeleccionEmpleadoAutorizante.html","personalPopupMap", $("#mensajeSeleccioneCliente").val());
	    		}else{
	    			abrirPopupPorCliente("popUpSeleccionEmpleadoAutorizante.html",'clienteCodigo',"personalPopupMap", $("#mensajeSeleccioneCliente").val());
	    		}	
	      	});
	    	$("#buscaDireccion").click(function(){
	    		if($("#bandera").val()=="true"){
	    			abrirPopupSinCliente("popUpSeleccionDirecciones.html","direccionesPopupMap", $("#mensajeSeleccioneCliente").val());
	    		}else{
	    			abrirPopupPorCliente("popUpSeleccionDirecciones.html",'clienteCodigo',"direccionesPopupMap", $("#mensajeSeleccioneCliente").val());
	    		}	
	      	});
	    	$("#buscaListaPrecios").click(function(){
	    		abrirPopupPorCliente("popUpSeleccionListaPreciosPorCliente.html",'clienteCodigo',"listaPreciosPopupMap", $("#mensajeSeleccioneCliente").val());
	      	});
	    	$("#buscaDeposito").click(function(){
	    		abrirPopupSeleccion("popUpSeleccionDeposito.html",null,null, null);
	      	});
	    	
	    	//$("#codigoPersonal").blur();
	    	//$("#codigoPersonalAutorizante").blur();
	    
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

//ajax
function getAjaxEmpleado(url, varName, elementName, callBack) {
	var input = document.getElementById(elementName);
	if (input == null)
		return;
	var url = url + '?' + varName + '=' + input.value + '&clienteId='
			+ $("#clienteId").val() ;
	if($("#bandera").val()==null || $("#bandera").val()!="true"){
		url += '&clienteEmpId='+  $("#clienteCodigo").val();
	}
	var request = new HttpRequest(url, callBack);
	request.send();
}
function getAjaxListaPrecioDefault(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if (input == null)
		return;
	var url = url + '?' + varName + '=' + input.value + '&dependencia='+  $("#codigoEmpresa").val() ;
	var request = new HttpRequest(url, callBack);
	request.send();
}
//ajax
function getAjaxListaPrecio(url, varName, elementName, callBack) {
	var input = document.getElementById(elementName);
	if (input == null)
		return;
	var url = url + '?' + varName + '=' + input.value + '&clienteId='
			+ $("#clienteId").val() + '&habilitado=true';
	var request = new HttpRequest(url, callBack);
	request.send();
}
// ajax
function getClienteCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
		$("#clienteCodigoLabel").html(sResponseText);
		$("#listaPrecioCodigo").val("");
		$("#listaPrecioCodigoLabel").html("");
		getAjaxListaPrecioDefault('searchListaPrecioDefecto.html' ,'codigo','clienteCodigo',
				getListaPrecioCallBack);
		if($('#bandera').val()==null || $('#bandera').val()!="true"){
			$("#codigoPersonal").val("");
			$("#codigoPersonalLabel").html("");
			$("#codigoPersonalAutorizante").val("");
			$("#codigoPersonalAutorizanteLabel").html("");
			$("#codigoDireccion").val("");
			$("#codigoDireccionLabel").html("");
		}
		
	} else {
		$("#clienteCodigo").val("");
		$("#clienteCodigoLabel").html("");
		$("#listaPrecioCodigo").val("");
		$("#listaPrecioCodigoLabel").html("");
		if($('#bandera').val()==null || $('#bandera').val()!="true"){
			$("#codigoPersonal").val("");
			$("#codigoPersonalLabel").html("");
			$("#codigoPersonalAutorizante").val("");
			$("#codigoPersonalAutorizanteLabel").html("");
			$("#codigoDireccion").val("");
			$("#codigoDireccionLabel").html("");
		}
	}
	// Llamar al servlet que trae el booleano bandera
}
function getListaPrecioCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {

		$("#listaPrecioCodigo").val(sResponseText);
		$("#listaPrecioCodigo").trigger("blur");
	}else{
		$("#listaPrecioCodigo").val("");
		$("#listaPrecioCodigoLabel").html("");
	}
}

// ajax
function getClienteCallBackMod(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
		$("#clienteCodigoLabel").html(sResponseText);
	} else {
		$("#clienteCodigo").val("");
		$("#clienteCodigoLabel").html("");
	}
}
// ajax
function getDireccionCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDireccionLabel").html(sResponseText);
	else {
		$("#codigoDireccion").val("");
		$("#codigoDireccionLabel").html("");
	}
}
// ajax
function abrirPopupDireccion(claseNom, mensaje, title) {
	if ($("#clienteCodigo").val() == null || $("#clienteCodigo").val() == "") {
		jAlert(mensaje, title);
		return;
	}
	var url = "precargaFormularioRequerimiento.html?clienteCodigoString="
			+ $("#clienteCodigo").val() + '&primeraVez=NO';
	jQuery.ajax({
		url : url,
		success : function(data) {
			filteredResponse = $(data).find(this.selector);
			if (filteredResponse.size() == 1) {
				$(this).html(filteredResponse);
			} else {
				$(this).html(data);
			}
			$(this).displayTagAjax();
		},
		data : ({
			"time" : new Date().getTime()
		}),
		context : $(".displayTagDiv." + claseNom)
	});
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.' + claseNom);
	popupOnDiv(div, 'darkLayer');
}

// ajax
function getCodigoSerieCallBack(sResponseText) {
	
	if (sResponseText != 'null' && sResponseText != "") {
		var array = sResponseText.split('-');
		$("#codigoSerieLabel").html(array[0]);
		var numero = Number(array[1]);
		numero = numero + 1;
		$("#numeroStr").val(agregarCeros(numero, 8));
	} else {
		$("#codigoSerie").val("");
		$("#codigoSerieLabel").html("");
	}
}

// ajax
function getTipoRequerimientoCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
		var array = sResponseText.split('-');
		var componentId = "tipoRequerimientoCod";
		setResponce(array[0], componentId);
		
		$("#fechaEntrega").val("");
		$("#horaEntrega").val("");
		
		if ($("#fechaAlta").val() != null && $("#fechaAlta").val() != '')
			$("#fechaEntrega").val(sumarHoras($("#fechaAlta").val(), array[1]));
		if ($("#horaEntrega").val() == null || $("#horaEntrega").val() == ''){
			var ahora = new Date(); 
			// Horas para sumar a la hora actual
			var horas = array[1];
			// Hora actual
			var nro_dias_float;
			var n;
			var nro_dias = 0;
			var nro_horas = 0;
			if(parseInt(horas)>=24){
				nro_dias_float = horas/24;
				n = nro_dias_float+'';
				nro_dias = parseInt(n.split('.')[0]);
				nro_horas = parseInt(n.split('.')[1]);
				if(isNaN(nro_horas)){
					nro_horas = parseInt("0");
				}
			}else{
				nro_dias = 0;
				nro_horas = parseInt(horas);
			}
			// Inicio - F
			var f = ahora.getHours()+ nro_horas;
			if(f>=18){
				//hora a setear en el date que estoy trabajando
				f=f-10;
				nro_dias++;
				
			}
			ahora.setHours(f);
			// Fin - F
			// Inicio Sumar Dias
			for(var i=0;i<nro_dias;i++){
				ahora.setDate(ahora.getDate()+1);
				if(ahora.getDay()==0){
					// Sumo 1 dias "Domingo"
					ahora.setDate(ahora.getDate()+1);
				}
				if(ahora.getDay()==6){
					// Sumo 2 dias "Sabado" 
					ahora.setDate(ahora.getDate()+2);
				}
			}
			// Fin Sumar Dias
			var hora = ahora.getHours();
			if(hora.toLocaleString().length==1){
				hora = "0"+hora;
			}
			var min = ahora.getMinutes();
			if(min.toLocaleString().length==1){
				min = "0"+min;
			}
			
			$("#horaEntrega").val(hora+":"+min);
			var dia = ahora.getDate();
			if(dia.toLocaleString().length==1){
				dia = "0"+dia;
			}
			var mes = ahora.getMonth()+1;
			if(mes.toLocaleString().length==1){
				mes = "0"+mes;
			}
			$("#fechaEntrega").val(dia+"/"+mes+"/"+ahora.getFullYear());
		}	
	} else {
		$("#tipoRequerimientoCod").val("");
		$("#tipoRequerimientoCodLabel").html("");
		$("#fechaEntrega").val("");
		$("#horaEntrega").val("");
	}
}

function sumarDias(){
	
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

// ajax
function getPersonalCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
		var array = sResponseText.split('-');
		$("#codigoPersonalLabel").html(array[0]);
		$("#codigoDireccion").val(array[1]);
		$("#idDireccionDefectoAnterior").val(array[1]);
		getAjax('direccionesEntregaServlet', 'codigo', 'codigoDireccion',
				getDireccionCallBack);
		if ($("#codigoPersonalAutorizante").val() == null
				|| $("#codigoPersonalAutorizante").val() == '') {
			$("#codigoPersonalAutorizante").val($("#codigoPersonal").val());
			$("#codigoPersonalAutorizanteLabel").html(array[0]);
		}
	} else {
		$("#codigoPersonal").val("");
		$("#codigoPersonalLabel").html("");
	}
}

// ajax
function getPersonalAutorizanteCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split('-');
		$("#codigoPersonalAutorizanteLabel").html(array[0]);
	}
	else {
		$("#codigoPersonalAutorizante").val("");
		$("#codigoPersonalAutorizanteLabel").html("");
	}
}

//ajax
function getDepositoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDepositoLabel").html(sResponseText);	
	else{
		$("#codigoDeposito").val("");
		$("#codigoDepositoLabel").html("");
	}
}

function abrirPopupPersonal(claseNom, mensaje, title) {
	if ($("#clienteCodigo").val() == null || $("#clienteCodigo").val() == "") {
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioRequerimiento.html?clienteCodigoString="
			+ $("#clienteCodigo").val()+ '&primeraVez=NO';
	jQuery.ajax({
		url : url,
		success : function(data) {
			filteredResponse = $(data).find(this.selector);
			if (filteredResponse.size() == 1) {
				$(this).html(filteredResponse);
			} else {
				$(this).html(data);
			}
			$(this).displayTagAjax();
		},
		data : ({
			"time" : new Date().getTime()
		}),
		context : $(".displayTagDiv." + claseNom)
	});
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.' + claseNom);
	popupOnDiv(div, 'darkLayer');
}

function abrirPopupListaPrecios(claseNom, mensaje, title) {
	if ($("#clienteCodigo").val() == null || $("#clienteCodigo").val() == "") {
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioRequerimiento.html?clienteCodigoString="
			+ $("#clienteCodigo").val()+ '&primeraVez=NO';
	jQuery.ajax({
		url : url,
		success : function(data) {
			filteredResponse = $(data).find(this.selector);
			if (filteredResponse.size() == 1) {
				$(this).html(filteredResponse);
			} else {
				$(this).html(data);
			}
			$(this).displayTagAjax();
		},
		data : ({
			"time" : new Date().getTime()
		}),
		context : $(".displayTagDiv." + claseNom)
	});
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.' + claseNom);
	popupOnDiv(div, 'darkLayer');
}

//ajax
function getListaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#listaPrecioCodigoLabel").html(sResponseText);
	else{
		$("#listaPrecioCodigo").val("");
		$("#listaPrecioCodigoLabel").html("");
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

function sumarHoras(fechaEntrada, dias) {
	// pasaremos la fecha a formato mm/dd/yyyy
	f = fechaEntrada.split('/');
	f = f[1] + '/' + f[0] + '/' + f[2];
	// 
	hoy = new Date(f);
	hoy.setHours(dias);
	mes = hoy.getMonth() + 1;
	dia = hoy.getDate();
	fecha = agregarCeros(dia, 2) + '/' + agregarCeros(mes, 2) + '/'
			+ hoy.getFullYear();
	return fecha;
}


// Metodo que se ejecuta al terminar de cargar todos los componentes de la
// pagina
function volver() {
	document.location = "mostrarRequerimiento.html";
}
function volverCancelar() {
	document.location = "mostrarRequerimiento.html";
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

function buscarElementosReferencia(mensaje) {
	if ($("#clienteCodigo").val() == null || $("#clienteCodigo").val() == "") {
		jAlert(mensaje);
		return;
	}
	$("#buscarElemento").val(true);
	$("#buscarElementoSinReferencia").val(false);
	$("#eliminarElemento").val(false);
	// document.location="iniciarRequerimientoElemento.html?clienteCodigoString="+$("#clienteCodigo").val();
	document.forms[0].submit();
}

function buscarElementosSinReferencia(mensaje) {
	if ($("#clienteCodigo").val() == null || $("#clienteCodigo").val() == "") {
		jAlert(mensaje);
		return;
	}
	//document.location="iniciarRequerimientoElementoSinReferencia.html?clienteCodigoString="+$("#clienteCodigo").val()+"&destinoURL=prepararActualizarRequerimiento.html";
	$("#buscarElemento").val(false);
	$("#buscarElementoSinReferencia").val(true);
	$("#eliminarElemento").val(false);
	document.forms[0].submit();
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

function abrirPopupPorClienteTodos(url,nombreClase, mensaje){
	
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
function abrirPopupSinCliente(url,inputValue,nombreClase, mensaje){
//	if(inputValue!=null){
//		if($("#"+inputValue).val()==null || $("#"+inputValue).val()==""){
//			jAlert(mensaje);
//			return;
//		}
//		url = url+"?clienteCodigoString="+$("#"+inputValue).val();
//	}
	
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