var menuAbierto=false;

$(document).ready(function() {
	//tabla empresa
	$("#empresa tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#empresa tbody tr").mouseout(function(){
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
      $('#empresa tbody tr').contextMenu('myMenu1', {
 
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
			$("#empresa tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
});

function agregar(){
	document.location="precargaFormularioEmpresa.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioEmpresa.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioEmpresa.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarEmpresa.html?id="+id;
		    }
		});
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosEmpresa.html";
}
function volver(){
	document.location="menu.html";
}
