var menuAbierto=false;
$(document).ready(function() {
	
	//Tooltips
	$("img[title]").tooltip();
	
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
	
	//Slide 1
	$('#grupoDiv').attr({'style':'display:block'});
	$('#grupoImg').click(function() {
		$('#grupoDiv').slideToggle('slideUp');
		$('#grupoImgSrcDown').slideToggle('slideUp');
		$('#grupoImgSrc').slideToggle('slideUp');
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
	
	$('#botonAgregar').click(function(){
		
	 	var allVals = [];
	    $("input[class='checklote']:checked").each(function() {
	    	allVals.push($(this).val());
	    });
	    if(allVals.length == 0)
	    {
	    	jAlert('Debe seleccionar al menos una operación para agregar a la hoja de ruta.','Selección de Operaciones');
			return;
		}
	    else
		{
	    	$("#idOperacion").val(allVals);
	    	var x=document.forms.formularioHojaRuta;
	    	x.action='agregarOperacionHojaRuta.html';
	    	x.submit();
	    }
});
	
	//tabla operaciones
	$("#listaOperacion tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
	});
	$("#listaOperacion tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	$("#operacionPlanificadas tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#operacionPlanificadas tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	$("#operacionPlanificadas tbody tr").click(function(){
		$(this).siblings().removeClass('tr_selected');
		$(this).toggleClass('tr_selected');
		$('#listaElementos tbody tr').show();
		if($(this).hasClass('tr_selected')){
			var id = $(this).find('#hdn_id_operacion').val();
			$('.operacionId').each(function(){
				if($(this).val()!=id)
					$(this).closest('tr').hide();
			});
		}
	});
	
	if($('#accion').val()!='CONSULTA'){
	    //PopUp
	    $('#operacionPlanificadas tbody tr').contextMenu('myMenu2', {
	    	bindings: {
			   'eliminar': function(t) {
				   eliminar($(t).find('#hdn_id_operacion').val());
		       }
	    	},
			onShowMenu: function(e, menu) {
				menuAbierto=true;
				return menu;
	
			},
			onHide: function(){
				menuAbierto=false;
				$("#operacionPlanificadas tbody tr").removeClass('tr_mouseover');
			}
	 
	    }); 
	    
	    //PopUp
	    $('#listaOperacion tbody tr').contextMenu('myMenu1', {
	    	bindings: {
			   'agregar': function(t) {
				   agregar($(t).find('#hdn_id').val());
			   }
	    	},
			onShowMenu: function(e, menu) {
				menuAbierto=true;
				return menu;
	
			},
			onHide: function(){
				menuAbierto=false;
				$("#listaOperacion tbody tr").removeClass('tr_mouseover');
			}
	 
	    }); 
	}
	
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
	$("#codigoSerie").blur(function(){
		getAjax('serieServletPorCodigoReturnPrefijo','codigo','codigoSerie',getCodigoSerieCallBack);
	});
	
	//botones búsqueda
	$("#buscaEmpresa").click(function(){
		abrirPopupSeleccion("popUpSeleccionEmpresa.html",null,"empresasPopupMap", null);
	});
	$("#buscaSucursal").click(function(){
		abrirPopupSeleccion("popUpSeleccionSucursal.html","codigoEmpresa","sucursalesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionCliente.html","codigoEmpresa","clientesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaTransporte").click(function(){
		abrirPopupSeleccion("popUpTransporte.html","codigoEmpresa","transportesPopupMap",$("#mensajeSeleccioneEmpresa").val());
	});	
	$("#buscaSerie").click(function(){
		abrirPopupSeleccion("popUpSeleccionSerie.html",null,null, null);
  	});
	$("#nuevaHojaRuta").click(function(){
		document.location="precargaFormularioHojaRuta.html?accion=NUEVO";
	});
	
	$("#tab_header_planificado").click(function(){
		 $("#tab_header_pendiente").removeClass("selected");
		 $("#tab_content_pendiente").hide();
		 $("#tab_header_planificado").addClass("selected");
		 $("#tab_content_planificado").show();
	});

	$("#tab_header_pendiente").click(function(){
		 $("#tab_header_planificado").removeClass("selected");
		 $("#tab_content_planificado").hide();
		 $("#tab_header_pendiente").addClass("selected");
		 $("#tab_content_pendiente").show();
	});
	
	$("#btnGuardar").click(function(){
		document.forms[0].submit();
	});
	$("#btnCancelar").click(function(){
		document.location="consultaHojaRuta.html";
	});
	$("#btnVolver").click(function(){
		document.location="consultaHojaRuta.html";
	});
	
	$("#codigoEmpresa").trigger('blur');
	$("#codigoSerie").trigger('blur');
});


function agregar(id){
	if(id!=null && id!=undefined){
		jConfirm('Agrega la operación?', 'Confirmar Agregar',function(r) {
		    if(r){
		    	$("#idOperacion").val(id);
		    	var x=document.forms.formularioHojaRuta;
		    	x.action='agregarOperacionHojaRuta.html';
		    	x.submit();
		    }
		});		
	}
}

function eliminar(id){
	if(id!=null && id!=undefined){
		jConfirm('Elimina la operación?', 'Confirmar Agregar',function(r) {
		    if(r){
		    	$("#idOperacionPlanificada").val(id);
		    	var x=document.forms.formularioHojaRuta;
		    	x.action='eliminarOperacionHojaRuta.html';
		    	x.submit();
		    }
		});		
	}
}
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

//ajax
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

function selectAll(box, classStyle) {
	var inTotal = document.getElementById("cantidadElementosSeleccionados");
	var total = inTotal.value;
	total=parseFloat(total);
	var chk = box.checked;
	total=0;
    $("#listaElementos").find('.'+classStyle).each( function(){
    	if(chk)
    		total+=1;
    	this.checked = chk;
    });
    inTotal.value=total;
    
    jQuery.ajax({
        url: 'marcarElemento.html?idElemento=selectAll&checkeado=false',
        success: function(data){
        	// alert(data);
        },
        data: ({"time":new Date().getTime()}),
        context: $(".selectorDiv")
    });
}

function marcarElemento(check, id){
	
	var inTotal = document.getElementById("cantidadElementosSeleccionados");
	var total = inTotal.value;
	total=parseFloat(total);
	if(check)
		total+=1;
	else
		total-=1;
	inTotal.value=total;
	
	jQuery.ajax({
        url: 'marcarElemento.html?idElemento='+id+'&checkeado='+check,
        success: function(data){
        	// alert(data);
        },
        data: ({"time":new Date().getTime()}),
        context: $(".selectorDiv")
    });
}
