 function eliminar(mensaje,url,id){
	 var unica=window.confirm(mensaje);
	 if (unica){	 
		 document.location=url+"="+id;
	 }
}