
function popupOff(divOpen,divHidden){
	$('#'+divOpen).hide();
	$('#'+divHidden).hide();
}
function popupOn(divOpen,divHidden){
	var left=($(window).width()-$('#'+divOpen).width())/2;
	$('#'+divOpen).css("left",left);
	
	$('#'+divHidden).show();
	$('#'+divOpen).show("slow");
}

function cerrarSesion(){
	document.location="j_spring_security_logout";
}
function mostrarErrores(errores){
	if(errores != undefined && errores == true)
 		popupOn('darkMiddle','darkLayer');	
}
function mostrarAvisos(avisosBan){
	if(avisosBan == true)
 		popupOn('darkMiddleAvisos','darkLayer');	
}
function mostrarWarnings(warningsBan){
	if(warningsBan == true)
 		popupOn('darkMiddleWarnings','darkLayer');	
}
function mostrarWait(){
 		popupOn('darkMiddleWait','darkLayer');	
}