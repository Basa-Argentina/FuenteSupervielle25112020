var isCtrl = false;
$(document).ready(function() {
	
//	if($('#chkLectorContenedor',top.document).is(':checked')) {
//		$("#codigoContenedor").attr("maxlength", "13");
//	}
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	
	document.onkeydown = myDownKeyPressHandler;
	document.onkeyup = myUpKeyPressHandler;
	
	$("#btnGuardarReferencia").click(function(){
		
		if($("#chkDoubleInput").is(":checked")){
			
			if($("#texto1").val()!=null && $("#texto1").val()!="")
			{
				if($("#texto1").val().length%2==0){
					
					primeraMitad = $("#texto1").val().substr(0,$("#texto1").val().length/2);
					segundaMitad = $("#texto1").val().substr($("#texto1").val().length/2,$("#texto1").val().length);
					
					//jAlert(primeraMitad +  " - "+segundaMitad);
					if(primeraMitad != segundaMitad){
						jAlert("El código cargado por duplicado no es coincidente");
						return false;
					} else {
						$("#texto1").val(primeraMitad);
					}
				} else { 
					jAlert("El código cargado por duplicado no es coincidente");
					return false;
				}
			} else {
				jAlert("Ingrese un código de longitud mayor a cero");
				return false;
			}
			
			
		}
		
		$("#btnGuardarReferencia").attr("disabled","disabled");
		$("#codigoEmpresa", top.document).attr('readonly','readonly');
		$("#buscaEmpresa", top.document).attr('disabled','disabled');
		$("#codigoSucursal", top.document).attr('readonly','readonly');
		$("#buscaSucursal", top.document).attr('disabled','disabled');
		$("#codigoCliente", top.document).attr('readonly','readonly');
		$("#buscaCliente", top.document).attr('disabled','disabled');
		$("#idLoteReferencia").val($("#id", top.document).val());
		if($('#porRango').val()!=null && $('#porRango').val()=='true')
			$('#referenciaFormulario').attr('action','agregarRangoReferencia.html');
		document.forms[0].submit();
		
		
	});
	
	$("#btnCancelar").click(function(){
		$('#sortForm').submit();
	});
	
	//Tooltips
	$("img[title]").tooltip();
	
	//PopUp
	if($('#accion', top.document).val()!="CONSULTA"){
	    $('#referencias_lote tbody tr').contextMenu('myMenu1', {
	      bindings: {
	        'eliminar': function(t) {
	        	eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val(),$('#indiceIndividual').val());
	        },
	        'modificar': function(t) {
		      	 modificar($(t).find('#hdn_id').val());
	        },
	        'agregarRango': function(t) {
		      	 modificar($(t).find('#hdn_id').val());
	        }
	      },
			onShowMenu: function(e, menu) {
				menuAbierto=true;
				return menu;
			},
			onHide: function(){
				menuAbierto=false;
				$("#referencias_lote tbody tr").removeClass('tr_mouseover');
			}
	    }); 
	}
	
	//binding OnBlur() para los campos con popup	
	$("#codigoClasificacionDocumental").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		searchAjax('seleccionClasificacionDocumental.html?nodo=I','codigoClasificacionDocumental','codigoCliente',getClasificacionDocumentalCallBack);
	});
	
	$("#prefijoCodigoTipoElemento").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		searchAjax('seleccionTipoElementoByPrefijoCodigo.html','prefijoCodigoTipoElemento','codigoCliente',getTipoElementoCallBack);
	});
	$("#codigoContenedor").blur(function(){
		// Para el caso de ingreso de datos mediante lector
		var codigo = $("#codigoContenedor").val();
		if($('#chkLectorContenedor',top.document).is(':checked')) {
			codigo = "0" + codigo;
			codigoEAN = codigo.substring(12,13);
			codigo = codigo.substring(0,12);
			$("#codigoContenedor").val(codigo);
			$("#codigoConteEAN").val(codigoEAN);
		}
				
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		//Modifica Adrian oculta el check y Cerrrar Caja Supervielle
		
		var codclient1 = $("#codigoCliente").val();
		if (codclient1 == "231"){
		
		$("#cerrarCaja").addClass("hiddenInput");
		$("#labelCajaCerrada").addClass("hiddenInput");

		}
		
		// Codigo contenedor contatenar los ceros con le prefijo
		valor = $("#codigoContenedor").val();
		if(valor.length>0 && valor.length<12){
			numCeros = $("#prefijoCodigoTipoElemento").val()+'0000000000'; 
			valor = numCeros.substring(0,numCeros.length-valor.length)+valor;
			$("#codigoContenedor").val(valor);
		}
		if(valor.length>12){
			codigo12 = valor.substring(0,12);
			codigoEAN = valor.substring(12,13);
			$("#codigoContenedor").val(codigo12);
			$("#codigoConteEAN").val(codigoEAN);
			$("#codigoConteEAN").blur();
		}
		if(valor.length==12)
			$("#codigoConteEAN").focus();
		
//		if($('#obj_hash').val()!=null && $('#obj_hash').val()!="")
//			searchAjax("seleccionContenedor2.html?limitarCliente=false",'codigoContenedor','codigoCliente',getContenedorCallBack);
//		else
//			searchAjax("seleccionContenedor2.html?limitarCliente=false&cerrado=false",'codigoContenedor','codigoCliente',getContenedorCallBack);
	});
	
	$("#codigoContenedor").keyup(function(){
		codigoConte = $("#codigoContenedor").val();
		if(codigoConte.length==12)
			$("#codigoConteEAN").focus();
	});
	
	$("#codigoConteEAN").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		var mensaje = $("#mensajeErrorEAN").val();
		//if($("#codigoElemento").val()!=null && $("#codigoElemento").val()!=''){
			codigo13 = $("#codigoContenedor").val() + $("#codigoConteEAN").val();
			if(checkEan(codigo13)){
				if($('#obj_hash').val()!=null && $('#obj_hash').val()!="")
					searchAjax("seleccionContenedor2.html?limitarCliente=false",'codigoContenedor','codigoCliente',getContenedorCallBack);
				else
					searchAjax("seleccionContenedor2.html?limitarCliente=false&cerrado=false",'codigoContenedor','codigoCliente',getContenedorCallBack);
			}
			else{
				jAlert(mensaje,"Error");
				$("#codigoContenedor").val('');
				$("#codigoConteEAN").val('');
				$("#codigoContenedorLabel").html('');
				$("#codigoContenedor").focus();
				return;
			}
		//}
	});
	
	$("#codigoElemento").blur(function(){
		codigo = $("#codigoElemento").val();
		if($('#chkLector',top.document).is(':checked'))
			codigo = "0"+codigo;
		if(codigo.length>12){
			codigo12 = codigo.substring(0,12);
			codigoEAN = codigo.substring(12,13);
			$("#codigoElemento").val(codigo12);
			$("#codigoEAN").val(codigoEAN);
			$("#codigoEAN").blur();
		}
		if(codigo.length==12)
			$("#codigoEAN").focus();
	});
	
	$("#codigoElemento").keyup(function(){
		codigo = $("#codigoElemento").val();
		if(codigo.length==12)
			$("#codigoEAN").focus();
	});
	$("#codigoEAN").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		var loteRefId=$("#id",top.document).val();
		var mensaje = $("#mensajeErrorEAN").val();
		//if($("#codigoElemento").val()!=null && $("#codigoElemento").val()!=''){
			codigo13 = $("#codigoElemento").val() + $("#codigoEAN").val();
			if(checkEan(codigo13)){
				searchAjax("seleccionElemento.html?limitarCliente=false&libresODistintoLoteId="+loteRefId,'codigoElemento','codigoCliente',getElementoCallBack);
			}
			else{
				jAlert(mensaje,"Error");
				$("#codigoElemento").val('');
				$("#codigoEAN").val('');
				$("#codigoElementoLabel").html('');
				$("#codigoElemento").focus();
				return;
			}
		//}
	});
	
	$("#codigoElementoDesde").blur(function(){
		var palabra = $("#codigoElementoDesde").val();
		if($('#referencias_lote:contains('+palabra+')').length > 0){
			$("#codigoElementoDesde").val("");
			$("#codigoElementoDesdeLabel").html("");
		}else{
			$('#codigoCliente').val($("#codigoCliente", top.document).val());
			var loteRefId=$("#id",top.document).val();
			searchAjax("seleccionElemento.html?limitarCliente=false&libresODistintoLoteId="+loteRefId,'codigoElementoDesde','codigoCliente',getElementoDesdeCallBack);
		}
	});
	
	$("#codigoElementoHasta").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		var loteRefId=$("#id",top.document).val();
		searchAjax("seleccionElemento.html?limitarCliente=false&libresODistintoLoteId="+loteRefId,'codigoElementoHasta','codigoCliente',getElementoHastaCallBack);
	});
	
	$("#numero1").blur(function(){
		var numeroFinal = $("#numero1").val();
		if($('#leeCodigoBarra').val()!=null && $('#leeCodigoBarra').val()=="true" && numeroFinal.length>=11){
			numeroFinal = numeroFinal.replace(/\D/g,'');//Se quitan todo los caracteres que no sean numeros
			if($('#leeCodigoBarraDesde').val()!="" && $('#leeCodigoBarraHasta').val()!=""){
				numeroFinal = numeroFinal.substring($('#leeCodigoBarraDesde').val(),$('#leeCodigoBarraHasta').val());
				$("#numero1").val(numeroFinal);
			}
		}
	});
	
	$("#bloquearClasificacionDocumental").click(function(){
		var image = $("#bloquearClasificacionDocumental");

		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoClasificacionDocumental").val(true);
			$("#codigoClasificacionDocumental").attr('readonly','readonly');
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoClasificacionDocumental").val(false);
			$("#codigoClasificacionDocumental").removeAttr('readonly');
		}
	});	
	
	$("#bloquearTipoElementoContenedor").click(function(){
		var image = $("#bloquearTipoElementoContenedor");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoTipoElementoContenedor").val(true);
			$("#prefijoCodigoTipoElemento").attr('readonly','readonly');
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoTipoElementoContenedor").val(false);
			$("#prefijoCodigoTipoElemento").removeAttr('readonly');
		}
	});	
	$("#bloquearContenedor").click(function(){
		var image = $("#bloquearContenedor");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoContenedor").val(true);
			$("#codigoContenedor").attr('readonly','readonly');
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoContenedor").val(false);
			$("#codigoContenedor").removeAttr('readonly');
		}
	});	
	$("#bloquearTexto1").click(function(){
		var image = $("#bloquearTexto1");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoTexto1").val(true);
			$("#texto1").attr('readonly','readonly');
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoTexto1").val(false);
			$("#texto1").removeAttr('readonly');
		}
	});	
	$("#bloquearTexto2").click(function(){
		var image = $("#bloquearTexto2");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoTexto2").val(true);
			$("#texto2").attr('readonly','readonly');
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoTexto2").val(false);
			$("#texto2").removeAttr('readonly');
		}
	});	
	$("#bloquearNumero1").click(function(){
		var image = $("#bloquearNumero1");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoNumero1").val(true);
			$("#numero1").attr('readonly','readonly');
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoNumero1").val(false);
			$("#numero1").removeAttr('readonly');
		}
	});	
	$("#bloquearNumero2").click(function(){
		var image = $("#bloquearNumero2");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoNumero2").val(true);
			$("#numero2").attr('readonly','readonly');
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoNumero2").val(false);
			$("#numero2").removeAttr('readonly');
		}
	});	
	$("#bloquearFecha1").click(function(){
		var image = $("#bloquearFecha1");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoFecha1").val(true);
			$("#fecha1").attr('readonly','readonly');
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoFecha1").val(false);
			$("#fecha1").removeAttr('readonly');
		}
	});	
	$("#bloquearFecha2").click(function(){
		var image = $("#bloquearFecha2");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoFecha2").val(true);
			$("#fecha2").attr('readonly','readonly');
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoFecha2").val(false);
			$("fecha2").removeAttr('readonly');
		}
	});	
	$("#incrementarElemento").click(function(){
		var iconoIncremento = $("#incrementarElemento");
		if(iconoIncremento.attr("src")=="images/noIncrementar.png"){
			iconoIncremento.attr("src", "images/incrementar.png");
			$("#incrementoElemento").val(true);
		}else{
			iconoIncremento.attr("src", "images/noIncrementar.png");	
			$("#incrementoElemento").val(false);
		}
		
	});
	
	$("#buscaClasificacionDocumental").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		abrirPopupSeleccion("popUpSeleccionClasificacionDocumental.html",'codigoCliente',"clasificacionDocumentalPopupMap", $("#mensajeSeleccioneCliente").val());
	});
	
	
	$("#buscaTipoElemento").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		abrirPopupSeleccion("popUpSeleccionTipoElementoPrefijo.html","codigoCliente",null, $("#mensajeSeleccioneCliente").val());
	  });
	
	$("#buscaContenedor").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		if($("#prefijoCodigoTipoElemento").val()==null || $("#prefijoCodigoTipoElemento").val()=="")
			abrirPopupSeleccion("popUpSeleccionContenedorEAN.html?cerrado=false&limitarCliente=false",'codigoCliente',"contenedoresPopupMap", $("#mensajeSeleccioneCliente").val());
		else
			abrirPopupSeleccion("popUpSeleccionContenedorEAN.html?cerrado=false&limitarCliente=false&prefijoCodigoTipoElemento="+$('#prefijoCodigoTipoElemento').val(),'codigoCliente',"contenedoresPopupMap", $("#mensajeSeleccioneCliente").val());
		
	});
	$("#buscaElemento").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		var loteRefId = $("#id",top.document).val();
		abrirPopupSeleccion("popUpSeleccionElementoEAN.html?limitarCliente=false&libresODistintoLoteId="+loteRefId,'codigoCliente',null, $("#mensajeSeleccioneCliente").val());
	});
	
	$("#buscaElementoDesde").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		var loteRefId = $("#id",top.document).val();
		abrirPopupSeleccionEditable("popUpSeleccionElementoEditable.html?limitarCliente=false&libresODistintoLoteId="+loteRefId,'codigoCliente','codigoElementoDesde', $("#mensajeSeleccioneCliente").val());
	});
	
	$("#buscaElementoHasta").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		var loteRefId = $("#id",top.document).val();
		abrirPopupSeleccionEditable("popUpSeleccionElementoEditable.html?limitarCliente=false&libresODistintoLoteId="+loteRefId,'codigoCliente','codigoElementoHasta', $("#mensajeSeleccioneCliente").val());
	});
	$("#buscaDescripcion").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		abrirPopupSeleccion("popUpSeleccionDescripcionClasificacionDocumental.html?codigo2="+$('#codigoCliente').val(),"codigoClasificacionDocumental","descripcionesPopupMap", $("#mensajeSeleccioneClasificacionDocumental").val());
	});
	
	//Popups Busqueda
    $('#botonPopupUsuario').click(function(){
  	  abrirPopupUsuario();
    });
	
	$("#codigoClasificacionDocumental").change(function(){
		$("#codigoClasificacionDocumental").trigger('blur');
	});
	
	$("#codigoUsuario").blur(function(){
		getAjax('seleccionUsuario.html','codigo','codigoUsuario',getUsuarioCallBack);
	});
	if($("#codigoUsuario").val() != null){
		getAjax('seleccionUsuario.html','codigo','codigoUsuario',getUsuarioCallBack);
	}
	
	$("#prefijoCodigoTipoElemento").trigger('blur');
	$("#buscaTipoElemento").trigger('blur');
	$("#codigoElemento").trigger('blur');

	$('input').keydown( function (event) { //event==Keyevent
		if(event.which == 17) isCtrl=true;
		if(event.which == 70 && isCtrl) { //ctrl + F
			var inputs = $(this).closest('form').find(':input:visible');
			inputs.eq( inputs.index(this)+ 1 ).click();	
			event.preventDefault();
		}
		if(event.which == 13 && isCtrl) { //ctrl + enter
			$(this).closest('form').submit();
			event.preventDefault(); 
		}
		if(event.which == 13 && !isCtrl) { //enter

				//var inputs = $(this).closest('form').find(':input:text:enabled,textarea');
				var inputs = $(this).closest('form').find(':input:text:not([readonly]),textarea');
				$(this).blur();
				inputs.eq( inputs.index(this)+ 1 ).focus();			
				event.preventDefault();
		}	
	});
	$("#prefijoCodigoTipoElemento").keydown( function (event) { //event==Keyevent
		
		if(event.which == 9 && !isCtrl) { //tab

				var inputs = $(this).closest('form').find(':input:text:enabled,textarea');
				$(this).blur();
				inputs.eq( inputs.index(this)+ 1 ).focus();			
				event.preventDefault();
		}	
	});
	$('input').keyup( function (event) { //event==Keyevent
		if(event.which == 17) isCtrl=false;
	});
	
	var focusEn = $("#hacerFocusEn").val();
	if(focusEn!="")
		$("#"+focusEn).focus();
		
	
	$('.seccion2',top.document).find('.botonCentrado').click(function(){
		
		//SI VIENE UNA RECARGA DE PAGINA DE "GUARDAR Y CONTINUAR" ENTONCES SE CARGAN LOS DATOS
		
		if($('#bloqueoTipo', top.document).val()!=null && $('#bloqueoTipo', top.document).val()=="true"){
			$('#bloqueoTipo', top.document).val("");
			$("#bloquearTipoElementoContenedor").click();
			if($('#codigoTipoPadre', top.document).val()!=null && $('#codigoTipoPadre', top.document).val()!=""){
				$('#prefijoCodigoTipoElemento').val($('#codigoTipoPadre', top.document).val());
				$('#codigoTipoPadre', top.document).val("");
				$('#prefijoCodigoTipoElemento').blur();
			} 
		}
		
		if($('#bloqueoContenedor', top.document).val()!=null && $('#bloqueoContenedor', top.document).val()=="true"){
			$('#bloqueoContenedor', top.document).val("");
			$("#bloquearContenedor").click();
			if($('#codigoContenedorPadre', top.document).val()!=null && $('#codigoContenedorPadre', top.document).val()!=""){
				$('#codigoContenedor').val($('#codigoContenedorPadre', top.document).val());
				$('#codigoContenedorComparar').val($('#codigoContenedorComparar', top.document).val());
				$('#codigoContenedorPadre', top.document).val("");
				$('#codigoContenedorComparar', top.document).val("");
				$('#codigoContenedor').blur();
			} 
		}
		
		if($('#incrementoElemento', top.document).val()!=null && $('#incrementoElemento', top.document).val()=="true"){
			$('#incrementoElemento', top.document).val("");
			$("#incrementarElemento").click();
			if($('#codigoElementoPadre', top.document).val()!=null && $('#codigoElementoPadre', top.document).val()!=""){
				$('#codigoElemento').val($('#codigoElementoPadre', top.document).val());
				$('#codigoElementoPadre', top.document).val("");
				$('#codigoElemento').blur();
			} 
		}
		
		if($('#bloqueoNumero1', top.document).val()!=null && $('#bloqueoNumero1', top.document).val()=="true"){
			$('#bloqueoNumero1Hijo').val($('#bloqueoNumero1', top.document).val());
			$('#bloqueoNumero1', top.document).val("");
			if($('#numero1Padre', top.document).val()!=null && $('#numero1Padre', top.document).val()!=""){
				$('#numero1Hijo').val($('#numero1Padre', top.document).val());
				$('#numero1Padre', top.document).val("");
			} 
		}
		
		if($('#bloqueoTexto1', top.document).val()!=null && $('#bloqueoTexto1', top.document).val()=="true"){
			$('#bloqueoTexto1Hijo').val($('#bloqueoTexto1', top.document).val());
			$('#bloqueoTexto1', top.document).val("");
			if($('#texto1Padre', top.document).val()!=null && $('#texto1Padre', top.document).val()!=""){
				$('#texto1Hijo').val($('#texto1Padre', top.document).val());
				$('#texto1Padre', top.document).val("");
			} 
		}
		
		if($('#bloqueoNumero2', top.document).val()!=null && $('#bloqueoNumero2', top.document).val()=="true"){
			$('#bloqueoNumero2Hijo').val($('#bloqueoNumero2', top.document).val());
			$('#bloqueoNumero2', top.document).val("");
			if($('#numero2Padre', top.document).val()!=null && $('#numero2Padre', top.document).val()!=""){
				$('#numero2Hijo').val($('#numero2Padre', top.document).val());
				$('#numero2Padre', top.document).val("");
			} 
		}
		
		if($('#bloqueoTexto2', top.document).val()!=null && $('#bloqueoTexto2', top.document).val()=="true"){
			$('#bloqueoTexto2Hijo').val($('#bloqueoTexto2', top.document).val());
			$('#bloqueoTexto2', top.document).val("");
			if($('#texto2Padre', top.document).val()!=null && $('#texto2Padre', top.document).val()!=""){
				$('#texto2Hijo').val($('#texto2Padre', top.document).val());
				$('#texto2Padre', top.document).val("");
			} 
		}
		
		if($('#bloqueoClasificacion', top.document).val()!=null && $('#bloqueoClasificacion', top.document).val()=="true"){
			$('#bloqueoClasificacion', top.document).val("");
			$("#bloquearClasificacionDocumental").click();
			if($('#codigoClasificacionDocumentalPadre', top.document).val()!=null && $('#codigoClasificacionDocumentalPadre', top.document).val()!=""){
				$('#codigoClasificacionDocumental').val($('#codigoClasificacionDocumentalPadre', top.document).val());
				$('#codigoClasificacionDocumentalPadre', top.document).val("");
				$('#codigoClasificacionDocumental').blur();
			} 
		}
		
		
	});
	
});
function eliminar(mensaje, id, indiceIndividual){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarReferencia.html?obj_hash="+id+"&indiceIndividual="+indiceIndividual;
		    }
		});		
	}
}
function modificar(id){
	if(id!=null && id!=undefined){
    	document.location="modificarReferencia.html?obj_hash="+id;
	}
}

