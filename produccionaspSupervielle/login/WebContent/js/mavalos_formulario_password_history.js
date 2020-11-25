$(document).ready(function() {			
	//Tooltips
	$("img[title]").tooltip();
	$("a[title]").tooltip();
	
});

function volverCancelar(){
	document.location="menu.html";
}
function enviar(form){
	document.getElementById(form).submit();
//	document.forms[0].submit();
}
