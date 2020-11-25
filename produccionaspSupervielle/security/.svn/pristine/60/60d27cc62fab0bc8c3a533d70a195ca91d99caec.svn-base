var menuAbierto=false;

$(document).ready(function() {
	//tabla user
	$("#user tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#user tbody tr").mouseout(function(){
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
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
		
	//PopUp
      $('#user tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
          'consultar': function(t) {
            consultar($(t).find('#hdn_id').val());
          },
 
          'modificar': function(t) {
            modificar($(t).find('#hdn_id').val());
 
          },
 
          'eliminar': function(t) {
 
        	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val());
 
          }
 
        },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#user tbody tr").removeClass('tr_mouseover');
		}
 
      }); 

});

function agregarUser(){
	document.location="precargaFormularioUser.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioUser.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioUser.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		var unica=window.confirm(mensaje);
		if(unica)
			document.location="eliminarUser.html?id="+id;
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function habilitarUser(id){
	document.location="habilitarUser.html?id="+id;
}
function desHabilitarUser(id){
	document.location="desHabilitarUser.html?id="+id;
}
function volver(){
	document.location="menu.html";
}
