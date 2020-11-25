var isCtrl = false;
//var zoom = 70; //Por defecto arranca en 70%

$(document).ready(function() {
	document.onkeydown = myDownKeyPressHandler;
	document.onkeyup = myUpKeyPressHandler;
	
	 $('#rearchivos_lote tbody tr').click(function(){
		 $("#rearchivos_lote tbody tr").removeClass('tr_mouseover');
		 $(this).addClass('tr_mouseover');
		 $("#orden").val($(this).find('#hdn_orden').val());

		 $("#objetoSeleccionado").val($(this).find('#hdn_id').val());
		 
		
		 $("#texto1").val($(this).find('#hdn_texto1').val());
		 $("#texto2").val($(this).find('#hdn_texto2').val());
		 $("#numero1Str").val($(this).find('#hdn_numero1').val());
		 $("#numero2Str").val($(this).find('#hdn_numero2').val());
		 $("#fecha1").val($(this).find('#hdn_fecha1').val());
		 $("#fecha2").val($(this).find('#hdn_fecha2').val());
	     $("#descripcion").val($(this).find('#hdn_descripcion').val());
	     $("#estado option[value='"+$(this).find('#hdn_estado').val()+"']").attr('selected', 'selected');
	     
	     
	     if($("#tipo", top.document).val()=='Digital' || $("#tipo", top.document).val()=='Electronico'){
		     $("#nombreArchivoDigital").val($(this).find('#hdn_nombreArchivoDigital').val());
		     $("#pathArchivoDigital").val($(this).find('#hdn_pathArchivoDigital').val());
		     $("#pathArchivoJPGDigital").val($(this).find('#hdn_pathArchivoJPGDigital').val());
		     $("#pathArchivoJPGDigitalAux").val($(this).find('#hdn_pathArchivoJPGDigital').val());
		     $("#codigoClasifDoc").val($(this).find('#hdn_codigoClasifDoc').val());
		     $("#codigoClasifDoc").blur();
		     
		     
		     if($("#nombreArchivoDigital").val().match(/.tif$/)){
			     $("#cantidadImagenes").val($(this).find('#hdn_cantidadImagenes').val());
			     cargarComboJPGDigital($(this).find('#hdn_cantidadImagenes').val());
			     if($(this).find('#hdn_cantidadImagenes').val()!=null && $(this).find('#hdn_cantidadImagenes').val()!= ''){
				     var total = parseInt($(this).find('#hdn_cantidadImagenes').val());
				     if(total > 0){
				    	 popupOnDiv($('#pop'),'darkLayer');
				    	 $("#jpgDigital").attr("src",'viewFileJPGLoteRearchivo.html?fileName='+$(this).find('#hdn_pathArchivoJPGDigital').val()+'1.jpg');
				    	 popupOffDiv($('#pop'),'darkLayer');
				     }
				 }
		     }
		     if($("#nombreArchivoDigital").val().match(/.pdf$/)){
		    	 popupOnDiv($('#pop'),'darkLayer');
		    	 $("#jpgDigital").attr("src",'../FlexPaper/common/simple_document.jsp?doc='+$(this).find('#hdn_nombreArchivoDigital').val()+'&ruta='+$(this).find('#hdn_pathArchivoJPGDigital').val());
		    	 popupOffDiv($('#pop'),'darkLayer');
		     }
		     
		     
		     
	     }
	     
	     $("#numero1Str").addClass("numeric");
	     $("#numero2Str").addClass("numeric");
	     $(".numeric").numeric();
	     
	 });
	
	$("#btnGuardar").click(function(){
		$("#codigoEmpresa", top.document).attr('readonly','readonly');
		$("#buscaEmpresa", top.document).attr('disabled','disabled');
		$("#codigoSucursal", top.document).attr('readonly','readonly');
		$("#buscaSucursal", top.document).attr('disabled','disabled');
		$("#codigoCliente", top.document).attr('readonly','readonly');
		$("#buscaCliente", top.document).attr('disabled','disabled');
		popupOnDiv($('#pop'),'darkLayer');
		document.forms[0].submit();
	});
	$("#btnCancelar").click(function(){
		document.location="seccionRearchivos.html";
	});
	var focusEn = $("#hacerFocusEn").val();
	if(focusEn!="")
		$("#"+focusEn).focus();
	//Tooltips
	$("img[title]").tooltip();
	
	//Popups Busqueda
    $('#botonPopupUsuario').click(function(){
  	  abrirPopupUsuario();
    });
    
    $("#codigoUsuario").blur(function(){
		getAjax('seleccionUsuario.html','codigo','codigoUsuario',getUsuarioCallBack);
	});
	if($("#codigoUsuario").val() != null){
		getAjax('seleccionUsuario.html','codigo','codigoUsuario',getUsuarioCallBack);
	}
	
	//binding OnBlur() para los campos con popup	
	$("#codigoClasificacionDocumental").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		searchAjax('seleccionClasificacionDocumental.html?nodo=I','codigoClasificacionDocumental','codigoCliente',getClasificacionDocumentalCallBack);
	});
	
	$("#codigoClasifDoc").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		searchAjax('seleccionClasificacionDocumental.html?nodo=I','codigoClasifDoc','codigoCliente',getClasifDocumentalCallBack);
	});
	
	$("#codigoContenedor").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		searchAjax('seleccionContenedor.html','codigoContenedor','codigoCliente',getContenedorCallBack);
	});
	
	$("#bloquearTexto1").click(function(){
		var image = $("#bloquearTexto1");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoTexto1").val(true);
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoTexto1").val(false);
		}
	});	
	$("#bloquearTexto2").click(function(){
		var image = $("#bloquearTexto2");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoTexto2").val(true);
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoTexto2").val(false);
		}
	});	
	$("#bloquearNumero1").click(function(){
		var image = $("#bloquearNumero1");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoNumero1").val(true);
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoNumero1").val(false);
		}
	});	
	$("#bloquearNumero2").click(function(){
		var image = $("#bloquearNumero2");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoNumero2").val(true);
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoNumero2").val(false);
		}
	});	
	$("#bloquearFecha1").click(function(){
		var image = $("#bloquearFecha1");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoFecha1").val(true);
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoFecha1").val(false);
		}
	});	
	$("#bloquearFecha2").click(function(){
		var image = $("#bloquearFecha2");
		if(image.attr("src")=="images/candado_abierto.gif"){
			image.attr("src", "images/candado_cerrado.gif");
			$("#bloqueoFecha2").val(true);
		}else{
			image.attr("src", "images/candado_abierto.gif");
			$("#bloqueoFecha2").val(false);
		}
	});	
	$("#buscaClasificacionDocumental").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		abrirPopupSeleccion("popUpSeleccionClasificacionDocumental.html",'codigoCliente',"clasificacionDocumentalPopupMap", $("#mensajeSeleccioneCliente").val());
	});
	$("#buscaClasifDoc").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		abrirPopupSeleccionConDestino("popUpSelClasificacionDocumental.html",'codigoCliente',"codigoClasifDoc", $("#mensajeSeleccioneCliente").val());
	});
	$("#buscaContenedor").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		abrirPopupSeleccion("popUpSeleccionContenedor.html",'codigoCliente',"contenedoresPopupMap", $("#mensajeSeleccioneCliente").val());
	});
	
	$("#codigoClasificacionDocumental").change(function(){
		$("#codigoClasificacionDocumental").trigger('blur');
	});
	$("#codigoContenedor").trigger('blur');
	
	$("#btnConfirmarLote").click(function(){
		confirmarLote();
	});
	
	$("#tipo", top.document).change(function(){
		if($(this).val()=='Digital'){
			$('#confirmarDiv').slideUp();
			$('#fileDiv').slideDown();
			$("#cantidad").attr('disabled','disabled');
			$("#cantidad").removeClass('requerido');
			$("#tdContenedor").css('display','block');
			$("#tdContenedorTitulo").css('display','block');
			$("#codigoContenedor").val(" ");
		}
		else if($(this).val()=='Electronico')
		{
			$('#confirmarDiv').slideUp();
			$('#fileDiv').slideDown();
			$("#cantidad").attr('disabled','disabled');
			$("#cantidad").removeClass('requerido');
			$("#tdContenedor").css('display','none');
			$("#tdContenedorTitulo").css('display','none');
			$("#codigoContenedor").val(" ");
		}
		else
		{
			$('#fileDiv').slideUp();
			$('#confirmarDiv').slideDown();
			$("#cantidad").removeAttr('disabled'); 
			$("#cantidad").addClass('requerido');
			$("#tdContenedor").css('display','block');
			$("#tdContenedorTitulo").css('display','block');
			$("#codigoContenedor").val(" ");
		}
			
	});
	
	$("#selectCantidadJPG").change(function(){
		if($(this)!=null && $(this)!=undefined && $(this).val()!=null){
			popupOnDiv($('#pop'),'darkLayer');
			var fileName = $("#pathArchivoJPGDigitalAux").val() + $(this).val() + '.jpg';
			$("#jpgDigital").attr("src",'viewFileJPGLoteRearchivo.html?fileName='+fileName); 
			popupOffDiv($('#pop'),'darkLayer');
		}
			
	});
	
	var zoom = parseInt($('#zoom').val());
	$('#jpgDigital').css({'height' : zoom +'%', 'width' : zoom +'%'});
	$('#zoomTxt').val(zoom + ' %');
	$('#zoom').val(zoom);
	
	var scrollY = $('#scrollY').val();
	var scrollX = $('#scrollX').val();
	$('#divImagen').animate({ scrollTop: scrollY }, 'slow');
	$('#divImagen').animate({ scrollLeft: scrollX }, 'slow');
	
	
	$('#divImagen').scroll(function() {
		  $('#scrollY').val($('#divImagen').scrollTop());
		  $('#scrollX').val($('#divImagen').scrollLeft());
	});
	
	
});