//ajax
function getClasificacionDocumentalCallBack(sResponseText){	
	var refrescar = false;
	if (sResponseText != 'null' && sResponseText != ""){
		//if($("#codigoClasificacionDocumentalLabel").html().trim()!=sResponseText.trim()){
		if($.trim($('#codigoClasificacionDocumentalLabel').html())!= $.trim(sResponseText)){
			$("#codigoClasificacionDocumentalLabel").html(sResponseText);
			refrescar = true;
		}
	}else{
		//if($("#codigoClasificacionDocumentalLabel").html().trim()!="")
		if($.trim($('#codigoClasificacionDocumentalLabel').html())!="")
			refrescar = true;
		$("#codigoClasificacionDocumentalLabel").html("");
		$("#codigoClasificacionDocumental").val("");
	}
	if(refrescar){
		if($('#porRango').val()==null || $('#porRango').val()=='false' || $('#porRango').val()=='')
			setTimeout(refresh, 2000);
	}
}

function refresh(){
	$("#referenciaFormulario").attr('action',"refrescarFormReferencia.html");
	$("#referenciaFormulario").submit();
}

function getContenedorCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split("-");
		$("#codigoContenedorLabel").html(array[0]);
		if(array[1]=="true"){
			if(($("#roleCerrarHdn").val()==null || $("#roleCerrarHdn").val()=="") && ($('#obj_hash').val()!=null && $('#obj_hash').val()!="")){
				$("#btnCancelar").click();
			}
			else{
				$('#cerrarCaja').attr('checked','checked');
			}
			
		}
		var codConteStr = $.trim($('#codigoContenedor').val());
		var codConteCompaStr = $.trim($('#codigoContenedorComparar').val());
		var codConte = parseInt(codConteStr);
		var codConteCompa = parseInt(codConteCompaStr);
		var url = window.location.pathname;
		//alert(url);
		if($('#codigoContenedor').val()!= $('#codigoContenedorComparar').val() && codConte != codConteCompa && url.indexOf("modificarReferencia.html") < 0){
			//alert("Entro");
			$('#codigoContenedorComparar').val($('#codigoContenedor').val());
			// Llamar al servicio para validar si el contenedor ya tenga referencias cargadas
			searchAjax("validarReferenciaContenedor.html",'codigoContenedor',null,getValidarReferenciaContenedorCallBack);
		}
	}else{
		$("#codigoContenedorLabel").html("");
		$("#codigoContenedor").val("");
		$("#codigoConteEAN").val("");
		var pathname = window.top.location.pathname;
		if($('#accion', top.document).val()!=null && $('#accion', top.document).val()=="MODIFICACION" 
			&& ($("#roleCerrarHdn").val()==null || $("#roleCerrarHdn").val()=="") && (pathname.indexOf("iniciarFormularioLoteReferencia.html") >= 0)){
	//			$("#contenedorGeneral").hide();
	//			jAlert("La caja o lote seleccionado está cerrado, para modificarlo por favor comuníquese con un administrador.");
		}
	}
}

