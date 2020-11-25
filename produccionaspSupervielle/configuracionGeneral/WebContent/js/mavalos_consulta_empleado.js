var menuAbierto=false;

$(document).ready(function() {
	//tabla empleado
	$("#empleado tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#empleado tbody tr").mouseout(function(){
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
      $('#empleado tbody tr').contextMenu('myMenu1', {
 
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
			$("#empleado tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
     
      $('#botonPopupCliente').click(function(){
    	  abrirPopupCliente()();
      });
  	 
  	  $("#codigoCliente").blur(function(){
		getAjax('clientesServlet','codigo','codigoCliente',getClienteCallBack);
	  });
  	
  	  $("#codigoCliente").trigger("blur");
});

//ajax
function getClienteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoClienteLabel").html("");
	}
}

//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

function abrirPopupCliente(){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert($('#errorSeleccioneEmpresa').val());
		return;
	}
	var url = "popUpSeleccionCliente.html?codigo="+$("#codigoEmpresa").val();
	jQuery.ajax({
        url: url,
        success: function(data){
           $(this).html(data);
           $(".displayTagDiv").displayTagAjax();
           popupOnDiv($(this).find('.darkMiddleClass'),'darkLayer');
        },
        data: ({"time":new Date().getTime()}),
        context: $(".selectorDiv")
    });
}

function agregarEmpleado(){
	document.location="precargaFormularioEmpleado.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioEmpleado.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioEmpleado.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarEmpleado.html?id="+id;
		    }
		});
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function habilitarEmpleado(id){
	document.location="habilitarEmpleado.html?id="+id;
}
function desHabilitarEmpleado(id){
	document.location="desHabilitarEmpleado.html?id="+id;
}
function volver(){
	document.location="menu.html";
}
