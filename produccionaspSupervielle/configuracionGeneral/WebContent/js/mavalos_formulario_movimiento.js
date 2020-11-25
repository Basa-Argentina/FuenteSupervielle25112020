$(document).ready(function() {		
	$("#descripcion").val($.trim($('#descripcion').val()));
	
	$('#descripcion').keydown(function() {
	    var longitud = $(this).val().length;
	    var resto = 60 - longitud;
	    if(resto <= 0){
	        $('#descripcion').attr("maxlength", 60);
	    }
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
	
	$("#cargarCodigosElementos").click(function(){
//		if($("#codigoDepositoOrigen").val()==null || $("#codigoDepositoOrigen").val()==""){
//		jAlert('Debe seleccionar previamente un depósito de origen');
//		return;
//	}
		var seleccionados=document.getElementById("direccionesSeleccionadasDer");
		var i=0;
		for (i=0;i<seleccionados.options.length;i++)
		  {
			seleccionados.options[i].selected=true;
		  }
		var lista = $('#direccionesSeleccionadasDer').val();
		if(lista!= null)
		{
			var combo = document.getElementById('anexarCodigos');
			var accion = $('#accion').val();
			var group =  $('input[name="'+$('input[type="radio"]').attr("name")+'"]');
			var claseMovimiento = getRadioButtonSelectedValue(group);
			var tipoMovimiento = $("input[name='tipoMovimiento']:checked").val();
			jConfirm('¿Estas seguro de cargar la lista de codigos de elementos?','Confirmar cargar lista de códigos',function(r){
				if(r){
				document.location="cargarListadoCodigosElementosMovimiento.html?listaCodigosElementos="+lista+"&codigoDepositoActual="+$('#codigoDepositoActual').val()
				+"&codigoDepositoOrigenDestino="+$('#codigoDepositoOrigenDestino').val()+"&codigoClienteEmp="+$('#codigoClienteEmp').val()
				+"&fecha="+$('#fecha').val()+"&accion="+accion+"&anexarCodigos="+combo.checked+"&codigoSucursalOrigenDestino="+$('#codigoSucursalOrigenDestino').val()
				+"&claseMovimiento="+claseMovimiento+"&id="+$('#id').val()+"&tipoMovimiento="+tipoMovimiento+"&descripcion="+$('#descripcion').val()+"&codigoResponsable="+$('#codigoResponsable').val();}
			});
		}
	});
	
	$("#importar").click(function(){
//		if($("#codigoDepositoOrigen").val()==null || $("#codigoDepositoOrigen").val()==""){
//			jAlert('Debe seleccionar previamente un depósito de origen');
//			return;
//		}
		if($("#codigoLectura").val()!=null && $("#codigoLectura").val()!=""){
			var cl = $('#codigoLectura').val();
			var combo = document.getElementById('anexar');
			var accion = $('#accion').val();
			var group =  $('input[name="'+$('input[type="radio"]').attr("name")+'"]');
			var claseMovimiento = getRadioButtonSelectedValue(group);
			var tipoMovimiento = $("input[name='tipoMovimiento']:checked").val();
			jConfirm('¿Estas seguro de importar la lectura ingresada?','Confirmar importar lectura',function(r){
				if(r){
				document.location="importarLecturaElementosMovimiento.html?codigoLectura="+cl+"&codigoDepositoActual="+$('#codigoDepositoActual').val()
				+"&codigoDepositoOrigenDestino="+$('#codigoDepositoOrigenDestino').val()+"&codigoClienteEmp="+$('#codigoClienteEmp').val()
				+"&fecha="+$('#fecha').val()+"&accion="+accion+"&anexar="+combo.checked+"&codigoSucursalOrigenDestino="+$('#codigoSucursalOrigenDestino').val()
				+"&claseMovimiento="+claseMovimiento+"&id="+$('#id').val()+"&tipoMovimiento="+tipoMovimiento+"&descripcion="+$('#descripcion').val()+"&codigoResponsable="+$('#codigoResponsable').val();}
			 });
		}
	});
	
	$("#agregar").click(function(){
		document.location="iniciarPrecargaFormularioMovimiento.html";
	});
	
	$("#botonQuitar").click(function(){
		var allVals = [];
	    $("input[class='checklote']:checked").each(function() {
	    	allVals.push($(this).val());
	    });
	    if(allVals.length == 0)
	    {
	    	jAlert('Debe seleccionar al menos un elemento para quitar.','Error al quitar');
			return;
		}
	    else
		{
	    	
	    	if($("input[name=checktodos]:checked").length == 1){
	    		allVals = "todos";
			}
	    	var group =  $('input[name="'+$('input[type="radio"]').attr("name")+'"]');
			var claseMovimiento = getRadioButtonSelectedValue(group);
			var tipoMovimiento = $("input[name='tipoMovimiento']:checked").val();
	    	document.location="eliminarMovimientoDetalle.html?codigoLectura="+$('#codigoLectura').val()+"&codigoDepositoActual="+$('#codigoDepositoActual').val()
			+"&codigoDepositoOrigenDestino="+$('#codigoDepositoOrigenDestino').val()+"&codigoClienteEmp="+$('#codigoClienteEmp').val()
			+"&fecha="+$('#fecha').val()+"&accion="+$('#accion').val()+"&codigoSucursalOrigenDestino="+$('#codigoSucursalOrigenDestino').val()
			+"&claseMovimiento="+claseMovimiento+"&id="+$('#id').val()+"&tipoMovimiento="+tipoMovimiento+"&seleccionados="+allVals+"&descripcion="+$('#descripcion').val()+"&codigoResponsable="+$('#codigoResponsable').val();
	    }
	});
	
	$("input[name='tipoMovimiento']").change(function(){
		
	    if ($("input[name='tipoMovimiento']:checked").val() == 'INGRESO'){
	    	
	    	if ($("input[name='claseMovimiento']:checked").val() == 'cliente'){
	    		
	    		$("#tdtituloCliente").show();
	    		$("#tdtituloClienteDestino").hide();
				$("#elemento thead th:nth-child(8)").css('display','');
				$('.trabajo').css('display','');
	    			
	    	}
	    	else if ($("input[name='claseMovimiento']:checked").val() == 'interno'){
	    		
	    		$("#tdtituloDepositoOrigen").show();
	    		$("#tdtituloSucursalOrigen").show();
	    		$("#tdtituloDepositoDestino").hide();
	    		$("#tdtituloSucursalDestino").hide();
				$("#elemento thead th:nth-child(8)").css('display','none');
				$('.trabajo').css('display','none');
	    	}

	    }
	    else if ($("input[name='tipoMovimiento']:checked").val() == 'EGRESO'){
	    	
	    	if ($("input[name='claseMovimiento']:checked").val() == 'cliente'){
	    		
	    		$("#tdtituloCliente").hide();
	    		$("#tdtituloClienteDestino").show();
				$("#elemento thead th:nth-child(8)").css('display','none');
				$('.trabajo').css('display','none');
	    			
	    	}
	    	else if ($("input[name='claseMovimiento']:checked").val() == 'interno'){
	    		
	    		$("#tdtituloDepositoDestino").show();
	    		$("#tdtituloDepositoOrigen").hide();
	    		$("#tdtituloSucursalDestino").show();
	    		$("#tdtituloSucursalOrigen").hide();
				$("#elemento thead th:nth-child(8)").css('display','none');
				$('.trabajo').css('display','none');
	    			
	    	}
	    }
	});
	
	//Tooltips
	$("img[title]").tooltip();
	
	//Validaciones y botones
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

	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	
	
	//Slide 1
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	//Slide 1
	$('#grupoDiv').attr({'style':'display:block'});
	$('#grupoImg').click(function() {
		$('#grupoDiv').slideToggle('slideUp');
		$('#grupoImgSrcDown').slideToggle('slideUp');
		$('#grupoImgSrc').slideToggle('slideUp');
	});
	//forzamos un evento de cambio para que se carge por primera vez
	//binding OnBlur() para los campos con popup
	$('input[type="radio"]').click(function(){
		changeRadio();
	});
	
	//Fin menu
  	//binding OnBlur() para los campos con popup
	$("#codigoResponsable").blur(function(){
		getAjax('userServlet','codigo','codigoResponsable',getUserCallBack);
	});
	$("#codigoDepositoOrigenDestino").blur(function(){
		getAjaxConParent('depositosPorCodigoSucursalServlet','codigo','codigoDepositoOrigenDestino','codigoSucursalOrigenDestino',getDepositoDestinoCallBack);
	});
	$("#codigoDepositoActual").blur(function(){
		getAjaxConParent('depositosPorCodigoSucursalServlet','codigo','codigoDepositoActual', 'codigoSucursalActual', getDepositoOrigenCallBack);
	});
	$("#codigoSucursalOrigenDestino").blur(function(){
		getAjaxConParent('sucursalesPorCodigoEmpresaServlet','codigo','codigoSucursalOrigenDestino', 'codigoEmpresa', getSucursalCallBack);
	});
	$("#codigoClienteEmp").blur(function(){
		getAjaxConParent('clientesPorEmpresaServlet','codigo','codigoClienteEmp', 'codigoEmpresa',getClienteCallBack);
	});
	$("#codigoLectura").blur(function(){
		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
	});

	if($("#codigoSucursalOrigenDestino").val() != null){
		getAjaxConParent('sucursalesPorCodigoEmpresaServlet','codigo','codigoSucursalOrigenDestino', 'codigoEmpresa', getSucursalCallBack);
	};
	if($("#codigoDepositoOrigenDestino").val() != null){
		getAjaxConParent('depositosPorCodigoSucursalServlet','codigo','codigoDepositoOrigenDestino', 'codigoSucursalOrigenDestino',getDepositoDestinoCallBack);
	}
	if($("#codigoPersonal").val() != null){
		getAjaxConParent('empleadosReturnCodigoDireccionServlet','codigo','codigoPersonal','codigoCliente',getPersonalCallBack);
	}
	if($("#codigoClienteEmp").val() != null){
		getAjaxConParent('clientesPorEmpresaServlet','codigo','codigoClienteEmp', 'codigoEmpresa', getClienteCallBack);
	}
	if($("#codigoLectura").val() != null){
		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
	}
	if($("#codigoDepositoActual").val() != null){
		getAjaxConParent('depositosPorCodigoSucursalServlet','codigo','codigoDepositoActual','codigoSucursalActual', getDepositoOrigenCallBack);
	}
	if($("#codigoResponsable").val() != null){
		getAjax('userServlet','codigo','codigoResponsable',getUserCallBack);
	}

	
	
	 //forzamos un evento de cambio para que se carge por primera vez
	 if($("#accion").val() != 'MODIFICACION' && $("#accion").val() != 'CONSULTA'){
		$("#tdcodigoDepositoDestino").hide();
		$("#tdcodigoSucursalDestino").hide();
		$("#tdtituloDepositoDestino").hide();
		$("#tdtituloSucursalDestino").hide();
		$("#tdtituloDepositoOrigen").hide();
		$("#tdtituloSucursalOrigen").hide();
		$("#tdcodigoCliente").show();
		$("#tdtituloCliente").hide();
		$("#tdtituloClienteDestino").hide();
		$("#tdcodigoPersonal").show();
		$("#tdtituloPersonal").show();
		$("#tdcodigoDireccion").show();
		$("#tdtituloDireccion").show();
		$("#tdHasta").show();
	}else{
		$("#tdcodigoDepositoDestino").show();
		$("#tdcodigoSucursalDestino").show();
		$("#tdtituloDepositoDestino").show();
		$("#tdtituloSucursalDestino").show();
		$("#tdtituloDepositoOrigen").show();
		$("#tdtituloSucursalOrigen").hide();
		$("#tdcodigoCliente").hide();	
		$("#tdtituloCliente").hide();
		$("#tdtituloClienteDestino").hide();
		$("#tdcodigoPersonal").hide();
		$("#tdtituloPersonal").hide();
		$("#tdcodigoDireccion").hide();
		$("#tdtituloDireccion").hide();
		$("#tdHasta").hide();
		$("#tableCantidad").hide();
	}

	 
	 $('#ingresoEgreso').change();
	 changeRadio();
	 
	 $('#direccionesSeleccionadasIzq').keypress(function(e) {
		if (e.keyCode == 13) {
			$('#direccionesSeleccionadasIzq').blur();
			leftToRight('direccionesSeleccionadas');
			$('#direccionesSeleccionadasIzq').focus();
		}
	 });
	 
	 listadoTipoTrabajo();
	 
});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarListaMovimientos.html";
}
function volverCancelar(){
	document.location="mostrarListaMovimientos.html";
}
function guardarYSalir(){
	if ($("input[name='tipoMovimiento']:checked").val() == 'INGRESO'){
		jConfirm("Si alguno de los elementos seleccionados tiene posición y se están ingresando en un depósito diferente, estos perderán dichas posiciones. Desea continuar?","ATENCION",function(res){
			if(res)
				document.forms[0].submit();
		});
	}
	else
	{
		document.forms[0].submit();
	}
}

//onBlur change Radio
function changeRadio(){
	
		var group =  $('input[name="'+$('input[type="radio"]').attr("name")+'"]');
		for(var i=0; i<group.length;i++){
			if(group[i].checked == true && group[i].value == "interno"){
				$("#tdcodigoDepositoDestino").show();
				$("#tdcodigoSucursalDestino").show();
				$("#tdtituloDepositoDestino").show();
				$("#tdtituloSucursalDestino").show();
				$("#tdcodigoDepositoOrigen").show();
				$("#tdtituloDepositoOrigen").hide();
				$("#tdtituloSucursalOrigen").hide();
				$("#tdcodigoDepositoActual").show();
				//$("#tdEgresoIngreso").hide();	
				$("#tdcodigoCliente").hide();
				$("#tdtituloCliente").hide();
				$("#tdtituloClienteDestino").hide();
				$('#tipoMovimiento').change();
			}else{
				//ocultamos los elementos
				$("#tdcodigoDepositoOrigen").show();
				$("#tdtituloDepositoOrigen").hide();
				$("#tdtituloSucursalOrigen").hide();
				$("#tdcodigoDepositoDestino").hide();
				$("#tdcodigoSucursalDestino").hide();
				$("#tdtituloDepositoDestino").hide();
				$("#tdtituloSucursalDestino").hide();
				$("#tdcodigoDepositoActual").show();
				//$("#tdEgresoIngreso").show();	
				$("#tdcodigoCliente").show();	
				$("#tdtituloCliente").show();
				$("#tdtituloClienteDestino").show();
				$('#tipoMovimiento').change();

			}
		}
	
}

function abrirPopupCargaElementos(){
//	if($("#codigoDepositoOrigen").val()==null || $("#codigoDepositoOrigen").val()==""){
//	jAlert('Debe seleccionar previamente un depósito de origen');
//	return;
//}
	var divOpen = $('#darkMiddleCargaElementos');
	divHidden = 'darkLayer';
	var left=($(window).width()-$(divOpen).width())/2;
	$(divOpen).css("left",left);
	
	$('#'+divHidden).show();
	$(divOpen).show("slow");
	
	$('#direccionesSeleccionadasIzq').focus();
	$('html, body').animate({ scrollTop: 100 }, 'slow');

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

function abrirPopupSucursal(claseNom, mensaje){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioMovimiento.html?empresaCodigo="+$("#codigoEmpresa").val();
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
//	if($("#codigoSucursalActual").val()==null || $("#codigoSucursalActual").val()==""){
//		jAlert(mensaje);
//		return;
//	}
	var url = "precargaFormularioMovimiento.html?empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursalActual").val();
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

function abrirPopupDepositoDestino(claseNom, mensaje){
	if($("#codigoSucursalOrigenDestino").val()==null || $("#codigoSucursalOrigenDestino").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioMovimiento.html?empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursalOrigenDestino").val();
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

function abrirPopupCliente(claseNom, mensaje,title){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioMovimiento.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupLectura(claseNom){
	$('html, body').animate({ scrollTop: 100 }, 'slow');
	var url = "precargaFormularioMovimiento.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupResponsables(claseNom){
	$('html, body').animate({ scrollTop: 100 }, 'slow');
	var url = "precargaFormularioMovimiento.html?empresaCodigo="+$("#codigoEmpresa").val();
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
function getLecturaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoLecturaLabel").html(sResponseText);
	else{
		$("#codigoLecturaLabel").html("");
		$("#codigoLectura").val("");
	}
}

//ajax
function getUserCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoResponsableLabel").html(sResponseText);
	else{
		$("#codigoResponsableLabel").html("");
		$("#codigoResponsable").val("");
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
function getDepositoDestinoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDepositoOrigenDestinoLabel").html(sResponseText);	
	else{
		$("#codigoDepositoOrigenDestino").val("");
		$("#codigoDepositoOrigenDestinoLabel").html("");
	}
}
//ajax
function getDepositoOrigenCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDepositoActualLabel").html(sResponseText);	
	else{
		$("#codigoDepositoActual").val("");
		$("#codigoDepositoActualLabel").html("");
	}
}
//ajax
function getSucursalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalOrigenDestinoLabel").html(sResponseText);
	else{
		$("#codigoSucursalOrigenDestinoLabel").html("");
		$("#codigoDepositoOrigenDestinoLabel").html("");
		$("#codigoDepositoOrigenDestino").val("");
	}
}
//ajax
function getClienteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteEmpLabel").html(sResponseText);
	else{
		$("#codigoClienteEmp").val("");
		$("#codigoClienteEmpLabel").html("");
	}
}

function getRadioButtonSelectedValue(ctrl)
{
    for(i=0;i<ctrl.length;i++)
        if(ctrl[i].checked) return ctrl[i].value;
}

function listadoTipoTrabajo(){
	
	    var allValues = [];
		    $('#elemento tr').each(function(i) {
		    	
		    	var id = $(this).find('#hdn_id').val();
		    	var input = $(this).find('select');
		      
		      if(input.length > 0) {
		        allValues.push(id +'-'+ input.val());
		      }
		    
		    });
		$('#listaTipoTrabajo').val(allValues);	    
}