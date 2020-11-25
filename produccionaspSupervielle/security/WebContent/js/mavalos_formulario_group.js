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
	
	//Busqueda
	$('#grupoDiv').attr({'style':'display:block'});
	$('#grupoImg').click(function() {
		$('#grupoDiv').slideToggle('slideUp');
		$('#grupoImgSrcDown').slideToggle('slideUp');
		$('#grupoImgSrc').slideToggle('slideUp');
	});
	
});

function volver(){
	document.location="mostrarGroup.html";
 }
 function volverCancelar(){
	document.location="mostrarGroup.html";
 }

 function guardarYSalir(){
 	seleccionarTodos("privilegiosGrupoSeleccionados");	 	
 	document.forms[0].submit();
 }