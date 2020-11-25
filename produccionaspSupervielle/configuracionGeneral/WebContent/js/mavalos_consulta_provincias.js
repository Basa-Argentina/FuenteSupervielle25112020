var menuAbierto2=false;
var idPciaSel = null;
$(document).ready(function() {
	//tabla lista
	$("#provincia tbody tr").click(function(){
		if(!menuAbierto2){
			$(".tr_selected").removeClass('tr_selected');
			$(this).addClass('tr_selected');
			idPciaSel = $(this).find('#hdn_id').val();
			if(idPciaSel != undefined)
				$('#iframeLocalidades').attr('src','mostrarLocalidades.html?id='+idPciaSel);
		}
	});
	$("#provincia tbody tr").mouseover(function(){
		if(!menuAbierto2)
			$(this).addClass('tr_mouseover');
			
	});
	$("#provincia tbody tr").mouseout(function(){
		if(!menuAbierto2)
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
      $('#provincia tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
          'modificar': function(t) {
            modificar($(t).find('#hdn_id').val(),$('#idPais').val()); 
          },
 
          'eliminar': function(t) { 
        	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val(),$(t).find('#hdn_idPais').val()); 
          }
 
        },
		onShowMenu: function(e, menu) {
			menuAbierto2=true;
			return menu;

		},
		onHide: function(){
			menuAbierto2=false;
			$("#provincia tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
      
});

function agregar(){
	document.location="guardarProvincia.html?accion="+$('#accion').val()+"&id="+$('#codigo').val()+"&nombre="+$('#nuevo').val()+"&idPais="+$('#idPais').val();
}

function modificar(idPcia,idPais){
	if(idPcia!=null)
		document.location="mostrarProvincias.html?accion=MODIFICAR&idPcia="+idPcia+"&id="+idPais;
}

function eliminar(mensaje, id, idPais){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarProvincia.html?id="+id+"&idPais="+idPais;
		    }
		});
	}
}

function buscarFiltro(){
	var idPais = $('#idPais').val();
	$('#paisId').val(idPais);
	document.forms[0].submit();
}
function volver(){
	document.location="menu.html";
}

function cancelar(){
	document.location="mostrarProvincias.html?accion=NUEVO&id="+$('#idPais').val();
}
