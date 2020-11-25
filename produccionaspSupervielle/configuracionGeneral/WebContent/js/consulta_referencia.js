var menuAbierto=false;

$(document).ready(function() {
	
	//fancybox
	$('#fancyboxImagenes').fancybox({
		'padding'			: 10,
		'titleShow'			: false,
		'showNavArrows'		: true,
		'autoDimensions'	: false,
		'width'         	: 870,
		'scrolling'			: 'auto',
		'height'        	: 600,
		'overlayColor'		: '#CCCCCC',
		'overlayOpacity'	: 0.7,
		'showCloseButton'	: true,
		'autoScale'			: false,
		'transitionIn'		: 'elastic',
		'transitionOut'		: 'elastic',
		'type'				: 'iframe'
		//'onClosed'			: function(){ $('#botonAgregar').removeAttr('disabled');}
	});
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	//Busqueda Doc
	$('#busquedaDocDiv').attr({'style':'display:block'});
	$('#busquedaDocImg').click(function() {
		$('#busquedaDocDiv').slideToggle('slideUp');
		$('#busquedaDocImgSrcDown').slideToggle('slideUp');
		$('#busquedaDocImgSrc').slideToggle('slideUp');
	});
	
	//Submit form on click
	$('#buscarDocumento').click(function(){
		//ejecutamos el servicio
		//var listaPalabras = "c://Archivos_Digitales//TIFF//prueba//0001//111//001//1113//08-05-2015//REMITOS SISTEMA 14-045.tif,bank\\0999\\5010\\610\\1312\\09-06-2015\\";
		/*$(":input", $('#formBusqueda')).each(function() {
			var type = this.type;
			var tag = this.tagName.toLowerCase();
			//limpiamos los valores de los campos…
			if (type == "text")
				this.value = "";
		});*/
		var listaPalabras = $('#palabraBusqueda').val();
		//guardamos los resultados en un input
		$('#listaPalabras').val(listaPalabras);	
		$('#formBusqueda').submit();
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
	
	$('#descargarImagenesZIP').click(function(){
		
	 	var allVals = [];
	    $("input[class='checklote']:checked").each(function() {
	    	allVals.push($(this).val());
	    });
	    if(allVals.length == 0)
	    {
	    	jAlert('Debe seleccionar al menos una imagen para descargar.');
			return;
		}
	    else
		{
	    	window.open('descargarImagenesZIP.html?&seleccionados='+allVals,'');
	    }
	});
	
	$('#descargarImagenesPDF').click(function(){
		
	 	var allVals = [];
	    $("input[class='checklote']:checked").each(function() {
	    	allVals.push($(this).val());
	    });
	    if(allVals.length == 0)
	    {
	    	jAlert('Debe seleccionar al menos una imagen para descargar.');
			return;
		}
	    else
		{
	    	window.open('descargarImagenesPDF.html?&seleccionados='+allVals,'');
	    }
	});
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
	//tabla items 
	$("#requerimientoElemento tbody tr").each(function(){
		if($(this).find('#hdn_path').val()!=null && $(this).find('#hdn_path').val()!='')
				$(this).addClass('tr_rearchivo');
	});
	
	//tabla items 
	$("#requerimientoElemento tbody tr").mouseover(function(){
		if(!menuAbierto){
				$(this).addClass('tr_mouseover');
			if($(this).find('#hdn_path').val()!=null && $(this).find('#hdn_path').val()!=''){
				$(this).removeClass('tr_rearchivo');
				$(this).addClass('tr_mouseover');
			}
		}
	});
	$("#requerimientoElemento tbody tr").mouseout(function(){
		if(!menuAbierto){
			$(this).removeClass('tr_mouseover');
			if($(this).find('#hdn_path').val()!=null && $(this).find('#hdn_path').val()!=''){
				$(this).removeClass('tr_mouseover');
				$(this).addClass('tr_rearchivo');
			}
		}
	});
	
	//PopUp con Anular movimiento
    $('#requerimientoElemento tbody tr').contextMenu('myMenu1', {

      bindings: {

        'verImagenes': function(t) {
        	verImagenes($(t).find('#hdn_idReferencia').val(),$(t).find('#hdn_path').val(),t);
        }

      },
		onShowMenu: function(e, menu) {
			var cell = e.currentTarget.children[14].innerHTML;
			var num = 1;
			if(cell == null || cell == "" || cell == undefined){
				menuAbierto=true;
				$('#verImagenes', menu).remove();
				return menu;
			}else{
				menuAbierto=true;
				return menu;
			}
			
		},
		onHide: function(){
			menuAbierto=false;
			$("#requerimientoElemento tbody tr").removeClass('tr_mouseover');
		}

    });
      
      //binding OnBlur() para los campos con popup
	$("#codigoContenedor").blur(function(){
    	 searchContenedorPorTipoYCliente('seleccionContenedor.html','codigoContenedor','codigoCliente','codigoTipoElemento',getContenedorCallBack);
  	});
	$("#codigoElemento").blur(function(){
		searchContenedorPorTipoYCliente('seleccionContenedorOElemento.html','codigoElemento','codigoCliente', null,getElementoCallBack);
 	});
	$("#codigoTipoElemento").blur(function(){
    	searchAjax('tipoElementoDescripcionServlet','codigoTipoElemento',null,getTipoElementoCallBack);
  	});
	$("#codigoTipoElemento").change(function(){
		searchAjax('tipoElementoDescripcionServlet','codigoTipoElemento',null,getTipoElementoCallBack);
	});
    //binding OnBlur() para los campos con popup	
	$("#codigoEmpresa").blur(function(){
		searchAjax('seleccionEmpresa.html','codigoEmpresa',null,getEmpresaCallBack);
	});
	$("#codigoSucursal").blur(function(){
		searchAjax('seleccionSucursal.html','codigoSucursal','codigoEmpresa',getSucursalCallBack);
	});
	$("#codigoCliente").blur(function(){
		searchAjax('seleccionCliente.html','codigoCliente','codigoEmpresa',getClienteCallBack);
	});
	
	$("#buscaEmpresa").click(function(){
		abrirPopupSeleccion("popUpSeleccionEmpresa.html",null,null,"empresasPopupMap", null);
	});
	$("#buscaSucursal").click(function(){
		abrirPopupSeleccion("popUpSeleccionSucursal.html","codigoEmpresa","sucursalesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionCliente.html","codigoEmpresa","clientesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	
	$("#codigoEmpresa").trigger('blur');
 	$("#codigoClasificacionDocumental").blur(function(){
 		searchAjax('seleccionClasificacionDocumental.html?','codigoClasificacionDocumental','codigoCliente',getClasificacionDocumentalCallBack);
 	});
     $("#buscaElemento").click(function(){
    	 abrirPopupSeleccionContenedor("popUpSeleccionElemento2.html","codigoCliente",'codigoTipoElemento', $("#mensajeSeleccioneCliente").val());
	  });
     $("#buscaContenedor").click(function(){
    	 abrirPopupSeleccionContenedor("popUpSeleccionContenedor.html","codigoCliente",null, $("#mensajeSeleccioneCliente").val());
	  });
     $("#buscaTipoElemento").click(function(){
 		abrirPopupSeleccion("popUpSeleccionTipoElemento.html","codigoCliente",null, $("#mensajeSeleccioneCliente").val());
 	  });
     $('#buscaTiposElementos').click(function(){
     	abrirPopupTiposElementos();
      });
     $("#buscaClasificacionDocumental").click(function(){
 		abrirPopupSeleccion("popUpSeleccionClasificacionDocumental.html",'codigoCliente',"clasificacionDocumentalPopupMap",$("#mensajeSeleccioneCliente").val());
 	 });
     
     $("#codigoClasificacionDocumental").change(function(){
 		$("#codigoClasificacionDocumental").trigger('blur');
 	});
     
     $("#codigoContenedor").trigger('blur');
     
     $("#codigoElemento").trigger('blur');
     
     $("#codigoTipoElemento").trigger('blur');
 	 
});

function verImagenes(id,path,t){
	if(path!=null){
		$(t).removeClass('tr_mouseover');
		$(t).addClass('tr_rearchivo');
		$("#fancyboxImagenes").attr("href",'abrirFancyBoxImagenesRearchivo.html?fileName='+path+'&idReferencia='+id);
		$('#fancyboxImagenes').click();
	}
}

function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosRequerimiento.html";
}

function volver(){
	document.location="menu.html";
}

//ajax
function getElementoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoElementoLabel").html(sResponseText);
	else{
		//$("#codigoContenedor").val("");
		$("#codigoElementoLabel").html("");
	}
}

//ajax
function getContenedorCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoContenedorLabel").html(sResponseText);
	else{
		//$("#codigoContenedor").val("");
		$("#codigoContenedorLabel").html("");
	}
}

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#codigoEmpresaLabel").html("");
		$("#codigoEmpresa").val("");
		$("#codigoSucursal").val("");
		$("#codigoCliente").val("");
	}
	$("#codigoSucursal").trigger('blur');
	$("#codigoCliente").trigger('blur');
}
function getSucursalCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#codigoSucursalLabel").html("");
		$("#codigoSucursal").val("");
	}
}
function getTipoElementoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoTipoElementoLabel").html(sResponseText);
	}
	else{
		$("#codigoTipoElemento").val("");
		$("#codigoTipoElementoLabel").html("");
	}
}

