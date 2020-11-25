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
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	history.back();
}
function volverCancelar(){
	document.location="mostrarUser.html";
}
function guardarYSalir(){
 	seleccionarTodos("assignedRoles");	 	
 	document.forms[0].submit();
}