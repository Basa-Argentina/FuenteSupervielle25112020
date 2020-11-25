var menuAbierto=false;
var menu2Abierto=false;
var idListaSel = null;
$(document).ready(function() {
	//tabla lista
	$("#lista tbody tr").click(function(){
		if(!menuAbierto){
			$(".tr_selected").removeClass('tr_selected');
			$(this).addClass('tr_selected');
			idListaSel = $(this).find('#hdn_id').val();
			if(idListaSel != undefined)
				buscarAsociaciones(idListaSel);
		}
	});
	$("#lista tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#lista tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	//tabla detalle
	$("#detalle tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
		
	});
	$("#detalle tbody tr").mouseout(function(){
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
      $('#lista tbody tr').contextMenu('myMenu1', {
 
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
			$("#lista tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
      
    //PopUp Menu Detalles
      $('#detalle tbody tr').contextMenu('myMenu2', {
 
        bindings: {
      
          'modificar': function(t) {
	  	  	modificarAsociacion($(t).find('#hdn_id').val()); 
          },
 
          'quitar': function(t) { 
        	quitarAsociacion($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val()); 
          }
 
        },
		onShowMenu: function(e, menu) {
        	menu2Abierto=true;
			return menu;

		},
		onHide: function(){
			menu2Abierto=false;
			$("#detalle tbody tr").removeClass('tr_mouseover');
		}
 
      }); 

});

function agregar(){
	document.location="precargaFormularioListaPrecios.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioListaPrecios.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioListaPrecios.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarListaPrecios.html?id="+id;
		    }
		});
	}
}
function asociar(mensaje){
	if(idListaSel!=null && idListaSel!=undefined)
		document.location="precargaFormularioAgregarConcepto.html?urlDestino=mostrarListaPrecios.html&idLista="+idListaSel;
	else
		//window.alert(mensaje);
		jAlert(mensaje);
}
function modificarAsociacion(id){
	if(id!=null)
		document.location=
			"precargaFormularioAgregarConcepto.html?" +
			"accion=MODIFICACION&" +
			"id="+id+"&" +
			"urlDestino=mostrarListaPrecios.html&" +
			"idLista="+idListaSel;
}
function quitarAsociacion(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		var unica=window.confirm(mensaje);
		if(unica)
			document.location="eliminarAsociacionListaPrecios.html?detalleId="+id+"&listaId="+idListaSel;
	}
}
function buscarAsociaciones(listaId) {
	//realizo el request
	jQuery.ajax({
                url: "mostrarListaPrecios.html?id="+listaId,
                success: function(data){
                   filteredResponse =  $(data).find(this.selector);
                   if(filteredResponse.size() == 1){
                        $(this).html(filteredResponse);                            
                   }else{
                        $(this).html(data);
                   }
                   $(this).displayTagAjax(bindingContextMenu);
                } ,
                data: ({"time":new Date().getTime()}),
                context: $(".displayTagDiv")
            });
	
	
}
function buscarAsociacionesOnBodyLoad(listaId) {
//	$(".tr_selected").removeClass('tr_selected');
	if(listaId != ""){
		$("#lista tbody tr").each(function(){
			if($(this).find('#hdn_id').val() == listaId)
				$(this).addClass('tr_selected');
		});
		idListaSel = listaId;
		buscarAsociaciones(idListaSel);
	}
}
function bindingContextMenu(){
	//PopUp Menu Detalles
    $('#detalle tbody tr').contextMenu('myMenu2', {

      bindings: {
    
        'modificar': function(t) {
	  	  	modificarAsociacion($(t).find('#hdn_id').val()); 
        },

        'quitar': function(t) { 
      	quitarAsociacion($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val()); 
        }

      },
		onShowMenu: function(e, menu) {
      	menu2Abierto=true;
			return menu;

		},
		onHide: function(){
			menu2Abierto=false;
			$("#detalle tbody tr").removeClass('tr_mouseover');
		}

    }); 
}
function buscarFiltro(){
	document.forms[0].submit();
}
function volver(){
	document.location="menu.html";
}
