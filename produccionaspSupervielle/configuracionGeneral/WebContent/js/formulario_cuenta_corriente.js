var menuAbierto=false;
$(document).ready(function(){
	
	//tabla operaciones
	$("#medioPago tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
	});
	$("#medioPago tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	$("#comprobante tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
	});
	$("#comprobante tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	//PopUp
    $('#comprobante tbody tr').contextMenu('myMenu2', {

      bindings: {

        'eliminarComprobante': function(t) {
      	 eliminarComprobante($(t).find('#hdn_eliminarComprobante').val(),$(t).find('#hdn_idComprobante').val());
        }

      },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#comprobante tbody tr").removeClass('tr_mouseover');
		}

    }); 
	
	//PopUp
    $('#medioPago tbody tr').contextMenu('myMenu1', {

      bindings: {

        'consultar': function(t) {
          consultar($(t).find('#hdn_id').val());
        },

        'modificar': function(t) {
          modificar($(t).find('#hdn_id').val(),$(t).find('#hdn_estado').val());
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
			$("#medioPago tbody tr").removeClass('tr_mouseover');
		}

    }); 
	
	
	
	$('#guardar').click(function(){
		document.forms[0].submit();
	});	
	
  $(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
  $(".numerico").numeric(); 	
  $("#codigoCliente").blur(function(){
		searchAjax('seleccionCliente.html','codigoCliente','codigoEmpresa',getClienteCallBack);
  });

  $("#codigoCliente").trigger('blur');
  
  
	  $('#codigoSerie').blur(function(){
			getAjaxCodigoSerie();
	  });
 
  
if($("#codigoSerie").val()!=null && $("#codigoSerie").val()!=""){
	$("#codigoSerie").trigger('blur');
}
  
  $("#agregarMedioCobro").click(function(){
	 	parent.$.fancybox({
			'width'				: '60%',
			'height'			: '60%',
			'autoScale'			: true,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
			onCleanup : function(){  
				return document.location = 'precargaFormularioCuentaCorriente.html?codigoSerie='
					+$("#codigoSerie").val()+
					'&codigoCliente='+$("#codigoCliente").val()+
					'&numeroComprobanteStr='+$("#numeroComprobanteStr").val()+
					'&notasFacturacion='+$("#notasFacturacion").val();
				
				
			},
			'href'				: 'mostrarFormularioMedioCobro.html?accion=NUEVO'
		});
	});	
	
	$("#agregarComprobante").click(function(){
		if($("#codigoCliente").val()==null || $("#codigoCliente").val()==''){
			jAlert("Seleccione un cliente");
		}else{
		 	parent.$.fancybox({
				'width'				: '35%',
				'height'			: '60%',
				'autoScale'			: false,
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'type'				: 'iframe',
				onCleanup : function(){
					return document.location = 'precargaFormularioCuentaCorriente.html?codigoSerie='
						+$("#codigoSerie").val()+
						'&codigoCliente='+$("#codigoCliente").val()+
						'&numeroComprobanteStr='+$("#numeroComprobanteStr").val()+
						'&notasFacturacion='+$("#notasFacturacion").val();
				},
				'href'				: 'mostrarFormularioComprobante.html?accion=NUEVO&codigoCliente='+$("#codigoCliente").val()
			});
		}	
	});	
	
	
});

function consultar(id){
	if(id!=null)
		parent.$.fancybox({
			'width'				: '60%',
			'height'			: '60%',
			'autoScale'			: true,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
			onCleanup : function(){  
				return document.location = 'precargaFormularioCuentaCorriente.html?codigoSerie='
					+$("#codigoSerie").val()+
					'&codigoCliente='+$("#codigoCliente").val()+
					'&numeroComprobanteStr='+$("#numeroComprobanteStr").val()+
					'&notasFacturacion='+$("#notasFacturacion").val();
				
				
			},
			'href'				: 'mostrarFormularioMedioCobro.html?accion=CONSULTAR&id='+id
		});
}

