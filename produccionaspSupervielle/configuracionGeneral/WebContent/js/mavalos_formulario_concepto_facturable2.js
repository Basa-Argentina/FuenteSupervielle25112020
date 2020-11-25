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
	
	//binding OnBlur() para los campos con popup
	$("#impuestoCodigo").blur(function(){
		getAjaxImpuesto('impuestosServlet','codigo','impuestoCodigo',getImpuestoCallBack);
	});
});

//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarConceptoFacturable.html";
}
function volverCancelar(){
	document.location="mostrarConceptoFacturable.html";
}
function guardarYSalir(){
 	document.forms[0].submit();
}

function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

//ajax
function getAjaxImpuesto(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getImpuestoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#impuestoCodigoLabel").html(sResponseText);
	else{
		$("#impuestoCodigo").val("");
		$("#impuestoCodigoLabel").html("");
	}
}