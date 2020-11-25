$(document).ready(function() {			
	//Tooltips
	$("img[title]").tooltip();
	$("a[title]").tooltip();
	
});

function volverCancelar(){
	document.location="index.html";
}
function enviar(){
	document.forms[0].submit();
}