function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarRearchivo.html?obj_hash="+id;
		    }
		});		
	}
}

function checkRefresh(){
	if($("#codigoClasificacionDocumental").val()!=""){
		refresh();
	}
}
//ajax
function getClasificacionDocumentalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoClasificacionDocumentalLabel").html(sResponseText);
	}else{
		$("#codigoClasificacionDocumentalLabel").html("");
		$("#codigoClasificacionDocumental").val("");
	}
}

function getClasifDocumentalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoClasifDocLabel").html(sResponseText);
	}else{
		$("#codigoClasifDocLabel").html("");
		$("#codigoClasifDoc").val("");
	}
}

function confirmarLote(){
	if($("#codigoClasificacionDocumental").val() == null || $("#codigoClasificacionDocumental").val()==''){
		jAlert($("#mensajeSeleccionarClasificacion").val(),'Error');
		return;
	}
	if($("#cantidad").val() == null || $("#cantidad").val()==''){
		jAlert($("#mensajeIngresarCantidad").val(),'Error');
		return;
	}
	if($("#cantidad").val() != null && $("#cantidad").val()!='' && parseInt($("#cantidad").val())==0){
		jAlert($("#mensajeIngresarCantidad").val(),'Error');
		return;
	}
	if($("#codigoContenedor").val() == null || $("#codigoContenedor").val()==''){
		jAlert($("#mensajeSeleccioneContenedor").val(),'Error');
		return;
	}
	$("#codigoEmpresa", top.document).attr('readonly','readonly');
	$("#buscaEmpresa", top.document).attr('disabled','disabled');
	$("#codigoSucursal", top.document).attr('readonly','readonly');
	$("#buscaSucursal", top.document).attr('disabled','disabled');
	$("#codigoCliente", top.document).attr('readonly','readonly');
	$("#buscaCliente", top.document).attr('disabled','disabled');
	$("#tipo", top.document).addClass('hiddenInput');
	$("#tipoAux", top.document).removeClass('hiddenInput');
	$("#tipoAux", top.document).val($("select#tipo option:selected", top.document).val());
	refresh();
}

