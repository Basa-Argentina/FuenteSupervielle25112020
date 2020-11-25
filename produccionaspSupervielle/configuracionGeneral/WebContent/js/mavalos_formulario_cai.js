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

});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	var accion = document.getElementById("accion");
	var accionSerie = document.getElementById("accionSerie");
	document.location="volverFormularioSerie.html?accionSerie="+accionSerie.value+"&accion="+accion.value;
}
function volverCancelar(){
	var accion = document.getElementById("accion");
	var accionSerie = document.getElementById("accionSerie");
	document.location="volverFormularioSerie.html?accionSerie="+accionSerie.value+"&accion="+accion.value;
}
function guardarYSalir(){
 	document.forms[0].submit();
}