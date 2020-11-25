var menuAbierto=false;

var idLocalidadSel = null;
$(document).ready(function() {
	//tabla lista
	$("#localidad tbody tr").click(function(){
		if(!menuAbierto){
			$(".tr_selected").removeClass('tr_selected');
			$(this).addClass('tr_selected');
			idLocalidadSel = $(this).find('#hdn_id').val();
			if(idLocalidadSel != undefined)
				$('#iframeBarrios').attr('src','mostrarBarrios.html?id='+idLocalidadSel);
				//buscarAsociaciones(idPaisSel);
		}
	});
	$("#localidad tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#localidad tbody tr").mouseout(function(){
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
      $('#localidad tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
    	  'consultar': function(t) {
    	  	consultar($(t).find('#hdn_id').val()); 
      	  },
      
          'modificar': function(t) {
            modificar($(t).find('#hdn_id').val(),$('#idPcia').val()); 
          },
 
          'eliminar': function(t) { 
        	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val(),$(t).find('#hdn_idPcia').val()); 
          }
 
        },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#localidad tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
      
});

function agregar(){
	document.location="guardarLocalidad.html?accion="+$('#accion').val()+"&id="+$('#codigo').val()+"&nombre="+$('#nuevo').val()+"&idPcia="+$('#idPcia').val();
}

function modificar(idLoc,idPcia){
	if(idLoc!=null)
		document.location="mostrarLocalidades.html?accion=MODIFICAR&idLoc="+idLoc+"&id="+idPcia;
}

function eliminar(mensaje, id, idPcia){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarLocalidad.html?id="+id+"&idPcia="+idPcia;
		    }
		});
	}
}

function buscarFiltro(){
	var idPcia = $('#idPcia').val();
	$('#pciaId').val(idPcia);
	document.forms[0].submit();
}
function volver(){
	document.location="menu.html";
}

function cancelar(){
	document.location="mostrarLocalidades.html?accion=NUEVO&id="+$('#idPcia').val();
}