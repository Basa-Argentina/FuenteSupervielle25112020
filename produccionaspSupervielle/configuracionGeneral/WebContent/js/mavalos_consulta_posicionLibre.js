var menuAbierto=false;

$(document).ready(function(){
	//tabla user
	$("#posicion tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#posicion tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	$("#importar").click(function(){
		var cl = $("#codigoLectura").val();
		if(cl!=""){
			var div = $("#pop");
			popupOnDiv(div,'darkLayer');
			document.location="importarLecturaPosicionLibre.html?codigoLectura="+cl;
		}
	});
	
	$("#asignar").click(function(){
			document.location="asignarPosicionLibre.html";
	});
	
	$("#guardar").click(function(){
		 if (confirm('¿Estas seguro de guardar esta asignación de posiciones?')){
			document.location="guardarPosicionLibre.html";
		 }
	});
	
	$("#cancelar").click(function(){
			var div = $("#pop");
			popupOnDiv(div,'darkLayer');
			document.location="mostrarPosicionLibre.html?accion=CANCELAR";
	});
	
	$("#buscar").click(function(){
		var div = $("#pop");
		popupOnDiv(div,'darkLayer');
		document.forms[0].submit();
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
      $('#posicion tbody tr').contextMenu('myMenu1', {
 
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
			$("#posicion tbody tr").removeClass('tr_mouseover');
		}
 
      });

    var div = $("#pop");
  	popupOffDiv(div,'darkLayer');
  	
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
//  	$("#codigoLectura").blur(function(){
//		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
//	});
//	if($("#codigoLectura").val() != null){
//		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
//	}
	
	$("#buscaLectura").click(function(){
		abrirPopupSeleccion("popUpSeleccionLectura.html","codigoEmpresa",null, null);
	  	});
	$("#codigoLectura").blur(function(){
		searchAjax('seleccionLectura.html','codigoLectura','codigoEmpresa',getLecturaCallBack);
	});
	if($("#codigoLectura").val() != null){
		searchAjax('seleccionLectura.html','codigoLectura','codigoEmpresa',getLecturaCallBack);
	}
	
	$("#buscaTipoJerarquia").click(function(){
		abrirPopupSeleccion("popUpSeleccionTipoJerarquia.html","codigoEmpresa",null, null);
	  	});
	$("#codigoTipoJerarquia").blur(function(){
		searchAjax('seleccionTipoJerarquia.html','codigoTipoJerarquia','codigoEmpresa',getTipoJerarquiaCallBack);
	});
	if($("#codigoTipoJerarquia").val() != null){
		searchAjax('seleccionTipoJerarquia.html','codigoTipoJerarquia','codigoEmpresa',getTipoJerarquiaCallBack);
	}
	
	$("#buscaJerarquia").click(function(){
		abrirPopupSeleccion("popUpSeleccionJerarquia.html","codigoTipoJerarquia","jerarquiasPopupMap", $("#mensajeSeleccioneTipoJerarquia").val());
	});
	$("#codigoJerarquia").blur(function(){
		searchAjax('seleccionJerarquia.html','codigoJerarquia','codigoTipoJerarquia',getJerarquiaCallBack);
	});
	if($("#codigoJerarquia").val() != null){
		searchAjax('seleccionJerarquia.html','codigoJerarquia','codigoTipoJerarquia',getJerarquiaCallBack);
	}
	
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
	
	$("#codigoLectura").blur();
	
	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	$('#botonBuscar').click(function(){
		var estDesde = $('#codigoDesdeEstante').val();
		var estHasta = $('#codigoHastaEstante').val();
		if(parseInt(estDesde) > parseInt(estHasta)){
			jAlert($('#errorCodEstMayor').val(),titulo );
			return false;
		}
		
		return true;
	});
});

function cambiarEstado(mensaje, title){
	var select = $("#selectedSel").val();
	if($("#selectedSel").val() == null || $("#selectedSel").val() == ""){
		jAlert(mensaje,title);
		return;
	}
	document.forms[1].submit();		
}

function consultar(cc, cd){
	if(cc!=null && cd)
		document.location="precargaFormularioPosicion.html?accion=VER_DETALLE&codigoConcepto="+cc+"&codigoDeposito="+cd;
}


function volver(){
	document.location="menu.html";
}
function abrirPopup(claseNom){
	$("#codigoSucursal").val("");
	$("#codigoDeposito").val("");
	var labelLista =  document.getElementById("codigoSucursalLabel");
	labelLista.textContent = "";
	var labelConcepto =  document.getElementById("codigoDepositoLabel");
	labelConcepto.textContent = "";
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
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
function abrirPopupLectura(claseNom){
	var url = "mostrarPosicionLibre.html";
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

//ajax
function getLecturaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoLecturaLabel").html(sResponseText);
	else{
		$("#codigoLecturaLabel").html("");
		$("#codigoLectura").val("");
	}
}
//ajax
function getTipoJerarquiaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoTipoJerarquiaLabel").html(sResponseText);
	else{
		$("#codigoTipoJerarquiaLabel").html("");
		$("#codigoTipoJerarquia").val("");
		$("#codigoJerarquia").val("");
		$("#codigoJerarquiaLabel").html("");
	}
}

//ajax
function getJerarquiaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoJerarquiaLabel").html(sResponseText);
	else{
		$("#codigoJerarquiaLabel").html("");
		$("#codigoJerarquia").val("");
	}
}