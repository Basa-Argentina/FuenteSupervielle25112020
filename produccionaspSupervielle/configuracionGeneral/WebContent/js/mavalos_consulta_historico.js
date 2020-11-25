var menuAbierto=false;

$(document).ready(function() {

	$("#historico tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#historico tbody tr").mouseout(function(){
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
	
	//Busqueda
	$('#busquedaDiv2').attr({'style':'display:block'});
	$('#busquedaImg2').click(function() {
		$('#busquedaDiv2').slideToggle('slideUp');
		$('#busquedaImgSrcDown2').slideToggle('slideUp');
		$('#busquedaImgSrc2').slideToggle('slideUp');
	});
	$('#busquedaDiv2 > table').addClass('seccion');
		
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
	//PopUp
      $('#historico tbody tr').contextMenu('myMenu1', {
 
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
			$("#historico tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
      
      //Popups Busqueda
      $('#botonPopupUsuario').click(function(){
    	  abrirPopupUsuario();
      });
    //Popups Busqueda
      $('#botonPopupUsuarioDia').click(function(){
    	  abrirPopupUsuarioDia();
      });
      
      $("#buscaCliente").click(function(){
  			abrirPopupSeleccion("popUpSeleccionCliente.html","codigoEmpresa","clientesPopupMap", $("#mensajeSeleccioneEmpresa").val());
  	  });
      
      $("#buscaElemento").click(function(){
     	 abrirPopupSeleccionContenedor("popUpSeleccionContenedor.html","codigoCliente",'codigoTipoElemento', null);
 	  });
      
      $('#buscaTiposElementos').click(function(){
      	abrirPopupTiposElementos();
       });
                  
    //Inicio menu
    	$("ul.topnav li").hover(
    		function() {
    			$(this).addClass("hover");
    			$(this).find("ul.subnav").slideDown('fast').show(); //Drop down the subnav on click  
    		}, 
    		function(){  
    			$(this).removeClass("hover");
    			$(this).find("ul.subnav").slideUp('fast'); //When the mouse hovers out of the subnav, move it back up  
    		}
        );
    	//Fin menu
      	//binding OnBlur() para los campos con popup	
    	$("#codigoUsuario").blur(function(){
    		getAjax('seleccionUsuario.html','codigo','codigoUsuario',getUsuarioCallBack);
    	});
    	if($("#codigoUsuario").val() != null){
    		getAjax('seleccionUsuario.html','codigo','codigoUsuario',getUsuarioCallBack);
    	}
    	if($("#codigoUsuarioDia").val() != null){
    		getAjax('seleccionUsuario.html','codigo','codigoUsuarioDia',getUsuarioDiaCallBack);
    	}
    	$("#codigoCliente").blur(function(){
    		searchAjax('seleccionCliente.html','codigoCliente','codigoEmpresa',getClienteCallBack);
    	});
    	$("#codigoTipoElemento").blur(function(){
    	    searchAjax('tipoElementoDescripcionServlet','codigoTipoElemento',null,getTipoElementoCallBack);
    	});
    	$("#codigoContenedor").blur(function(){
       	 searchContenedorPorTipoYCliente('seleccionContenedor.html','codigoContenedor','codigoCliente',null,getElementoCallBack);
     	});
    	$('#pop').hide();
    	
    	$("#codigoUsuario").blur();
    	$("#codigoUsuarioDia").blur();
    	$("#codigoCliente").blur();
    	$("#codigoTipoElemento").blur();
    	$("#codigoContenedor").blur();

});

function agregar(){
	document.location="precargaFormularioElemento.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioElemento.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioElemento.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	var request = new HttpRequest("validarEliminarElemento.html?id="+id, getValidadorElemntoCallBack);
		    	request.send();	
		    }
		});
	}
}
function buscarFiltro(){
	//alert($('#pagesize').val());
	jAlert($('#pagesize').val());
	if($('#pagesize').val() == 'Todos'){
		jConfirm(mensaje, 'Ha seleccionado "Todos" y esto puede demorar mucho tiempo, está seguro de continuar??',function(r) {
		    if(r){
		    	document.forms[0].submit();
		    }
		});
	}else{
		document.forms[0].submit();
	}
}
function borrarFiltros(){
	document.location="borrarFiltrosElemento.html";
}
function volver(){
	document.location="menu.html";
}

function abrirPopupUsuario(){
	var url = "popUpSeleccionUsuario.html?codigo="+$("#codigoEmpresa").val();
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
function abrirPopupUsuarioDia(){
	var url = "popUpSeleccionUsuario.html?codigo="+$("#codigoEmpresa").val()+"&field=codigoUsuarioDia";
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

//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getAjaxConParent(url, varName, elementName, parentId,callBack){

		var input = document.getElementById(elementName);
		if(input == null)
			return;
		var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val()+'&empresaId='+$("#"+parentId+"").val();	
		var request = new HttpRequest(url, callBack);
		request.send();
	
}
//ajax
function getUsuarioCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoUsuarioLabel").html(sResponseText);
	else{
		$("#codigoUsuario").val("");
		$("#codigoUsuarioLabel").html("");
	}
}
//ajax
function getUsuarioDiaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoUsuarioDiaLabel").html(sResponseText);
	else{
		$("#codigoUsuarioDia").val("");
		$("#codigoUsuarioDiaLabel").html("");
	}
}

