$(document).ready(function() {		
	var urlParams = {};
	(function () {
	    var e,
	        a = /\+/g,  // Regex for replacing addition symbol with a space
	        r = /([^&=]+)=?([^&]*)/g,
	        d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
	        q = window.location.search.substring(1);

	    while (e = r.exec(q))
	       urlParams[d(e[1])] = d(e[2]);
	})();

	$("#cargarCodigosElementos").click(function(){
//		if($("#codigoDepositoOrigen").val()==null || $("#codigoDepositoOrigen").val()==""){
//		jAlert('Debe seleccionar previamente un depósito de origen');
//		return;
//	}
		
		$("#direccionesSeleccionadasDer").each(function(){
            $("#direccionesSeleccionadasDer option").attr("selected","selected"); 
        });
		var lista = $('#direccionesSeleccionadasDer').val();
		if(lista!= null)
		{
			var combo = document.getElementById('anexarCodigos');
			var accion = $('#accion').val();
			var group =  $('input[name="'+$('input[type="radio"]').attr("name")+'"]');
			var tipoRemito = getRadioButtonSelectedValue(group);
			var ingresoEgreso = $("input[name='ingresoEgreso']:checked").val();
			var verificaLectura = $("input[name='verificaLectura']:checked").val();
			 if (confirm('¿Estas seguro de cargar la lista de codigos de elementos?')){
				document.location="cargarListadoCodigosElementos.html?listaCodigosElementos="+lista+"&codigoDepositoOrigen="+$('#codigoDepositoOrigen').val()
				+"&codigoDepositoDestino="+$('#codigoDepositoDestino').val()+"&codigoCliente="+$('#codigoCliente').val()
				+"&codigoDireccion="+$('#codigoDireccion').val()+"&codigoPersonal="+$('#codigoPersonal').val()
				+"&codigoTransporte="+$('#codigoTransporte').val()+"&codigoSerie="+$('#codigoSerie').val()+"&anexarCodigos="+combo.checked
				+"&fechaEmision="+$('#fechaEmision').val()+"&fechaEntrega="+$('#fechaEntrega').val()+"&accion="+accion
				+"&tipoRemito="+tipoRemito+"&prefijo="+$('#prefijo').val()+"&id="+$('#id').val()+"&ingresoEgreso="+ingresoEgreso
				+"&numeroSinPrefijo="+$('#numeroSinPrefijo').val()+"&verificaLectura="+verificaLectura;
		}
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
			var tipoRemito = getRadioButtonSelectedValue(group);
			var ingresoEgreso = $("input[name='ingresoEgreso']:checked").val();
			var verificaLectura = $("input[name='verificaLectura']:checked").val();
			 if (confirm('¿Estas seguro de importar la lectura ingresada?')){
				document.location="importarLecturaElementosRemito.html?codigoLectura="+cl+"&codigoDepositoOrigen="+$('#codigoDepositoOrigen').val()
				+"&codigoDepositoDestino="+$('#codigoDepositoDestino').val()+"&codigoCliente="+$('#codigoCliente').val()
				+"&codigoDireccion="+$('#codigoDireccion').val()+"&codigoPersonal="+$('#codigoPersonal').val()
				+"&codigoTransporte="+$('#codigoTransporte').val()+"&codigoSerie="+$('#codigoSerie').val()+"&anexar="+combo.checked
				+"&fechaEmision="+$('#fechaEmision').val()+"&fechaEntrega="+$('#fechaEntrega').val()+"&accion="+accion
				+"&tipoRemito="+tipoRemito+"&prefijo="+$('#prefijo').val()+"&id="+$('#id').val()+"&ingresoEgreso="+ingresoEgreso;
				+"&numeroSinPrefijo="+$('#numeroSinPrefijo').val()+"&accion="+$('#accion').val()+"&verificaLectura="+verificaLectura;
			 }
		}
	});
	
	$("input[name='ingresoEgreso']").change(function(){
		
	    if ($("input[name='ingresoEgreso']:checked").val() == 'ingreso'){
	    	$("#tdcodigoDepositoOrigen").hide();
			$("#tdtituloDepositoOrigen").hide();
			$("#tdcodigoDepositoDestino").show();
			$("#tdtituloDepositoDestino").show();
			$("#tdcodigoCliente").show();	
			$("#tdtituloCliente").show();
			if($("input[name='tipoRemito']:checked").val() == 'cliente')
			{
				if(urlParams["accion"] != 'MODIFICACION' && urlParams["accion"] != 'CONSULTA'){
					$("#tablaDoble").show();
					$("#tablaUnica").hide();
				}else{
					if($("#verificoMovHdn").val()=='SI')
					{
						$("#tablaDoble").show();
						$("#tablaUnica").hide();
					}else{
						$("#tablaDoble").hide();
						$("#tablaUnica").show();
					}
				}
			}
			else
			{
				$("#tablaDoble").hide();
				$("#tablaUnica").show();
			}
	    }
	    else if ($("input[name='ingresoEgreso']:checked").val() == 'egreso'){
	    	$("#tdcodigoDepositoOrigen").show();
			$("#tdtituloDepositoOrigen").show();
			$("#tdcodigoDepositoDestino").hide();
			$("#tdtituloDepositoDestino").hide();
			$("#tdcodigoCliente").show();	
			$("#tdtituloCliente").show();
			$("#tablaDoble").hide();
			$("#tablaUnica").show();
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
	$("#codigoDepositoDestino").blur(function(){
		getAjax('depositosServlet','codigo','codigoDepositoDestino',getDepositoDestinoCallBack);
	});
	$("#codigoDepositoOrigen").blur(function(){
		getAjaxConParent('depositosServlet','codigo','codigoDepositoOrigen', 'codigoSucursal', getDepositoOrigenCallBack);
	});
	$("#codigoCliente").blur(function(){
		getAjaxConParent('clientesPorEmpresaServlet','codigo','codigoCliente', 'codigoEmpresa',getClienteCallBack);
	});
	$("#codigoSerie").blur(function(){
		getAjaxConParent('serieDescripcionPrefijoServlet','codigo','codigoSerie', 'codigoEmpresa',getSerieCallBack);
	});
	$("#codigoTransporte").blur(function(){
		getAjaxConParent('transportesServlet','codigo','codigoTransporte', 'codigoEmpresa',getTransporteCallBack);
	});
	$("#codigoPersonal").blur(function(){
		getPersonalAjaxConParent('empleadosReturnCodigoDireccionServlet','codigo','codigoPersonal', 'codigoCliente',getPersonalCallBack);
	});
	$("#codigoLectura").blur(function(){
		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
	});
	$("#codigoDireccion").blur(function(){
		getAjax('direccionesEntregaServlet','codigo','codigoDireccion',getDireccionCallBack);
  	});
	if($("#codigoDepositoDestino").val() != null){
		getAjax('depositosServlet','codigo','codigoDepositoDestino',getDepositoDestinoCallBack);
	}
	if($("#codigoPersonal").val() != null){
		getPersonalAjaxConParent('empleadosReturnCodigoDireccionServlet','codigo','codigoPersonal','codigoCliente',getPersonalCallBack);
	}
	if($("#codigoCliente").val() != null){
		getAjaxConParent('clientesPorEmpresaServlet','codigo','codigoCliente', 'codigoEmpresa', getClienteCallBack);
	}
	if($("#codigoSerie").val() != null){
		getAjaxConParent('serieDescripcionPrefijoServlet','codigo','codigoSerie','codigoEmpresa',getSerieCallBack);
	}
	if($("#codigoTransporte").val() != null){
		getAjaxConParent('transportesServlet','codigo','codigoTransporte', 'codigoEmpresa',getTransporteCallBack);
	}
	if($("#codigoDireccion").val() != null){
		getAjax('direccionesEntregaServlet','codigo','codigoDireccion',getDireccionCallBack);
  	}
	if($("#codigoLectura").val() != null){
		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
	}
	if($("#codigoDepositoOrigen").val() != null){
		getAjaxConParent('depositosServlet','codigo','codigoDepositoOrigen',getDepositoOrigenCallBack);
	}
	
	 //forzamos un evento de cambio para que se carge por primera vez
	 if(urlParams["accion"] != 'MODIFICACION' && urlParams["accion"] != 'CONSULTA'){
		$("#tdcodigoDepositoDestino").hide();
		$("#tdtituloDepositoDestino").hide();
		$("#tdcodigoCliente").show();
		$("#tdtituloCliente").show();
		$("#tdcodigoPersonal").show();
		$("#tdtituloPersonal").show();
		$("#tdcodigoDireccion").show();
		$("#tdtituloDireccion").show();
		$("#tdHasta").show();
	}else{
		$("#tdcodigoDepositoDestino").show();
		$("#tdtituloDepositoDestino").show();
		$("#tdcodigoCliente").hide();	
		$("#tdtituloCliente").hide();
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
	 
});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarRemito.html";
}
function volverCancelar(){
	document.location="mostrarRemito.html";
}
function guardarYSalir(){
	if ($("input[name='ingresoEgreso']:checked").val() == 'ingreso'){
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
				$("#tdtituloDepositoDestino").show();
				$("#tdcodigoDepositoOrigen").show();
				$("#tdtituloDepositoOrigen").show();
				//$("#tdEgresoIngreso").hide();	
				$("#tdcodigoCliente").hide();
				$("#tdtituloCliente").hide();
				//$("#codigoCliente").val("");
				//document.getElementById("codigoClienteLabel").innerHTML = "";
				$("#tdcodigoPersonal").hide();
				$("#tdtituloPersonal").hide();
				$("#tdtituloPersonalVacio").show();
				$("#tdcodigoPersonalVacio").show();
				$("#tdcodigoDireccion").hide();
				$("#tdtituloDireccion").hide();
				$("#codigoDireccionLabel").hide();
				//document.getElementById("codigoDireccionLabel").innerHTML = "";
				$("#tdtituloDireccionVacio").show();
				$("#tdcodigoDireccionVacio").show();
				//$("#codigoPersonal").val("");
			}else{
				//ocultamos los elementos
				$("#tdcodigoDepositoOrigen").show();
				$("#tdtituloDepositoOrigen").show();
				$("#tdcodigoDepositoDestino").hide();
				$("#tdtituloDepositoDestino").hide();
				//$("#tdEgresoIngreso").show();	
				$("#tdcodigoCliente").show();	
				$("#tdtituloCliente").show();
				$("#tdcodigoPersonal").show();
				$("#tdtituloPersonal").show();
				$("#tdtituloPersonalVacio").hide();
				$("#tdcodigoPersonalVacio").hide();
				$("#tdcodigoDireccion").show();
				$("#tdtituloDireccion").show();
				$("#codigoDireccionLabel").show();
				$("#tdtituloDireccionVacio").hide();
				$("#tdcodigoDireccionVacio").hide();
				$('#ingresoEgreso').change();
				//seteamos los valores a nulo
				//$("#codigoDepositoDestino").val("");
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
	var url = "precargaFormularioRemito.html?empresaCodigo="+$("#codigoEmpresa").val();
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
	if($("#codigoSucursal").val()==null || $("#codigoSucursal").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioRemito.html?empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursal").val();
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
	if($("#codigoSucursal").val()==null || $("#codigoSucursal").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioRemito.html?empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursal").val();
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
	var url = "precargaFormularioRemito.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupSerie(claseNom, mensaje){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioRemito.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupTransporte(claseNom, mensaje){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioRemito.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupPersonal(claseNom, mensaje, title){
	if($("#codigoCliente").val()==null || $("#codigoCliente").val()==""){
		jAlert(mensaje);
		return;
	}
	var url = "precargaFormularioRemito.html?clienteCodigo="+$("#codigoCliente").val();
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
function abrirPopupDireccion(claseNom, mensaje, title){
	if($("#codigoCliente").val()==null || $("#codigoCliente").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "precargaFormularioRemito.html?clienteCodigo="+$("#codigoCliente").val();
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
	var url = "precargaFormularioRemito.html?empresaCodigo="+$("#codigoEmpresa").val();
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
		$("#codigoLectura").html("");
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
function getPersonalAjaxConParent(url, varName, elementName, parentId,callBack){
	if($("#"+parentId+"").val()!=null && $("#"+parentId+"").val()!=""){
		var input = document.getElementById(elementName);
		var personalId = $("#"+parentId+"").val();
		if(input == null)
			return;
		if($('#accion').val()!=null && $('#accion').val()!= '' && $('#accion').val()!= 'NUEVO')
			personalId = "";
		var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val()+'&clienteEmpId='+personalId+'&tipoSerie=R'+'&habilitado=true';	
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
		$("#codigoDepositoDestinoLabel").html(sResponseText);	
	else{
		$("#idCliente").val("");
		$("#codigoDepositoDestinoLabel").html("");
	}
}
//ajax
function getDepositoOrigenCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDepositoOrigenLabel").html(sResponseText);	
	else{
		$("#idCliente").val("");
		$("#codigoDepositoOrigenLabel").html("");
	}
}
//ajax
function getSucursalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoSucursalLabel").html("");
		$("#codigoDepositoLabel").html("");
		$("#codigoDeposito").val("");
	}
}
//ajax
function getClienteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoClienteLabel").html("");
	}
}
//ajax
function getTransporteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoTransporteLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoTransporteLabel").html("");
	}
}
//ajax
function getPersonalCallBack(sResponseText){	
	var componentId = "codigoPersonal";
	setResponcePersonal(sResponseText, componentId);
}
//ajax
function getDireccionCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDireccionLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoDireccionLabel").html("");
	}
}
//ajax
function setResponcePersonal(sResponseText, componentId){
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split("-");		
		$("#"+componentId+"Label").html(array[0]);
		$("#codigoDireccion").val(array[1]);
		getAjax('direccionesEntregaServlet','codigo','codigoDireccion',getDireccionCallBack);
	}else{
		$("#"+componentId).val("");
		$("#"+componentId+"Label").html("");
		$("#codigoDireccion").val("");
	}	
}
//ajax
function getSerieCallBack(sResponseText){	
	var componentId = "codigoSerie";
	setResponceSerie(sResponseText, componentId);
}
//ajax
function setResponceSerie(sResponseText, componentId){
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split("-");		
		$("#"+componentId+"Label").html(array[0]);
		if ($("#accion").val() == "NUEVO" /*&& $("#actualizaNumero").val() != "NO"*/) {
			$("#prefijo").val(array[1]);
			$("#numeroSinPrefijo").val(array[2]);
		}
		$("#actualizaNumero").val("SI");
	}else{
		$("#"+componentId).val("");
		$("#"+componentId+"Label").html("");
		if ($("#accion").val() == "NUEVO" /*&& $("#actualizaNumero").val() != "NO"*/) {
		$("#prefijo").val("");
		$("#numeroSinPrefijo").val("");
		}
		$("#actualizaNumero").val("SI");
	}	
}

function getRadioButtonSelectedValue(ctrl)
{
    for(i=0;i<ctrl.length;i++)
        if(ctrl[i].checked) return ctrl[i].value;
}