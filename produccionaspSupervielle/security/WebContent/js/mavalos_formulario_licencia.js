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
	
});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	history.back();
}
function volverCancelar(){
	document.location="mostrarLicencia.html";
}
function guardarYSalir(){
 	document.forms[0].submit();
}