function refresh(){
	$("#rearchivoFormulario").attr('action',"refrescarFormRearchivo.html");
	popupOnDiv($('#pop'),'darkLayer');
	$("#rearchivoFormulario").submit();
}

function getContenedorCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoContenedorLabel").html(sResponseText);
	else{
		$("#codigoContenedorLabel").html("");
		$("#codigoContenedor").val("");
	}
}

function myDownKeyPressHandler(evtobj) {
	var unicode=evtobj.charCode? evtobj.charCode : evtobj.keyCode;
  if(unicode == 17) isCtrl=true;
  //13 = enter
  if(unicode == 13 && isCtrl == true) {
	popupOnDiv($('#pop'),'darkLayer');
	document.forms[0].submit();
	return false;
  }

}
function myUpKeyPressHandler(evtobj) {
  var unicode=evtobj.charCode? evtobj.charCode : evtobj.keyCode;
  if(unicode == 17) isCtrl=false;
}

//function limpiarClasificacionDocumental(){
//	if($("#codigoClasificacionDocumental").val()!=""){
//		$("#codigoClasificacionDocumental").val("");
//		$("#codigoClasificacionDocumental").trigger('blur');
//	}
//}


function seleccionarItemTabla(item){
	if(item == null || item == undefined || item == '')
		return;
//	$("#rearchivos_lote").find('#hdn_orden').each( function(){
//		if(this.value == item){
//			$("#rearchivos_lote tbody tr").trigger('click');
//    	}
//    });
	
}

