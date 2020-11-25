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
		'onClosed'			: function(){ $('#botonAgregar').removeAttr('disabled');}
	});
	
	
	//tabla Detalles
	$("#preFactura tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#preFactura tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});	
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
	//PopUp click derecho
    $('#preFactura tbody tr').contextMenu('myMenu1', {

      bindings: {

        'consultar': function(t) {
          consultar($(t).find('#hdn_orden').val());
        },

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
			$("#preFactura tbody tr").removeClass('tr_mouseover');
		}

    }); 
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	
	//botones
	$('#botonCargar').click(function(){
		$(this).attr('disabled', 'disabled');
		codEmpresa = $('#codigoEmpresa').val();		
		if(codEmpresa != null && codEmpresa!=""){
			document.forms[0].submit();		;
		}else{			
			var tittle = $('#tituloError').val();
			var message =$('#codigoEmpresaRequerido').val(); 
			jAlert(message,tittle);
			$('#botonCargar').removeAttr('disabled');
		}		
	});
	
	$('#selectPeriodo').click(function(){
		
	});
	
	$('#selectPeriodo').change(function(){
		if($('#añoPeriodo').val()!=0){
			$('#periodo').val($('#selectPeriodo option:selected').val());
			var año = $('#añoPeriodo').val();
			var añoA = $('#añoPeriodo').val();
			mes = parseInt($('#periodo').val());
			if(mes<12)
				mes++;
			else if(mes==12){
				mes=1;
				añoA++;
			}
			$('#fechaFacturacion').val("01/"+mes+"/"+añoA);
			$('#descripcion').val($('#selectPeriodo option:selected').text()+" de "+año);
		}
		else{
			jAlert('Seleccione primero un año.','Período de Facturación');
			$('#selectPeriodo').val('0');
		}
	});
	
	$('#botonCargar').removeAttr('disabled');
	
	$('#tipoMovimientoInt').val($('#tipoMovimientoSelected').val());
	
	
	$('#botonGenerarConceptos').click(function(){
		if($('#periodo').val()== null || $('#periodo').val()== ""){
		jAlert('Debe seleccionar previamente un periodo de facturación.');
		return;
	}		
		$(this).attr('disabled','disabled');
		var div = $("#pop");
		popupOnDiv(div,'darkLayer');
		document.location = "generarConceptosMensualesLoteFacturacion.html?&accion="+$('#accion').val()+"&añoPeriodo="+$('#añoPeriodo').val()
		+ "&periodo="+$('#periodo').val() + "&codigoEmpresa="+$('#codigoEmpresa').val() + "&codigoSucursal="+$('#codigoSucursal').val()
		+ "&fechaRegistro="+$('#fechaRegistro').val()+"&fechaFacturacion="+$('#fechaFacturacion').val()+"&descripcion="+$('#descripcion').val()
		+ "&id="+$('#id').val();
	});
	
	$('#botonAgregar').click(function(){
		if($('#periodo').val()== null || $('#periodo').val()== ""){
		jAlert('Debe seleccionar previamente un periodo de facturación.');
		return;
	}		
		$(this).attr('disabled','disabled');
		$('#fancyboxAgregarDetalle').click();
	});
	
	$('#botonEliminar').click(function(){
		
		var allVals = [];
	    $("input[class='checklote']:checked").each(function() {
	    	allVals.push($(this).val());
	    });
	    if(allVals.length == 0)
	    {
	    	jAlert('Debe seleccionar al menos un concepto para desasociar.');
			return;
		}
	    else
		{
	    	document.location='eliminarLoteFacturacionDetalle.html?&seleccionados='+allVals
	    	+'&accion='+$('#accion').val()+'&descripcion='+$('#descripcion').val()
	    	+'&fechaRegistro='+$('#fechaRegistro').val()+'&fechaFacturacion='+$('#fechaFacturacion').val();
	    }
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
	
	if($('#activado').val()!=null && $('#activado').val()=="no")
	{
		$('#selectPeriodo').attr('disabled', 'disabled');
		$('#añoPeriodo').attr('disabled', 'disabled');
	}
	
	$('#numero').blur();
	
	var div = $("#pop");
  	popupOffDiv(div,'darkLayer');
});

function consultar(orden){
	document.location="iniciarPrecargaFormularioPreFactura.html?accionPreFactura=CONSULTA&ordenPreFactura="+orden;
}

function modificar(orden){
	document.location="iniciarPrecargaFormularioPreFactura.html?accionPreFactura=MODIFICACION&ordenPreFactura="+orden;
}

function volver(){
	document.location="mostrarLoteFacturacionSinFiltrar.html";
}

function volverCancelar(){
	document.location="mostrarLoteFacturacionSinFiltrar.html";
}

function guardarYSalir(){
 	document.forms[0].submit();
}

function eliminar(mensaje, idEliminar){
	if(idEliminar!=null && idEliminar!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar eliminar',function(r) {
		    if(r){
		    	document.location="eliminarPreFactura.html?ordenPreFactura="+idEliminar+"&accionPreFactura="+$('#accion').val();
		    }
		});
	}
}