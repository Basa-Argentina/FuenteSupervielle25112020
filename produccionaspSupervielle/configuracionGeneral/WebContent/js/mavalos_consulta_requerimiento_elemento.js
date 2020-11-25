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
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
	//tabla items 
	$("#requerimientoElemento tbody tr").each(function(){
		if($(this).find('#hdn_path').val()!=null && $(this).find('#hdn_path').val()!='')
				$(this).addClass('tr_rearchivo');
		if($(this).find('#hdn_enReq').val()!=null && $(this).find('#hdn_enReq').val()!='' && $(this).find('#hdn_enReq').val()=='SI')
			$(this).addClass('tr_enRequerimiento');
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
			var cell = e.currentTarget.children[16].children[0].value;
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

//      $(".integer").numeric(false, function() { 
//  		this.value = ""; 
//  		this.focus(); 
//  	});
      
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
    //binding OnBlur() para los campos con popup	
 	$("#codigoClasificacionDocumental").blur(function(){
 		searchAjax('seleccionClasificacionDocumental.html?nodo=I','codigoClasificacionDocumental','codigoCliente',getClasificacionDocumentalCallBack);
 	});
     
 	 $("#buscaElemento").click(function(){
    	 abrirPopupSeleccionContenedor("popUpSeleccionElemento2.html","codigoCliente",'codigoTipoElemento', null);
	  });
     $("#buscaContenedor").click(function(){
    	 abrirPopupSeleccionContenedor("popUpSeleccionContenedor.html","codigoCliente",null, null);
	  });
     $("#buscaTipoElemento").click(function(){
 		abrirPopupSeleccion("popUpSeleccionTipoElemento.html","clienteCodigoRequerimientoElemento",null, null);
 	  });
     $('#buscaTiposElementos').click(function(){
    	abrirPopupTiposElementos();
     });
     $("#buscaClasificacionDocumental").click(function(){
 		abrirPopupSeleccion("popUpSeleccionClasificacionDocumental.html",'codigoCliente',"clasificacionDocumentalPopupMap","");
 	 });
     
     $("#codigoClasificacionDocumental").change(function(){
  		$("#codigoClasificacionDocumental").trigger('blur');
  	});
     
     $("#buscaLectura").click(function(){
 		abrirPopupLecturaNuevo();
   	});
 	
 	$("#codigoLectura").blur(function(){
 		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
 	});
 	
    // $("#codigoClasificacionDocumental").trigger('blur');
     $("#codigoContenedor").trigger('blur');
     $("#codigoElemento").trigger('blur');
     $("#codigoTipoElemento").trigger('blur');
     $("#codigoLectura").trigger('blur');
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

//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val()/*+'&utilizada=false'*/;	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

function abrirPopupLecturaNuevo(){

	var url = "popUpSeleccionLectura.html?codigo="+$("#codigoEmpresa").val()/*+"&utilizada=false"*/;
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
function getLecturaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
	{
		$("#codigoLecturaLabel").html(sResponseText);	
	}
	else
	{
		$("#codigoLecturaLabel").html("");
		$("#codigoLectura").val("");
	}	
}
