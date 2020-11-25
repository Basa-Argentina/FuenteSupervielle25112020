var menuAbierto=false;

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
	
	//Inicio menu
	$("ul.topnav li").hover(
		function() {
			$(this).addClass("hover");
			$(this).find("ul.subnav").slideDown('fast').show(); //Drop down the subnav on click  
		}, 
		function(){  
			$(this).removeClass("hover");
			$(this).find("ul.subnav").slideUp('fast'); //When the mouse hovers out of the subnav, move it back up  
		}
    ); 
	
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
	
	$("#codigo").keyup(function(){
		changeCodigo();
	    });
	
	 //binding OnBlur() para los campos con popup
	$("#conceptoCodigoVenta").blur(function(){
		getAjax2('conceptoFacturableServlet','codigo','conceptoCodigoVenta',null,-1,0,getConceptoVentaCallBack);
	});
	$("#conceptoCodigoGuarda").blur(function(){
		getAjax2('conceptoFacturableServlet','codigo','conceptoCodigoGuarda',null,-1,2,getConceptoGuardaCallBack);
	});
	$("#conceptoCodigoStock").blur(function(){
		getAjax2('conceptoFacturableServlet','codigo','conceptoCodigoStock',null,1,0,getConceptoStockCallBack);
	});
	
	checkSelectVenta();
	checkSelectGuarda();
	checkSelectStock();
	
	tipoEtiquetaPreseleccionado=$('#tipoEtiquetaPreseleccionado').val();
	$('#tipoEtiquetaInt').val(tipoEtiquetaPreseleccionado);
});

function volver(){
	document.location="mostrarTipoElemento.html";
}
function volverCancelar(){
	document.location="mostrarTipoElemento.html";
}
function guardarYSalir(){
	//Hacemos el submit
 	document.forms[0].submit();
}

function changeCodigo(){
	var codigo = document.getElementById("codigo");
	var valor = codigo.value;
	codigo.value = valor.toUpperCase();
}

function abrirPopup(claseNom){
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
function getAjax2(url, varName, elementId, parentId, mode, tipo,callBack){
	var input = document.getElementById(elementId);
	var parentValueId = "";
	if(parentId != null)
		parentValueId = '&val='+$("#"+parentId+"Label").html();
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&mode='+mode+'&tipo='+tipo+parentValueId;	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getConceptoVentaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		{
		$("#conceptoCodigoVentaLabel").html(sResponseText);
	}
	else
		{
		$("#idCliente").val("");
		$("#conceptoCodigoVenta").val("");
		$("#conceptoCodigoVentaLabel").html("");
	}
}

//ajax
function getConceptoGuardaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#conceptoCodigoGuardaLabel").html(sResponseText);
	}
	else{
		$("#idCliente").val("");
		$("#conceptoCodigoGuarda").val("");
		$("#conceptoCodigoGuardaLabel").html("");
	}
}

//ajax
function getConceptoStockCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#conceptoCodigoStockLabel").html(sResponseText);
	}
	else{
		$("#idCliente").val("");
		$("#conceptoCodigoStock").val("");
		$("#conceptoCodigoStockLabel").html("");
	}
}


function checkSelectVenta(){
	var combo = document.getElementById('generaConceptoVenta');
	var select = document.getElementById('conceptoCodigoVenta');
	var select2 = document.getElementById('conceptoCodigoVentaLabel');
		if(combo.checked == true)
		{
			select.disabled = false;
			document.getElementById('seccion1').style.display="";
			//getAjax('conceptoFacturableServlet','codigo','conceptoCodigoVenta',getConceptoVentaCallBack);
		}
		else
		{
			select.disabled = true;
			select.value = "";
			select2.innerHTML = "";
			//getAjax('conceptoFacturableServlet','codigo','conceptoCodigoVenta',getConceptoVentaCallBack);
			document.getElementById('seccion1').style.display="none";
		}
}

function checkSelectGuarda(){
	var combo = document.getElementById('generaConceptoGuarda');
	var select = document.getElementById('conceptoCodigoGuarda');
	var select2 = document.getElementById('conceptoCodigoGuardaLabel');
		if(combo.checked == true)
		{
			select.disabled = false;
			document.getElementById('seccion2').style.display="";
			//getAjax('conceptoFacturableServlet','codigo','conceptoCodigoGuarda',getConceptoGuardaCallBack);
		}
		else
		{
			select.disabled = true;
			select.value = "";
			select2.innerHTML = "";
			//getAjax('conceptoFacturableServlet','codigo','conceptoCodigoGuarda',getConceptoGuardaCallBack);
			document.getElementById('seccion2').style.display="none";
		}
}

function checkSelectStock(){
	var combo = document.getElementById('descuentaStock');
	var select = document.getElementById('conceptoCodigoStock');
	var select2 = document.getElementById('conceptoCodigoStockLabel');
		if(combo.checked == true)
		{
			select.disabled = false;
			document.getElementById('seccion3').style.display="";
			//getAjax2('conceptoFacturableServlet','codigo','conceptoCodigoStock',null,1,0,getConceptoStockCallBack);
		}
		else
		{
			select.disabled = true;
			select.value = "";
			select2.innerHTML = "";
			//getAjax2('conceptoFacturableServlet','codigo','conceptoCodigoGuarda',null,1,0,getConceptoStockCallBack);
			document.getElementById('seccion3').style.display="none";
		}
}

function formato(caracteres)
{
	tam = document.getElementById("prefijoCodigo").value.length;
	valor = document.getElementById("prefijoCodigo").value;
	aux = caracteres - tam;
	if(tam <= 3)
	{
		var i = 0;
		while(i<aux)
		{
			valor= '0'+valor;
			i++;
		}
		
	}
	document.getElementById("prefijoCodigo").value = valor;
}