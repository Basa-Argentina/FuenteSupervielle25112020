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

	//Tooltips
	$("img[title]").tooltip();
	
	
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
	 if(urlParams["accion"] == 'NUEVO' || $('#accion').val() == 'NUEVO'){
		 $("#detalle").hide();
		 $("#formADS").show();
		//Fin menu
		//binding OnBlur() para los campos con popup
		$("#codigoOrigenDestino").blur(function(){
			if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
				jAlert($('#errorCodigoEmpresaReq').val(),$('#errorTitulo').val());
				return;
			}
			if($("#codigoSucursal").val()==null || $("#codigoSucursal").val()==""){
				jAlert($('#errorCodigoSucursalReq').val(),$('#errorTitulo').val());
				return;
			}
			getAjax('depositosServlet','codigo','codigoOrigenDestino',getDepositoCallBack);
		});
		
		$("#codigoEmpresa").blur(function(){
			getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
		});
		
		$("#codigoSucursal").blur(function(){
			if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
				jAlert($('#errorCodigoEmpresaReq').val(),$('#errorTitulo').val());
				return;
			}
			getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
		});
	 }else if(urlParams["accion"] == 'VER_DETALLE' || $('#accion').val() == 'VER_DETALLE'){
		 $("#detalle").show();
		 $("#formADS").hide();
	 }
	 
	 $("#fecha").val($("#fechaAux").val());
	
	 $('#tipoMovimiento').change(function(){
		var option = $(this).val();
		if(option!='Egreso'){
			$('#codigoEmpresa').attr('disabled',true);
			$('#buttonCodigoEmpresa').attr('disabled',true);
			$('#codigoSucursal').attr('disabled',true);
			$('#buttonCodigoSucursal').attr('disabled',true);
			$('#codigoOrigenDestino').attr('disabled',true);
			$('#buttonCodigoOrigenDestino').attr('disabled',true);			
		}else{
			$('#codigoEmpresa').removeAttr("disabled");
			$('#buttonCodigoEmpresa').removeAttr("disabled");
			$('#codigoSucursal').removeAttr("disabled");
			$('#buttonCodigoSucursal').removeAttr("disabled");
			$('#codigoOrigenDestino').removeAttr("disabled");
			$('#buttonCodigoOrigenDestino').removeAttr("disabled");	
		}		
	 });
	 //validacion inicial
	 if($('#tipoMovimiento').val()!='Egreso'){
			$('#codigoEmpresa').attr('disabled',true);
			$('#buttonCodigoEmpresa').attr('disabled',true);
			$('#codigoSucursal').attr('disabled',true);
			$('#buttonCodigoSucursal').attr('disabled',true);
			$('#codigoOrigenDestino').attr('disabled',true);
			$('#buttonCodigoOrigenDestino').attr('disabled',true);			
		}else{
			$('#codigoEmpresa').removeAttr("disabled");
			$('#buttonCodigoEmpresa').removeAttr("disabled");
			$('#codigoSucursal').removeAttr("disabled");
			$('#buttonCodigoSucursal').removeAttr("disabled");
			$('#codigoOrigenDestino').removeAttr("disabled");
			$('#buttonCodigoOrigenDestino').removeAttr("disabled");	
		}
});

//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarStock.html";
}
function volverCancelar(){
	document.location="mostrarStock.html";
}
function guardarYSalir(){
	if($('#tipoMovimiento').val()=='Ingreso'){
		$('#codigoEmpresa').val('');
		$('#codigoSucursal').val('');
		$('#codigoOrigenDestino').val('');
	}else{
//		if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
//			jAlert($('#errorCodigoEmpresaReq').val(),$('#errorTitulo').val());
//			return false;
//		}
//		if($("#codigoSucursal").val()==null || $("#codigoSucursal").val()==""){
//			jAlert($('#errorCodigoSucursalReq').val(),$('#errorTitulo').val());
//			return false;
//		}
//		if($("#codigoOrigenDestino").val()==null || $("#codigoOrigenDestino").val()==""){
//			jAlert($('#errorCodigoDepositoReq').val(),$('#errorTitulo').val());
//			return false;
//		}
		if($('#codigoOrigenDestino').val()==$('#codigoDeposito').val()){
			var mensaje = $('#errorCodDepIgualDestino').val();
			var title = $('#errorTitulo').val();
			jAlert(mensaje,title);
			return false;
		}
	}
	document.forms[0].submit();
}

function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

function abrirPopupEmpresa(claseNom){
	$("#codigoSucursal").val("");
	$("#codigoOrigenDestino").val("");
	var labelLista =  document.getElementById("codigoSucursalLabel");
	labelLista.textContent = "";
	var labelConcepto =  document.getElementById("codigoOrigenDestinoLabel");
	labelConcepto.textContent = "";
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

function abrirPopupSucursal(claseNom, mensaje, title){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "precargaFormularioStock.html?accion=NUEVO&empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupDeposito(claseNom, mensaje, title){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje,title);
		return;
	}
	if($("#codigoSucursal").val()==null || $("#codigoSucursal").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "precargaFormularioStock.html?accion=NUEVO&empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursal").val();
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
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getDepositoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoOrigenDestinoLabel").html(sResponseText);
	else{
		$('#codigoOrigenDestino').val('');
		$("#codigoOrigenDestinoLabel").html("");
	}
}
//ajax
function getSucursalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{		
		$("#codigoSucursalLabel").html("");
		$('#codigoSucursal').val('');
	}
	$('#codigoOrigenDestino').val('');
	$("#codigoOrigenDestinoLabel").html("");
}

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#codigoEmpresa").val("");
		$("#codigoEmpresaLabel").html(sResponseText);
	}
	$('#codigoOrigenDestino').val('');
	$("#codigoOrigenDestinoLabel").html("");
	$("#codigoSucursalLabel").html("");
	$('#codigoSucursal').val('');
}