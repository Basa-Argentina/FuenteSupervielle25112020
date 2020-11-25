var menuAbierto=false;

$(document).ready(function() {
	//tabla elemento
	$("#elemento tbody td:nth-child(11)").click(function(){
		var input = $(this).find('input');

		if(input.length > 0) {
			$('#codigoDesde').val(input.val());
	        $('#codigoHasta').val(input.val());
	      }else{
	    	  $('#codigoDesde').val($(this).text());
		      $('#codigoHasta').val($(this).text());
	      }
			
	});
	$("#elemento tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#elemento tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	//Checkbox
	$("input[name=checktodos]").change(function(){
		$('input[type=checkbox]').each( function() {			
			if($("input[name=checktodos]:checked").length == 1){
				this.checked = true;
			} else {
				this.checked = false;
			}
		});
	});
	
	$('#imprimirModulos').click(function(){
		codigoLectura = $("#codigoLectura").val();
		window.open('imprimirEtiquetasElementoCodigoBarra.html?codigoLectura='+codigoLectura+"&modo=ventana"+"&cantidad="+$('#cantImprimir').val(),'');
	});
	
	$('#descargarImprimirModulos').click(function(){
		codigoLectura = $("#codigoLectura").val();
		window.open('imprimirEtiquetasElementoCodigoBarra.html?codigoLectura='+codigoLectura+"&modo=descarga"+"&cantidad="+$('#cantImprimir').val(),'');
	});
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
	
	//Imprimir
	$('#imprimirDiv').attr({'style':'display:block'});
	$('#imprimirImg').click(function() {
		$('#imprimirDiv').slideToggle('slideUp');
		$('#imprimirImgSrcDown').slideToggle('slideUp');
		$('#imprimirImgSrc').slideToggle('slideUp');
	});
	$('#imprimirDiv > table').addClass('seccion');
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
		
	//PopUp
      $('#elemento tbody tr').contextMenu('myMenu1', {
 
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
			$("#elemento tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
      
      //Popups Busqueda
      $('#botonPopupEmpresa').click(function(){
    	  abrirPopupEmpresa();
      });
      
      $('#botonPopupSucursales').click(function(){
    	  abrirPopupSucursal();
      });
      
      $('#botonPopupDeposito').click(function(){
    	  abrirPopupDeposito();
      });
      
      $('#botonPopupCliente').click(function(){
    	  abrirPopupCliente()();
      });
      
      $('#botonPopupContenedor').click(function(){
    	 abrirPopupContenedor(); 
      });
      
      $('#botonPopupLectura').click(function(){
    	 abrirPopupLectura(); 
      });
      
      $('#botonPopupTipoElemento').click(function(){
    	  abrirPopupTipoElemento();
      });
      
      $("#buscaClasificacionDocumental").click(function(){
   		abrirPopupSeleccion("popUpSeleccionClasificacionDocumental.html",'codigoCliente',"clasificacionDocumentalPopupMap","");
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
    	$("#codigoContenedor").blur(function(){
    		getAjax('elementosServlet','codigo','codigoContenedor',getElementoCallBack);
    	});
    	$("#codigoTipoElemento").blur(function(){
    		getAjax('tipoElementoDescripcionServlet','codigo','codigoTipoElemento',getTipoElementoCallBack);
    	});	
    	$("#codigoLectura").blur(function(){
    		getAjaxConParent('lecturasPorEmpresaServlet','codigo','codigoLectura','codigoEmpresa',getLecturaCallBack);
    	});

    	if($("#codigoContenedor").val() != null){
    		getAjax('elementosServlet','codigo','codigoContenedor',getElementoCallBack);
    	}
    	if($("#codigoLectura").val() != null){
    		getAjaxConParent('lecturasPorEmpresaServlet','codigo','codigoLectura','codigoEmpresa',getLecturaCallBack);
    	}
    	if($("#codigoTipoElemento").val() != null){
    		getAjax('tipoElementoDescripcionServlet','codigo','codigoTipoElemento',getTipoElementoCallBack);
    	}
    	//binding OnBlur() para los campos con popup	
     	$("#codigoClasificacionDocumental").blur(function(){
     		searchAjax('seleccionClasificacionDocumental.html?nodo=I','codigoClasificacionDocumental','codigoCliente',getClasificacionDocumentalCallBack);
     	});
     	 $("#codigoClasificacionDocumental").change(function(){
       		$("#codigoClasificacionDocumental").trigger('blur');
       	});
    	
    	$('#pop').hide();

    	
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
		    	document.location="eliminarElemento.html?id="+id;
		    }
		});
	}
}
function buscarFiltro(){
	//alert($('#pagesize').val());
	jAalert($('#pagesize').val());
	if($('#pagesize').val() == 'Todos'){
		jConfirm(mensaje, 'Ha seleccionado "Todos" y esto puede demorar mucho tiempo, est� seguro de continuar??',function(r) {
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
function seleccionar(mensaje){
	var allVals = [];
    $("input[class='checklote']:checked").each(function() {
    	allVals.push($(this).val());
    });
    if(allVals.length == 0)
    {
    	jAlert('Debe seleccionar al menos un elemento para agregar.');
		return;
	}
    $('#requerimientoElementosSel').val(allVals);
	document.forms[1].submit();
}

function volverCancelar(){
	//Desmarco toda la tabla porque vuelve por submit
	$('input[type=checkbox]').each( function() {
		this.checked = false;
	});
	document.forms[1].submit();
}

function abrirPopupContenedor(){
	
	//var url = "popUpSeleccionContenedor.html?codigo="+$('#codigoCliente').val();
	var url = "popUpSeleccionContenedor.html";
//	$.post(url, {codigo:+$('#codigoCliente').val()}, function(data){
//												        $('.selectorDiv').html(data);
//												        $(".displayTagDiv").displayTagAjax();
//												        popupOnDiv($(this).find('.darkMiddleClass'),'darkLayer');
//												        $('#pop').hide();
//     												});
	
	jQuery.ajax({
        url: url,
        success: function(data){
           $(this).html(data);
           $(".displayTagDiv").displayTagAjax();
           popupOffDiv($('#pop'),'darkLayer');
           popupOnDiv($(this).find('.darkMiddleClass'),'darkLayer'); 
        },
        type: "GET",
        timeout:120000,
        data: ({"time":new Date().getTime()})+"&codigo="+$('#codigoCliente').val(),
        context: $(".selectorDiv"),
        beforeSend:function(){
        	popupOnDiv($('#pop'),'darkLayer');
        },
        error:function(){
        	jAlert("Ha ocurrido un error...");
        	popupOffDiv($('#pop'),'darkLayer');
        }
    });
}

function abrirPopupLectura(){
	var url = "popUpSeleccionLectura.html?codigo="+$("#codigoEmpresa").val();
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

function abrirPopupTipoElemento(){
	var url = "popUpSeleccionTipoElemento.html";
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
function getElementoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoContenedorLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoContenedorLabel").html("");
	}
}

//ajax
function getLecturaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoLecturaLabel").html(sResponseText);
	else{
		$("#codigoLecturaLabel").html("");
		$("#codigoLectura").val("");
	}
}

function getTipoElementoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoTipoElementoLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoTipoElementoLabel").html("");
	}
}

//ajax
function getClasificacionDocumentalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoClasificacionDocumentalLabel").html(sResponseText);
	}
	else{
		$("#codigoClasificacionDocumental").val("");
		$("#codigoClasificacionDocumentalLabel").html("");
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