function getValidarReferenciaContenedorCallBack(sResponseText){
	if(sResponseText!=''){
		jAlert('El elemento tiene referencias cargadas');
		return;
	}
}
function getTipoElementoCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != ""){
		$("#prefijoCodigoTipoElementoLabel").html(sResponseText);
		$("#codigoContenedor").trigger('blur');
	}else{
		$("#prefijoCodigoTipoElementoLabel").html("");
		$("#prefijoCodigoTipoElemento").val("");
	}
}
function getElementoCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoElementoLabel").html(sResponseText);
	}else{
		$("#codigoEAN").val("");
		$("#codigoElementoLabel").html("");
		$("#codigoElemento").val("");
	}
}
function getElementoDesdeCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoElementoDesdeLabel").html(sResponseText);
	}else{
		$("#codigoElementoDesdeLabel").html("");
		$("#codigoElementoDesde").val("");
	}
}
function getElementoHastaCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoElementoHastaLabel").html(sResponseText);
	}else{
		$("#codigoElementoHastaLabel").html("");
		$("#codigoElementoHasta").val("");
	}
}
function myDownKeyPressHandler(evtobj) {	
  var unicode=evtobj.charCode? evtobj.charCode : evtobj.keyCode;
  if(unicode == 17) isCtrl=true;
  //13 = enter
          
  if(unicode == 13 && isCtrl == true) {
	document.forms[0].submit();
	return false;
  }
  
  if(window.event && window.event.keyCode == 116){ 
	     return false;
  } 
}
function myUpKeyPressHandler(evtobj) {
  var unicode=evtobj.charCode? evtobj.charCode : evtobj.keyCode;
  if(unicode == 17) isCtrl=false;
}

