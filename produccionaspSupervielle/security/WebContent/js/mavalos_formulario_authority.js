$(document).ready(function() {			
	//Tooltips
	$("img[title]").tooltip();
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
	
	//Busqueda
	$('#grupoDiv').attr({'style':'display:block'});
	$('#grupoImg').click(function() {
		$('#grupoDiv').slideToggle('slideUp');
		$('#grupoImgSrcDown').slideToggle('slideUp');
		$('#grupoImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
});

function volver(){
	history.back();
 }
 function volverCancelar(){
	document.location="mostrarAuthority.html";
 }

 function guardarYSalir(){	 	
 	document.forms[0].submit();
 }