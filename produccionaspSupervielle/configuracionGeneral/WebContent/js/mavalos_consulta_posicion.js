var menuAbierto=false;

$(document).ready(function() {
	//tabla user
	$("#posicion tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#posicion tbody tr").mouseout(function(){
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
	$("#codigoEmpresa").blur(function(){
		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
	});
	$("#codigoSucursal").blur(function(){
		getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
	});
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
	if($("#codigoSucursal").val() != null){
		getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
	}
	if($("#codigoEmpresa").val() != null){
		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
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
	
	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	$('#imprimirModulos').click(function(){
		window.open('imprimirEtiquetasModuloCodigoBarra.html','');
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

function cambiarEstado(mensaje, title){
	var select = $("#selectedSel").val();
	if($("#selectedSel").val() == null || $("#selectedSel").val() == ""){
		jAlert(mensaje,title);
		return;
	}
	var div = $("#pop");
	popupOnDiv(div,'darkLayer');
	document.forms[1].submit();		
}

function consultar(cc, cd){
	if(cc!=null && cd)
		document.location="precargaFormularioPosicion.html?accion=VER_DETALLE&codigoConcepto="+cc+"&codigoDeposito="+cd;
}
function buscarFiltro(){
	document.forms[0].submit();
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

function abrirPopupSucursal(claseNom, mensaje, title){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "mostrarPosicion.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupDeposito(claseNom, mensaje, title){
	if($("#codigoSucursal").val()==null || $("#codigoSucursal").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "mostrarPosicion.html?empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursal").val();
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

function abrirPopupSeccion(claseNom, mensaje, title){
	if($("#codigoDeposito").val()==null || $("#codigoDeposito").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "mostrarPosicion.html?empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursal").val()+"&depositoCodigo="+$("#codigoDeposito").val();
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
		$("#codigoSeccionLabel").html(sResponseText);	
	else{
		$("#idCliente").val("");
		$("#codigoSeccionLabel").html("");
	}
}

//ajax
function getDepositoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDepositoLabel").html(sResponseText);	
	else{
		$("#idCliente").val("");
		$("#codigoDepositoLabel").html("");
		$("#codigoSeccionLabel").html("");
		$("#codigoSeccion").val("");
	}
}
//ajax
function getSucursalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoSucursalLabel").html("");
		$("#codigoDepositoLabel").html("");
		$("#codigoDeposito").val("");
	}
}

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoEmpresaLabel").html("");
		$("#codigoDepositoLabel").html("");
		$("#codigoSucursalLabel").html("");
		$("#codigoDeposito").val("");
		$("#codigoSucursal").val("");
	}
}

function exportar(){
	window.open('exportar.html','popupPosiciones','width=900,height=700,scrollbars=1');
}