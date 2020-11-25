var menuAbierto=false;

$(document).ready(function(){
	//tabla user
	$("#modulo tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#modulo tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
		
	//PopUp
      $('#modulo tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
          'consultar': function(t) {
            consultar($(t).find('#hdn_cc').val(), $(t).find('#hdn_cd').val());
          }
 
        },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#modulo tbody tr").removeClass('tr_mouseover');
		}
 
      });
	
	var div = $("#pop");
  	popupOffDiv(div,'darkLayer');
  	
  	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	//Imprimir
	$('#imprimirDiv').attr({'style':'display:block'});
	$('#imprimirImg').click(function() {
		$('#imprimirDiv').slideToggle('slideUp');
		$('#imprimirImgSrcDown').slideToggle('slideUp');
		$('#imprimirImgSrc').slideToggle('slideUp');
	});
	$('#imprimirDiv > table').addClass('seccion');
	
	$("#codigoDeposito").blur(function(){
		getAjax('depositosServlet','codigo','codigoDeposito',getDepositoCallBack);
	});
	$("#codigoSeccion").blur(function(){
		getAjax('seccionesServlet','codigo','codigoSeccion',getSeccionCallBack);
	});
	if($("#codigoSeccion").val() != null){
		getAjax('seccionesServlet','codigo','codigoSeccion',getSeccionCallBack);
	}
	if($("#codigoDeposito").val() != null){
		getAjax('depositosServlet','codigo','codigoDeposito',getDepositoCallBack);
	}
	
	//Validaciones y botones
	$('.completarZeros').blur(function(e){
		valorSinZeros='';
		valorSinZeros=$(this).val();
		zerosIzq='';
		if(valorSinZeros!=null && valorSinZeros!=''){
			maximo=$(this).attr('maxlength');
			actual=valorSinZeros.length;
			for(i=0;i<(maximo-actual);i++){
				zerosIzq+='0';
			}
		}
		$(this).val(zerosIzq+valorSinZeros);
	});
	
	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	
	$('#imprimirModulos').click(function(){
		codigoLectura = $("#codigoLectura").val();
		window.open('imprimirEtiquetasModuloCodigoBarra.html?modo=ventana'+'&cantidad='+$('#cantImprimir').val(),'');
	});
	
	$('#descargarImprimirModulos').click(function(){
		codigoLectura = $("#codigoLectura").val();
		window.open('imprimirEtiquetasModuloCodigoBarra.html?modo=descarga'+'&cantidad='+$('#cantImprimir').val(),'');
	});
	
	$('#botonBuscar').click(function(){
		var estDesde = $('#codigoDesdeEstante').val();
		var estHasta = $('#codigoHastaEstante').val();
		if(estDesde > estHasta){
			jAlert($('#errorCodEstMayor').val(),titulo );
			return false;
		}
		
		return true;
	});
});

function volver(){
	document.location="menu.html";
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
function getSeccionCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoLabel").html(sResponseText);	
	else{
		$('#codigo').val('');
		$("#codigoLabel").html("");
	}
}

//ajax
function getDepositoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDepositoLabel").html(sResponseText);	
	else{			
		$("#codigoDepositoLabel").html("");
		$("#codigoSeccionLabel").html("");
		$("#codigoSeccion").val("");
	}
}

function abrirPopupSeccion(claseNom, mensaje, title){
	if($("#codigoDeposito").val()==null || $("#codigoDeposito").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "mostrarImpresionEtiquetaModulo.html?depositoCodigo="+$("#codigoDeposito").val();
	jQuery.ajax({
        url: url,
        success: function(data){
           filteredResponse =  $(data).find(this.selector);
           if(filteredResponse.size() == 1){
                $(this).html(filteredResponse);                            
           }else{
                $(this).html(data);
           }
           $(this).displayTagAjax();
        } ,
        data: ({"time":new Date().getTime()}),
        context: $(".displayTagDiv."+ claseNom)
    });
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}