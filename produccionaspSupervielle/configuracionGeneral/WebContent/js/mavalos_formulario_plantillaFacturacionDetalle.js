var menuAbierto = false;

$(document).ready(function() {
						
	$("#descripcionConcepto").val($.trim($('#descripcionConcepto').val()));
	
	$('#descripcionConcepto').keydown(function() {
	    var longitud = $(this).val().length;
	    var resto = 200 - longitud;
	    if(resto <= 0){
	        $('#descripcionConcepto').attr("maxlength", 200);
	    }
	});
	
	
	
			// Tooltips
			$("img[title]").tooltip();

			// tabla items
			$("#plantillaFacturacionDetalle tbody tr").mouseover(function() {

				if (!menuAbierto)
					$(this).addClass('tr_mouseover');

			});
			$("#plantillaFacturacionDetalle tbody tr").mouseout(function() {
				if (!menuAbierto)
					$(this).removeClass('tr_mouseover');
			});

			//PopUp
			if($('#accion').val()!='CONSULTA'){
		      $('#plantillaFacturacionDetalle tbody tr').contextMenu('myMenu1', {
		 
		        bindings: {
		 
		           'modificar': function(t) {
		        	   
		            modificar($(t).find('#hdn_orden').val());
		 
		          },
		 
		          'eliminar': function(t) {
		 
		        	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_orden').val());
		 
		          }
		 
		        },
				onShowMenu: function(e, menu) {
					menuAbierto=true;
					return menu;

				},
				onHide: function(){
					menuAbierto=false;
					$("#plantillaFacturacionDetalle tbody tr").removeClass('tr_mouseover');
				}
		 
		      });
			}
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
			
			
			

			$(".integer").numeric(false, function() {
				this.value = "";
				this.focus();
			});

			// binding OnBlur() para los campos con popup
			$("#codigoConcepto").blur(function() {
				getAjax('conceptoFacturableServlet', 'codigo', 'codigoConcepto',getConceptoCallBack);
			});
			if ($("#codigoConcepto").val() != null) {
				getAjax('conceptoFacturableServlet', 'codigo', 'codigoConcepto',getConceptoCallBack);
			}
	    	$("#buscaConcepto").click(function(){
	    		abrirPopupSeleccion("popUpSeleccionConceptoFacturable.html",null,null, null);
	      	});
	    	
	    	$("#cancelarMod").click(function(){
	    		document.location = "precargaFormularioPlantillaFacturacionDetalle.html?accion="+$('#accion').val()+"&idPlantilla="+$('#idPlantilla').val();
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
function getConceptoCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
		$("#codigoConceptoLabel").html(sResponseText);
		if($('#descripcionConcepto').val()==null || $('#descripcionConcepto').val().length==0 || $('#descripcionConcepto').val()==""){
			$("#descripcionConcepto").val(sResponseText);
		}
	} else {
		$("#codigoConcepto").val("");
		$("#codigoConceptoLabel").html("");
		$("#descripcionConcepto").val("");
	}
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

function modificar(orden){
	if(orden!=null)
		document.location="precargaFormularioPlantillaFacturacionDetalle.html?accion=MODIFICACION&idPlantilla="+$('#idPlantilla').val()+"&accionDetalle=MODIFICACION&orden="+orden;
}

function eliminar(mensaje, orden) {
	if (orden != null && orden != undefined && mensaje != undefined) {
		jConfirm(mensaje, 'Confirmar Eliminar', function(r) {
			if (r) {document.location = "eliminarPlantillaFacturacionDetalle.html?accion="+$('#accion').val()+"&orden="+orden;
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