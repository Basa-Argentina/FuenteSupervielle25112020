var menuAbierto=false;

var idLocalidadSel = null;
$(document).ready(function() {
	//tabla lista
	
	$("#barrio tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#barrio tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
		
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:none'});
	
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
      $('#barrio tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
          'modificar': function(t) {
            modificar($(t).find('#hdn_id').val(),$('#idLoc').val()); 
          },
 
          'eliminar': function(t) { 
        	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val(),$(t).find('#hdn_idLoc').val()); 
          }
 
        },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#barrio tbody tr").removeClass('tr_mouseover');
		}
 
      });
      

      
});

function agregar(){
	document.location="guardarBarrio.html?accion="+$('#accion').val()+"&id="+$('#codigo').val()+"&nombre="+$('#nuevo').val()+"&idLoc="+$('#idLoc').val();
}

function modificar(idBarrio,idLoc){
	if(idBarrio!=null)
		document.location="mostrarBarrios.html?accion=MODIFICAR&idBarrio="+idBarrio+"&id="+idLoc;
}

function eliminar(mensaje, id, idLoc){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarBarrio.html?id="+id+"&idLoc="+idLoc;
		    }
		});
	}
}

function buscarFiltro(){
	var idLoc = $('#idLoc').val();
	$('#locId').val(idLoc);
	document.forms[0].submit();
}
function volver(){
	document.location="menu.html";
}

function cancelar(){
	document.location="mostrarBarrios.html?accion=NUEVO&id="+$('#idLoc').val();
}