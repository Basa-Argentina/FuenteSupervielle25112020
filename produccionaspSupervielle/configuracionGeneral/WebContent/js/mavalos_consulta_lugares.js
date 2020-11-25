var menuAbierto=false;
var idPaisSel = null;
$(document).ready(function() {
	//tabla lista
	$("#pais tbody tr").click(function(){
		if(!menuAbierto){
			$(".tr_selected").removeClass('tr_selected');
			$(this).addClass('tr_selected');
			idPaisSel = $(this).find('#hdn_id').val();
			if(idPaisSel != undefined)
				$('#iframePcias').attr('src','mostrarProvincias.html?id='+idPaisSel);
				//buscarAsociaciones(idPaisSel);
		}
	});
	$("#pais tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#pais tbody tr").mouseout(function(){
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
      $('#pais tbody tr').contextMenu('myMenu1', {
 
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
			$("#pais tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
      
});

function agregar(){
	document.location="guardarPais.html?accion="+$('#accion').val()+"&id="+$('#codigo').val()+"&nombre="+$('#nuevo').val();
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioListaPrecios.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="mostrarLugares.html?accion=MODIFICAR&id="+id;
}

function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarPais.html?id="+id;
		    }
		});
	}
}

function buscarFiltro(){
	document.forms[0].submit();
}
function volver(){
	document.location="menu.html";
}

function cancelar(){
	document.location="mostrarLugares.html?accion=NUEVO";
}