function modificar(id){
	if(id!=null)
		parent.$.fancybox({
			'width'				: '60%',
			'height'			: '60%',
			'autoScale'			: true,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
			onCleanup : function(){  
				return document.location = 'precargaFormularioCuentaCorriente.html?codigoSerie='
					+$("#codigoSerie").val()+
					'&codigoCliente='+$("#codigoCliente").val()+
					'&numeroComprobanteStr='+$("#numeroComprobanteStr").val()+
					'&notasFacturacion='+$("#notasFacturacion").val();
				
				
			},
			'href'				: 'mostrarFormularioMedioCobro.html?accion=MODIFICAR&id='+id
		});
}

function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	jQuery.ajax({
		            url: 'eliminarMedioCobro.html?id='+id,
		            success: function(data){
		            	document.location = 'precargaFormularioCuentaCorriente.html?codigoSerie='
							+$("#codigoSerie").val()+
							'&codigoCliente='+$("#codigoCliente").val()+
							'&numeroComprobanteStr='+$("#numeroComprobanteStr").val()+
							'&notasFacturacion='+$("#notasFacturacion").val();
		            },
		            data: ({"time":new Date().getTime()}),
		            context: $(".selectorDiv")
		        });
		    }
		});		
	}
}

function eliminarComprobante(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	jQuery.ajax({
		            url: 'eliminarComprobante.html?id='+id,
		            success: function(data){
		            	document.location = 'precargaFormularioCuentaCorriente.html?codigoSerie='
							+$("#codigoSerie").val()+
							'&codigoCliente='+$("#codigoCliente").val()+
							'&numeroComprobanteStr='+$("#numeroComprobanteStr").val()+
							'&notasFacturacion='+$("#notasFacturacion").val();
		            },
		            data: ({"time":new Date().getTime()}),
		            context: $(".selectorDiv")
		        });
		    }
		});		
	}
}
function getClienteCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#codigoClienteLabel").html("");
		$("#codigoCliente").val("");
	}
	
	try{
		var iframe = document.getElementById('seccionReferencias');
		iframe = iframe.contentWindow;
		iframe.limpiarClasificacionDocumental();
	}catch(e){}
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

function getCodigoSerieCallBack(sResponseText){
	if (sResponseText == 'null' || sResponseText == ""){
		$("#codigoSerieLabel").html("");
		$("#codigoSerie").val("");
		$('#prefijoSerie').html("XXXX");
		if($('#accion').val()=='NUEVO' || $('#accion').val()==""
			|| $('#accion').val()==null){
			$('#numeroComprobanteStr').val("");
		}
	}else{
		respuesta = sResponseText.split(";*;");
		$('#codigoSerieLabel').html(respuesta[0]);
		$('#prefijoSerie').html(respuesta[1]);
		if($('#accion').val()=='NUEVO' || $('#accion').val()==""
			|| $('#accion').val()==null){
			if($('#numeroComprobanteStr').val()==""){
				$('#numeroComprobanteStr').val(respuesta[2]);
			}
		}
	}
}


function volverCancelar(){
	document.location="iniciarCuentaCorriente.html";
}

function abrirPopupClienteEmp(){
	headerNoModificable = $('#headerFacturaNoModificable').val();
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
//	headerNoModificable = $('#headerFacturaNoModificable').val();
//	if(headerNoModificable=='FALSE' || headerNoModificable == 'false'){
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
//	}
}

function setImporte(id,importe, saldoDisponible){

    if(parseFloat(importe)<=parseFloat(saldoDisponible)){
		jQuery.ajax({
	        url: 'setImporte.html?id='+id+'&importe='+importe,
	        success: function(data){
	        	document.location = 'precargaFormularioCuentaCorriente.html?codigoSerie='
					+$("#codigoSerie").val()+
					'&codigoCliente='+$("#codigoCliente").val()+
					'&numeroComprobanteStr='+$("#numeroComprobanteStr").val()+
					'&notasFacturacion='+$("#notasFacturacion").val();
	        },
	        data: ({"time":new Date().getTime()}),
	        context: $(".selectorDiv")
	    });
    }else{
    	jAlert('El monto imputado es mayor al saldo disponible del comprobante');
    }
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

	var url = "serieServletParaRecibo";
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
