var menuAbierto=false;

$(document).ready(function() {
	//tabla user
	$("#insModAppLog tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#insModAppLog tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
});

function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosInsModAppLog.html";
}
function volver(){
	document.location="menu.html";
}
function consultarError(id){
	window.open('consultaInsModAppLogDetalle.html?id='+id,'','left=20,top=20,width=900,height=600,scrollbars=1,resizable=0');
}
function eliminar(mensaje,url){
	 var unica=window.confirm(mensaje);
	 if (unica){	 
		 document.location=url;
	 }
}