function limpiarClasificacionDocumental(){
	if($("#codigoClasificacionDocumental").val()!=""){
		$("#codigoClasificacionDocumental").val("");
		$("#codigoClasificacionDocumental").trigger('blur');
	}
}
function mostrarMensaje(mensaje){
	if(mensaje!=undefined)
		jConfirm(mensaje, $('#confirmarContinuarConReferencia').val());		
}

function bloquearClasificacionDocumental(){
	var image = $("#bloquearClasificacionDocumental");

	if(image.attr("src")=="images/candado_abierto.gif"){
		image.attr("src", "images/candado_cerrado.gif");
		$("#bloqueoClasificacionDocumental").val(true);
		$("#codigoClasificacionDocumental").attr('readonly','readonly');
	}else{
		image.attr("src", "images/candado_abierto.gif");
		$("#bloqueoClasificacionDocumental").val(false);
		$("#codigoClasificacionDocumental").removeAttr('readonly');
	}
}

function bloquearTipoContenedor(){
	var image = $("#bloquearTipoElementoContenedor");
	if(image.attr("src")=="images/candado_abierto.gif"){
		image.attr("src", "images/candado_cerrado.gif");
		$("#bloqueoTipoElementoContenedor").val(true);
		$("#prefijoCodigoTipoElemento").attr('readonly','readonly');
	}else{
		image.attr("src", "images/candado_abierto.gif");
		$("#bloqueoTipoElementoContenedor").val(false);
		$("#prefijoCodigoTipoElemento").removeAttr('readonly');
	}
}

