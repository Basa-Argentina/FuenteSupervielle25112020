var menuAbierto=false;

$(document).ready(function() {
	//tabla licencia
	$("#licencia tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#licencia tbody tr").mouseout(function(){
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
	
		
	//PopUp
      $('#licencia tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
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
			$("#licencia tbody tr").removeClass('tr_mouseover');
		}
 
      }); 

});

function agregar(){
	document.location="precargaFormularioLicencia.html";
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioLicencia.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		var unica=window.confirm(mensaje);
		if(unica)
			document.location="eliminarLicencia.html?id="+id;
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function volver(){
	document.location="menu.html";
}
