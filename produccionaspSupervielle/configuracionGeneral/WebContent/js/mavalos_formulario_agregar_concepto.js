$(document).ready(function() {	

	//Tooltips
	$("img[title]").tooltip();
	
	//Slide 1
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	//bindings keyup Uppercase
	$('.upperCase').keyup(function() {
		$(this).val($(this).val().toUpperCase());
	});
	//bindings keyup caracteres alfanumericos con espacios
	$('.alphaNumericSpace').keyup(function() {
		if (this.value.match(/[^a-zA-Z0-9 ]/g)) {
            this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, '');
        }
	});
	//bindings keyup caracteres alfanumericos sin espacios
	$('.alphaNumeric').keyup(function() {
		if (this.value.match(/[^a-zA-Z0-9]/g)) {
			this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');
		}
	});
	//bindings keyup caracteres numericos
	$('.numeric').keyup(function() {
		if (this.value.match(/[^0-9 ]/g)) {
            this.value = this.value.replace(/[^0-9]/g, '');
        }
	});
	//bindings keyup caracteres alfabeticos
	$('.alphabetic').keyup(function() {
		if (this.value.match(/[^a-zA-Z ]/g)) {
            this.value = this.value.replace(/[^a-zA-Z ]/g, '');
        }
	});
	
	//binding OnBlur() para los campos con popup
	$("#conceptoCodigo").blur(function(){
		getAjaxConcepto('conceptoFacturableServlet','codigo','conceptoCodigo',getConceptoCallBack);
	});
	
	//binding OnBlur() para los campos con popup
	$("#listaPreciosCodigo").blur(function(){
		getAjaxListaPrecios('listasPreciosServlet','codigo','listaPreciosCodigo',getListaPreciosCallBack);
	});
	
	//binding OnBlur() para el campo valor (calculo precio concepto)
	$("#valor").blur(function(){
		getAjaxPrecioConcepto('calcularPrecioConceptoFacturableServlet',getPrecioConceptoCallBack);
	});
	//binding change() para el combobox de tipo de variacion (calculo precio concepto)
	$("#variacionId").change(function() {
		getAjaxPrecioConcepto('calcularPrecioConceptoFacturableServlet',getPrecioConceptoCallBack);
	});
});

//ajax 
function getAjaxConcepto(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getConceptoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#conceptoCodigoLabel").html(sResponseText);
		// trato de actualizar el precio calculado del concepto a asociar (si se puede)
		getAjaxPrecioConcepto('calcularPrecioConceptoFacturableServlet',getPrecioConceptoCallBack);
	}else{
		$("#conceptoCodigo").val("");
		$("#conceptoCodigoLabel").html("");
	}	
}
//ajax
function getAjaxListaPrecios(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getListaPreciosCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#listaPreciosCodigoLabel").html(sResponseText);
	else{
		$("#listaPreciosCodigo").val("");
		$("#listaPreciosCodigoLabel").html("");
	}	
}

//ajax
function getAjaxPrecioConcepto(url, callBack){
	if($("#valor") == null || $("#conceptoCodigo") == null || $("#variacionId") == null)
		return;
	var url = url+
		'?valor='+$("#valor").val()+
		'&clienteId='+$("#clienteId").val()+
		'&conceptoCodigo='+$("#conceptoCodigo").val()+
		'&variacionId='+$("#variacionId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getPrecioConceptoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#precioConcepto").val(sResponseText);
	else{
		$("#precioConcepto").val("");
	}	
}

//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarListaPrecios.html";
}
function volverCancelar(){
	document.location="mostrarListaPrecios.html";
}
function guardarYSalir(){
 	document.forms[0].submit();
}