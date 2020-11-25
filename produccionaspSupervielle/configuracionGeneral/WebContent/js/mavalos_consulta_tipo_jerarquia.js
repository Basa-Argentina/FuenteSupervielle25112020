var menuAbierto=false;
var idTipoSel = null;
$(document).ready(function() {
	//tabla lista
	$("#tipo tbody tr").click(function(){
		if(!menuAbierto){
			$(".tr_selected").removeClass('tr_selected');
			$(this).addClass('tr_selected');
			idTipoSel = $(this).find('#hdn_id').val();
			if(idTipoSel != undefined)
				buscarAsociaciones(idTipoSel);
		}
	});
	$("#tipo tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});	
	
	$("#tipo tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	//tabla detalle
	$("#jerarquia tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
		
	});
	$("#jerarquia tbody tr").mouseout(function(){
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
      $('#tipo tbody tr').contextMenu('myMenu1', {
 
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
      $('#jerarquia tbody tr').contextMenu('myMenu1', {
 
        bindings: {
    	  
    	  'consultar': function(t) {
    	  	  consultarJerarquia($(t).find('#hdn_id').val()); 
    	  },
      
          'modificar': function(t) {
    		  modificarJerarquia($(t).find('#hdn_id').val()); 
          },
 
          'eliminar': function(t) { 
        	  eliminarJerarquia($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val()); 
          }
 
        },
		onShowMenu: function(e, menu) {
        	menu2Abierto=true;
			return menu;

		},
		onHide: function(){
			menu2Abierto=false;
			$("#jerarquia tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
    if($("#hdn_idTipoJerarquia").val() != null || $("#hdn_idTipoJerarquia").val() != ""){		 
  		setRowTipoJerarquia();
  	}

});

function setRowTipoJerarquia(){
	var idTipo = $("#hdn_idTipoJerarquia").val();
	var table = document.getElementById("tipo");
	var tbody = table.getElementsByTagName("tbody")[0];
    var rows = tbody.getElementsByTagName("tr");
    for (i=0; i < rows.length; i++) {
        var input = rows[i].children[0];
        if(input.children.length>0){
	        var idInput = input.children[0];
	        var value = idInput.value;        
	        if (value == $("#hdn_idTipoJerarquia").val()) {
	            rows[i].className=rows[i].className+' tr_selected';
	        }
        }
    }	
}

function agregar(){
	document.location="precargaFormularioTipoJerarquia.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioTipoJerarquia.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioTipoJerarquia.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarTipoJerarquia.html?id="+id;
		    }
		});		
	}
}
function agregarJerarquia(mensaje, title){
	if(idTipoSel!=null && idTipoSel!=undefined)
		document.location="precargaFormularioJerarquia.html?urlDestino=mostrarTipoJerarquia.html&idTipoSel="+idTipoSel;
	else
		jAlert(mensaje,title);
}
function consultarJerarquia(id){
	if(id!=null)
		document.location="precargaFormularioJerarquia.html?accion=CONSULTA&id="+id;
}
function modificarJerarquia(id){
	if(id!=null)
		document.location=
			"precargaFormularioJerarquia.html?" +
			"accion=MODIFICACION&" +
			"id="+id+"&" +
			"urlDestino=mostrarTipoJerarquia.html&" +
			"idTipo="+idTipoSel;
}
function eliminarJerarquia(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		var unica=window.confirm(mensaje);
		if(unica)
			document.location="eliminarJerarquia.html?jerarquiaId="+id;
	}
}
function buscarAsociaciones(listaId) {
	//realizo el request
	jQuery.ajax({
                url: "mostrarTipoJerarquia.html?id="+listaId,
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
function buscarAsociacionesOnBodyLoad(tipoId) {
//	$(".tr_selected").removeClass('tr_selected');
	if(tipoId != ""){
		$("#tipo tbody tr").each(function(){
			if($(this).find('#hdn_id').val() == tipoId)
				$(this).addClass('tr_selected');
		});
		idTipoSel = tipoId;
		buscarAsociaciones(idTipoSel);
	}
}
function bindingContextMenu(){
	//PopUp Menu Detalles
    $('#jerarquia tbody tr').contextMenu('myMenu1', {

      bindings: {
  	  
  	  'consultar': function(t) {
  	  	  consultarJerarquia($(t).find('#hdn_id').val()); 
  	  },
    
        'modificar': function(t) {
  		  modificarJerarquia($(t).find('#hdn_id').val()); 
        },

        'eliminar': function(t) { 
      	  eliminarJerarquia($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val()); 
        }

      },
		onShowMenu: function(e, menu) {
      	menu2Abierto=true;
			return menu;

		},
		onHide: function(){
			menu2Abierto=false;
			$("#jerarquia tbody tr").removeClass('tr_mouseover');
		}

    }); 
}
function buscarFiltro(){
	document.forms[0].submit();
}
function volver(){
	document.location="menu.html";
}