//ajax
function getClienteCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#codigoClienteLabel").html("");
		$("#codigoCliente").val("");
	}
}	
function getClasificacionDocumentalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoClasificacionDocumentalLabel").html(sResponseText);
		setTimeout(refresh, 2000);
	}
	else{
		$("#codigoClasificacionDocumental").val("");
		$("#codigoClasificacionDocumentalLabel").html("");
		setTimeout(refresh, 2000);
	}
}

function refresh(){
	$("#formBusqueda").attr('action',"refrescarConsultaReferencia.html");
	$("#formBusqueda").submit();
}

function activar(check){
	if($('#'+check).is(':checked')){
		$('#'+check+'Btn').removeClass('buttonSelect');
		$('#'+check).attr('checked', false);
	}
	else{
		$('#'+check+'Btn').addClass('buttonSelect');
		$('#'+check).attr('checked', true);
	}
}

function seleccionar(mensaje){
	var inTotal = document.getElementById("cantidadReferenciasSeleccionadas");
	var total = inTotal.value;
	if(total == 0){
		jAlert(mensaje);
		return;
	}
	document.forms[1].submit();
}

function volverCancelar(){
	//Desmarco toda la tabla porque vuelve por submit
	$("#requerimientoElemento").find('.selectableCheckbox').each( function(){
    	this.checked = false;
    });
	document.forms[1].submit();
}

function sumarReferencia(opSuma){
	var inTotal = document.getElementById("cantidadReferenciasSeleccionadas");
	var total = inTotal.value;
	total=parseFloat(total);
	if(opSuma)
		total+=1;
	else
		total-=1;
	inTotal.value=total;
}

function selectAll(box, classStyle) {
	var inTotal = document.getElementById("cantidadReferenciasSeleccionadas");
	var total = inTotal.value;
	total=parseFloat(total);
    var chk = box.checked;
    total=0;
    $("#requerimientoElemento").find('.'+classStyle).each( function(){
    	if(chk)
    		total+=1;
    	this.checked = chk;
    });
    inTotal.value=total;
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