function bloquearContenedor(){
	var image = $("#bloquearContenedor");
	if(image.attr("src")=="images/candado_abierto.gif"){
		image.attr("src", "images/candado_cerrado.gif");
		$("#bloqueoContenedor").val(true);
		$("#codigoContenedor").attr('readonly','readonly');
	}else{
		image.attr("src", "images/candado_abierto.gif");
		$("#bloqueoContenedor").val(false);
		$("#codigoContenedor").removeAttr('readonly');
	}
}

function incrementarElemento(){
	var iconoIncremento = $("#incrementarElemento");
	if(iconoIncremento.attr("src")=="images/noIncrementar.png"){
		iconoIncremento.attr("src", "images/incrementar.png");
		$("#incrementoElemento").val(true);
	}else{
		iconoIncremento.attr("src", "images/noIncrementar.png");	
		$("#incrementoElemento").val(false);
	}	
}

function bloquearNumero1(){
	var image = $("#bloquearNumero1");
	if(image.attr("src")=="images/candado_abierto.gif"){
		image.attr("src", "images/candado_cerrado.gif");
		$("#bloqueoNumero1").val(true);
		$("#numero1").attr('readonly','readonly');
	}else{
		image.attr("src", "images/candado_abierto.gif");
		$("#bloqueoNumero1").val(false);
		$("#numero1").removeAttr('readonly');
	}
}