function pasar(){
	$("#codigoClasificacionDocumental2").val($("#codigoClasificacionDocumental").val());
	$("#codigoContenedor2").val($("#codigoContenedor").val());
	$("#indiceIndividual2").val($("input[@name='indiceIndividual']:checked").val());
	
	$('#codigoCliente2').val($("#codigoCliente", top.document).val());
	$('#tipo2').val($("select#tipo option:selected", top.document).val());
	$('#codigoEmpresa2').val($("#codigoEmpresa", top.document).val());
	$('#codigoSucursal2').val($("#codigoSucursal", top.document).val());
	if($("#file").val() == null || $("#file").val()==''){
		jAlert($("#mensajeSeleccionarFile").val(),'Error');
		return;
	}
	var elem = $("#file").val().split('.');
	var cadena = elem[1];
	if(cadena.toLowerCase()!=null){
		
		if($('#tipoImg').val()!=null && $('#tipoImg').val()!=undefined)
		{
			if($('#tipoImg').val()=='TIFF' && (cadena.toLowerCase() != 'zip' && cadena.toLowerCase() != 'tiff' && cadena.toLowerCase() != 'tif' && cadena.toLowerCase() != 'jpg')){
				jAlert($("#mensajeSeleccionarFileTIFF").val(),'Error');
				return;
			}
			else if($('#tipoImg').val()=='PDF' && (cadena.toLowerCase() != 'zip' && cadena.toLowerCase() != 'pdf')){
				jAlert($("#mensajeSeleccionarFilePDF").val(),'Error');
				return;
			}
		}
		else if(cadena.toLowerCase() != 'zip')
		{
			jAlert($("#mensajeSeleccionarFile").val(),'Error');
			return;
		}
		
	}
		
	if($("#codigoClasificacionDocumental").val() == null || $("#codigoClasificacionDocumental").val()==''){
		jAlert($("#mensajeSeleccionarClasificacion").val(),'Error');
		return;
	}
	if(($("#codigoContenedor").val() == null || $("#codigoContenedor").val()=='') && $("#tipo", top.document).val()!="Electronico")
	{
		jAlert($("#mensajeSeleccioneContenedor").val(),'Error');
		return;
	}
	$("#codigoEmpresa", top.document).attr('readonly','readonly');
	$("#buscaEmpresa", top.document).attr('disabled','disabled');
	$("#codigoSucursal", top.document).attr('readonly','readonly');
	$("#buscaSucursal", top.document).attr('disabled','disabled');
	$("#codigoCliente", top.document).attr('readonly','readonly');
	$("#btnGuardar", top.document).removeAttr('disabled');
	$("#tipo", top.document).addClass('hiddenInput');
	$("#tipoAux", top.document).removeClass('hiddenInput');
	$("#tipoAux", top.document).val($("select#tipo option:selected", top.document).val());
	popupOnDiv($('#pop'),'darkLayer');
	$("#fileUploadForm").submit();
}

function downloadFile(fileName){
	document.location="downloadFileLoteRearchivo.html?fileName="+fileName;
}

function view(fileName) {
	var iframe = parent.frames[1];
	iframe.window.location.href = 'viewFileJPGLoteRearchivo.html?fileName='+fileName;
}

function view2() {
	var iframe = parent.frames[1];
	//alert(iframe);
	jAlert(iframe);
	iframe.window.location.href = 'viewFileJPGLoteRearchivo.html?fileName=c://error.JPG';
}

function cargarComboJPGDigital(cantidad){
	if(cantidad==null || cantidad == undefined)
		return;
	
	if($("#nombreArchivoDigital").val().match(/.tif$/)){
		var total = parseInt(cantidad);
		var select = document.getElementById ('selectCantidadJPG');
		while (select.options.length) { 
			select.options.remove (0); 
	    } 
	
	    for (var i=1; i <= total; i++) { 
	        var option = new Option (i, i); 
	        select.options.add (option); 
	    }
	}
	
    $('#imageDiv').slideDown();
}

function zoomIn(){
	var zoom = parseInt($('#zoom').val());
	if(zoom < 200){
		zoom = zoom + 10;
		$('#jpgDigital').css({'height' : zoom +'%', 'width' : zoom +'%'});
		$('#zoomTxt').val(zoom + ' %');
		$('#zoom').val(zoom);
	}
}

function zoomOut(){
	var zoom = parseInt($('#zoom').val());
	if(zoom > 10){
		zoom = zoom - 10;
		$('#jpgDigital').css({'height' : zoom +'%', 'width' : zoom +'%'});
		$('#zoomTxt').val(zoom + ' %');
		$('#zoom').val(zoom);
	}
}

function abrirPopupSeleccionConDestino(url,inputValue,destino, mensaje){
	if(inputValue!=null){
		if($("#"+inputValue).val()==null || $("#"+inputValue).val()==""){
			jAlert(mensaje);
			return;
		}
		if(url.indexOf("?") != -1)
			url = url+'&codigo='+$("#"+inputValue).val();
		else
			url = url+'?codigo='+$("#"+inputValue).val();
		
		if(destino!=null && destino!="" && destino!=undefined)
			url = url + '&destino='+destino;
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