function abrirPopupSeleccionContenedor(url,inputValue,codigoTipoElemento, mensaje){
	if(inputValue!=null){
		if($("#"+inputValue).val()==null || $("#"+inputValue).val()==""){
			jAlert(mensaje);
			return;
		}
		if(url.indexOf("?") != -1){
			url = url+'&codigo='+$("#"+inputValue).val();}
		else{
			url = url+'?codigo='+$("#"+inputValue).val();}
		
		if(codigoTipoElemento){
			url = url+'&codigoTipoElemento='+$("#"+codigoTipoElemento).val();
		}
	}
	
	if($('#pop').length>0){
		jQuery.ajax({
	        url: url,
	        success: function(data){
	           $(this).html(data);
	           $(".displayTagDiv").displayTagAjax();
	           popupOffDiv($('#pop'),'darkLayer');
	           popupOnDiv($(this).find('.darkMiddleClass'),'darkLayer');
	        },
	        data: ({"time":new Date().getTime()}),
	        context: $(".selectorDiv"),
	        beforeSend:function(){
	        	popupOnDiv($('#pop'),'darkLayer');
	        },
	        error:function(){
	        	jAlert("Ha ocurrido un error...");
	        	popupOffDiv($('#pop'),'darkLayer');
	        }
	    });
	}else {
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
}

function getClienteCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#codigoClienteLabel").html("");
		$("#codigoCliente").val("");
	}
}

//ajax
function getElementoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoContenedorLabel").html(sResponseText);
	else{
		//$("#codigoContenedor").val("");
		$("#codigoContenedorLabel").html("");
	}
}
//ajax
function getTipoElementoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoTipoElementoLabel").html(sResponseText);
	}
	else{
		$("#codigoTipoElemento").val("");
		$("#codigoTipoElementoLabel").html("");
	}
}

function abrirPopupTiposElementos(){
	var url = "popUpSeleccionTiposElementos.html";
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

//ajax
function searchContenedorPorTipoYCliente(url, codigoContenedor,dependence, codigoTipoElemento,callBack){
	var value = $("#"+codigoContenedor).val();
	if(url.indexOf("?") != -1)
		url = url+'&codigo='+value;
	else
		url = url+'?codigo='+value;;
	if(dependence){
		url = url+'&dependencia='+$("#"+dependence).val();
	}
	if(codigoTipoElemento){
		url = url+'&codigoTipoElemento='+$("#"+codigoTipoElemento).val();
	}
	var request = new HttpRequest(url, callBack);
	request.send();	
}