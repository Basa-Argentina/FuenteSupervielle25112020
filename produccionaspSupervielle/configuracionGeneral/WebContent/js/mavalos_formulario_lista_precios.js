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
	
	//binding OnBlur() para los campos con popup
	$("#impuestoCodigo").blur(function(){
		getAjaxImpuesto('impuestosServlet','codigo','impuestoCodigo',getImpuestoCallBack);
	});
	//Binding del input con la funcion en el evento keyup
	$("#codigo").keyup(function(){
		changeCodigo();
	});
	checkSelect();
});

//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarListaPrecios.html";
}
function volverCancelar(){
	document.location="mostrarListaPrecios.html";
}
function aceptar(){
	var opcion = $("input[name=radio_group]:checked").val();  
	$("#opcion").val(opcion);
	guardarYSalir();
}
function guardarYSalir(){
 	document.forms[0].submit();
}
function popupOpcionDetalle(accion, vacia){
	if(accion != "NUEVO" && vacia != true)
		mostrarOpcion();
	else
		guardarYSalir();
}
function changeCodigo(){
	var codigo = document.getElementById("codigo");
	var valor = codigo.value;
	codigo.value = valor.toUpperCase();
}

function checkSelect(){
	var combo = document.getElementById('usaVencimiento');
	var select = document.getElementById('fechaVencimiento');
		if(combo.checked == true)
		{
			select.disabled = false;
			document.getElementById('seccion1').style.display="";
			
		}
		else
		{
			select.disabled = true;
			select.value = "";
			document.getElementById('seccion1').style.display="none";
		}
				

}