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
	
	//Validaciones y botones
	$('.completarZeros').blur(function(e){
		valorSinZeros='';
		valorSinZeros=$(this).val();
		zerosIzq='';
		maximo=$(this).attr('maxlength');
		actual=valorSinZeros.length;
		for(i=0;i<(maximo-actual);i++){
			zerosIzq+='0';
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
	
	$("#codigoTipoElemento").blur(function(){
		getAjax('tipoElementoServlet','codigo','codigoTipoElemento',getTipoElementoCallBack);
		setTimeout(function() {
			calcularHasta();
		}, 3000);
	});	
	$("#codigoEmpresa").blur(function(){
		searchAjax('seleccionEmpresa.html','codigoEmpresa',null,getEmpresaCallBack);
	});	
	$("#codigoCliente").blur(function(){
		searchAjax('seleccionCliente.html','codigoCliente','codigoEmpresa',getClienteCallBack);
	});
	$("#codigoContenedor").blur(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		searchAjax('seleccionContenedor.html','codigoContenedor','codigoCliente',getContenedorCallBack);
	});
	$("#codigoSucursal").blur(function(){
		searchAjax('seleccionSucursal.html','codigoSucursal','codigoEmpresa',getSucursalCallBack);
	});
	if($("#codigoEmpresa").val() != null){
		searchAjax('seleccionEmpresa.html','codigoEmpresa',null,getEmpresaCallBack);
	}
	if($("#codigoCliente").val() != null){
		searchAjax('seleccionCliente.html','codigoCliente','codigoEmpresa',getClienteCallBack);
	}
	if($("#codigoContenedor").val() != null){
		searchAjax('seleccionContenedor.html','codigoContenedor','codigoCliente',getContenedorCallBack);
	}
	if($("#codigoTipoElemento").val() != null){
		getAjax('tipoElementoServlet','codigo','codigoTipoElemento',getTipoElementoCallBack);
	}
	
	$("#buscaTipoElemento").click(function(){
		abrirPopupSeleccion("popUpSeleccionTipoElemento.html", null, null, "tipoElementosPopupMap", null);
	});
	
	$("#buscaEmpresa").click(function(){
		abrirPopupSeleccion("popUpSeleccionEmpresa.html", null, null, "empresasPopupMap", null);
	});
	
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionCliente.html","codigoEmpresa","clientesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	
	$("#buscaContenedor").click(function(){
		$('#codigoCliente').val($("#codigoCliente", top.document).val());
		abrirPopupSeleccion("popUpSeleccionContenedor.html",'codigoCliente',"contenedoresPopupMap", $("#mensajeSeleccioneCliente").val());
	});
	
	 //forzamos un evento de cambio para que se carge por primera vez
	 if(urlParams["accion"] != 'MODIFICACION' && urlParams["accion"] != 'CONSULTA'){
		$("#cantidad").show();
		$("#hasta").show();
		$("#tdCantidad").show();
		$("#tdHasta").show();
	}else{
		$("#cantidad").hide();
		$("#hasta").hide();	
		$("#tdCantidad").hide();
		$("#tdHasta").hide();
		$("#tableCantidad").hide();
	}
	
	 $("#cantidad").blur(function(){
			calcularHasta();
		});
	 
	 calcularHasta();
});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarElemento.html";
}
function volverCancelar(){
	document.location="mostrarElementosSinFiltrar.html";
}
function guardarYSalir(){
 	document.forms[0].submit();
}

function calcularHasta(){
	if ($("#accion").val() == "NUEVO") {
		
		if ($("#cantidad").val() != null && $("#cantidad").val() != ""
				&& $("#codigo").val() != null && $("#codigo").val() != "") {
			var cantidad = quitarCerosIzq($("#cantidad").val());
			var codigo = quitarCerosIzq($("#codigo").val());
			$("#hasta").val(parseInt(cantidad) + parseInt(codigo) - 1);
			valorSinZeros = '';
			valorSinZeros = $("#hasta").val();
			zerosIzq = '';
			maximo = $("#hasta").attr('maxlength');
			actual = valorSinZeros.length;
			for (i = 0; i < (maximo - actual); i++) {
				zerosIzq += '0';
			}
			$("#hasta").val(zerosIzq + valorSinZeros);
		} else {
			$("#hasta").val("");
		}
	}
}

//Funcion para quitar ceros a la izquierda
function quitarCerosIzq(string){
	var str="";
	var i=-1;
	while(string.charAt(++i)==0);
	// en "i" esta el indice del primer caracter no igual a cero

	str=string.substring(i,string.length);

	return str;
} 


//onBlur change Radio
function changeRadio(){
	var group =  $('input[name="'+$('input[type="radio"]').attr("name")+'"]');
	for(var i=0; i<group.length;i++){
		if(group[i].checked == true && group[i].value == "Rango"){
			$("#cantidad").show();
			$("#hasta").show();
			$("#tdCantidad").show();
			$("#tdHasta").show();
		}else{
			//ocultamos los elementos
			$("#cantidad").hide();
			$("#hasta").hide();	
			$("#tdCantidad").hide();
			$("#tdHasta").hide();
			//seteamos los valores a nulo
			$("#cantidad").val("");
			$("#hasta").val("");
		}
	}
}

function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

function abrirPopupCliente(claseNom, mensaje, title){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "precargaFormularioElemento.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupElemento(claseNom){
	var url = "precargaFormularioElemento.html?clienteCodigo="+$("#codigoCliente").val();
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


function abrirPopupTipoElemento(claseNom, mensaje){
	var url = "precargaFormularioElemento.html";
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
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#codigoEmpresaLabel").html("");
		$("#codigoEmpresa").val("");
		$("#codigoCliente").val("");
	}
	$("#codigoCliente").trigger('blur');
}

//ajax
function getClienteCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#codigoClienteLabel").html("");
		$("#codigoCliente").val("");
	}
}
//ajax
function getContenedorCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoContenedorLabel").html(sResponseText);
	else{
		$("#codigoContenedorLabel").html("");
		$("#codigoContenedor").val("");
	}
}

//ajax
function getTipoElementoCallBack(sResponseText){	
	var componentId = "codigoTipoElemento";
	setResponce(sResponseText, componentId);
}
//ajax
function setResponce(sResponseText, componentId){
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split("-");
		$("#"+componentId).val(array[0]);		
		$("#"+componentId+"Label").html(array[1]);
		$("#"+componentId+"Label2").html(array[2]);
		$("#"+componentId+"Label3").html(array[2]);
		if ($("#accion").val() == "NUEVO") {
		$("#codigo").val(array[3]);
		}
	}else{
		$("#"+componentId).val("");
		$("#"+componentId+"Label").html("");
		$("#"+componentId+"Label2").html("");
		$("#"+componentId+"Label3").html("");
		if ($("#accion").val() == "NUEVO") {
		$("#codigo").val("");
		}
	}	
}