function bloquearTexto1(){
	var image = $("#bloquearTexto1");
	if(image.attr("src")=="images/candado_abierto.gif"){
		image.attr("src", "images/candado_cerrado.gif");
		$("#bloqueoTexto1").val(true);
		$("#texto1").attr('readonly','readonly');
	}else{
		image.attr("src", "images/candado_abierto.gif");
		$("#bloqueoTexto1").val(false);
		$("#texto1").removeAttr('readonly');
	}
}

function abrirPopupUsuario(){
	var url = "popUpSeleccionUsuario.html?codigo="+$("#codigoEmpresa", top.document).val();
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
function getUsuarioCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoUsuarioLabel").html(sResponseText);
	else{
		$("#codigoUsuario").val("");
		$("#codigoUsuarioLabel").html("");
	}
}

//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#codigoCliente", top.document).val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

function abrirPopupSeleccionEditable(url,inputValue,nombreClase, mensaje){
	if(inputValue!=null){
		if($("#"+inputValue).val()==null || $("#"+inputValue).val()==""){
			jAlert(mensaje);
			return;
		}
		if(url.indexOf("?") != -1)
			url = url+'&codigo='+$("#"+inputValue).val()+'&cajaRespuesta='+nombreClase;
		else
			url = url+'?codigo='+$("#"+inputValue).val()+'&cajaRespuesta='+nombreClase;
	}
	
	if($('#pop').length>0){
		jQuery.ajax({
	        url: url,
	        success: function(data){
	           $(this).html(data);
	           $(".displayTagDiv").displayTagAjax();
	           popupOffDiv($('#pop'),'darkLayer');
	           popupOnDiv($(this).find('.darkMiddleClass'),'darkLayer');
	        },
	        data: ({"time":new Date().getTime()}),
	        context: $(".selectorDiv"),
	        beforeSend:function(){
	        	popupOnDiv($('#pop'),'darkLayer');
	        },
	        error:function(){
	        	jAlert("Ha ocurrido un error...");
	        	popupOffDiv($('#pop'),'darkLayer');
	        }
	    });
	}else {
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